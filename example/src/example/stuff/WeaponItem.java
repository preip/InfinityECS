package example.stuff;

/**
 *
 * @author Simon
 */
public class WeaponItem extends Item{

	private final int _dmg;
	private final int _range;
	private final WEAPONTYPE _type;
	
	public WeaponItem(int dmg, int range, WEAPONTYPE type){
		_dmg = dmg;
		_range = range;
		_type = type;
	}
	
	public WEAPONTYPE getType(){
		return _type;
	}
	
	public int getDMG(){
		return _dmg;
	}
	public int getRange() {
		return _range;
	}
	
	@Override
	public boolean fromXML(String xml) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public String toXML() {
		throw new UnsupportedOperationException("Not supported yet.");
	}
	
	public enum WEAPONTYPE{
		LASER,ROCKET,BALLISTIC
	}
	
}
