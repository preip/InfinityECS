package example.components;

import infinity.ecs.core.*;
/**
 *
 * @author Simon
 */
public class CounterComponent extends Component {
    
    public int counter;
    
    public CounterComponent(Entity entity) throws IllegalStateException {
	super(entity);
	counter = 0;
    } 
}