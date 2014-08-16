package us.dhmc.elixr.commands;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import us.dhmc.elixr.commands.arguments.Argument;


@Retention(RetentionPolicy.RUNTIME)
public @interface Command {
    
    String[] aliases();

    String desc() default "";
    
    String[] permissions() default {};
    
    Argument[] arguments() default {};
    
    boolean allowsConsole() default true;
    
    boolean playerRequired() default false;

}