package me.border.tempmute;

import me.border.tempmute.commands.CommandTempmute;
import me.border.tempmute.commands.CommandUnmute;
import me.border.tempmute.listeners.MuteListener;
import me.border.tempmute.utils.Utils;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.util.HashMap;

public class Main extends JavaPlugin {

    public static HashMap<String, Long> muted = new HashMap<String, Long>();
    public static String Path = "plugins/SeniorTeamTempmute" + File.separator + "MuteList.dat";

    public void onEnable(){
        getConfig().options().copyDefaults(true);
        new CommandTempmute(this);
        new Utils(this);
        new CommandUnmute(this);
        getServer().getPluginManager().registerEvents(new MuteListener(this), this);

        File file = new File(Path);
        new File("plugins/SeniorTeamTempmute").mkdir();

        if(file.exists()){
            muted = load();
        }

        if(muted == null){
            muted = new HashMap<String, Long>();
        }
    }

    public void onDisable(){
        save();
    }

    public static void save(){
        File file = new File("plugins/SeniorTeamTempmute" + File.separator + "MuteList.dat");
        new File("plugins/SeniorTeamTempmute").mkdir();
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try{
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(Path));
            oos.writeObject(muted);
            oos.flush();
            oos.close();
            //Handle I/O exceptions
        }catch(Exception e){
            e.printStackTrace();
        }
    }


    @SuppressWarnings("unchecked")
    public static HashMap<String, Long> load(){
        try{
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(Path));
            Object result = ois.readObject();
            ois.close();
            return (HashMap<String,Long>)result;
        }catch(Exception e){
            return null;
        }
    }
}
