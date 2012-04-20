package at.netcrawler.io.json;

import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;

import at.andiwand.library.util.TypeToken;
import at.netcrawler.network.model.NetworkCable;
import at.netcrawler.network.topology.TopologyCable;
import at.netcrawler.network.topology.TopologyDevice;
import at.netcrawler.network.topology.TopologyInterface;
import at.netcrawler.network.topology.UnknownTopologyInterface;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;


public class JsonTopologyCableAdapter extends JsonAdapter<TopologyCable> {
	
	private static final String NETWORK_CABLE_PROPERTY = "networkCable";
	private static final Type NETWORK_CABLE_TYPE = new TypeToken<NetworkCable>() {}
			.getType();
	
	private static final String CONNECTED_INTERFACES_PROPERTY = "connectedInterfaces";
	private static final String CONNECTED_INTERFACES_DEVICE_NAME_PROPERTY = "deviceName";
	private static final String CONNECTED_INTERFACES_INTERFACE_NAME_PROPERTY = "interfaceName";
	
	@Override
	public JsonElement serialize(TopologyCable src, Type typeOfSrc,
			JsonSerializationContext context) {
		JsonObject result = new JsonObject();
		
		result.add(NETWORK_CABLE_PROPERTY, context.serialize(src
				.getNetworkCable()));
		
		JsonArray connectedInterfaceArray = new JsonArray();
		result.add(CONNECTED_INTERFACES_PROPERTY, connectedInterfaceArray);
		
		for (TopologyInterface interfaze : src.getConnectedInterfaces()) {
			JsonObject connectedInterface = new JsonObject();
			connectedInterfaceArray.add(connectedInterface);
			
			String deviceName = JsonTopologyAdapter
					.getSerializedDeviceName(interfaze.getDevice());
			connectedInterface.add(CONNECTED_INTERFACES_DEVICE_NAME_PROPERTY,
					new JsonPrimitive(deviceName));
			
			if (interfaze instanceof UnknownTopologyInterface) continue;
			connectedInterface.add(
					CONNECTED_INTERFACES_INTERFACE_NAME_PROPERTY,
					new JsonPrimitive(interfaze.getName()));
		}
		
		return result;
	}
	
	@Override
	public TopologyCable deserialize(JsonElement json, Type typeOfT,
			JsonDeserializationContext context) throws JsonParseException {
		JsonObject object = json.getAsJsonObject();
		
		NetworkCable networkCable = context.deserialize(object
				.get(NETWORK_CABLE_PROPERTY), NETWORK_CABLE_TYPE);
		
		Set<TopologyInterface> connectedInterfaces = new HashSet<TopologyInterface>();
		
		JsonArray connectedInterfaceArray = object.get(
				CONNECTED_INTERFACES_PROPERTY).getAsJsonArray();
		for (JsonElement element : connectedInterfaceArray) {
			JsonObject connectedInterface = element.getAsJsonObject();
			
			TopologyInterface interfaze;
			String interfaceName = connectedInterface.get(
					CONNECTED_INTERFACES_INTERFACE_NAME_PROPERTY).getAsString();
			
			if (interfaceName == null) {
				interfaze = new UnknownTopologyInterface();
			} else {
				String deviceName = connectedInterface.get(
						CONNECTED_INTERFACES_DEVICE_NAME_PROPERTY)
						.getAsString();
				TopologyDevice device = JsonTopologyAdapter
						.getDeserializedTopologyDevice(deviceName);
				interfaze = device.getInterfaceByName(interfaceName);
			}
			
			connectedInterfaces.add(interfaze);
		}
		
		return new TopologyCable(networkCable, connectedInterfaces);
	}
}