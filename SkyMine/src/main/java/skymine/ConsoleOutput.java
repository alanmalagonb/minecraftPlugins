package skymine;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
public class ConsoleOutput {

    @Getter
    @Setter
    private boolean debug = false;

    @Getter
    @Setter
    private boolean colors = false;

    @Setter
    private ConsoleCommandSender console = null;

    @Getter
    @Setter
    @NotNull
    private String prefix = "";

    @Getter
    private final List<CommandSender> listeners = new ArrayList<>();

    @Getter
    private final List<CommandSender> personalDebug = new ArrayList<>();

    public ConsoleOutput(JavaPlugin plugin) {
        this.console = plugin.getServer().getConsoleSender();
    }

    public void colored(String msg) {
        if (!colors || console == null) return;

        console.sendMessage(Utils.color(msg));
    }


    public void info(String msg) {
        final String finalMsg = prefix + "&7INFO: " + msg;

        if (colors) colored(finalMsg);
        else Bukkit.getLogger().info(Utils.stripColor(finalMsg));

        toListeners(finalMsg);
    }


    public void toListeners(String message) {
        final String finalMessage = Utils.color(message);
        listeners.forEach(c -> c.sendMessage(finalMessage));
    }
}