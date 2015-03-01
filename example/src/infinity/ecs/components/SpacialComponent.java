package infinity.ecs.components;

import infinity.ecs.core.Component;
import infinity.ecs.core.ComponentType;

public class SpacialComponent implements Component {

	public float xPos;
	public float yPos;
	public float zPos;
	
	public float xRot;
	public float yRot;
	public float zRot;
	public float wRot;
	
	public float xScale;
	public float yScale;
	public float zScale;
	
	public SpacialComponent() {
		xPos = 0;
		yPos = 0;
		zPos = 0;
		
		xRot = 0;
		yRot = 0;
		zRot = 0;
		wRot = 1;
		
		xScale = 1;
		yScale = 1;
		zScale = 1;
	}
	
	@Override
	public ComponentType getComponentType() {
		return ComponentType.get(this.getClass());
	}
	
}
