package example.systems;

import example.components.InventoryComponent;
import example.components.StatusComponent;
import example.stuff.Item;
import example.stuff.ShieldItem;
import example.stuff.WeaponItem;
import infinity.ecs.core.ComponentMask;
import infinity.ecs.core.ComponentType;
import infinity.ecs.core.Entity;
import infinity.ecs.core.EntityManager;
import infinity.ecs.core.EntitySystem;
import infinity.ecs.messaging.MessageDispatcher;
import infinity.ecs.utils.ReadOnlyCollection;

/**
 *
 * @author Simon
 */
public class InventorySystem extends EntitySystem {
   
    //Needs InventoryComponent, StatusComponent
    private final ComponentType _inType = ComponentType.get(InventoryComponent.class);
	private final ComponentType _statusType = ComponentType.get(StatusComponent.class);
	
    private ReadOnlyCollection<Entity> _allEntities;
    private EntityManager _manager;
    private MessageDispatcher _dispatcher;
    private boolean _isInitialized;
    
    public InventorySystem(ComponentMask mask){
		super(mask);
    }
    
    @Override
    public void update(int time){
		for(Entity entity : _allEntities){
			InventoryComponent inventory  = (InventoryComponent) entity.getComponent(_inType);
			StatusComponent status = (StatusComponent) entity.getComponent(_statusType);
			
			for(Item item : inventory.items){
				//There must be a better way to do this .... IDs or something
				if(item.getClass() == WeaponItem.class){
					WeaponItem weapon = (WeaponItem) item;
					status.dmg = weapon.getDMG();
					status.range = weapon.getRange();
				}
				if(item.getClass() == ShieldItem.class){
					ShieldItem shield = (ShieldItem) item;
					status.curShield += shield.getEnergy();
				}
			}
		}
    }
    
    @Override
    public void terminate(){}
    
    @Override
    public void initialize(EntityManager manager, MessageDispatcher dispatcher){
		_manager = manager;
		_dispatcher = dispatcher;
		_isInitialized = true;
		_allEntities = _manager.getEntitiesByMask(_mask);
    }
    
    @Override
    public boolean isInitialized(){
		return _isInitialized;
    }
}
