package ua.klesaak.holoclear;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Projectile;
import ua.klesaak.holoclear.util.UtilityMethods;

import static ua.klesaak.holoclear.HoloClearPlugin.*;

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
      String name = UtilityMethods.replaceAll(TIME_PATTERN, customName, ()-> String.valueOf(this.time));
      if (entity instanceof Projectile) {
         name = UtilityMethods.replaceAll(COUNT_PATTERN, name, ()-> String.valueOf(1));
      } else {
         name = UtilityMethods.replaceAll(COUNT_PATTERN, name, ()-> String.valueOf(((Item) entity).getItemStack().getAmount()));
      }
      this.entity.setCustomName(name);
      this.time--;
   }
}
