package skymine.providers;

import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.regions.Region;
import org.bukkit.entity.Player;
import skymine.Message;
import skymine.SkyMine;

public class WorldEditProvider {

    private final WorldEditPlugin worldEdit;

    public WorldEditProvider() {
        this.worldEdit = (WorldEditPlugin) SkyMine.getInstance().getServer().getPluginManager().getPlugin("WorldEdit");
    }

    public Region getSelection(Player player) {
        Region selection;
        try {
            selection = worldEdit.getSession(player).getSelection(BukkitAdapter.adapt(player.getWorld()));
        } catch (IncompleteRegionException e) {
            player.sendMessage(Message.NO_SELECTION.get(player));
            if (SkyMine.getInstance().getConsoleOutput().isDebug())
                e.printStackTrace();
            return null;
        }
        return selection;
    }
}