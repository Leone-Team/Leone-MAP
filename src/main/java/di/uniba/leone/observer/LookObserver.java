/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package di.uniba.leone.observer;

import di.uniba.leone.game.Game;
import di.uniba.leone.game.GameObserver;
import di.uniba.leone.parser.ActionInGame;
import di.uniba.leone.type.CommandType;
import di.uniba.leone.type.Item;
import java.util.List;

/**
 *
 * @author giann
 */
public class LookObserver implements GameObserver {
    @Override
    public String update(Game game, ActionInGame actioningame) {
        StringBuilder msg = new StringBuilder();
        if (actioningame.getCommand().getType() == CommandType.LOOK) {
            List<Item> itemsInRoom = game.getCurrentRoom().getItems();
            if (itemsInRoom != null && !itemsInRoom.isEmpty()) {
                msg.append("Noti alcuni oggetti interessanti:\n");
                for (Item item : itemsInRoom) {
                    msg.append("- ").append(item.getDescription()).append("\n");
                }
            } else {
                msg.append("Non noti nulla che ti possa interessare.");
            }
        }
        return msg.toString();
    }

}
