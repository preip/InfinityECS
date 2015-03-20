package example.stuff;

/**
 *
 * @author Simon
 */
public abstract class Item {
    
	
	//Some kind of ID for the items would be nice.
	/*
    protected int _id;
    
    public int getID(){
		return _id;
    }*/
    
    public abstract boolean fromXML(String xml);
    public abstract String toXML();
}
