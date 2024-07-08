/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package di.uniba.leone.game;

import java.time.LocalTime;
import java.util.List;
import java.util.Scanner;

public class GameTime implements Runnable {
        private LocalTime timeBegin;
        private LocalTime timeEnd;
        private String nickname;

        public GameTime(String nickname) {
            this.nickname = nickname;
        }

    GameTime() {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

        public void start() {
            this.timeBegin = LocalTime.now();
        }
        
        public void stop() {
            this.timeEnd = LocalTime.now();
        }

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
            System.out.println("La durata della partita e' stata di: " +LocalTime.ofSecondOfDay(count));
        }
        
        public long getCount() {
            if (timeBegin == null || timeEnd == null) {
                return Long.MAX_VALUE;
            }
            return timeEnd.toSecondOfDay() - timeBegin.toSecondOfDay();
        }
        
        public void classification(List<GameTime> players) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Inserire il nickname: ");
            String nickname = scanner.nextLine();
      
            players.sort((c1, c2) -> Long.compare(c1.getCount(), c2.getCount()));
 
            System.out.println("La classifica dei giocatori e': ");
            for (GameTime c : players) {
            if (c.getCount() != Long.MAX_VALUE) {
                System.out.println(c.nickname + ": " + c.getCount());
            } else {
                    System.out.println(c.nickname + " non ha concluso il gioco, che fifone/a!!");
                }
            }
        }  
}

