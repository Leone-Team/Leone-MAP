package di.uniba.leone.observer;

import di.uniba.leone.game.Game;
import di.uniba.leone.game.GameObserver;
import di.uniba.leone.parser.ActionInGame;
import di.uniba.leone.type.CommandType;
import static di.uniba.leone.type.CommandType.TURN_OFF;
import static di.uniba.leone.type.CommandType.TURN_ON;
import static di.uniba.leone.type.CommandType.WEAR;
import di.uniba.leone.type.Item;
import di.uniba.leone.type.QuestionRiddle;
import di.uniba.leone.type.Riddle;
import java.io.Serializable;
import java.util.Scanner;

public class TurnObserver implements GameObserver, Serializable {

    private static final long serialVersionUID = 1L;

    @Override
    public String update(Game game, ActionInGame actioningame) {
        StringBuilder msg = new StringBuilder();
        CommandType commandType = actioningame.getCommand().getType();
        Boolean rightCmds = commandType == CommandType.TURN_ON
                || commandType == CommandType.TURN_OFF
                || commandType == CommandType.WEAR;
        Item item = actioningame.getItem1();

        if (rightCmds) {
            if (item == null) {
                msg.append("Non c'è niente qui.");
                return msg.toString();
            }

            boolean isPickupable = item.isPickupable();
            boolean isTurnable = item.isTurnable();

            if (isPickupable) {
                // Controllo che l'oggetto sia nell'inventario se è pickupable
                if (!game.getInventory().contains(item.getID())) {
                    msg.append("Non puoi usare questo oggetto perché non è nel tuo inventario.");
                    return msg.toString();
                } else {
                    switch (commandType) {
                        case TURN_ON ->
                            handleTurnOn(game, item, msg);

                        case TURN_OFF ->
                            handleTurnOff(game, item, msg);

                        case WEAR ->
                            handleWear(game, item, msg);

                        default ->
                            msg.append("Azione non riconosciuta.");
                    }
                }
            } else if (isTurnable) {
                // Controllo che l'oggetto sia nella stanza se è solo turnable
                if (!game.getCurrentRoom().getItems().contains(item.getID())) {
                    msg.append("Non puoi usare questo oggetto perché non è nella stanza.");
                    return msg.toString();
                } else {
                    switch (commandType) {
                        case TURN_ON ->
                            handleTurnOn(game, item, msg);

                        case TURN_OFF ->
                            handleTurnOff(game, item, msg);

                        case WEAR ->
                            handleWear(game, item, msg);

                        default ->
                            msg.append("Azione non riconosciuta.");
                    }
                }
            } else {
                msg.append("Non puoi fare questa azione con questo oggetto.");
                return msg.toString();
            }

        }
        return msg.toString();
    }

    private void handleTurnOn(Game game, Item item, StringBuilder msg) {
        if (item.isTurnable()) {
            if (!item.isTurned_on()) {
                Riddle riddle = game.getRiddles().values().stream()
                        .filter(r -> r.getTargetItem().equals(item.getID()))
                        .findFirst().orElse(null);
                if (riddle != null && riddle instanceof QuestionRiddle qRiddle) {
                    // Per accendere o spegnere l'oggetto devi rispondere all'indovinello
                    System.out.println(qRiddle.getDescription());
                    System.out.print(qRiddle.getQuestion() + "\nRisposta>");
                    Scanner scanner = new Scanner(System.in);
                    String ans = scanner.nextLine();
                    qRiddle.resolved(ans);
                }
                if (riddle != null && riddle.isSolved()) {
                    item.setTurned_on(true);
                    msg.append("Hai acceso: ").append(item.getFirstName());
                } else if (riddle != null && !riddle.isSolved() && riddle instanceof QuestionRiddle qRiddle) {
                    if (qRiddle.getCounter() == 3) {
                        game.setRunning(false);
                        qRiddle.setCounter(0);
                        game.getMrMsg().displayMsg(qRiddle.getDeathMsg());
                    } else {
                        game.getMrMsg().displayMsg(">Risposta errata!");
                    }
                } else if (riddle == null) {
                    item.setTurned_on(true);
                    msg.append("Hai acceso: ").append(item.getFirstName());
                }
            } else {
                msg.append(item.getFirstName()).append(" è già acceso.");
            }
            // Se è la torcia, illumina la stanza 
            if (item.getID() == 8) {
                game.getCurrentRoom().setLighted(true);
                msg.append(" La stanza è ora illuminata.");
            }
        } else {
            msg.append("Non puoi accendere questo oggetto.");
        }
    }

    private void handleTurnOff(Game game, Item item, StringBuilder msg) {
        if (item.isTurnable()) {
            if (item.isTurned_on()) {
                Riddle riddle = game.getRiddles().values().stream()
                        .filter(r -> r.getTargetItem().equals(item.getID()))
                        .findFirst().orElse(null);
                if (riddle != null && riddle instanceof QuestionRiddle qRiddle) {
                    // Per accendere o spegnere l'oggetto devi rispondere all'indovinello
                    game.getMrMsg().displayMsg(qRiddle.getDescription());
                    game.getMrMsg().displayMsg(qRiddle.getQuestion() + "\nRisposta>");
                    String ans = game.getMrMsg().getMsg();
                    qRiddle.resolved(ans);
                }
                if (riddle != null && riddle.isSolved()) {
                    item.setTurned_on(false);
                    msg.append("Hai spento: ").append(item.getFirstName());
                    if(riddle.getId() == 5){
                        game.getItems().get(17).setTurnable(true);//sblocca il quadro
                        game.getMrMsg().displayMsg("Il quadro non è più protetto");
                    }
                    if(riddle.getId() == 6)
                    {
                        item.setTurned_on(false);
                        game.getMrMsg().displayMsg(">Hai vinto A.L.! Adesso Marilù e Leone saranno al sicuro! Anche Giustino.");
                        game.setWin(true);
                    }
                } else if (riddle != null && !riddle.isSolved() && riddle instanceof QuestionRiddle qRiddle) {
                    if (qRiddle.getCounter() == 3) {
                        game.setRunning(false);
                        qRiddle.setCounter(0);
                        game.getMrMsg().displayMsg(qRiddle.getDeathMsg());
                    } else {
                        game.getMrMsg().displayMsg(">Risposta errata!");
                    }
                } else if (riddle == null) {
                    item.setTurned_on(false);
                    msg.append("Hai spento: ").append(item.getFirstName());
                }
            } else {
                msg.append(item.getFirstName()).append(" è già spento.");
            }
            // Se è la torcia, la stanza non è più illuminata 
            if (item.getID() == 8) {
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
    }

    private void handleWear(Game game, Item item, StringBuilder msg) {
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
