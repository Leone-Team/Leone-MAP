package di.uniba.leone.observer;

import di.uniba.leone.game.Game;
import di.uniba.leone.game.GameObserver;
import di.uniba.leone.parser.ActionInGame;
import di.uniba.leone.type.CommandType;
import di.uniba.leone.type.Item;

public class TurnObserver implements GameObserver {
    @Override
    public String update(Game game, ActionInGame actioningame) {
        StringBuilder msg = new StringBuilder();
        if (actioningame.getCommand().getType() == CommandType.TURN_ON) {
            Item item = actioningame.getItem();
            if (item != null) {
                if (item.isTurnable()) {
                    msg.append("Hai acceso: ").append(item.getFirstName());
                    // se è la torcia mi illumina la stanza 
                    if (item.getID() == 8) {
                        game.getCurrentRoom().setLighted(true);
                        msg.append(" La stanza è ora illuminata.");
                    }
                } else {
                    msg.append("Non puoi accendere questo oggetto.");
                }
            } else {
                msg.append("Non c'è niente da accendere qui.");
            }
        }
        return msg.toString();
    }
}
