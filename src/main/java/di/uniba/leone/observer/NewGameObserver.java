package di.uniba.leone.observer;

import di.uniba.leone.game.Game;
import di.uniba.leone.game.GameObserver;
import di.uniba.leone.parser.ActionInGame;
import di.uniba.leone.type.CommandType;
import java.io.File;

/**
 *
 * @author feder
 */
public class NewGameObserver implements GameObserver {

    @Override
    public String update(Game game, ActionInGame actioningame) {
        StringBuilder msg = new StringBuilder();

        if (actioningame.getCommand().getType() == CommandType.NEW_GAME) {
            File f = new File("./leone_game/partite");
            if (f.listFiles().length < 3) {
                File newFile = new File(f, "partita".concat(Integer.toString(f.listFiles().length + 1)));
                newFile.mkdir();
            }
        }
        return msg.toString();
    }

}
