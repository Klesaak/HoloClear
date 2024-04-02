package ua.klesaak.holoclear;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Entity;
import ua.klesaak.holoclear.util.UtilityMethods;

import static ua.klesaak.holoclear.HoloClearPlugin.TIME_PATTERN;

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
      this.entity.setCustomName(UtilityMethods.replaceAll(TIME_PATTERN, customName, ()-> String.valueOf(this.time)));
      this.time--;
   }
}
