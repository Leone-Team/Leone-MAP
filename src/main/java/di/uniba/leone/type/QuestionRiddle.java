
package di.uniba.leone.type;

import java.util.Set;

public class QuestionRiddle extends Riddle{
    public final String question;
    public final String answer;
    public final String deathMsg;
    
    public QuestionRiddle(Integer id, Set<CommandType> blacklist, String description, Boolean solved, Integer targetItem, String question, String answer, String deathMsg){
        super(id, blacklist, description, solved, targetItem);
        this.question = question;
        this.answer = answer;
        this.deathMsg = deathMsg;
       
    }
    
    public void resolved(String answer) {
        if (this.answer.contentEquals(answer)){
            setSolved(true);
            setCounter(0);
        }
        else{
            setCounter(getCounter() + 1);
        }
        
    }

    public String getQuestion() {
        return question;
    }
    
    
    
}
