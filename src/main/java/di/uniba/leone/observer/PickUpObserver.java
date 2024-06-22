package di.uniba.leone.observer;

import di.uniba.leone.game.Game;
import di.uniba.leone.game.GameObserver;
import di.uniba.leone.parser.ActionInGame;
import di.uniba.leone.type.CommandType;
import di.uniba.leone.type.Container;
import di.uniba.leone.type.Item;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author giann
 */
public class PickUpObserver implements GameObserver {

    @Override
    public String update(Game game, ActionInGame actioningame) {
        StringBuilder msg = new StringBuilder();
        if (actioningame.getCommand().getType() == CommandType.PICK_UP) {
            Item item = actioningame.getItem1();
            if (item != null) {
                if (item.isPickupable()) {
                    boolean itemFound = false;

                    // Controlla se l'oggetto è direttamente nella stanza
                    if (game.getCurrentRoom().getItems().contains(actioningame.getItem1().getID())) {
                        game.getInventory().add(item.getID());
                        game.getCurrentRoom().getItems().remove(actioningame.getItem1().getID());
                        msg.append("Hai raccolto: ").append(item.getDescription());
                        itemFound = true;
                    }

                    // Cerca l'oggetto nei contenitori nella stanza solo se non è stato trovato nella stanza
                    if (!itemFound) {
                        itemFound = scanContainersForItem(game, actioningame.getItem1().getID(), msg);
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

    private boolean scanContainersForItem(Game game, int itemID, StringBuilder msg) {
        List<Integer> itemIds = game.getCurrentRoom().getItems(); // Lista di ID degli item nella stanza
        for (Integer itemId : itemIds) {
            Item roomItem = game.getItemByID(itemId);
            if (roomItem instanceof Container) {
                Container container = (Container) roomItem;
                Iterator<Integer> iterator = container.getItems().iterator();
                while (iterator.hasNext()) {
                    Integer containerItemId = iterator.next();
                    if (containerItemId.equals(itemID)) {
                        game.getInventory().add(containerItemId);
                        iterator.remove(); // Rimuove l'ID dell'oggetto dal contenitore
                        msg.append("Hai raccolto: ").append(game.getItemByID(containerItemId).getDescription());
                        return true; // Oggetto trovato e raccolto, uscita dal metodo
                    }
                }
            }
        }
        return false; // Oggetto non trovato nei contenitori
    }
}