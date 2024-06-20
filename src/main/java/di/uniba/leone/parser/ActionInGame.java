/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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

    public Command getCommand() {
        return command;
    }

    public void setCommand(Command command) {
        this.command = command;
    }

    public Item getItem1() {
        return item1;
    }

    public void setItem1(Item item) {
        this.item1 = item;
    }

    public Item getItem2() {
        return item2;
    }

    public void setItem2(Item item2) {
        this.item2 = item2;
    }
}
