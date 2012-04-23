package at.netcrawler.io.json;

import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;

import at.andiwand.library.util.TypeToken;
import at.netcrawler.network.model.NetworkCable;
import at.netcrawler.network.topology.TopologyCable;
import at.netcrawler.network.topology.TopologyInterface;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;


public class JsonTopologyCableAdapter extends JsonAdapter<TopologyCable> {
	
	private static final String NETWORK_CABLE_PROPERTY = "networkCable";
	private static final Type NETWORK_CABLE_TYPE = new TypeToken<NetworkCable>() {}
			.getType();
	
	private static final String CONNECTED_INTERFACES_PROPERTY = "connectedInterfaces";
	private static final Type CONNECTED_INTERFACES_ELEMENT_TYPE = new TypeToken<TopologyInterface>() {}
			.getType();
	
	@Override
	public JsonElement serialize(TopologyCable src, Type typeOfSrc,
			JsonSerializationContext context) {
		JsonObject result = new JsonObject();
		
		result.add(NETWORK_CABLE_PROPERTY, context.serialize(src
				.getNetworkCable()));
		
		JsonArray connectedInterfaces = new JsonArray();
		result.add(CONNECTED_INTERFACES_PROPERTY, connectedInterfaces);
		
		for (TopologyInterface topologyInterface : src.getConnectedInterfaces()) {
			connectedInterfaces.add(context.serialize(topologyInterface));
		}
		
		return result;
	}
	
	@Override
	public TopologyCable deserialize(JsonElement json, Type typeOfT,
			JsonDeserializationContext context) throws JsonParseException {
		JsonObject object = json.getAsJsonObject();
		
		NetworkCable networkCable = context.deserialize(object
				.get(NETWORK_CABLE_PROPERTY), NETWORK_CABLE_TYPE);
		
		Set<TopologyInterface> connectedTopologyInterfaces = new HashSet<TopologyInterface>();
		
		JsonArray connectedInterfaces = object.get(
				CONNECTED_INTERFACES_PROPERTY).getAsJsonArray();
		for (JsonElement connectedInterface : connectedInterfaces) {
			TopologyInterface topologyInterface = context.deserialize(
					connectedInterface, CONNECTED_INTERFACES_ELEMENT_TYPE);
			connectedTopologyInterfaces.add(topologyInterface);
		}
		
		return new TopologyCable(networkCable, connectedTopologyInterfaces);
	}
}