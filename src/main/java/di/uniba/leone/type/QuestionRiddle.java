
package di.uniba.leone.type;

import java.util.Set;

/**
 *
 * @author giann
 */
public class QuestionRiddle extends Riddle{
    private final String question;
    private final String answer;
    private final String deathMsg;
    private final Integer MAX_TRIES = 3;
    private Integer counter;

    /**
     *
     * @param id
     * @param blacklist
     * @param description
     * @param solved
     * @param targetItem
     * @param question
     * @param answer
     * @param deathMsg
     */
    public QuestionRiddle(Integer id, Set<CommandType> blacklist, String description, Boolean solved, Integer targetItem, String question, String answer, String deathMsg){
        super(id, blacklist, description, solved, targetItem);
        this.question = question;
        this.answer = answer;
        this.deathMsg = deathMsg;
        this.counter = 0;
    }
    
    
    /** 
     * @param answer
     */
    public void resolved(String answer) {
        if (this.answer.toLowerCase().contentEquals(answer.toLowerCase())){
            setSolved(true);
            setCounter(0);
        }
        else{
            setCounter(getCounter() + 1);
        }
        
    }

    
    /** 
     * @return String
     */
    public String getQuestion() {
        return question;
    }

    
    /** 
     * @return String
     */
    public String getDeathMsg() {
        return deathMsg;
    }
    
    
    
}
