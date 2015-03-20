package example.components;

import infinity.ecs.core.Component;

/**
 *
 * @author Simon
 */
public class StatusComponent extends Component {
	
	//Dynamic 
    public int curHealth;
    public int curShield;
	
	//Static
	public int maxHealth;
	public int maxShield;
	public int dmg;
	public int range;
	
    public StatusComponent() {
		curHealth = 100;
		curShield = 0;
		
		maxHealth = 100;
		maxShield = 0;
		dmg = 0;
		range = 0;
    }
}
