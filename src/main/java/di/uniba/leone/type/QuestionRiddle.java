
package di.uniba.leone.type;

import java.util.Set;

public class QuestionRiddle extends Riddle{
    public final String question;
    public final String answer;
    
    public QuestionRiddle(Integer id, Set<CommandType> blacklist, String description, Boolean solved, Integer targetItem, String question, String answer){
        super(id, blacklist, description, solved, targetItem);
        this.question = question;
        this.answer = answer;
    }
    
    public void resolved(String answer) {
        if (this.answer.contentEquals(answer))
            setSolved(true);
        
    }

    public String getQuestion() {
        return question;
    }
    
    
    
}
