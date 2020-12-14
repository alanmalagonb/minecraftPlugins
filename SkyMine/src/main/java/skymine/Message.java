package skymine;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public enum Message {

    PREFIX("Prefix", "&a[&bSkyMine&a] &r"),

    UPDATE("Update", "&6&m-----&r &3&lSkyMine &6&m-----\n" +
            "&eA new update was found!\n" +
            "&eCurrent version: &c%version%\n" +
            "&eNew version: &a%newVersion%\n" +
            "&6&m-----------------------"),


    ONLY_PLAYERS("Console-Sender-Error", "&cI'm sorry but the console can not perform this command!"),

    DATA_CHECK("Data-Check", "&eNombre para el config: &d%block%"),
    DATA_CHECK_ON("Data-Check-On", "&aModo checando-bloque activado!"),
    DATA_CHECK_OFF("Data-Check-Off", "&cModo checando-bloque desactivado!"),

    WORLD_EDIT_NOT_INSTALLED("WorldEdit-Not-Installed", "&cRegion functions require World Edit."),
    NO_SELECTION("No-Region-Selected", "&cI'm sorry but you need to select a CUBOID region first!"),
    DUPLICATED_REGION("Duplicated-Region", "&cThere is already a region with that name!"),
    SET_REGION("Set-Region", "&aRegion successfully saved!"),
    REMOVE_REGION("Remove-Region", "&aRegion successfully removed!"),
    UNKNOWN_REGION("Unknown-Region", "&cThere is no region with that name!");

    @Getter
    private final String path;

    @Getter
    @Setter
    private String value;

    @Getter
    private static boolean insertPrefix = false;

    public String get() {
        return Utils.color(Utils.parse(insertPrefix ? "%prefix%" + this.value : this.value));
    }

    public String get(Player player) {
        return Utils.color(Utils.parse(insertPrefix ? "%prefix%" + this.value : this.value, player));
    }

    Message(String path, String value) {
        this.path = path;
        this.value = value;
    }

    public static void load() {

        FileConfiguration messages = SkyMine.getInstance().getFiles().getMessages().getFileConfiguration();

        if (!messages.contains("Insert-Prefix"))
            messages.set("Insert-Prefix", true);
        insertPrefix = messages.getBoolean("Insert-Prefix", true);

        for (Message msg : values()) {
            String str = messages.getString("Messages." + msg.getPath());

            if (str == null) {
                messages.set("Messages." + msg.getPath(), msg.getValue());
                continue;
            }

            msg.setValue(str);
        }

        SkyMine.getInstance().getFiles().getMessages().save();
    }
}