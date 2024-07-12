package di.uniba.leone.type;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author giann
 */
public class Riddle implements Serializable {

    private static final long serialVersionUID = 1L;
    private final Integer id;
    private final Set<CommandType> blackList = new HashSet();
    private final String description;
    private boolean solved;
    private Integer counter;
    private final Integer targetItem;

    /**
     *
     * @param id
     * @param blackList
     * @param description
     * @param solved
     * @param targetItem
     */
    public Riddle(Integer id, Set<CommandType> blackList, String description, Boolean solved, Integer targetItem) {
        this.id = id;
        this.blackList.addAll(blackList);
        this.description = description;
        this.solved = solved;
        this.counter = 0;
        this.targetItem = targetItem;

    }

    
    /** 
     * @return Integer
     */
    public Integer getId() {
        return id;
    }

    
    /** 
     * @return boolean
     */
    public boolean isSolved() {
        return solved;
    }

    
    /** 
     * @param solved
     */
    public void setSolved(boolean solved) {
        this.solved = solved;
    }

    /**
     *
     * @return
     */
    public Integer getCounter() {
        return counter;
    }

    /**
     *
     * @param counter
     */
    public void setCounter(Integer counter) {
        this.counter = counter;
    }

    /**
     *
     * @return
     */
    public Integer getTargetItem() {
        return targetItem;
    }

    /**
     *
     * @return
     */
    public Set<CommandType> getBlackList() {
        return blackList;
    }

    /**
     *
     * @return
     */
    public String getDescription() {
        return description;
    }

}
