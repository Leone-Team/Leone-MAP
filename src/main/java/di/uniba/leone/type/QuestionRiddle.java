
package di.uniba.leone.type;

import java.util.List;

public class QuestionRiddle extends Riddle{
    public final String answer;
    
    public QuestionRiddle(Integer id, List<CommandType> whitelist, String description, Boolean solved, Integer MAX_TRIES, String answer){
        super(id, whitelist, description, solved, MAX_TRIES);
        this.answer = answer;
    }


    public void resolved(String answer) {
        if(answer.compareTo(this.answer) == 0)
            setSolved(true);
        else{
            System.out.println("Risposta errata");
            setCounter(getCounter() + 1);
        }
    }
    
}
