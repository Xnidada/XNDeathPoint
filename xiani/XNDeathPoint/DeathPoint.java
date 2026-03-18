package xiani.XNDeathPoint;

import java.time.LocalDateTime;
import org.bukkit.Location;

public class DeathPoint {
  private final String worldName;
  
  private final Location location;
  
  private final LocalDateTime time;
  
  public DeathPoint(String worldName, Location location, LocalDateTime time) {
    this.worldName = worldName;
    this.location = location;
    this.time = time;
  }
  
  public String getWorldName() {
    return this.worldName;
  }
  
  public Location getLocation() {
    return this.location;
  }
  
  public LocalDateTime getTime() {
    return this.time;
  }
}
