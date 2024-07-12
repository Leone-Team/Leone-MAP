package di.uniba.leone.type;

import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author giann
 */
public class Container extends Item{
    private Set<Integer> items = new HashSet();
    private boolean openable; 

    /**
     *
     * @param id
     * @param description
     * @param names
     * @param peakable
     * @param useable
     * @param turnable
     * @param breakable
     * @param openable
     * @param broken
     * @param turned_on
     * @param items
     */
    public Container(Integer id, String description, String names, Boolean peakable, Boolean useable, Boolean turnable, Boolean breakable, Boolean openable, Boolean broken, Boolean turned_on, String items){
        super(id, description, names, peakable, useable, turnable, breakable, broken, turned_on);
        for(String item: items.split(",")){
            this.items.add(Integer.valueOf(item));
        }
        this.openable = openable;
    }

    
    /** 
     * @return Set<Integer>
     */
    public Set<Integer> getItems() {
        return items;
    }

    
    /** 
     * @param items
     */
    public void setItems(Set<Integer> items) {
        this.items = items;
    }

    
    /** 
     * @param obj
     * @return boolean
     */
    @Override
    public boolean equals(Object obj) {
        if(obj.getClass() != getClass())
            return false;
        
        Container other = (Container) obj;
        return other.getID().compareTo(this.getID()) == 0;}

    /**
     *
     * @return
     */
    @Override
    public int hashCode() {
        return this.getID().hashCode();
    }

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        return super.toString() + ", items:" + items; 
    }

    /**
     *
     * @return
     */
    public boolean isOpenable() {
        return openable;
    }

    /**
     *
     * @param openable
     */
    public void setOpenable(boolean openable) {
        this.openable = openable;
    } 
}

