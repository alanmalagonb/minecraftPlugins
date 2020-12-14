package skymine.configuration;

import lombok.Getter;
import skymine.SkyMine;

public class Files {

    @Getter
    private ConfigFile settings;
    @Getter
    private ConfigFile messages;
    @Getter
    private ConfigFile blockList;
    @Getter
    private ConfigFile regions;

    public Files() {
        load();
    }

    public void load() {
        settings = new ConfigFile(SkyMine.getInstance(), "Settings.yml");
        messages = new ConfigFile(SkyMine.getInstance(), "Messages.yml");
        blockList = new ConfigFile(SkyMine.getInstance(), "Blocklist.yml");
        regions = new ConfigFile(SkyMine.getInstance(), "Regions.yml");
    }
}
