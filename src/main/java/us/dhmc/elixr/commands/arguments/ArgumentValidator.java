package us.dhmc.elixr.commands.arguments;

import us.dhmc.elixr.commands.exceptions.CommandArgumentException;

public interface ArgumentValidator {

    public void validate( String arg ) throws CommandArgumentException;
    
}