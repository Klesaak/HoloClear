package ua.klesaak.holoclear.manager;

import lombok.val;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.scheduler.BukkitTask;
import ua.klesaak.holoclear.HoloClearPlugin;
import ua.klesaak.holoclear.util.MCColorUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

//todo commands, disabledWorlds
public class ClearManager implements Listener {
    public static final Pattern TIME_PATTERN = Pattern.compile("(time)", Pattern.LITERAL);
    public static final Pattern COUNT_PATTERN = Pattern.compile("(count)", Pattern.LITERAL);

    private final HoloClearPlugin holoClearPlugin;
    private final Set<ItemEntity> entityList = Collections.newSetFromMap(new ConcurrentHashMap<>(1024));

    private BukkitTask clearTask;
    private int clearTimeInSeconds;
    private String icon;


    public ClearManager(HoloClearPlugin holoClearPlugin) {
        this.holoClearPlugin = holoClearPlugin;
        this.reload();
        val server = this.holoClearPlugin.getServer();
        server.getPluginManager().registerEvents(this, this.holoClearPlugin);
        this.clearTask = Bukkit.getScheduler().runTaskTimerAsynchronously(this.holoClearPlugin, () -> {
            System.out.println("SIZE: " + this.entityList.size());
            for(ItemEntity item : this.entityList) {
                if (item.getEntity().getLocation().getWorld().getEntities().isEmpty()) {
                    this.entityList.remove(item);
                } else if (item.getTime() <= 0) {
                    item.getEntity().remove();
                    this.entityList.remove(item);
                } else {
                    item.decrementTime(this.icon);
                }
            }
        }, 0, 20L);

        Bukkit.getScheduler().runTaskLater(this.holoClearPlugin, () -> {
            for (World world : server.getWorlds()) {
                for (Chunk chunk : world.getLoadedChunks()) {
                    for (Entity entity : chunk.getEntities()) {
                        if (entity instanceof Projectile || entity instanceof Item) {
                            this.entityList.add(new ItemEntity(entity, this.clearTimeInSeconds));
                            System.out.println("ADDED ENTITY: " + entity.getEntityId());
                        }
                    }
                }
            }
        }, 50L);
    }

    public void reload() {
        this.holoClearPlugin.saveDefaultConfig();
        this.holoClearPlugin.reloadConfig();
        this.clearTimeInSeconds = this.holoClearPlugin.getConfig().getInt("clearTime", 20);
        this.icon = MCColorUtils.color(this.holoClearPlugin.getConfig().getString("icon"));
    }

    public void stop() {
        if (this.clearTask == null) return;
        this.clearTask.cancel();
        this.clearTask = null;
    }

    @EventHandler
    public void onArrowShoot(ProjectileHitEvent e) {
        this.entityList.add(new ItemEntity(e.getEntity(), this.clearTimeInSeconds));
    }

    @EventHandler
    public void onItemSpawn(ItemSpawnEvent e) {
        Item item = e.getEntity();
        this.entityList.add(new ItemEntity(item, this.clearTimeInSeconds));
    }
}
