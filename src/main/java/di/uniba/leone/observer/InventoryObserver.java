/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package di.uniba.leone.observer;

import di.uniba.leone.game.GameObserver;
import di.uniba.leone.game.Game;
import di.uniba.leone.parser.ActionInGame;
import di.uniba.leone.type.CommandType;
import di.uniba.leone.type.Item;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author giann
 */
public class InventoryObserver implements GameObserver, Serializable {

    private static final long serialVersionUID = 1L;

    @Override
    public String update(Game game, ActionInGame actioningame) {
        StringBuilder msg = new StringBuilder();
        if (actioningame.getCommand().getType() == CommandType.INVENTORY) {
            if (game.getInventory().isEmpty()) {
                msg.append("Il tuo inventario è vuoto!");
            } else {
                msg.append("Nel tuo inventario ci sono:\n");
                for (Integer itemID : game.getInventory()) {
                    Item item = game.getItemByID(itemID);
                    msg.append(item.getFirstName()).append(": ").append(item.getDescription()).append("\n");
                }
            }
        }
        return msg.toString();
    }
}
