/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package di.uniba.leone.observer;

import di.uniba.leone.game.Game;
import di.uniba.leone.game.GameObserver;
import di.uniba.leone.parser.ActionInGame;
import di.uniba.leone.type.CommandType;
import di.uniba.leone.type.Container;
import di.uniba.leone.type.Item;
import java.util.Iterator;

/**
 *
 * @author giann
 */
public class PickUpObserver implements GameObserver {
    @Override
    public String update(Game game, ActionInGame actioningame) {
        StringBuilder msg = new StringBuilder();
        if (actioningame.getCommand().getType() == CommandType.PICK_UP) {
            Item item = actioningame.getItem();
            if (item != null) {
                if (item.isPickupable()) {
                    boolean itemFound = false;

                    // Controlla se l'oggetto è direttamente nella stanza
                    if (game.getCurrentRoom().getItems().contains(item)) {
                        game.getInventory().add(item.getID());
                        game.getCurrentRoom().getItems().remove(item);
                        msg.append("Hai raccolto: ").append(item.getDescription());
                        itemFound = true;
                    }

                    // Cerca l'oggetto nei contenitori nella stanza solo se non è stato trovato nella stanza
                    if (!itemFound) {
                        itemFound = scanContainersForItem(game, item, msg);
                    }

                    // Se l'oggetto non è stato trovato né nella stanza né nei contenitori
                    if (!itemFound) {
                        msg.append("Non riesci a trovare l'oggetto da raccogliere.");
                    }
                } else {
                    msg.append("Non puoi raccogliere questo oggetto.");
                }
            } else {
                msg.append("Non c'è niente da raccogliere qui.");
            }
        }
        return msg.toString();
    }

    private boolean scanContainersForItem(Game game, Item item, StringBuilder msg) {
        for (Item roomItem : game.getCurrentRoom().getItems()) {
            if (roomItem instanceof Container) {
                Container container = (Container) roomItem;
                Iterator<Integer> iterator = container.getItems().iterator();
                while (iterator.hasNext()) {
                    Integer itemId = iterator.next();
                    if (itemId.equals(item.getID())) {
                        game.getInventory().add(itemId);
                        iterator.remove(); // Rimuove l'oggetto dal contenitore
                        msg.append("Hai raccolto: ").append(item.getDescription());
                        return true; // Oggetto trovato e raccolto, uscita dal metodo
                    }
                }
            }
        }
        return false; // Oggetto non trovato nei contenitori
    }
}
