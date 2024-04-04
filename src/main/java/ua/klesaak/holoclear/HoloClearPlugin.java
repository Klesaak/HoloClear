package ua.klesaak.holoclear;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import ua.klesaak.holoclear.manager.ClearManager;

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
