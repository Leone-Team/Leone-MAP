package di.uniba.leone.observer;

import di.uniba.leone.game.Game;
import di.uniba.leone.game.GameObserver;
import di.uniba.leone.parser.ActionInGame;
import di.uniba.leone.type.CommandType;
import di.uniba.leone.type.Item;

public class UseObserver implements GameObserver {

    @Override
    public String update(Game game, ActionInGame actioningame) {
        StringBuilder msg = new StringBuilder();
        if (actioningame.getCommand().getType() == CommandType.USE) {
            Item item = actioningame.getItem1();
            if (item != null) {
                // Controlla se l'oggetto è nell'inventario
                if (game.getInventory().contains(item.getID())) {
                    if (item.isUsable()) {
                        switch (item.getFirstName().toLowerCase()) {
                            case "manuale" -> {
                                msg.append("Nel manuale c'è scritto: \n");
                                msg.append("Per disattivare il contatore, dovrai solo staccare i fusibili nell'ordine corretto.\n");
                                msg.append("So che saresti tentato di staccarli tutti a caso, ma per tua sfortuna c'è un ordine da rispettare... \n");
                                msg.append("Prima il fusibile che brucia come un rubino ardente \n");
                                msg.append("Poi il fusibile splendente come uno smeraldo brillante \n");
                                msg.append("Infine, il fusibile che scintilla come un topazio prezioso \n");
                            }
                            case "cuscino" -> {
                                if (actioningame.getItem2() != null) {
                                    if (actioningame.getItem2().isBreakable()) {
                                        msg.append("E tu pensavi davvero di rompere ").append(actioningame.getItem2().getFirstName()).append(" con un cuscino? Patetico... \n");
                                    } else {
                                        msg.append("Guarda che ").append(actioningame.getItem2().getFirstName()).append(" non si può rompere... \n");
                                    }
                                } else {
                                    msg.append("E cosa vuoi farci con un cuscino? Giocare a battaglia di cuscini con il demone del materasso? Patetico... \n");
                                }
                            }
                            case "mazza" -> {
                                if (actioningame.getItem2() != null) {
                                    if (actioningame.getItem2().getFirstName().contentEquals("sveglia") && game.getCurrentRoom().getName().contentEquals("Camera da Letto")) {
                                        if (!game.getItemByID(actioningame.getItem2().getID()).isBroken()) {
                                            msg.append("Accipicchia che colpo! Finalmente ti sei sbarazzato di quel rumore assordante! \n");
                                            game.getItemByID(actioningame.getItem2().getID()).setBroken(true);
                                        } else {
                                            msg.append("Beh e' gia' ridotta in pezzi... Cosa vuoi farci piu' ? \n");
                                        }
                                    } else if (actioningame.getItem2().getFirstName().contentEquals("caldaia") && game.getCurrentRoom().getName().contentEquals("Cantina")) {
                                        if (!game.getItemByID(actioningame.getItem2().getID()).isBroken()) {
                                            msg.append("E' questo quello che chiamano problem solving? Spaccare tutto? Beh almeno ha funzionato... \n");
                                            game.getItemByID(actioningame.getItem2().getID()).setBroken(true);
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
                        }
                    } else {
                        msg.append("C'è un luogo e un momento per ogni cosa! Ma non ora.");
                    }
                } else {
                    msg.append("E da dove lo vorresti tirare fuori? Non è nel tuo inventario...");
                }
            } else {
                msg.append("Non puoi usare questo oggetto.");
            }
        }
        return msg.toString();
    }
}
