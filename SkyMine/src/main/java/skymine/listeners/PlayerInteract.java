package skymine.listeners;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.material.Crops;
import skymine.Message;
import skymine.SkyMine;
import skymine.Utils;

public class PlayerInteract implements Listener {

    private final SkyMine plugin;

    public PlayerInteract(SkyMine instance) {
        this.plugin = instance;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {

        Player player = event.getPlayer();

        if (event.getAction().toString().toUpperCase().contains("BLOCK") && Utils.dataCheck.contains(player.getName())) {
            if (event.getClickedBlock() == null) return;

            event.setCancelled(true);
            player.sendMessage(Message.DATA_CHECK.get(player).replace("%block%", event.getClickedBlock().getType().toString()));
            return;
        }

        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && plugin.getConfig().getBoolean("Bone-Meal-Override", false)) {
            if (!player.getInventory().getItemInMainHand().getType().equals(Material.BONE_MEAL) ||
                    event.getClickedBlock() == null)
                return;

            if (!(event.getClickedBlock().getState().getData() instanceof Crops))
                return;

        }
    }
}