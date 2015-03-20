package example.stuff;

/**
 *
 * @author Simon
 */
public class ShieldItem extends Item {

	private final int _energy;
	private final int _rechargeTime;

	public ShieldItem(int energy, int recharge) {
		_energy = energy;
		_rechargeTime = recharge;
	}

	public int getEnergy() {
		return _energy;
	}

	public int getRecharge() {
		return _rechargeTime;
	}
	
	@Override
	public String toXML() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean fromXML(String xml) {
		throw new UnsupportedOperationException();
	}

}
