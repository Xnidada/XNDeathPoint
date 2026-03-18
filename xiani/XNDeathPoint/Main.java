package xiani.XNDeathPoint;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
  private DeathPointManager deathPointManager;
  
  private Economy economy;
  
  public void onEnable() {
    int pluginId = 22654;
    Metrics metrics = new Metrics(this, pluginId);
    getLogger().info("XNDeathPoint 插件已加载!需要更多定制插件或超低价12900k服务器租用联系QQ:2650297783");
    saveDefaultConfig();
    this.deathPointManager = new DeathPointManager(this);
    if (!setupEconomy()) {
      getLogger().warning("[XNDeathPoint]未找到经济系统插件 Vault 或未设置经济系统，传送将免费进行");
    } 
    getServer().getPluginManager().registerEvents(new DeathPointListener(this.deathPointManager), (Plugin)this);
    getCommand("deathpoint").setExecutor(new DeathPointCommand(this.deathPointManager, this.economy, this));
  }
  
  private boolean setupEconomy() {
    if (getServer().getPluginManager().getPlugin("Vault") == null)
      return false; 
    RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
    if (rsp == null)
      return false; 
    this.economy = (Economy)rsp.getProvider();
    return (this.economy != null);
  }
  
  public void onDisable() {
    getLogger().info("XNDeathPoint 插件已卸载！需要更多定制插件或超低价 12900k 服务器租用联系 QQ:2650297783");
    this.deathPointManager.saveDeathPoints();
  }
  
  /**
   * 获取消息，支持颜色代码和变量替换
   * @param key 消息键
   * @param defaultMsg 默认消息
   * @param placeholders 占位符及其替换值（交替的 key-value 对）
   * @return 处理后的消息
   */
  public String getMessage(String key, String defaultMsg, Object... placeholders) {
    String msg = getConfig().getString("messages." + key, defaultMsg);
    
    // 处理占位符替换
    if (placeholders != null && placeholders.length > 0) {
      for (int i = 0; i < placeholders.length; i += 2) {
        if (i + 1 < placeholders.length && placeholders[i] instanceof String && placeholders[i + 1] != null) {
          msg = msg.replace((String) placeholders[i], placeholders[i + 1].toString());
        }
      }
    }
    
    // 转换颜色代码
    return msg.replace('&', '§');
  }
}
