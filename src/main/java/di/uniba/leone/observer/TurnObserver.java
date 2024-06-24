package di.uniba.leone.observer;

import di.uniba.leone.game.Game;
import di.uniba.leone.game.GameObserver;
import di.uniba.leone.parser.ActionInGame;
import di.uniba.leone.type.CommandType;
import di.uniba.leone.type.Item;
import di.uniba.leone.type.QuestionRiddle;
import di.uniba.leone.type.Riddle;
import java.util.Scanner;

public class TurnObserver implements GameObserver {

    @Override
    public String update(Game game, ActionInGame actioningame) {
        StringBuilder msg = new StringBuilder();
        if (actioningame.getCommand().getType() == CommandType.TURN_ON) {
            Item item = actioningame.getItem1();
            if (item != null) {
                if (item.isTurnable()) {
                    if (item.isTurned_on() == false) {
                        Riddle riddle = game.getRiddles().values().stream().filter(r -> r.getTargetItem().equals(item.getID())).findFirst().orElse(null);
                        if (riddle !=null && riddle instanceof QuestionRiddle qRiddle){
                            //per accendere o spegnere l'oggetto devi rispondere all'indovinello
                            System.out.println(qRiddle.getDescription());
                            System.out.print(qRiddle.getQuestion() + "\nRisposta>");
                            Scanner scanner = new Scanner(System.in);
                            System.out.println(qRiddle.getDescription());
                            String ans = scanner.nextLine();
                            qRiddle.resolved(ans);
                        }
                        if(riddle != null && riddle.isSolved()){
                            item.setTurned_on(true);
                            msg.append("Hai acceso: ").append(item.getFirstName());
                        }else if(riddle != null && !riddle.isSolved()){
                            System.out.println(">Risposta errata!");
                        }else if(riddle == null){
                            item.setTurned_on(true);
                            msg.append("Hai acceso: ").append(item.getFirstName());
                        }
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
                        Riddle riddle = game.getRiddles().values().stream().filter(r -> r.getTargetItem().equals(item.getID())).findFirst().orElse(null);
                        if (riddle !=null && riddle instanceof QuestionRiddle qRiddle){
                            //per accendere o spegnere l'oggetto devi rispondere all'indovinello
                            System.out.println(qRiddle.getDescription());
                            System.out.print(qRiddle.getQuestion() + "\nRisposta>");
                            Scanner scanner = new Scanner(System.in);
                            String ans = scanner.nextLine();
                            qRiddle.resolved(ans);
                        }
                        if(riddle != null && riddle.isSolved()){
                            item.setTurned_on(false);
                            msg.append("Hai spento: ").append(item.getFirstName());
                        }else if(riddle != null && !riddle.isSolved()){
                            System.out.println(">Risposta errata!");
                        }else if(riddle == null){
                            item.setTurned_on(false);
                            msg.append("Hai spento: ").append(item.getFirstName());
                        }
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
        } else if (actioningame.getCommand().getType() == CommandType.WEAR) {
            Item item = actioningame.getItem1();
            System.out.println(item.getFirstName());
            switch(item.getFirstName()) {
                case "cuffie" -> {
                    System.out.println("cuffie indossate");
                            if (game.getCurrentRoom().getName().contentEquals("Camera da Letto")) {
                                    game.getItemByID(item.getID()).setTurned_on(true);
                                    msg.append("E chi l'avrebbe detto che Rick Astley sarebbe stato più piacevole di una semplice sveglia... Finalmente puoi esplorare la stanza! \n");
                            } else {
                                msg.append("Mi dispiace, sei stato rickrollato, è in riproduzione Never Gonna Give You Up!! \n");
                            }
                        }
                case "occhiali" -> {
                            if (game.getCurrentRoom().getName().contentEquals("Soggiorno")) {
                                    game.getItemByID(item.getID()).setTurned_on(true);
                                    msg.append("Finalmente riesci a vedere qualcosa! \n");
                            } else {
                                msg.append("Devo ammettere che ti donano... Nah non è vero e poi non vedi nulla, a cosa ti servono qui?? \n");
                            }
                }
                case "tuta" -> {
                            if (game.getCurrentRoom().getName().contentEquals("Cantina")) {
                                    game.getItemByID(item.getID()).setTurned_on(true);
                                    msg.append("Grazie alle ormai vecchie doti da pompiere di Giustino sei in grado di resistere al caldo! \n");
                            } else {
                                msg.append("E tu vorresti metterti a fare il pompiere in ").append(game.getCurrentRoom().getName()).append(" ?? Patetico... \n");
                            } 
                }
            }
            
        }
        return msg.toString();
    }
}
