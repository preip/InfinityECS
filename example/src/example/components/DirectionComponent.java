package example.components;

import infinity.ecs.core.Component;

/**
 *
 * @author Simon
 */
public class DirectionComponent extends Component {

    public float xDir;
    public float yDir;
    public float zDir;

    public float xSpeed;
    public float ySpeed;
    public float zSpeed;

    public DirectionComponent() {
		xDir = 0;
		yDir = 0;
		zDir = 0;

		xSpeed = 0;
		ySpeed = 0;
		zSpeed = 0;
    }

}
