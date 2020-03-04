package me.border.tempmute.commands;

import me.border.tempmute.Main;
import me.border.tempmute.utils.MuteUnit;
import me.border.tempmute.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class CommandTempmute implements CommandExecutor {

    public Main plugin;

    public CommandTempmute(Main plugin){
        this.plugin = plugin;

        plugin.getCommand("tempmute").setExecutor(this);
    }

    public boolean onCommand(final CommandSender sender,final Command cmd,final String label,final String[] args) {
        if (sender.hasPermission("tempmute.tempmute")){
            if (args.length <= 1){
                sender.sendMessage(Utils.ucs("Tempmute.correct_usage"));
                return true;
            }
            final OfflinePlayer offlineTarget = Bukkit.getOfflinePlayer(args[0]);
            try
            {
                final String unit = args[1].substring(args[1].length() - 1);
                final int temp = Integer.parseInt(args[1].substring(0, args[1].length() - 1));
                long endOfMute = System.currentTimeMillis() + MuteUnit.getTicks(unit, temp);

                long now = System.currentTimeMillis();
                long diff = endOfMute - now;
                if (diff > 0) {
                    getMute().remove(offlineTarget.getName().toLowerCase());
                    setMute(offlineTarget.getName().toLowerCase(), endOfMute);

                    args[0] = "";
                    args[1] = "";
                    String message = getMSG(endOfMute);
                    StringBuilder reason = new StringBuilder();
                    for (String s : args) {
                        reason.append(s);
                    }
                    for (final Player all : Bukkit.getServer().getOnlinePlayers()) {
                        if (all.hasPermission("tempmute.tempmute")) {
                            all.sendMessage(Utils.ucs("Tempmute.notification").replaceAll("%player%", sender.getName()).replaceAll("%target%", offlineTarget.getName()).replaceAll("%time%", message).replaceAll("%reason%", String.valueOf(reason)));
                        }
                    }
                    if (offlineTarget.isOnline()) {
                        offlineTarget.getPlayer().sendMessage(Utils.ucs("Tempmute.blacklisted").replaceAll("%time%", message));
                    }
                    return true;
                } else {
                    sender.sendMessage(Utils.ucs("Tempmute.correct_usage"));
                    return true;
                }
            } catch (NumberFormatException e){
                sender.sendMessage(Utils.ucs("Tempmute.correct_usage"));
                return true;
            }
        } else {
            sender.sendMessage(Utils.ucs("noPermission"));
        }

        return false;
    }


    public HashMap<String, Long> getMute(){
        return Main.muted;
    }

    public void setMute(String name, long end){
        getMute().put(name, end);
    }

    public static String getMSG(long endOfMute){
        String message = "";

        long now = System.currentTimeMillis();
        long diff = endOfMute - now;
        int seconds = (int) (diff / 1000);

        if(seconds >= 60*60*24){
            int days = seconds / (60*60*24);
            seconds = seconds % (60*60*24);

            message += days + " Day(s) ";
        }
        if(seconds >= 60*60){
            int hours = seconds / (60*60);
            seconds = seconds % (60*60);

            message += hours + " Hour(s) ";
        }
        if(seconds >= 60){
            int min = seconds / 60;
            seconds = seconds % 60;

            message += min + " Minute(s) ";
        }
        if(seconds >= 0){
            message += seconds + " Second(s) ";
        }

        return message;
    }
}

