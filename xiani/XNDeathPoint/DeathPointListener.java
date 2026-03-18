package xiani.XNDeathPoint;

import java.time.LocalDateTime;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;

public class DeathPointListener implements Listener {
  private final DeathPointManager deathPointManager;
  
  public DeathPointListener(DeathPointManager deathPointManager) {
    this.deathPointManager = deathPointManager;
  }
  
  @EventHandler
  public void onPlayerDeath(PlayerDeathEvent event) {
    String playerName = event.getEntity().getName();
    String worldName = event.getEntity().getWorld().getName();
    LocalDateTime deathTime = LocalDateTime.now();
    this.deathPointManager.addDeathPoint(playerName, worldName, event.getEntity().getLocation(), deathTime);
  }
  
  @EventHandler
  public void onInventoryClick(InventoryClickEvent event) {
    if (event.getInventory().getHolder() instanceof DeathPointGUI) {
      event.setCancelled(true);
      ((DeathPointGUI)event.getInventory().getHolder()).handleClick(event);
    } 
  }
}
