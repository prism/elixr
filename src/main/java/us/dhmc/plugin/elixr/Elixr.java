package us.dhmc.plugin.elixr;

import static org.bukkit.ChatColor.*;
import mkremins.fanciful.FancyMessage;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import us.dhmc.elixr.StringTheory;
import us.dhmc.elixr.StringTheory.StringBaseline;
import us.dhmc.elixr.StringTheory.TokenFilter;
import us.dhmc.elixr.commands.CommandManager;
import us.dhmc.elixr.commands.exceptions.CommandArgumentException;
import us.dhmc.elixr.commands.exceptions.CommandPermissionException;
import us.dhmc.elixr.commands.exceptions.IllegalCommandSenderException;
import us.dhmc.plugin.elixr.ElixrCommands.ParentCommand;

public class Elixr extends JavaPlugin {

    public static CommandManager commands;
    public static StringTheory msg;
    
    
    /**
     * Enables the plugin and activates our player listeners
     */
    @Override
    public void onEnable(){
        
        // Init messenger
        msg = new StringTheory();
        msg.baseline(":head", new StringBaseline(WHITE){
            public void format( FancyMessage fancy ){
                fancy.then("craftys // ").color(GOLD);
            }
        });
        msg.baseline(":error", new StringBaseline(RED){
            public void format( FancyMessage fancy ){
                fancy.then("craftys // ").color(GOLD);
            }
        });
        msg.filter("cmd", new TokenFilter(){
            public void format( FancyMessage fancy, String tokenVal ){
                fancy.then(tokenVal).color(YELLOW).suggest(tokenVal).tooltip("Click to prefill command!");
            }
        });
        
        setupCommands();
    }
    
    /**
     * 
     */
    private void setupCommands() {
        commands = new CommandManager(this);
        try {
            commands.register( ParentCommand.class );
        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }
    
    /**
     * 
     */
    @Override
    public boolean onCommand( CommandSender sender, Command cmd, String commandLabel, String[] args ){
        try {
            commands.execute( sender, cmd, args );
        } catch ( IllegalCommandSenderException e ){
            msg.send(sender,":error "+ e.getMessage());
        } catch ( CommandPermissionException e ){
            msg.send(sender,":error "+ e.getMessage());
        } catch ( CommandArgumentException e ) {
            msg.send(sender,":error "+ e.getMessage());
        } catch ( Exception e ){
            e.printStackTrace();
        }
        return true;
    }
}