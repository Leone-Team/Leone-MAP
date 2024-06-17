
package di.uniba.leone.type;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Item {
    
    private final Integer id;
    private final String name;
    private String description = "";
    private final Set<String> alias = new HashSet();
    private boolean peakable;
    private boolean useable;
    private boolean turnable;
    
    //costruttore
    public Item(String item){
        ArrayList<String> tokens = new ArrayList();
        tokens.addAll(Arrays.asList(item.split("/")));
        id = Integer.valueOf(tokens.get(0));
        name = tokens.get(1);
        description = tokens.get(2);
        alias.addAll(Arrays.asList(tokens.get(3).split(",")));
        
        peakable = tokens.get(4).compareToIgnoreCase("true") == 0;
        useable = tokens.get(5).compareToIgnoreCase("true") == 0;
        turnable = tokens.get(6).compareToIgnoreCase("true") == 0;
        
 
    }
    
    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isPeakable() {
        return peakable;
    }

    public void setPeakable(boolean peakable) {
        this.peakable = peakable;
    }

    public boolean isUseable() {
        return useable;
    }

    public void setUseable(boolean useable) {
        this.useable = useable;
    }

    public boolean isTurnable() {
        return turnable;
    }

    public void setTurnable(boolean turnable) {
        this.turnable = turnable;
    }

    public Set<String> getAlias() {
        return alias;
    }

    
    @Override
    public boolean equals(Object obj) {
        if(obj.getClass() != getClass())
            return false;
        
        Item other = (Item) obj;
        return other.getId().compareTo(this.getId()) == 0;
    }

    @Override
    public int hashCode() {
        return this.getId().hashCode();
    }

    @Override
    public String toString() {
        return "Item{" + "id=" + id + ", name=" + name + ", description=" + description + ", alias=" + alias + ", peakable=" + peakable + ", useable=" + useable + ", turnable=" + turnable + '}';
    }
    
    
    
}
