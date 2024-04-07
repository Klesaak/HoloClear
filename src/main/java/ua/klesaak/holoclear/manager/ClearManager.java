package ua.klesaak.holoclear.manager;

import lombok.Getter;
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
import org.bukkit.event.world.WorldInitEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.scheduler.BukkitTask;
import ua.klesaak.holoclear.HoloClearPlugin;
import ua.klesaak.holoclear.command.ReloadCommand;
import ua.klesaak.holoclear.configuration.ConfigFile;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

//todo clear-command
@Getter
public class ClearManager implements Listener {
    private final HoloClearPlugin holoClearPlugin;

    private ConfigFile configFile;
    private Set<ItemEntity> entityList = Collections.newSetFromMap(new ConcurrentHashMap<>(1024));
    private BukkitTask clearTask;


    public ClearManager(HoloClearPlugin holoClearPlugin) {
        this.holoClearPlugin = holoClearPlugin;
        this.reload();
        holoClearPlugin.getServer().getPluginManager().registerEvents(this, this.holoClearPlugin);
        new ReloadCommand(this);
    }

    public void reload() {
        this.configFile = new ConfigFile(this.holoClearPlugin);
        this.stop();
        if (!this.entityList.isEmpty()) {
            this.entityList.forEach(ItemEntity::remove);
            this.entityList = Collections.newSetFromMap(new ConcurrentHashMap<>(1024));
        }
        this.holoClearPlugin.getServer().getWorlds().forEach(world -> {
            if (!this.configFile.isDisabledWorld(world)) this.loadAllDropFromWorld(world);
        });
        this.clearTask = Bukkit.getScheduler().runTaskTimerAsynchronously(this.holoClearPlugin, () -> {
            for (ItemEntity item : this.entityList) {
                if (item.getEntity().getLocation().getWorld().getEntities().isEmpty()) {
                    this.entityList.remove(item);
                } else if (item.getTime() <= 0) {
                    item.remove();
                    this.entityList.remove(item);
                } else {
                    item.decrementTime(this.configFile.getIcon());
                }
            }
        }, 0, 20L);
    }

    public void stop() {
        if (this.clearTask == null) return;
        this.clearTask.cancel();
        this.clearTask = null;
    }

    private void loadAllDropFromWorld(World world) {
        for (Chunk chunk : world.getLoadedChunks()) {
            for (Entity entity : chunk.getEntities()) {
                if (entity instanceof Projectile || entity instanceof Item) {
                    this.entityList.add(new ItemEntity(entity, this.configFile.getClearTimeInSeconds()));
                }
                System.out.println("ENTITIES LENGTH: " + chunk.getEntities().length);
            }
        }
    }

    @EventHandler
    public void onArrowShoot(ProjectileHitEvent e) {
        val entity = e.getEntity();
        if (this.configFile.isDisabledWorld(entity.getLocation().getWorld())) return;
        this.entityList.add(new ItemEntity(entity, this.configFile.getClearTimeInSeconds()));
    }

    @EventHandler
    public void onItemSpawn(ItemSpawnEvent e) {
        Item item = e.getEntity();
        if (this.configFile.isDisabledWorld(item.getLocation().getWorld())) return;
        this.entityList.add(new ItemEntity(item, this.configFile.getClearTimeInSeconds()));
    }

    @EventHandler
    public void onWorldLoadEvent(WorldInitEvent e) { //todo не работает сука
        val world = e.getWorld();
        if (this.configFile.isDisabledWorld(world)) return;
        this.loadAllDropFromWorld(world);
    }
}
