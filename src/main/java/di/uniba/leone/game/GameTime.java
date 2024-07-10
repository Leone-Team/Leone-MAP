/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package di.uniba.leone.game;

import di.uniba.leone.gui.MsgManager;
import java.io.Serializable;
import java.time.LocalTime;

public class GameTime implements Runnable, Serializable {

    private static final long serialVersionUID = 1L;
    private LocalTime timeBegin;
    private LocalTime timeEnd;
    private String nickname = "player";
    private boolean win = false;

    public GameTime(String nickname) {
        this.nickname = nickname;
    }

    public GameTime() {
    }

    public void start() {
        this.timeBegin = LocalTime.now();
    }

    public void stop() {
        this.timeEnd = LocalTime.now();
    }

    @Override
    public void run() {
        synchronized (this) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Il thread è stato interrotto.");
            }
        }
    }

    public void outputGameTime() {
        int count = timeEnd.toSecondOfDay() - timeBegin.toSecondOfDay();
        System.out.println("La durata della partita e' stata di: " + LocalTime.ofSecondOfDay(count));
    }

    public long getScore() {
        if (timeBegin == null || timeEnd == null) {
            return Long.MIN_VALUE;
        }
        return timeEnd.toSecondOfDay() - timeBegin.toSecondOfDay();
    }

    public void showScore(MsgManager mrMsg) {
        if (win) {
            mrMsg.displayMsg(nickname + ": " + getScore()+" secondi");
        } else {
            mrMsg.displayMsg(nickname + ": ha abbandonato dopo "+getScore()+" secondi , che fifone/a!!");
        }
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }

    public void setWin(boolean win) {
        this.win = win;
    }

    
}
