package me.border.tempmute.listeners;

import me.border.tempmute.Main;
import me.border.tempmute.commands.CommandTempmute;
import me.border.tempmute.utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.HashMap;

public class MuteListener implements Listener {
    public Main plugin;
    public MuteListener(Main plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e){
        Player p = e.getPlayer();

        if(getMute().containsKey(p.getName().toLowerCase())){
            if(getMute().get(p.getName().toLowerCase()) != null){
                long endOfMute = getMute().get(p.getName().toLowerCase());
                long now = System.currentTimeMillis();
                long diff = endOfMute - now;

                if(diff<=0){
                    getMute().remove(p.getName().toLowerCase());
                }else{
                    e.setCancelled(true);
                    p.sendMessage(Utils.ucs("Tempmute.attempt_talk").replaceAll("%time%", CommandTempmute.getMSG(endOfMute)));
                }
            }
        }
    }

    public HashMap<String, Long> getMute(){
        return Main.muted;
    }

}
