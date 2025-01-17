package di.uniba.leone.observer;

import di.uniba.leone.game.Game;
import di.uniba.leone.game.GameObserver;
import di.uniba.leone.parser.ActionInGame;
import di.uniba.leone.type.CommandType;
import di.uniba.leone.type.Container;
import di.uniba.leone.type.Item;
import java.io.Serializable;

/**
 *
 * @author giann
 */
public class OpenObserver implements GameObserver, Serializable {

    private static final long serialVersionUID = 1L;

    
    /** 
     * @param game
     * @param actioningame
     * @return String
     */
    @Override
    public String update(Game game, ActionInGame actioningame) {
        StringBuilder msg = new StringBuilder();
        if (actioningame.getCommand().getType() == CommandType.OPEN) {
            if (actioningame.getItem1() == null) {
                msg.append("Non puoi aprire nulla.");
            } else {
                Item item = actioningame.getItem1();
                if (item instanceof Container) {
                    Container container = (Container) item;
                    msg.append("Hai aperto: ").append(container.getFirstName());
                    if (!container.getItems().isEmpty()) {
                        msg.append(". ").append(container.getFirstName()).append(" contiene:");
                        for (Integer itemId : container.getItems()) {
                            Item next = game.getItemByID(itemId);
                            msg.append(" ").append(next.getFirstName());
                        }
                    } else {
                        msg.append(". ").append(container.getFirstName()).append(" è vuoto.");
                    }
                    msg.append("\n");
                } else {
                    msg.append("Non puoi aprire questo oggetto.");
                }
            }
        }
        return msg.toString();
    }
}
