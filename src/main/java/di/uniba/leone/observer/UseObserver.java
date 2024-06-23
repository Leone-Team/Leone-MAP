package di.uniba.leone.observer;

import di.uniba.leone.game.Game;
import di.uniba.leone.game.GameObserver;
import di.uniba.leone.parser.ActionInGame;
import di.uniba.leone.type.CommandType;
import di.uniba.leone.type.Item;
import java.util.HashSet;
import java.util.Set;

public class UseObserver implements GameObserver {

  
    @Override
    public String update(Game game, ActionInGame actioningame) {
        StringBuilder msg = new StringBuilder();
        if (actioningame.getCommand().getType() == CommandType.USE) {
            Item item = actioningame.getItem1();
            if (item != null) {
                if (item.isUsable()) {

                } else {
                    switch (item.getFirstName()) {
                        case "Manuale" -> {
                            msg.append("Nel manuale c'è scritto: \n");
                            msg.append("Per disattivare il contatore, dovrai solo staccare i giusti fusibili.\n");
                            msg.append("So che saresti tentato di staccarli tutti a caso, ma per tua sfortuna c'è un ordine da rispettare... \n");
                            msg.append("Prima il fusibile che brucia come un rubino ardente \n");
                            msg.append("Poi il fusibile splendente come uno smeraldo brillante \n");
                            msg.append("Infine, il fusibile che scintilla come un topazio prezioso \n");
                        }
                        case "cuscino" -> {
                            if (actioningame.getItem2() != null) {
                                if (actioningame.getItem2().isBreakable() == true) {
                                    msg.append("E tu pensavi davvero di rompere ").append(actioningame.getItem2().getFirstName()).append(" con un cuscino? Patetico... \n");
                                } else {
                                    msg.append("Guarda che ").append(actioningame.getItem2().getFirstName()).append(" non si può rompere... \n");
                                }
                            } else {
                                msg.append("E cosa vuoi farci con un cuscino? Giocare a battaglia di cuscini con il demone del materasso? Patetico... \n");
                            }
                        }
                        case "tuta" -> {
                            if (game.getCurrentRoom().getName().contentEquals("Cantina")) {
                                if (game.getCurrentRoom().isLocked() == true) {
                                    game.getCurrentRoom().setLocked(false);
                                    msg.append("Grazie alle ormai vecchie doti da pompiere di Giustino sei in grado di resistere al caldo! \n");
                                } else {
                                    msg.append("Ti piace così tanto fare il pompiere? Guarda che la stai già indossando, stupido cane! \n");
                                }
                            } else {
                                msg.append("E tu vorresti metterti a fare il pompiere in ").append(game.getCurrentRoom().getName()).append(" ?? Patetico... \n");
                            }
                        }
                        case "cuffie" -> {
                            if (game.getCurrentRoom().getName().contentEquals("Camera da Letto")) {
                                if (game.getCurrentRoom().isLocked() == true) {
                                    game.getCurrentRoom().setLocked(false);
                                    msg.append("E chi l'avrebbe detto che Rick Astley sarebbe stato più piacevole di una semplice sveglia... Finalmente puoi esplorare la stanza! \n");
                                } else {
                                    msg.append("Mi dispiace, sei stato rickrollato, è in riproduzione Never Gonna Give You Up!! \n");
                                }
                            } else {
                                msg.append("Mi dispiace, sei stato rickrollato, è in riproduzione Never Gonna Give You Up!! \n");
                                msg.append("E' così potente che non riesci a sentire più nulla dei suoni provenienti dalla stanza... \n");
                            }
                        }
                        case "mazza" -> {
                            if (actioningame.getItem2() != null) {
                                if (actioningame.getItem2().getFirstName().contentEquals("sveglia") && game.getCurrentRoom().getName().contentEquals("Camera da Letto")) {
                                    if (game.getItemByID(actioningame.getItem2().getID()).isBroken() == false) {
                                        game.getCurrentRoom().setLocked(false);
                                        msg.append("Accipicchia che colpo! Finalmente ti sei sbarazzato di quel rumore assordante! \n");
                                        game.getItemByID(actioningame.getItem2().getID()).setBroken(true);
                                    } else {
                                        msg.append("Beh e' gia' ridotta in pezzi... Cosa vuoi farci piu' ? \n");
                                    }
                                } else if (actioningame.getItem2().getFirstName().contentEquals("caldaia") && game.getCurrentRoom().getName().contentEquals("Cantina")) {
                                    if (game.getItemByID(actioningame.getItem2().getID()).isBroken() == false) {
                                        msg.append("E' questo quello che chiamano problem solving? Spaccare tutto? Beh almeno ha funzionato... \n");
                                        game.getItemByID(actioningame.getItem2().getID()).setBroken(true);
                                        game.getCurrentRoom().setLocked(false);
                                    } else {
                                        msg.append("Beh e' gia' ridotta in pezzi... Cosa vuoi farci piu' ? \n");
                                    }
                                } else {
                                    msg.append("Wooo, calma con quella mazza... Non vorrai mica spaccare tutto eh? \n");
                                }
                            } else {
                                msg.append("Eh si, con quella mazza potresti seriamente fare danni, se solo non la sventolassi in aria senza motivo... \n");
                            }
                        }
                        case "occhiali" -> {
                            if (game.getCurrentRoom().getName().contentEquals("Soggiorno")) {
                                if (game.getCurrentRoom().isLocked() == true) {
                                    game.getCurrentRoom().setLocked(false);
                                    msg.append("Finalmente riesci a vedere qualcosa! \n");
                                } else {
                                    msg.append("Li hai già indossati, cosa c'è, vuoi un altro paio? \n");
                                }
                            } else {
                                msg.append("Devo ammettere che ti donano... Nah non è vero e poi non vedi nulla, a cosa ti servono qui?? \n");
                            }
                        }
                    }
                }
            } else {
                msg.append("Non puoi utilizzare questo oggetto.");
            }
        }
        return msg.toString();
    }
}
