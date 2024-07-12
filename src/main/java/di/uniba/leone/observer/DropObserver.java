package di.uniba.leone.observer;

import di.uniba.leone.game.Game;
import di.uniba.leone.game.GameObserver;
import di.uniba.leone.parser.ActionInGame;
import di.uniba.leone.type.CommandType;
import di.uniba.leone.type.Item;
import java.io.Serializable;

public class DropObserver implements GameObserver, Serializable {
    private static final long serialVersionUID = 1L;
    
    @Override
    public String update(Game game, ActionInGame actioningame) {
        StringBuilder msg = new StringBuilder();
        if (actioningame.getCommand().getType() == CommandType.DROP) {
            Item item = actioningame.getItem1();
            if (item != null) {
                if (game.getInventory().contains(item.getID())) {
                    game.getInventory().remove(item.getID());
                    game.getCurrentRoom().getItems().add(item.getID());
                    msg.append("Hai lasciato: ").append(item.getDescription());
                } else {
                    msg.append("Non hai questo oggetto nell'inventario.");
                }
            } else {
                msg.append("Non c'è niente da lasciare qui.");
            }
        }
        return msg.toString();
    }
}