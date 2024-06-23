
package di.uniba.leone.type;

import java.util.Map;
import java.util.Set;

public class ItemRiddle extends Riddle {
    private final Integer tool;
    
    public ItemRiddle(Integer id, Set<CommandType> blacklist, String description, Boolean solved, Integer targetItem, Integer tool){
        super(id, blacklist, description, solved, targetItem);
        
        this.tool = tool;
    }

    
    public void resolved(Map<Integer, Item> items) {
        if(items.get(getTargetItem()).isBroken() || !items.get(getTargetItem()).isTurned_on() || items.get(tool).isTurned_on())
            setSolved(true);
        else
            setCounter(getCounter() + 1);
    }
    
    
}
