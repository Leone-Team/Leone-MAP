/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package di.uniba.leone.type;

/**
 *
 * @author giann
 */
public class Riddle {
    private static int ID;
    private String question;
    private String answer;
    private boolean solved;

    public Riddle(String question, String answer, boolean solved) {
        this.question = question;
        this.answer = answer;
        this.solved = solved;
    }

    public Riddle() {
    }
    
    public boolean checkAnswer(String answer) {
        if (answer == this.question)
            return true;
        else
            return false;
    }
}
