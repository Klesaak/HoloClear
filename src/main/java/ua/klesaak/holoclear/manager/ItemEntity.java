package ua.klesaak.holoclear.manager;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Projectile;
import ua.klesaak.holoclear.util.UtilityMethods;

import static ua.klesaak.holoclear.configuration.ConfigFile.*;

@Getter @Setter
public class ItemEntity {
   private final Entity entity;
   private int time;

   public ItemEntity(Entity entity, int time) {
      this.entity = entity;
      this.entity.setCustomNameVisible(true);
      this.time = time;
   }

   public void decrementTime(String customName) {
      String name = UtilityMethods.replaceAll(TIME_PATTERN, customName, ()-> this.time);
      if (entity instanceof Projectile) {
         name = UtilityMethods.replaceAll(COUNT_PATTERN, name, ()-> 1);
      } else {
         name = UtilityMethods.replaceAll(COUNT_PATTERN, name, ()-> ((Item) entity).getItemStack().getAmount());
      }
      this.entity.setCustomName(name);
      this.time--;
   }

   public void remove() {
      this.entity.remove();
   }
}
