package skymine;


import com.google.common.base.Strings;
import lombok.experimental.UtilityClass;
import org.bukkit.*;
import org.bukkit.boss.BossBar;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@UtilityClass
public class Utils {

    public final List<String> dataCheck = new ArrayList<>();

    public final List<Color> colors = new ArrayList<>();

    static {
        colors.add(Color.AQUA);
        colors.add(Color.BLUE);
        colors.add(Color.FUCHSIA);
        colors.add(Color.GREEN);
        colors.add(Color.LIME);
        colors.add(Color.ORANGE);
        colors.add(Color.WHITE);
        colors.add(Color.YELLOW);
    }

    public String locationToString(Location loc) {
        World world = loc.getWorld();
        return world == null ? "" : world.getName() + ";" + loc.getX() + ";" + loc.getY() + ";" + loc.getZ();
    }

    public Location stringToLocation(String str) {
        String[] arr = str.split(";");
        Location newLoc = new Location(Bukkit.getWorld(arr[0]), Double.parseDouble(arr[1]), Double.parseDouble(arr[2]), Double.parseDouble(arr[3]));
        return newLoc.clone();
    }

    public String parse(String string) {
        string = string.replaceAll("(?i)%prefix%", Message.PREFIX.getValue());
        return string;
    }

    public String parse(String string, Player player) {
        string = Utils.parse(string);
        string = string.replaceAll("(?i)%player%", player.getName());
        return string;
    }

    public String stripColor(String msg) {
        return msg != null ? ChatColor.stripColor(msg) : null;
    }

    @Nullable
    public String color(@Nullable String msg) {
        return color(msg, '&');
    }

    @Nullable
    public String color(@Nullable String msg, char colorChar) {
        return msg == null ? null : ChatColor.translateAlternateColorCodes(colorChar, msg);
    }

}
