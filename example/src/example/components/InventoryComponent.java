package example.components;

import example.stuff.Item;
import infinity.ecs.core.Component;
import infinity.ecs.utils.IndexedCollection;

/**
 *
 * @author Simon
 */
public class InventoryComponent extends Component{
    
    public IndexedCollection<Item> items;
    public int maxSize;
    
    public InventoryComponent(){
	items = new IndexedCollection<>();
    }
}
