package di.uniba.leone.parser;

import di.uniba.leone.type.Command;
import di.uniba.leone.type.Item;

/**
 *
 * @author feder
 */
public class ActionInGame {
    private Command command;
    private Item item1;
    private Item item2;

    
    /** 
     * @return Command
     */
    public Command getCommand() {
        return command;
    }

    
    /** 
     * @param command
     */
    public void setCommand(Command command) {
        this.command = command;
    }

    
    /** 
     * @return Item
     */
    public Item getItem1() {
        return item1;
    }

    /**
     *
     * @param item
     */
    public void setItem1(Item item) {
        this.item1 = item;
    }

    /**
     *
     * @return
     */
    public Item getItem2() {
        return item2;
    }

    /**
     *
     * @param item2
     */
    public void setItem2(Item item2) {
        this.item2 = item2;
    }
}
