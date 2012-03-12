package at.netcrawler.io.json;

import at.andiwand.library.network.ip.IPv4Address;
import at.andiwand.library.network.ip.SubnetMask;
import at.andiwand.library.network.mac.MACAddress;
import at.netcrawler.network.model.NetworkModel;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


public class JsonHelper {
	
	private static final Gson GSON;
	
	static {
		GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(IPv4Address.class,
				new JsonIPv4AddressAdapter());
		builder.registerTypeAdapter(MACAddress.class,
				new JsonMACAddressAdapter());
		builder.registerTypeAdapter(NetworkModel.class,
				new JsonNetworkModelAdapter());
		builder.registerTypeAdapter(SubnetMask.class,
				new JsonSubnetMaskAdapter());
		
		GSON = builder.create();
	}
	
	public static Gson getGson() {
		return GSON;
	}
}
