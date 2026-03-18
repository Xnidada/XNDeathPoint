package xiani.XNDeathPoint;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class DeathPointCommand implements CommandExecutor {

    private final DeathPointManager deathPointManager;
    private final Economy economy;
    private final Main plugin;

    public DeathPointCommand(DeathPointManager deathPointManager, Economy economy, Main plugin) {
        this.deathPointManager = deathPointManager;
        this.economy = economy;
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!command.getName().equalsIgnoreCase("deathpoint")) {
            return false;
        }

        if (args.length == 0) {
            return false;
        }

        /* ======================
           /deathpoint open
           ====================== */
        if (args[0].equalsIgnoreCase("open")) {

            // 如果有第二个参数，则是帮助指定玩家打开他的GUI
            if (args.length >= 2) {
                // 控制台或玩家都可以使用此功能
                
                // 检查权限（如果玩家执行的话）
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    if (!player.hasPermission("deathpoint.use.other")) {
                        player.sendMessage(plugin.getMessage("no_permission_other", "&c你没有权限帮助其他玩家打开死亡点界面"));
                        return true;
                    }
                }

                String targetPlayerName = args[1];
                
                // 检查目标玩家是否存在且在线
                Player targetPlayer = Bukkit.getPlayer(targetPlayerName);
                if (targetPlayer == null) {
                    sender.sendMessage(plugin.getMessage("player_not_online", "&c玩家 {player} 不在线或不存在", "{player}", targetPlayerName));
                    return true;
                }

                // 为目标玩家打开他自己的死亡点GUI
                DeathPointGUI gui = new DeathPointGUI(
                        deathPointManager.getDeathPoints(targetPlayerName),
                        economy,
                        plugin
                );
                targetPlayer.openInventory(gui.getInventory());
//                targetPlayer.sendMessage("§f[§bXNDeathPoint§f] §a管理员已为你打开死亡点界面");
                
                // 给执行者发送确认消息
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    player.sendMessage(plugin.getMessage("gui_open_confirm", "&a已为玩家 {player} 打开死亡点界面", "{player}", targetPlayerName));
                } else {
                    // 控制台执行
                    sender.sendMessage(plugin.getMessage("gui_open_confirm", "&a已为玩家 {player} 打开死亡点界面", "{player}", targetPlayerName));
                }
                return true;
            }

            if (!(sender instanceof Player)) {
                sender.sendMessage(plugin.getMessage("console_only_player", "&c只有玩家可以使用此命令打开自己的死亡点界面"));
                return true;
            }

            Player player = (Player) sender;
            
            if (!player.hasPermission("deathpoint.use")) {
                player.sendMessage(plugin.getMessage("no_permission", "&c你没有权限使用此命令"));
                return true;
            }

            DeathPointGUI gui = new DeathPointGUI(
                    deathPointManager.getDeathPoints(player.getName()),
                    economy,
                    plugin
            );
            player.openInventory(gui.getInventory());
            return true;
        }

        /* ======================
           /deathpoint bk
           ====================== */
        if (args[0].equalsIgnoreCase("bk")) {

            if (!(sender instanceof Player)) {
                sender.sendMessage(plugin.getMessage("only_player", "&c只有玩家可以使用此命令"));
                return true;
            }

            Player player = (Player) sender;

            if (!player.hasPermission("deathpoint.use")) {
                player.sendMessage(plugin.getMessage("no_permission", "&c你没有权限使用此命令"));
                return true;
            }

            List<DeathPoint> points = deathPointManager.getDeathPoints(player.getName());

            if (points == null || points.isEmpty()) {
                player.sendMessage(plugin.getMessage("no_death_records", "&c你还没有死亡记录"));
                return true;
            }

            // 最近一次死亡点
            DeathPoint last = points.get(0);

            World world = Bukkit.getWorld(last.getWorldName());
            if (world == null) {
                player.sendMessage(plugin.getMessage("world_not_loaded", "&c死亡点所在世界未加载，无法传送"));
                return true;
            }

            Location loc = last.getLocation();
            loc.setWorld(world);

            player.teleport(loc);
            player.sendMessage(plugin.getMessage("teleport_recent", "&a已传送到最近一次死亡点"));
            return true;
        }

        /* ======================
           /deathpoint reload
           ====================== */
        if (args[0].equalsIgnoreCase("reload")) {

            if (!sender.hasPermission("deathpoint.reload")) {
                sender.sendMessage(plugin.getMessage("no_permission", "&c你没有权限执行此命令"));
                return true;
            }

            plugin.reloadConfig();
            sender.sendMessage(plugin.getMessage("config_reload", "&a已重新加载配置文件"));
            return true;
        }

        return false;
    }
}
