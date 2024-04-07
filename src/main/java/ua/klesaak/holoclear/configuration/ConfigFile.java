package ua.klesaak.holoclear.configuration;

import lombok.Getter;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;
import ua.klesaak.holoclear.util.MCColorUtils;
import ua.klesaak.holoclear.util.PluginConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Getter
public class ConfigFile extends PluginConfig {
    public static final Pattern TIME_PATTERN = Pattern.compile("(time)", Pattern.LITERAL);
    public static final Pattern COUNT_PATTERN = Pattern.compile("(count)", Pattern.LITERAL);

    private final int clearTimeInSeconds;
    private final String icon;
    private final List<String> disabledWorlds = new ArrayList<>(16);


    public ConfigFile(JavaPlugin plugin) {
        super(plugin, "config.yml");
        this.clearTimeInSeconds = this.getInt("clearTime");
        this.icon = MCColorUtils.color(this.getString("icon"));
        this.getStringList("disabledWorlds").forEach(worldName -> this.disabledWorlds.add(worldName.toLowerCase()));
    }

    public boolean isDisabledWorld(World world) {
        return this.disabledWorlds.contains(world.getName().toLowerCase());
    }

    public boolean isDisabledWorld(String worldName) {
        return this.disabledWorlds.contains(worldName.toLowerCase());
    }
}
