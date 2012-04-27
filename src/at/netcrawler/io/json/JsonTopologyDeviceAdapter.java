package at.netcrawler.io.json;

import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;

import at.andiwand.library.util.TypeToken;
import at.netcrawler.network.model.NetworkDevice;
import at.netcrawler.network.model.NetworkInterface;
import at.netcrawler.network.topology.TopologyDevice;
import at.netcrawler.network.topology.TopologyInterface;
import at.netcrawler.network.topology.UnknownTopologyInterface;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;


// TODO: remove?
public class JsonTopologyDeviceAdapter extends JsonAdapter<TopologyDevice> {
	
	private static final String NETWORK_DEVICE_PROPERTY = "networkDevice";
	private static final Type NETWORK_DEVICE_TYPE = new TypeToken<NetworkDevice>() {}
			.getType();
	private static final String INTERFACE_NAMES_PROPERTY = "interfaceNames";
	private static final Type INTERFACE_NAMES_TYPE = new TypeToken<Set<String>>() {}
			.getType();
	
	@Override
	public JsonElement serialize(TopologyDevice src, Type typeOfSrc,
			JsonSerializationContext context) {
		Set<String> interfaceNames = new HashSet<String>();
		
		for (TopologyInterface interfaze : src.getInterfaces()) {
			if (interfaze instanceof UnknownTopologyInterface) continue;
			
			interfaceNames.add(interfaze.getName());
		}
		
		JsonObject result = new JsonObject();
		result.add(NETWORK_DEVICE_PROPERTY, context.serialize(src
				.getNetworkDevice()));
		result.add(INTERFACE_NAMES_PROPERTY, context.serialize(interfaceNames));
		
		return result;
	}
	
	@Override
	public TopologyDevice deserialize(JsonElement json, Type typeOfT,
			JsonDeserializationContext context) throws JsonParseException {
		JsonObject object = json.getAsJsonObject();
		
		NetworkDevice networkDevice = context.deserialize(object
				.get(NETWORK_DEVICE_PROPERTY), NETWORK_DEVICE_TYPE);
		TopologyDevice result = new TopologyDevice(networkDevice);
		
		Set<String> interfaceNames = context.deserialize(object
				.get(INTERFACE_NAMES_PROPERTY), INTERFACE_NAMES_TYPE);
		Set<NetworkInterface> networkInterfaces = networkDevice
				.getValue(NetworkDevice.INTERFACES);
		for (String interfaceName : interfaceNames) {
			for (NetworkInterface networkInterface : networkInterfaces) {
				if (!interfaceName.equals(networkInterface
						.getValue(NetworkInterface.NAME))) continue;
				
				result.addInterface(new TopologyInterface(networkInterface));
				break;
			}
		}
		
		return result;
	}
	
}