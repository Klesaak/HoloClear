package ua.klesaak.holoclear;

import org.bukkit.event.Listener;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.PluginLoadOrder;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.annotation.command.Command;
import org.bukkit.plugin.java.annotation.command.Commands;
import org.bukkit.plugin.java.annotation.permission.Permission;
import org.bukkit.plugin.java.annotation.permission.Permissions;
import org.bukkit.plugin.java.annotation.plugin.*;
import org.bukkit.plugin.java.annotation.plugin.author.Author;
import ua.klesaak.holoclear.manager.ClearManager;


@Plugin(name = "HoloClear", version = "1.0")
@Author("Klesaak")
@LoadOrder(PluginLoadOrder.STARTUP)
@Website("https://t.me/klesaak")
@ApiVersion(ApiVersion.Target.v1_16)
@Commands({
        @Command(name = "hc-reload",
                aliases = {"holoclear-reload", "clear-reload"},
                desc = "Reload command.",
                permission = "holoclear.reload")
})
@Description("Simple holographic, asynchronously drop cleaner plugin.")
@Permissions({
        @Permission(name = "holoclear.reload", defaultValue = PermissionDefault.OP, desc = "Access to use reload command."),
        @Permission(name = "holoclear.clear", defaultValue = PermissionDefault.OP, desc = "Access to use admin command.")
})
public final class HoloClearPlugin extends JavaPlugin implements Listener {
    private ClearManager clearManager;

    @Override
    public void onEnable() {
        this.clearManager = new ClearManager(this);
    }

    @Override
    public void onDisable() {
        this.clearManager.stop();
    }
}
