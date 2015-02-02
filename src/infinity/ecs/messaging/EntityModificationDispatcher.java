package infinity.ecs.messaging;

import infinity.ecs.core.ComponentType;
import infinity.ecs.core.Entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Class that handles the dispatching of entity modification messages to all registered endpoints
 * that have registered under the ComponentType of the message. Also handles registering and
 * deregistering of endpoints and the creation of new endpoints.
 * <p>
 * <b>Dev-Notes:</b><br>
 * System may be inflexible, because messages consist only of the manipulated entity and the type
 * of component that has been manipulated. It should be evaluated if that is sufficient for all
 * cases or if there is a need for custom modification messages.
 * The advantage of the current version is the avoidance of the creation of new message objects
 * by simply passing references, thus saving memory and speed.
 * 
 * @author preip
 * @version 0.7
 */
public final class EntityModificationDispatcher {
	/**
	 * A list of all registered message endpoints indexed by the content types for which they
	 * have registered. Used to dispatch messages to all relevant recipients.
	 */
	private final Map<ComponentType, List<EntityModificationEndpoint>> _register;
	
	/**
	 * Create a new instance of the MessageDispatcher class.
	 */
	public EntityModificationDispatcher() {
		_register = new HashMap<ComponentType, List<EntityModificationEndpoint>>();
	}
	
	/**
	 * Creates a new endpoint associated with this dispatcher
	 * @return The created endpoint.
	 */
	public EntityModificationEndpoint CreateEndpoint() {
		return new EntityModificationEndpoint(this);
	}
	
	//----------------------------------------------------------------------------------------------
	// Dispatching
	//----------------------------------------------------------------------------------------------
	
	/**
	 * Dispatches a message consisting of the manipulated entity and the type of the component
	 * which has been manipulated to all registered endpoints that the message concerns.
	 */
	public void dispatch(Entity entity, ComponentType componentType) {
		List<EntityModificationEndpoint> regEntry = _register.get(componentType);
		// if there is an entry for the content type specified in the message, dispatch
		// the message to every endpoint in the list
		if (regEntry != null)
			for (EntityModificationEndpoint endpoint : regEntry)
				endpoint.receive(entity, componentType);
	}
	
	//----------------------------------------------------------------------------------------------
	// Registering
	//----------------------------------------------------------------------------------------------

	/**
	 * Register the specified endpoint with the specified component types.
	 */
	public void registerReceiver(EntityModificationEndpoint endpoint,
			ComponentType... componentTypes) {
		for (ComponentType cType : componentTypes)
			registerReceiver(endpoint, cType);
	}
	
	/**
	 * Register the specified endpoint with the specified component types.
	 */
	public void registerReceiver(EntityModificationEndpoint endpoint,
			List<ComponentType> componentTypes) {
		Iterator<ComponentType> iter = componentTypes.iterator();
		while(iter.hasNext())
			deregisterReceiver(endpoint, iter.next());
	}
	
	/**
	 * Internal method which handles the registration of a endpoint to a single component type.
	 */
	private void registerReceiver(EntityModificationEndpoint endpoint,
			ComponentType componentType) {
		List<EntityModificationEndpoint> regEntry = _register.get(componentType);
		// if there already is a register entry for the current component type, simply add the
		// endpoint to the list if it wasn't already part of it and continue with the next type
		if (regEntry != null) {
			if (!regEntry.contains(endpoint))
				regEntry.add(endpoint);
			return;
		}
		// if there was no entry for the current component type, create one with the
		// current endpoint as it's sole member
		regEntry = new ArrayList<EntityModificationEndpoint>();
		regEntry.add(endpoint);
		_register.put(componentType, regEntry);
	}
	
	//----------------------------------------------------------------------------------------------
	// Deregistering
	//----------------------------------------------------------------------------------------------
	
	public void deregisterReceiver(EntityModificationEndpoint endpoint,
			ComponentType... componentTypes) {
		for (ComponentType cType : componentTypes)
			registerReceiver(endpoint, cType);
	}
	
	public void deregisterReceiver(EntityModificationEndpoint endpoint,
			List<ComponentType> componentTypes) {
		Iterator<ComponentType> iter = componentTypes.iterator();
		while(iter.hasNext())
			deregisterReceiver(endpoint, iter.next());
	}
	
	private void deregisterReceiver(EntityModificationEndpoint endpoint,
			ComponentType componentType) {
		List<EntityModificationEndpoint> regEntry = _register.get(componentType);
		if (regEntry != null)
			regEntry.remove(endpoint);
	}
}
