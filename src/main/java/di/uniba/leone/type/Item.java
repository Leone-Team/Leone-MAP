package di.uniba.leone.type;

import java.io.Serializable;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author giann
 */
public class Item implements Serializable {

    private static final long serialVersionUID = 1L;
    private final Integer id;
    private String description = "";
    private final List<String> names = new LinkedList<>();
    private boolean pickupable;
    private boolean usable;
    private boolean turnable;
    private boolean breakable;
    private boolean turned_on;
    private boolean broken;

    //costruttore

    /**
     *
     * @param id
     * @param description
     * @param names
     * @param peakable
     * @param useable
     * @param turnable
     * @param breakable
     * @param broken
     * @param turned_on
     */
    public Item(Integer id, String description, String names, Boolean peakable, Boolean useable, Boolean turnable, Boolean breakable, Boolean broken, Boolean turned_on) {
        this.id = id;
        this.description = description;
        this.names.addAll(Arrays.asList(names.replaceAll(" ", "").split(",")));
        this.pickupable = peakable;
        this.usable = useable;
        this.turnable = turnable;
        this.breakable = breakable;
        this.broken = broken;
        this.turned_on = turned_on;
    }

    
    /** 
     * @return Integer
     */
    public Integer getID() {
        return id;
    }

    
    /** 
     * @return String
     */
    public String getDescription() {
        return description;
    }

    
    /** 
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     *
     * @return
     */
    public boolean isPickupable() {
        return pickupable;
    }

    /**
     *
     * @param pickupable
     */
    public void setPickupable(boolean pickupable) {
        this.pickupable = pickupable;
    }

    /**
     *
     * @return
     */
    public boolean isUsable() {
        return usable;
    }

    /**
     *
     * @param usable
     */
    public void setUsable(boolean usable) {
        this.usable = usable;
    }

    /**
     *
     * @return
     */
    public boolean isTurnable() {
        return turnable;
    }

    /**
     *
     * @param turnable
     */
    public void setTurnable(boolean turnable) {
        this.turnable = turnable;
    }

    /**
     *
     * @return
     */
    public List<String> getNames() {
        return names;
    }

    // Metodo per ottenere il primo nome dal set

    /**
     *
     * @return
     */
    public String getFirstName() {
        return names.getFirst();
    }

    /**
     *
     * @return
     */
    public boolean isBreakable() {
        return breakable;
    }

    /**
     *
     * @return
     */
    public boolean isTurned_on() {
        return turned_on;
    }

    /**
     *
     * @return
     */
    public boolean isBroken() {
        return broken;
    }

    /**
     *
     * @param breakable
     */
    public void setBreakable(boolean breakable) {
        this.breakable = breakable;
    }

    /**
     *
     * @param turned_on
     */
    public void setTurned_on(boolean turned_on) {
        this.turned_on = turned_on;
    }

    /**
     *
     * @param broken
     */
    public void setBroken(boolean broken) {
        this.broken = broken;
    }

    /**
     *
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj) {
        if (obj.getClass() != getClass()) {
            return false;
        }

        Item other = (Item) obj;
        return other.getID().compareTo(this.getID()) == 0;
    }

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
        return "Item{" + "id=" + id + ", name=" + names + ", description=" + description + ", peakable=" + pickupable + ", usable=" + usable + ", turnable=" + turnable + '}';
    }

}
