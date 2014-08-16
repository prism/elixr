package us.dhmc.elixr.commands.arguments;

import us.dhmc.elixr.commands.exceptions.CommandArgumentException;

public class ArgumentCurrencyValidator implements ArgumentValidator {

    /**
     * 
     */
    public void validate(String arg) throws CommandArgumentException {
        try {
            Float.parseFloat( arg );
        } catch( NumberFormatException e ){
            throw new CommandArgumentException("Argument "+arg+" invalid: must be currency");
        }
    }
}