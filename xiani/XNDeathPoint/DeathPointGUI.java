package xiani.XNDeathPoint;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class DeathPointGUI implements InventoryHolder {
  private final Inventory inventory;
  
  private final Economy economy;
  
  private final Main plugin;
  
  public DeathPointGUI(List<DeathPoint> deathPoints, Economy economy, Main plugin) {
    this.plugin = plugin;
    String guiTitle = plugin.getConfig().getString("gui.title", "&f[&bXNDeathPoint&f] &c死亡点传送");
    this.inventory = Bukkit.createInventory(this, 9, guiTitle.replace('&', '§'));
    this.economy = economy;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    for (int i = 0; i < deathPoints.size(); i++) {
      DeathPoint deathPoint = deathPoints.get(i);
      String pointKey = "point" + (i + 1);
      String materialName = plugin.getConfig().getString("gui.items." + pointKey + ".material", "SKULL_ITEM");
      Material material = Material.matchMaterial(materialName);
      if (material == null)
        material = Material.SKULL_ITEM; 
      ItemStack item = new ItemStack(material, 1);
      ItemMeta meta = item.getItemMeta();
      String displayName = plugin.getConfig().getString("gui.items." + pointKey + ".name", "§c死亡点 #" + (i + 1));
      meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', displayName));
      List<String> loreConfig = plugin.getConfig().getStringList("gui.items." + pointKey + ".lore");
      if (loreConfig.isEmpty())
        loreConfig = Arrays.asList(new String[] { "§7世界: §f" + deathPoint
              .getWorldName(), "§7坐标: §f" + deathPoint
              .getLocation().getBlockX() + ", " + deathPoint.getLocation().getBlockY() + ", " + deathPoint.getLocation().getBlockZ(), "§7时间: §f" + deathPoint
              .getTime().format(formatter), "§6所需金币: §e" + plugin
              .getConfig().getInt("teleport_costs." + pointKey, 0) + "金币" }); 
      List<String> lore = new ArrayList<>();
      for (String line : loreConfig)
        lore.add(ChatColor.translateAlternateColorCodes('&', line)
            .replace("{world}", deathPoint.getWorldName())
            .replace("{x}", String.valueOf(deathPoint.getLocation().getBlockX()))
            .replace("{y}", String.valueOf(deathPoint.getLocation().getBlockY()))
            .replace("{z}", String.valueOf(deathPoint.getLocation().getBlockZ()))
            .replace("{time}", deathPoint.getTime().format(formatter))
            .replace("{cost}", String.valueOf(plugin.getConfig().getInt("teleport_costs." + pointKey, 0)))); 
      meta.setLore(lore);
      item.setItemMeta(meta);
      this.inventory.setItem(i, item);
    } 
  }
  
  public Inventory getInventory() {
    return this.inventory;
  }
  
  public void handleClick(InventoryClickEvent event) {
    if (event.getCurrentItem() != null && event.getCurrentItem().getItemMeta() != null) {
      final Player player = (Player)event.getWhoClicked();
      final int slot = event.getSlot();
      if (slot >= 0 && slot < this.inventory.getSize()) {
        ItemStack item = this.inventory.getItem(slot);
        if (item != null && item.getItemMeta() != null) {
          List<String> lore = item.getItemMeta().getLore();
          if (lore != null && lore.size() >= 4) {
            final int cost;
            final String worldName = ChatColor.stripColor(((String)lore.get(0)).substring(6));
            String[] coords = ChatColor.stripColor(((String)lore.get(1)).substring(6)).split(", ");
            final int x = Integer.parseInt(coords[0]);
            final int y = Integer.parseInt(coords[1]);
            final int z = Integer.parseInt(coords[2]);
            try {
              cost = Integer.parseInt(ChatColor.stripColor(lore.get(3)).replaceAll("[^0-9]", ""));
            } catch (NumberFormatException e) {
              player.sendMessage(plugin.getMessage("cost_parse_error", "&c传输点费用解析错误，请联系管理员！"));
              e.printStackTrace();
              return;
            } 
            // 检查是否有经济系统
            boolean hasEconomy = (DeathPointGUI.this.economy != null);
            
            // 如果有经济系统，检查余额；如果没有则直接传送
            if (!hasEconomy || this.economy.getBalance((OfflinePlayer)player) >= cost) {
              (new BukkitRunnable() {
                  public void run() {
                    Location location = new Location(Bukkit.getWorld(worldName), x, y, z);
                    player.teleport(location);
                    player.sendMessage(plugin.getMessage("teleport_success", "&a已传送到死亡点 #{slot}", "{slot}", String.valueOf(slot + 1)));
                    
                    // 关闭 GUI
                    player.closeInventory();
                    
                    // 如果有经济系统才扣费
                    if (hasEconomy && cost > 0) {
                      DeathPointGUI.this.economy.withdrawPlayer((OfflinePlayer)player, cost);
                    }
                  }
                }).runTask((Plugin)this.plugin);
            } else {
              player.sendMessage(plugin.getMessage("not_enough_money", "&c你没有足够的金币传送到此死亡点！"));
            } 
          } 
        } 
      } 
    } 
  }
}
