package xiani.XNDeathPoint;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class DeathPointManager {
  private final Map<String, LinkedList<DeathPoint>> deathPoints;
  
  private final File dataFile;
  
  private final YamlConfiguration config;
  
  private final JavaPlugin plugin;
  
  private final List<String> enabledWorlds;
  
  public DeathPointManager(JavaPlugin plugin) {
    this.plugin = plugin;
    this.dataFile = new File(plugin.getDataFolder(), "deathpoints.yml");
    this.config = YamlConfiguration.loadConfiguration(this.dataFile);
    this.deathPoints = loadDeathPoints();
    this.enabledWorlds = plugin.getConfig().getStringList("enabled_worlds");
  }
  
  private Map<String, LinkedList<DeathPoint>> loadDeathPoints() {
    Map<String, LinkedList<DeathPoint>> loadedDeathPoints = new HashMap<>();
    if (this.config.contains("deathpoints"))
      for (String playerName : this.config.getConfigurationSection("deathpoints").getKeys(false)) {
        List<DeathPoint> points = new LinkedList<>();
        for (String key : this.config.getConfigurationSection("deathpoints." + playerName).getKeys(false)) {
          String worldName = this.config.getString("deathpoints." + playerName + "." + key + ".world");
          int x = this.config.getInt("deathpoints." + playerName + "." + key + ".x");
          int y = this.config.getInt("deathpoints." + playerName + "." + key + ".y");
          int z = this.config.getInt("deathpoints." + playerName + "." + key + ".z");
          LocalDateTime time = LocalDateTime.parse(this.config.getString("deathpoints." + playerName + "." + key + ".time"));
          Location location = new Location(this.plugin.getServer().getWorld(worldName), x, y, z);
          points.add(new DeathPoint(worldName, location, time));
        } 
        loadedDeathPoints.put(playerName, (LinkedList<DeathPoint>)points);
      }  
    return loadedDeathPoints;
  }
  
  public void saveDeathPoints() {
    this.config.set("deathpoints", null);
    for (Map.Entry<String, LinkedList<DeathPoint>> entry : this.deathPoints.entrySet()) {
      String playerName = entry.getKey();
      List<DeathPoint> points = entry.getValue();
      for (int i = 0; i < points.size(); i++) {
        DeathPoint point = points.get(i);
        String path = "deathpoints." + playerName + "." + i;
        this.config.set(path + ".world", point.getWorldName());
        this.config.set(path + ".x", Integer.valueOf(point.getLocation().getBlockX()));
        this.config.set(path + ".y", Integer.valueOf(point.getLocation().getBlockY()));
        this.config.set(path + ".z", Integer.valueOf(point.getLocation().getBlockZ()));
        this.config.set(path + ".time", point.getTime().toString());
      } 
    } 
    try {
      this.config.save(this.dataFile);
    } catch (IOException e) {
      e.printStackTrace();
    } 
  }
  
  public void addDeathPoint(String playerName, String worldName, Location location, LocalDateTime time) {
    if (!this.enabledWorlds.contains(worldName))
      return; 
    LinkedList<DeathPoint> playerDeathPoints = this.deathPoints.computeIfAbsent(playerName, k -> new LinkedList());
    if (!playerDeathPoints.isEmpty()) {
      DeathPoint lastDeathPoint = playerDeathPoints.getFirst();
      Duration duration = Duration.between(lastDeathPoint.getTime(), time);
      if (duration.getSeconds() < 5L)
        return; 
    } 
    DeathPoint deathPoint = new DeathPoint(worldName, location, time);
    playerDeathPoints.addFirst(deathPoint);
    if (playerDeathPoints.size() > 9)
      playerDeathPoints.removeLast(); 
    saveDeathPoints();
  }
  
  public List<DeathPoint> getDeathPoints(String playerName) {
    return this.deathPoints.getOrDefault(playerName, new LinkedList<>());
  }
}
