
package di.uniba.leone.type;

import java.util.Set;

public class QuestionRiddle extends Riddle{
    private final String question;
    private final String answer;
    private final String deathMsg;
    private final Integer MAX_TRIES = 3;
    private Integer counter;
    public QuestionRiddle(Integer id, Set<CommandType> blacklist, String description, Boolean solved, Integer targetItem, String question, String answer, String deathMsg){
        super(id, blacklist, description, solved, targetItem);
        this.question = question;
        this.answer = answer;
        this.deathMsg = deathMsg;
        this.counter = 0;
    }
    
    public void resolved(String answer) {
        if (this.answer.toLowerCase().contentEquals(answer)){
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

    public String getDeathMsg() {
        return deathMsg;
    }
    
    
    
}
