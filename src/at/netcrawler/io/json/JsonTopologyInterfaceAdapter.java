package at.netcrawler.io.json;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;

import at.netcrawler.network.topology.TopologyDevice;
import at.netcrawler.network.topology.TopologyInterface;
import at.netcrawler.network.topology.UnknownTopologyInterface;


public class JsonTopologyInterfaceAdapter extends
		JsonAdapter<TopologyInterface> {
	
	private static final String DEVICE_NAME_PROPERTY = "deviceName";
	private static final String INTERFACE_NAME_PROPERTY = "interfaceName";
	
	@Override
	public JsonElement serialize(TopologyInterface src, Type typeOfSrc,
			JsonSerializationContext context) {
		JsonObject result = new JsonObject();
		
		String deviceName = JsonTopologyAdapter.getSerializedDeviceName(src
				.getDevice());
		result.add(DEVICE_NAME_PROPERTY, new JsonPrimitive(deviceName));
		
		if (!(src instanceof UnknownTopologyInterface))
			result.add(INTERFACE_NAME_PROPERTY,
					new JsonPrimitive(src.getName()));
		
		return result;
	}
	
	@Override
	public TopologyInterface deserialize(JsonElement json, Type typeOfT,
			JsonDeserializationContext context) throws JsonParseException {
		JsonObject object = json.getAsJsonObject();
		
		JsonElement interfaceNameElement = object.get(INTERFACE_NAME_PROPERTY);
		if (interfaceNameElement == null)
			return new UnknownTopologyInterface();
		
		String deviceName = object.get(DEVICE_NAME_PROPERTY).getAsString();
		TopologyDevice topologyDevice = JsonTopologyAdapter
				.getDeserializedTopologyDevice(deviceName);
		
		String interfaceName = interfaceNameElement.getAsString();
		return topologyDevice.getInterfaceByName(interfaceName);
	}
	
}