package me.border.tempmute.commands;

import me.border.tempmute.Main;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import me.border.tempmute.utils.Utils;

import java.util.HashMap;

public class CommandUnmute implements CommandExecutor {
    public Main plugin;

    public CommandUnmute(Main plugin){
        this.plugin = plugin;

        plugin.getCommand("unmute").setExecutor(this);
    }

    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (args.length == 1) {
            final OfflinePlayer offlineTarget = Bukkit.getOfflinePlayer(args[0]);
            if (getMute().containsKey(offlineTarget.getName().toLowerCase())) {
                getMute().remove(offlineTarget.getName().toLowerCase());
                for (final Player all : Bukkit.getOnlinePlayers()) {
                    if (all.hasPermission("tempmute.tempmute")) {
                        all.sendMessage(Utils.ucs("Tempmute.unblacklisted").replaceAll("%player%", sender.getName()).replaceAll("%target%", offlineTarget.getName()));
                    }
                }
                if (offlineTarget.isOnline()) {
                    offlineTarget.getPlayer().sendMessage(Utils.ucs("Tempmute.notification_cancel"));
                }
                return true;
            } else {
                sender.sendMessage(Utils.ucs("Tempmute.playerNotBlacklisted").replaceAll("%target%", offlineTarget.getName()));
                return true;
            }
        } else {
           sender.sendMessage(Utils.ucs("illegalArguments"));
        }
        return false;
    }

    public HashMap<String, Long> getMute(){
        return Main.muted;
    }
}
