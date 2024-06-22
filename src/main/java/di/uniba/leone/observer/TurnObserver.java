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
            Item item = actioningame.getItem1();
            if (item != null) {
                if (item.isTurnable()) {
                    if (item.isTurned_on() == false) {
                        msg.append("Hai acceso: ").append(item.getFirstName());
                    } else if (item.isTurned_on() == true) {
                        msg.append(item.getFirstName()).append(" e' gia' acceso");
                    }
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
        } else if (actioningame.getCommand().getType() == CommandType.TURN_OFF) {
            Item item = actioningame.getItem1();
            if (item != null) {
                if (item.isTurnable()) {
                    if (item.isTurned_on() == true) {
                        msg.append("Hai spento: ").append(item.getFirstName());
                    } else {
                        msg.append(item.getFirstName()).append(" e' gia' spento.");
                    }
                    // se è la torcia la stanza non è più illuminata 
                    if (item.getID() == 8) {
                        item.setTurned_on(false);
                        game.getCurrentRoom().setLighted(false);
                        msg.append(" La stanza ora è completamente buia.");
                    }
                } else {
                    if (item.getID() == 9) {
                        msg.append("La casa non ti permette di spegnere la televisione.");
                    } else {
                        msg.append("Non puoi spegnere questo oggetto.");
                    }
                }
            } else {
                msg.append("Non c'è niente da spegnere qui.");
            }
        }
        return msg.toString();
    }
}
