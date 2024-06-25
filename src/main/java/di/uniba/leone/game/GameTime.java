/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package di.uniba.leone.game;

import java.time.LocalTime;

public class GameTime implements Runnable {
        private LocalTime timeBegin;
        private LocalTime timeEnd;
        
        public void start() {
            this.timeBegin = LocalTime.now();
        }
        
        public void stop() {
            this.timeEnd = LocalTime.now();
        }
        
        public void run() {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Il thread è stato interrotto.");
            }
        }
        
        public void outputGameTime() {
            int count = timeEnd.toSecondOfDay() - timeBegin.toSecondOfDay();
            System.out.println("La durata della partita e' stata di: " +LocalTime.ofSecondOfDay(count));
        }
}

