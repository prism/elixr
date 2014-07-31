package me.botsko.elixr;

import org.bukkit.Location;
import org.bukkit.World.Environment;

public class WorldUtils {
    
    /**
     * Creates lightning that doesn't strike the ground, only thunder is heard
     * @param block
     */
    public static void thunder( Location loc ){
        loc.setY(350D);
        if(loc.getWorld().getEnvironment() == Environment.NORMAL){
            loc.getWorld().strikeLightning(loc);
        }
    }
}