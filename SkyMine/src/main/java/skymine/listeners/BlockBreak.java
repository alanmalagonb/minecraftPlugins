package skymine.listeners;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.regions.CuboidRegion;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.scheduler.BukkitRunnable;
import skymine.SkyMine;
import skymine.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.SplittableRandom;


public class BlockBreak implements Listener {

    private final SkyMine plugin;

    public BlockBreak(SkyMine plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event){
        Block block = event.getBlock();
        World world = block.getWorld();
        boolean useRegions = plugin.getConfig().getBoolean("Use-Regions", false);
        Material curMaterial = block.getType();
        Location location = event.getBlock().getLocation();

        if (useRegions) {
            if (plugin.getWorldEditProvider() == null) return;

            boolean isInRegion = false;
            Material material = null;

            ConfigurationSection regionSection = plugin.getFiles().getRegions().getFileConfiguration().getConfigurationSection("Regions");

            List<String> regions = regionSection == null ? new ArrayList<>() : new ArrayList<>(regionSection.getKeys(false));

            for (String region : regions) {
                String max = plugin.getFiles().getRegions().getFileConfiguration().getString("Regions." + region + ".Max");
                String min = plugin.getFiles().getRegions().getFileConfiguration().getString("Regions." + region + ".Min");
                String mat_str = plugin.getFiles().getRegions().getFileConfiguration().getString("Regions." + region + ".material");

                if (min == null || max == null)
                    continue;

                Location locA = Utils.stringToLocation(max);
                Location locB = Utils.stringToLocation(min);

                if (locA.getWorld() == null || !locA.getWorld().equals(world))
                    continue;

                CuboidRegion selection = new CuboidRegion(BukkitAdapter.asBlockVector(locA), BukkitAdapter.asBlockVector(locB));

                if (selection.contains(BukkitAdapter.asBlockVector(block.getLocation()))) {
                    isInRegion = true;
                    material = Material.getMaterial(mat_str);
                    break;
                }
            }

            if (isInRegion) {
                if(block.getType() == material || block.getType() == Material.COBBLESTONE || block.getType() == Material.STONE){

                    Material finalMaterial = material;

                    new BukkitRunnable(){
                        @Override
                        public void run(){
                            process(finalMaterial, block, curMaterial);
                        }
                    }.runTask(plugin);
                    Material finalMaterial1 = material;
                    new BukkitRunnable(){
                        @Override
                        public void run() {
                            Block actual = location.getBlock();
                            if(actual.getType() == Material.COBBLESTONE) probability(actual, finalMaterial1);
                        }
                    }.runTaskLater(plugin,(long) 8*20);
                }else{
                    event.setCancelled(true);
                }
            }
        }

    }

    private void process(Material zoneMaterial, Block block,Material breakBlockMaterial) {
        ConfigurationSection blockList = plugin.getFiles().getBlockList().getFileConfiguration().getConfigurationSection("Blocks");

        if(breakBlockMaterial != Material.COBBLESTONE){
            List<String> blocks = blockList == null ? new ArrayList<>() : new ArrayList<>(blockList.getKeys(false));
            for (String blockC : blocks) {
                String replace_into = plugin.getFiles().getBlockList().getFileConfiguration().getString("Blocks." + blockC + ".replace-into");
                if (breakBlockMaterial == Material.valueOf(blockC)) {
                    block.setType(Material.valueOf(replace_into));
                    break;
                }
            }
        }else{
            block.setType(Material.BEDROCK);
            new BukkitRunnable(){
                @Override
                public void run(){
                    probability(block,zoneMaterial);
                }
            }.runTaskLater(plugin,(long) 5*20);
        }
    }

    private void probability(Block block,Material material){
        SplittableRandom random = new SplittableRandom();
        boolean whoKnows = random.nextInt(1, 101) <= 25;
        if(whoKnows) block.setType(material);
        else block.setType(Material.STONE);
    }


}
