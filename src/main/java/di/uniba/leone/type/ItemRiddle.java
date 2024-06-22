
package di.uniba.leone.type;

import java.util.List;
import java.util.Map;

public class ItemRiddle extends Riddle {
    private final Integer targetItem;
    
    public ItemRiddle(Integer id, List<CommandType> whitelist, String description, Boolean solved, Integer MAX_TRIES, Integer targetItem){
        super(id, whitelist, description, solved, MAX_TRIES);
        this.targetItem = targetItem;
    }

    public void resolved(Map<Integer, Item> items) {
        if(items.get(targetItem).isBroken() || items.get(targetItem).isTurned_on() == false)
            setSolved(true);
        else
            setCounter(getCounter() + 1);
    }
    
    
}
