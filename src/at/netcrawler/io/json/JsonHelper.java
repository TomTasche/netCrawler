package at.netcrawler.io.json;

import java.net.InetAddress;

import at.andiwand.library.network.ip.IPv4Address;
import at.andiwand.library.network.ip.SubnetMask;
import at.andiwand.library.network.mac.MACAddress;
import at.netcrawler.network.model.NetworkModel;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


public class JsonHelper {
	
	public static GsonBuilder getGsonBuilder() {
		GsonBuilder builder = new GsonBuilder();
		
		builder.setPrettyPrinting();
		
		builder.registerTypeAdapter(IPv4Address.class,
				new JsonIPv4AddressAdapter());
		builder.registerTypeAdapter(InetAddress.class,
				new JsonIPv4AddressAdapter());
		builder.registerTypeAdapter(MACAddress.class,
				new JsonMACAddressAdapter());
		builder.registerTypeAdapter(NetworkModel.class,
				new JsonNetworkModelAdapter());
		builder.registerTypeAdapter(SubnetMask.class,
				new JsonSubnetMaskAdapter());
		
		return builder;
	}
	
	public static Gson getGson() {
		return getGsonBuilder().create();
	}
	
}