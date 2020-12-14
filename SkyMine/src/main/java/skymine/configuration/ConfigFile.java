package skymine.configuration;

import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import skymine.SkyMine;
import skymine.Utils;

import java.io.File;
import java.io.IOException;

public class ConfigFile {

    @Getter
    private final String path;

    @Getter
    private FileConfiguration fileConfiguration;

    @Getter
    private File file;

    private final JavaPlugin plugin;

    public ConfigFile(JavaPlugin plugin, String path) {
        this.path = path.contains(".yml") ? path : path + ".yml";
        this.plugin = plugin;

        load();
    }

    public void load() {
        this.file = new File(plugin.getDataFolder(), this.path);

        if (!file.exists()) {
            try {
                SkyMine.getInstance().saveResource(this.path, false);
            } catch (IllegalArgumentException e) {
                try {
                    file.createNewFile();
                } catch (IOException e1) {
                    SkyMine.getInstance().getServer().getConsoleSender().sendMessage(Utils.color("&a[&bSkyMine&a] &cCould not create " + this.path));
                    return;
                }
            }

            SkyMine.getInstance().getServer().getConsoleSender().sendMessage(Utils.color("&a[&bSkyMine&a] &aCreated " + this.path));
        }

        SkyMine.getInstance().getServer().getConsoleSender().sendMessage(Utils.color("&a[&bSkyMine&a] &7Loaded " + this.path));

        fileConfiguration = YamlConfiguration.loadConfiguration(file);
    }

    public void save() {
        try {
            fileConfiguration.save(file);
        } catch (IOException e) {
            SkyMine.getInstance().getServer().getConsoleSender().sendMessage(Utils.color("&a[&bSkyMine&a] &cCould not save " + this.path));
        }
    }
}