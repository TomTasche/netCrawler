package at.netcrawler.io.json;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import at.andiwand.library.util.TypeToken;
import at.andiwand.library.util.collections.HashMultiset;
import at.andiwand.library.util.collections.Multiset;
import at.netcrawler.network.topology.HashTopology;
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
	
	private static final ThreadLocal<Map<TopologyDevice, String>> SERIALIZED_DEVICE_NAMES = new ThreadLocal<Map<TopologyDevice, String>>();
	private static final ThreadLocal<Map<String, TopologyDevice>> DESERIALIZED_TOPOLOGY_DEVICES = new ThreadLocal<Map<String, TopologyDevice>>();
	
	private static final String DEVICES_PROPERTY = "devices";
	private static final Type DEVICES_ELEMENT_TYPE = new TypeToken<TopologyDevice>() {}
			.getType();
	
	private static final String CABLES_PROPERTY = "cables";
	private static final Type CABLES_ELEMENT_TYPE = new TypeToken<TopologyCable>() {}
			.getType();
	
	public static boolean isSerialized() {
		return SERIALIZED_DEVICE_NAMES.get() != null;
	}
	
	public static boolean isDeserialized() {
		return DESERIALIZED_TOPOLOGY_DEVICES.get() != null;
	}
	
	public static Map<TopologyDevice, String> getSerializedDeviceNames() {
		Map<TopologyDevice, String> result = SERIALIZED_DEVICE_NAMES.get();
		if (result == null)
			throw new IllegalStateException("No translation context");
		return result;
	}
	
	public static String getSerializedDeviceName(TopologyDevice topologyDevice) {
		return getSerializedDeviceNames().get(topologyDevice);
	}
	
	public static Map<String, TopologyDevice> getDeserializedTopologyDevices() {
		Map<String, TopologyDevice> result = DESERIALIZED_TOPOLOGY_DEVICES
				.get();
		if (result == null)
			throw new IllegalStateException("No translation context");
		return result;
	}
	
	public static TopologyDevice getDeserializedTopologyDevice(String deviceName) {
		return getDeserializedTopologyDevices().get(deviceName);
	}
	
	private static void setSerializedDeviceNames(
			Map<TopologyDevice, String> serializedDeviceNames) {
		SERIALIZED_DEVICE_NAMES.set(Collections
				.unmodifiableMap(serializedDeviceNames));
	}
	
	private static void setDeserializedTopologyDevices(
			Map<String, TopologyDevice> deserializedTopologyDevices) {
		DESERIALIZED_TOPOLOGY_DEVICES.set(Collections
				.unmodifiableMap(deserializedTopologyDevices));
	}
	
	public static void freeTranslation() {
		SERIALIZED_DEVICE_NAMES.remove();
		DESERIALIZED_TOPOLOGY_DEVICES.remove();
	}
	
	@Override
	public JsonElement serialize(Topology src, Type typeOfSrc,
			JsonSerializationContext context) {
		freeTranslation();
		
		JsonObject result = new JsonObject();
		
		JsonObject devices = new JsonObject();
		result.add(DEVICES_PROPERTY, devices);
		
		Multiset<String> deviceNames = new HashMultiset<String>();
		Map<TopologyDevice, String> serializedDeviceNames = new HashMap<TopologyDevice, String>();
		
		for (TopologyDevice device : src.getVertices()) {
			String hostname = device.getHostname();
			String uniqueHostname = hostname + "-"
					+ deviceNames.getElementCount(hostname);
			serializedDeviceNames.put(device, uniqueHostname);
			devices.add(uniqueHostname, context.serialize(device));
			deviceNames.add(hostname);
		}
		
		setSerializedDeviceNames(serializedDeviceNames);
		
		JsonArray cables = new JsonArray();
		result.add(CABLES_PROPERTY, cables);
		
		for (TopologyCable cable : src.getEdges()) {
			cables.add(context.serialize(cable));
		}
		
		return result;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public Topology deserialize(JsonElement json, Type typeOfT,
			JsonDeserializationContext context) throws JsonParseException {
		freeTranslation();
		
		// Class<? extends Topology> topologyClass = (Class<? extends Topology>)
		// typeOfT;
		JsonObject object = json.getAsJsonObject();
		
		try {
			// Topology result = topologyClass.newInstance();
			Topology result = new HashTopology();
			
			JsonObject devices = object.get(DEVICES_PROPERTY).getAsJsonObject();
			Map<String, TopologyDevice> deserializedTopologyDevices = new HashMap<String, TopologyDevice>();
			
			for (Map.Entry<String, JsonElement> entry : devices.entrySet()) {
				TopologyDevice topologyDevice = context.deserialize(entry
						.getValue(), DEVICES_ELEMENT_TYPE);
				deserializedTopologyDevices.put(entry.getKey(), topologyDevice);
				result.addVertex(topologyDevice);
			}
			
			setDeserializedTopologyDevices(deserializedTopologyDevices);
			
			JsonArray cables = object.get(CABLES_PROPERTY).getAsJsonArray();
			
			for (JsonElement element : cables) {
				TopologyCable cable = context.deserialize(element,
						CABLES_ELEMENT_TYPE);
				result.addEdge(cable);
			}
			
			return result;
		} catch (JsonParseException e) {
			throw e;
		} catch (Exception e) {
			throw new JsonParseException(e);
		}
	}
	
}