
package di.uniba.leone.type;

import java.util.HashSet;
import java.util.Set;

public class Riddle {
    private final Integer id;
    private final Set<CommandType> blackList = new HashSet();
    private final String description;
    private boolean solved;
    private Integer counter;
    private final Integer MAX_TRIES = 3;
    private final Integer targetItem;
    
    
    public Riddle(Integer id, Set<CommandType> blackList, String description, Boolean solved, Integer targetItem){
        this.id = id;
        this.blackList.addAll(blackList);
        this.description = description;
        this.solved = solved;
        this.counter = 0;
        this.targetItem = targetItem;
    }

    public Integer getId() {
        return id;
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

    public Integer getTargetItem() {
        return targetItem;
    }

    public Set<CommandType> getBlackList() {
        return blackList;
    }

    public String getDescription() {
        return description;
    }
    
    

}
