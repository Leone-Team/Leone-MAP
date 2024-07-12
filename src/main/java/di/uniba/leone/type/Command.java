/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package di.uniba.leone.type;

import java.util.HashSet;
import java.util.Set;
import java.util.Arrays;

/**
 *
 * @author giann
 */
public class Command {
    
    private CommandType type;
    private Set<String> names = new HashSet();
    
    /**
     *
     * @param type
     * @param names
     */
    public Command(CommandType type, Set<String> names ) {
        this.type = type;
        this.names.addAll(names);
    }

    
    /** 
     * @return CommandType
     */
    public CommandType getType() {
        return type;
    }

    
    /** 
     * @return Set<String>
     */
    public Set<String> getNames() {
        return names;
    }
    
    
    /** 
     * @param type
     */
    public void setType(CommandType type) {
        this.type = type;
    }

    /**
     *
     * @param names
     */
    public void setNames(Set<String> names) {
        this.names = names;
    }

    /**
     *
     * @param names
     */
    public void setNames(String[] names) {
        this.names = new HashSet<>(Arrays.asList(names));
    }
}
