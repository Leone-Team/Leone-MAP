package di.uniba.leone.game;
import di.uniba.leone.gui.MsgManager;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author feder
 */
public class Ranking implements Serializable {

    private static final long serialVersionUID = 1L;
    List<GameTime> ranking = new ArrayList();

    public Ranking(List<GameTime> ranking) {
        this.ranking = ranking;
    }

    public Ranking() {
    }

    public void classification(MsgManager mrMsg) {
        ranking.sort((c1, c2) -> Long.compare(c1.getScore(), c2.getScore()));
        GameTime game = new GameTime();
        mrMsg.displayMsg("La classifica dei giocatori e': ");
        for (GameTime c : ranking) {
            if (c.getScore() != Long.MAX_VALUE) {
                mrMsg.displayMsg(c.getNickname() + ": " + c.getScore());
            } else {
                mrMsg.displayMsg(c.getNickname() + " non ha concluso il gioco, che fifone/a!!");
            }
        }
    }

    public void addPlayerToRank(GameTime newPlayer) {
        Optional<GameTime> existingPlayerOpt = ranking.stream().filter(player -> player.getNickname().contentEquals(newPlayer.getNickname())).findFirst();
        if (existingPlayerOpt.isPresent()) {

            GameTime player = existingPlayerOpt.get();
            if (newPlayer.getScore() > player.getScore()) {
                ranking.remove(player);
                ranking.add(newPlayer);
            }
        } else {
            ranking.add(newPlayer);
        }
    }
}
