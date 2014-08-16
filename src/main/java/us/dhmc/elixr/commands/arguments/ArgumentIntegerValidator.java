package us.dhmc.elixr.commands.arguments;

import us.dhmc.elixr.commands.exceptions.CommandArgumentException;

public class ArgumentIntegerValidator implements ArgumentValidator {

    /**
     * 
     */
    public void validate(String arg) throws CommandArgumentException {
        try {
            Integer.parseInt( arg );
        } catch( NumberFormatException e ){
            throw new CommandArgumentException("Argument "+arg+" invalid: must be a number");
        }
    }
}