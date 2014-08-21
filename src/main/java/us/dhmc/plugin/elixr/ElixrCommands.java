package us.dhmc.plugin.elixr;

import java.util.List;

import org.bukkit.command.CommandSender;

import us.dhmc.elixr.commands.Command;
import us.dhmc.elixr.commands.CommandArguments;
import us.dhmc.elixr.commands.NestedCommands;
import us.dhmc.elixr.commands.arguments.Argument;

public class ElixrCommands {
    
    /**
     * Root level commands
     */
    public static class ParentCommand {
        @Command(
            aliases = {"e","elixr"},
            desc = "Testing elixr commands framework",
            arguments = {
                @Argument( name = "keyword", defaultValue = "albedo" ),
                @Argument( name = "keyword2" )
            }
        )
        @NestedCommands(ElixrCommands.class)
        public static void shop( final CommandSender sender, final CommandArguments args ){
            

            Elixr.msg.know("i",args.length()).send(sender,":msg Arg length: {i}" );
            for( String arg : args.get() ){
                Elixr.msg.know("i",arg).send(sender,":msg arg: {i}" );
            }
            
            Elixr.msg.know("c","/e ?").send(sender,":msg This command does nothing. Check {c|cmd}" );
        }
    }
    
    
    /**
     * 
     * @param args
     * @param sender
     * @throws CommandException
     */
    @Command(aliases = { "help","?" }, desc = "View help" )
    public static void help( final CommandSender sender, final CommandArguments args ){
        List<String> usages = Elixr.commands.getUsages();
        for( String help : usages ){
            Elixr.msg.send(sender,":msg " + help );
        }
    }
}