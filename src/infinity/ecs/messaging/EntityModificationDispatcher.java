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
 * 
 * @author preip
 */
public final class EntityModificationDispatcher {
	
	 // Dev-Notes:
	 // System may be inflexible, because messages consist only of the manipulated entity and the type
	 // of component that has been manipulated. It should be evaluated if that is sufficient for all
	 // cases or if there is a need for custom modification messages.
	 // The advantage of the current version is the avoidance of the creation of new message objects
	 // by simply passing references, thus saving memory and speed.
	
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
	 * 
	 * @param sender The endpoint which initiates the dispatch
	 * @param entity The entity which has been modified
	 * @param componentType The type of the component which has been modified
	 */
	public void dispatch(EntityModificationEndpoint sender, Entity entity,
			ComponentType componentType) {
		List<EntityModificationEndpoint> regEntry = _register.get(componentType);
		// if there is an entry for the content type specified in the message, dispatch
		// the message to every endpoint in the list, apart from the sender
		if (regEntry != null)
			for (EntityModificationEndpoint endpoint : regEntry)
				if (endpoint != sender)
					endpoint.receive(entity, componentType);
	}
	
	//----------------------------------------------------------------------------------------------
	// Registering
	//----------------------------------------------------------------------------------------------

	/**
	 * Register the specified endpoint with the specified component types.
	 * 
	 * @param endpoint The endpoint which wants to register a set of component types
	 * @param componentTypes A set of component for which the endpoint wants to register
	 */
	public void register(EntityModificationEndpoint endpoint,
			ComponentType... componentTypes) {
		for (ComponentType cType : componentTypes)
			registerInternal(endpoint, cType);
	}
	
	/**
	 * Register the specified endpoint with the specified component types.
	 * 
	 * @param endpoint The endpoint which wants to register a set of component types
	 * @param componentTypes A set of component for which the endpoint wants to register
	 */
	public void register(EntityModificationEndpoint endpoint,
			List<ComponentType> componentTypes) {
		Iterator<ComponentType> iter = componentTypes.iterator();
		while(iter.hasNext())
			registerInternal(endpoint, iter.next());
	}
	
	/**
	 * Internal method which handles the registration of a endpoint to a single component type.
	 * 
	 * @param endpoint The endpoint which should be registered
	 * @param componentType The type of component the endpoint wants to register to
	 */
	private void registerInternal(EntityModificationEndpoint endpoint,
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
	
	/**
	 * Deregister the specified endpoint with the specified component types.
	 * 
	 * @param endpoint The endpoint which should be deregistered
	 * @param componentTypes The type of component the endpoint wants to deregister from
	 */
	public void deregister(EntityModificationEndpoint endpoint,
			ComponentType... componentTypes) {
		for (ComponentType cType : componentTypes)
			deregisterInternal(endpoint, cType);
	}
	
	/**
	 * Deregister the specified endpoint with the specified component types.
	 * 
	 * @param endpoint The endpoint which should be deregistered
	 * @param componentTypes The type of component the endpoint wants to deregister from
	 */
	public void deregister(EntityModificationEndpoint endpoint,
			List<ComponentType> componentTypes) {
		Iterator<ComponentType> iter = componentTypes.iterator();
		while(iter.hasNext())
			deregisterInternal(endpoint, iter.next());
	}
	
	/**
	 * Internal method which handles the deregistration of a endpoint from a single component type.
	 * 
	 * @param endpoint The endpoint which should be deregistered
	 * @param componentType The type of component the endpoint wants to register from
	 */
	private void deregisterInternal(EntityModificationEndpoint endpoint,
			ComponentType componentType) {
		List<EntityModificationEndpoint> regEntry = _register.get(componentType);
		if (regEntry != null)
			regEntry.remove(endpoint);
	}
}
