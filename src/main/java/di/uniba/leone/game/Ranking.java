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

    /**
     *
     * @param ranking
     */
    public Ranking(List<GameTime> ranking) {
        this.ranking = ranking;
    }

    /**
     *
     */
    public Ranking() {
    }

    
    /** 
     * @param mrMsg
     */
    public void classification(MsgManager mrMsg) {
        List<GameTime> rnkWinner = new ArrayList();
        List<GameTime> rnkLoser = new ArrayList();
        for (GameTime player : ranking) {
            if (player.hasWin()) {
                rnkWinner.add(player);
            } else {
                rnkLoser.add(player);
            }
        }

        rnkWinner.sort((c1, c2) -> Long.compare(c1.getScore(), c2.getScore()));
        rnkLoser.sort((c1, c2) -> Long.compare(c2.getScore(), c1.getScore()));
        GameTime game = new GameTime();
        mrMsg.displayMsg("La classifica dei giocatori e': ");
        for (GameTime c : rnkWinner) {
            c.showScore(mrMsg);
        }
        for (GameTime c : rnkLoser) {
            c.showScore(mrMsg);
        }
    }

    
    /** 
     * @param newPlayer
     */
    public void addPlayerToRank(GameTime newPlayer) {
        Optional<GameTime> existingPlayerOpt = ranking.stream().filter(player -> player.getNickname().contentEquals(newPlayer.getNickname())).findFirst();
        if (existingPlayerOpt.isPresent()) {

            GameTime player = existingPlayerOpt.get();
            if (newPlayer.hasWin() && !player.hasWin()) {
                ranking.remove(player);
                ranking.add(newPlayer);
            } else if ((newPlayer.hasWin() && player.hasWin()) && (newPlayer.getScore() < player.getScore())) {
                ranking.remove(player);
                ranking.add(newPlayer);
            } else if ((!newPlayer.hasWin() && !player.hasWin()) && (newPlayer.getScore() < player.getScore())) {
                ranking.remove(player);
                ranking.add(newPlayer);
            }
        } else {
            ranking.add(newPlayer);
        }
    }
}
