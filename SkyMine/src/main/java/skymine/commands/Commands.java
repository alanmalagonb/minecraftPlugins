package skymine.commands;



import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.regions.Region;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;
import skymine.Message;
import skymine.SkyMine;
import skymine.Utils;

import java.util.*;

public class Commands implements CommandExecutor, Listener {
    private final SkyMine plugin;

    public Commands(SkyMine plugin){
        this.plugin = plugin;

    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            sender.sendMessage(Utils.color("&8&m        &r &3SkyMine &f" + plugin.getDescription().getVersion() + " &8&m        &r"
                    + "\n&3/" + label + " check &8- &7Check the name + data of the block to put in the blocklist."
                    + "\n&3/" + label + " region &8- &7All the info to set a region."));
            return true;
        }

        Player player;

        switch (args[0].toLowerCase()){
            case "check":
                if (checkConsole(sender))
                    return true;
                player = (Player) sender;

                if (!Utils.dataCheck.contains(player.getName())) {
                    Utils.dataCheck.add(player.getName());
                    player.sendMessage(Message.DATA_CHECK_ON.get(player));
                } else {
                    Utils.dataCheck.remove(player.getName());
                    player.sendMessage(Message.DATA_CHECK_OFF.get(player));
                }
                break;
            case "region":
                if (checkConsole(sender))
                    return true;

                player = (Player) sender;

                if (args.length == 1 || args.length > 5) {
                    player.sendMessage(Utils.color("&6&m-----&r &3&lSkyMine &6&m-----"
                            + "\n&3/" + label + "  region set <name> <ore> <delay>"
                            + "\n&3/" + label + "  region list"
                            + "\n&6&m-----------------------"));
                    return true;
                }

                if (args.length == 2) {
                    if (args[1].equalsIgnoreCase("list")) {
                        ConfigurationSection regions = plugin.getFiles().getRegions().getFileConfiguration().getConfigurationSection("Regions");
                        Set<String> setregions = Objects.requireNonNull(regions).getKeys(false);
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&m-----&r &3&lBlockRegen &6&m-----"));
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eHere is a list of all your regions."));
                        player.sendMessage(" ");
                        for (String checkregions : setregions) {
                            player.sendMessage(ChatColor.AQUA + "- " + checkregions);
                        }
                        player.sendMessage(" ");
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&m-----------------------"));
                        return true;
                    }
                }

                if (args.length == 5) {
                    if (args[1].equalsIgnoreCase("set")) {

                        if (plugin.getWorldEditProvider() == null) {
                            player.sendMessage(Message.WORLD_EDIT_NOT_INSTALLED.get(player));
                            return true;
                        }

                        Region selection = plugin.getWorldEditProvider().getSelection(player);

                        if (selection == null) {
                            return true;
                        }

                        List<String> regions = new ArrayList<>();

                        if (plugin.getFiles().getRegions().getFileConfiguration().getString("Regions") != null) {
                            ConfigurationSection regionSection = plugin.getFiles().getRegions().getFileConfiguration().getConfigurationSection("Regions");
                            regions = new ArrayList<>(Objects.requireNonNull(regionSection).getKeys(false));
                        }

                        if (regions.contains(args[2])) {
                            player.sendMessage(Message.DUPLICATED_REGION.get(player));
                            return true;
                        }

                        plugin.getFiles().getRegions().getFileConfiguration().set("Regions." + args[2] + ".Min", Utils.locationToString(BukkitAdapter.adapt(player.getWorld(), selection.getMinimumPoint())));
                        plugin.getFiles().getRegions().getFileConfiguration().set("Regions." + args[2] + ".Max", Utils.locationToString(BukkitAdapter.adapt(player.getWorld(), selection.getMaximumPoint())));
                        plugin.getFiles().getRegions().getFileConfiguration().set("Regions." + args[2] + ".material", args[3]);
                        plugin.getFiles().getRegions().getFileConfiguration().set("Regions." + args[2] + ".delay", Integer.parseInt(args[4]));
                        plugin.getFiles().getRegions().save();
                        player.sendMessage(Message.SET_REGION.get(player));
                        return true;
                    }


                }
                if (args.length == 3) {
                    if (plugin.getFiles().getRegions().getFileConfiguration().getString("Regions") == null) {
                        player.sendMessage(Message.UNKNOWN_REGION.get(player));
                    } else {
                        ConfigurationSection regions = plugin.getFiles().getRegions().getFileConfiguration().getConfigurationSection("Regions");
                        Set<String> setregions = Objects.requireNonNull(regions).getKeys(false);

                        if (setregions.contains(args[2])) {
                            plugin.getFiles().getRegions().getFileConfiguration().set("Regions." + args[2], null);
                            plugin.getFiles().getRegions().save();
                            player.sendMessage(Message.REMOVE_REGION.get(player));
                        } else {
                            player.sendMessage(Message.UNKNOWN_REGION.get(player));
                        }

                        return true;
                    }
                    return true;
                }
                break;
        }




        return true;
    }

    private boolean checkConsole(CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Message.ONLY_PLAYERS.get());
            return true;
        }

        return false;
    }
}
