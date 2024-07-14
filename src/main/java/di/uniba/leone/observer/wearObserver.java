package di.uniba.leone.observer;

import di.uniba.leone.game.Game;
import di.uniba.leone.game.GameObserver;
import di.uniba.leone.parser.ActionInGame;
import di.uniba.leone.type.CommandType;
import di.uniba.leone.type.Item;
import java.io.Serializable;

/**
 *
 * @author feder
 */
public class wearObserver implements GameObserver, Serializable {

    
    /** 
     * @param game
     * @param actioningame
     * @return String
     */
    @Override
    public String update(Game game, ActionInGame actioningame) {

        StringBuilder msg = new StringBuilder();
        CommandType commandType = actioningame.getCommand().getType();
        Boolean rightCmds = commandType == CommandType.WEAR;
        Item item = actioningame.getItem1();

        if (rightCmds) {
            if (item == null) {
                msg.append(">Cosa indossa?");
                return msg.toString();
            } else {
                if (!game.getInventory().contains(item.getID())) {
                    msg.append(">Non puoi indossare questo oggetto perché non è nel tuo inventario.");
                    return msg.toString();
                } else {
                    switch (item.getFirstName().toLowerCase()) {
                        case "cuffie" -> {
                            if (game.getCurrentRoom().getName().equals("Camera da Letto")) {
                                game.getItemByID(item.getID()).setTurned_on(true);
                                msg.append("E chi l'avrebbe detto che Rick Astley sarebbe stato più piacevole di una semplice sveglia... Finalmente puoi esplorare la stanza! \n");
                            } else {
                                msg.append("Mi dispiace, sei stato rickrollato, è in riproduzione Never Gonna Give You Up!! \n");
                            }
                        }
                        case "occhiali" -> {
                            if (game.getCurrentRoom().getName().equals("Soggiorno")) {
                                game.getItemByID(item.getID()).setTurned_on(true);
                                msg.append("Finalmente riesci a vedere qualcosa! \n");
                            } else {
                                msg.append("Devo ammettere che ti donano... Nah non è vero e poi non vedi nulla, a cosa ti servono qui?? \n");
                            }
                        }
                        case "tuta" -> {
                            if (game.getCurrentRoom().getName().equals("Cantina")) {
                                game.getItemByID(item.getID()).setTurned_on(true);
                                msg.append("Grazie alle ormai vecchie doti da pompiere di Giustino sei in grado di resistere al caldo! \n");
                            } else {
                                msg.append("E tu vorresti metterti a fare il pompiere in ").append(game.getCurrentRoom().getName()).append(" ?? Patetico... \n");
                            }
                        }
                        default ->
                            msg.append("Non puoi indossare questo oggetto.");
                    }
                }
            }
        }
        return msg.toString();
    }
}

