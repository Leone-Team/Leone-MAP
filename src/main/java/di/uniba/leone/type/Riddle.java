
package di.uniba.leone.type;

import java.util.LinkedList;
import java.util.List;

public abstract class Riddle {
    private final Integer id;
    private final List<CommandType> whiteList = new LinkedList();
    private final String description;
    private boolean solved;
    private Integer counter;
    private final Integer MAX_TRIES;
    
    
    public Riddle(Integer id, List<CommandType> whiteList, String description, Boolean solved, Integer MAX_TRIES){
        this.id = id;
        this.whiteList.addAll(whiteList);
        this.description = description;
        this.solved = solved;
        this.counter = 0;
        this.MAX_TRIES = MAX_TRIES;
    }
    
    public boolean isSolved() {
        return solved;
    }

    public void setSolved(boolean solved) {
        this.solved = solved;
    }

    public Integer getCounter() {
        return counter;
    }

    public void setCounter(Integer counter) {
        this.counter = counter;
    }
    
    
}
