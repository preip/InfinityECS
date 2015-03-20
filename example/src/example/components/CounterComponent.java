package example.components;

import infinity.ecs.core.*;

/**
 *
 * @author Simon
 */
public class CounterComponent extends Component {

    public int counter;

    public CounterComponent() {
		counter = 0;
    }
}
