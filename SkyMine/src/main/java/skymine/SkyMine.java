package skymine;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import jdk.nashorn.internal.ir.Block;
import lombok.Getter;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import skymine.commands.Commands;
import skymine.configuration.Files;
import skymine.listeners.BlockBreak;
import skymine.listeners.PlayerInteract;
import skymine.providers.WorldEditProvider;

import java.util.Random;

public class SkyMine extends JavaPlugin {

    @Getter
    public static SkyMine instance;

    @Getter
    private WorldEditProvider worldEditProvider;

    @Getter
    private Files files;

    @Getter
    private Random random;

    @Getter
    public ConsoleOutput consoleOutput;

    @Override
    public void onEnable() {
        instance = this;
        random = new Random();
        files = new Files();
        Message.load();
        consoleOutput = new ConsoleOutput(this);
        consoleOutput.setColors(true);
        registerListeners();

        checkDependencies();

        getCommand("skymine").setExecutor(new Commands(this));
        consoleOutput.info("&bYou are using" + (getDescription().getVersion().contains("-b") ? " &cDEVELOPMENT&b" : "") + " version &f" + getDescription().getVersion());
        consoleOutput.info("&bAlways backup if you are not sure about things.");

    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    public void checkDependencies() {
        setupWorldEdit();
    }


    private void setupWorldEdit() {
        if (worldEditProvider != null) return;

        Plugin worldEditPlugin = getServer().getPluginManager().getPlugin("WorldEdit");

        if (!(worldEditPlugin instanceof WorldEditPlugin))
            return;

        this.worldEditProvider = new WorldEditProvider();
        consoleOutput.info("WorldEdit found! &aEnabling regions.");
    }

    private void registerListeners() {
        PluginManager pluginManager = this.getServer().getPluginManager();
        pluginManager.registerEvents(new Commands(this), this);
        pluginManager.registerEvents(new PlayerInteract(this), this);
        pluginManager.registerEvents(new BlockBreak(this), this);
    }

    @Override
    public @NotNull FileConfiguration getConfig() {
        return files.getSettings().getFileConfiguration();
    }
}
