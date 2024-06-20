/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package di.uniba.leone.observer;

import di.uniba.leone.game.Game;
import di.uniba.leone.game.GameObserver;
import di.uniba.leone.parser.ActionInGame;
import di.uniba.leone.type.CommandType;
import di.uniba.leone.type.Container;
import di.uniba.leone.type.Item;
import java.util.Iterator;

public class OpenObserver implements GameObserver {


    @Override
    public String update(Game game, ActionInGame actioningame) {
        StringBuilder msg = new StringBuilder();
        if (actioningame.getCommand().getType() == CommandType.OPEN) {
            /*ATTENZIONE: quando un oggetto contenitore viene aperto, tutti gli oggetti contenuti
                * vengongo inseriti nella stanza o nell'inventario a seconda di dove si trova l'oggetto contenitore.
                * Potrebbe non esssere la soluzione ottimale.
             */
            if (actioningame.getItem() == null) {
                msg.append("Non puoi aprire nulla.");
            } else {
                if (actioningame.getItem() != null) {
                    if (actioningame.getItem().isOpenable() && actioningame.getItem().isOpen() == false) {
                        if (actioningame.getItem() instanceof Container) {
                            msg.append("Hai aperto: ").append(actioningame.getItem().getFirstName());
                            Container c = (Container) actioningame.getItem();
                            if (!c.getItems().isEmpty()) {
                                msg.append(c.getFirstName()).append(" contiene:");
                                Iterator<Integer> it = c.getItems().iterator();
                                while (it.hasNext()) {
                                    Integer ItemID = it.next();
                                    Item next = game.getItemByID(ItemID);
                                    game.getCurrentRoom().getItems().add(next);
                                    msg.append(" ").append(next.getFirstName());
                                    it.remove();
                                }
                                msg.append("\n");
                            }
                            actioningame.getItem().setOpen(true);
                        } else {
                            msg.append("Hai aperto: ").append(actioningame.getItem().getFirstName());
                            actioningame.getItem().setOpen(true);
                        }
                    } else {
                        msg.append("Non puoi aprire questo oggetto.");
                    }
                }
            }
        }
        return msg.toString();
    }
}
