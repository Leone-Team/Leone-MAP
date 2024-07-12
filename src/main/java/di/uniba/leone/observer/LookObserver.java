package di.uniba.leone.observer;

import di.uniba.leone.game.Game;
import di.uniba.leone.game.GameObserver;
import di.uniba.leone.parser.ActionInGame;
import di.uniba.leone.type.CommandType;
import di.uniba.leone.type.Item;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author giann
 */
public class LookObserver implements GameObserver, Serializable {
       private static final long serialVersionUID = 1L; 
    
    /** 
     * @param game
     * @param actioningame
     * @return String
     */
    @Override
    public String update(Game game, ActionInGame actioningame) {
        StringBuilder msg = new StringBuilder();
        if (actioningame.getCommand().getType() == CommandType.LOOK) {
            if (!game.getCurrentRoom().isLighted()) {
                msg.append("È troppo buio per vedere qualcosa.");
            } else {
                List<Integer> itemIdsInRoom = game.getCurrentRoom().getItems();
                if (itemIdsInRoom != null && !itemIdsInRoom.isEmpty()) {
                    msg.append("Noti alcuni oggetti interessanti:\n");
                    for (Integer itemId : itemIdsInRoom) {
                        Item item = game.getItemByID(itemId);
                        if (item != null) {
                            msg.append("- ").append(item.getDescription()).append("\n");
                        }
                    }
                } else {
                    msg.append("Non noti nulla che ti possa interessare.");
                }
            }
        }
        return msg.toString();
    }
}
