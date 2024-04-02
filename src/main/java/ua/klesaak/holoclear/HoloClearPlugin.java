package ua.klesaak.holoclear;

import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import ua.klesaak.holoclear.util.MCColorUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public final class HoloClearPlugin extends JavaPlugin implements Listener {
    public static final Pattern TIME_PATTERN = Pattern.compile("(time)", Pattern.LITERAL);
    private int clearTimeInSeconds;
    private String icon;
    private final List<ItemEntity> entityList = new ArrayList<>(1024);

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        this.clearTimeInSeconds = this.getConfig().getInt("clearTime", 20);
        this.icon = MCColorUtils.color(this.getConfig().getString("icon"));
        this.getServer().getPluginManager().registerEvents(this, this);
        this.getServer().getScheduler().runTaskTimerAsynchronously(this, () -> {
            for(int i = 0; i < this.entityList.size(); ++i) {
                ItemEntity item = this.entityList.get(i);
                if (item.getEntity().getLocation().getWorld().getEntities().isEmpty()) {
                    this.entityList.remove(item);
                } else if (item.getTime() <= 0) {
                    item.getEntity().remove();
                    this.entityList.remove(item);
                } else {
                    item.decrementTime(this.icon);
                }
            }
        }, 20L, 20L);

        for (World world : this.getServer().getWorlds()) {
            Chunk[] loadedChunks = world.getLoadedChunks();
            for (int loadedChunksLength = loadedChunks.length, i = 0; i < loadedChunksLength; ++i) {
                Chunk chunk = loadedChunks[i];
                Entity[] entities = chunk.getEntities();
                for (int entitiesLength = entities.length, j = 0; j < entitiesLength; ++j) {
                    Entity entity = entities[j];
                    if (entity instanceof Projectile || entity instanceof Item) {
                        this.entityList.add(new ItemEntity(entity, this.clearTimeInSeconds));
                    }
                }
            }
        }
    }

    @Override
    public void onDisable() {
        this.getServer().getScheduler().cancelTasks(this);
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
