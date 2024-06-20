
package di.uniba.leone.type;

public class Riddle {
    private static int id;
    private String question;
    private String answer;
    private boolean solved;
    

    public Riddle(int id, String question, String answer, boolean solved) {
        this.id = id;
        this.question = question;
        this.answer = answer;
        this.solved = solved;
    }

}
