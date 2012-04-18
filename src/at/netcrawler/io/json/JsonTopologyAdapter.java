package at.netcrawler.io.json;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import at.andiwand.library.util.TypeToken;
import at.andiwand.library.util.collections.HashMultiset;
import at.andiwand.library.util.collections.Multiset;
import at.netcrawler.network.topology.Topology;
import at.netcrawler.network.topology.TopologyCable;
import at.netcrawler.network.topology.TopologyDevice;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;


public class JsonTopologyAdapter extends JsonAdapter<Topology> {
	
	private static final String DEVICES_PROPERTY = "devices";
	private static final Type DEVICES_ELEMENT_TYPE = new TypeToken<TopologyDevice>() {}
			.getType();
	
	private static final String CABLES_PROPERTY = "cables";
	
	public JsonElement serialize(Topology src, Type typeOfSrc,
			JsonSerializationContext context) {
		JsonObject result = new JsonObject();
		
		JsonObject devices = new JsonObject();
		result.add(DEVICES_PROPERTY, devices);
		
		Multiset<String> deviceNames = new HashMultiset<String>();
		Map<TopologyDevice, String> deviceNameMap = new HashMap<TopologyDevice, String>();
		
		for (TopologyDevice device : src.getVertices()) {
			String hostname = device.getHostname();
			String uniqueHostname = hostname + "-"
					+ deviceNames.getElementCount(hostname);
			deviceNameMap.put(device, uniqueHostname);
			devices.add(uniqueHostname, context.serialize(device));
			deviceNames.add(hostname);
		}
		
		JsonArray cables = new JsonArray();
		result.add(CABLES_PROPERTY, cables);
		
		JsonTopologyCableAdapter cableAdapter = new JsonTopologyCableAdapter();
		cableAdapter.setNameMap(deviceNameMap);
		
		for (TopologyCable cable : src.getEdges()) {
			cables.add(cableAdapter.serialize(cable, TopologyCable.class,
					context));
		}
		
		return result;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public Topology deserialize(JsonElement json, Type typeOfT,
			JsonDeserializationContext context) throws JsonParseException {
		Class<? extends Topology> topologyClass = (Class<? extends Topology>) typeOfT;
		JsonObject object = json.getAsJsonObject();
		
		try {
			Topology result = topologyClass.newInstance();
			
			JsonObject devices = object.get(DEVICES_PROPERTY).getAsJsonObject();
			
			Map<String, TopologyDevice> deviceMap = new HashMap<String, TopologyDevice>();
			
			for (Map.Entry<String, JsonElement> entry : devices.entrySet()) {
				TopologyDevice topologyDevice = context.deserialize(entry
						.getValue(), DEVICES_ELEMENT_TYPE);
				deviceMap.put(entry.getKey(), topologyDevice);
				result.addVertex(topologyDevice);
			}
			
			JsonArray cables = object.get(CABLES_PROPERTY).getAsJsonArray();
			
			JsonTopologyCableAdapter cableAdapter = new JsonTopologyCableAdapter();
			cableAdapter.setDeviceMap(deviceMap);
			
			for (JsonElement element : cables) {
				result.addEdge(cableAdapter.deserialize(element,
						TopologyCable.class, context));
			}
			
			return result;
		} catch (JsonParseException e) {
			throw e;
		} catch (Exception e) {
			throw new JsonParseException(e);
		}
	}
	
}