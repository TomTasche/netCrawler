package at.netcrawler.io.json;

import java.awt.Point;
import java.net.InetAddress;

import at.andiwand.library.network.ip.IPv4Address;
import at.andiwand.library.network.ip.SubnetMask;
import at.andiwand.library.network.mac.MACAddress;
import at.netcrawler.network.model.NetworkModel;
import at.netcrawler.network.topology.Topology;
import at.netcrawler.network.topology.TopologyCable;
import at.netcrawler.network.topology.TopologyDevice;
import at.netcrawler.network.topology.TopologyInterface;
import at.netcrawler.ui.component.TopologyViewer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


public class JsonHelper {
	
	public static GsonBuilder getGsonBuilder() {
		GsonBuilder builder = new GsonBuilder();
		
		builder.setPrettyPrinting();
		
		// @formatter:off
		builder.registerTypeAdapter(Point.class, new JsonPointAdapter());
		builder.registerTypeAdapter(IPv4Address.class, new JsonIPv4AddressAdapter());
		builder.registerTypeAdapter(InetAddress.class,new JsonIPv4AddressAdapter());
		builder.registerTypeAdapter(MACAddress.class, new JsonMACAddressAdapter());
		builder.registerTypeAdapter(SubnetMask.class, new JsonSubnetMaskAdapter());
		builder.registerTypeAdapter(TopologyDevice.class, new JsonTopologyDeviceAdapter());
		builder.registerTypeAdapter(TopologyCable.class, new JsonTopologyCableAdapter());
		builder.registerTypeAdapter(TopologyViewer.class, new JsonTopologyViewerAdapter());
		
		builder.registerTypeHierarchyAdapter(NetworkModel.class, new JsonNetworkModelAdapter());
		builder.registerTypeHierarchyAdapter(TopologyInterface.class, new JsonTopologyInterfaceAdapter());
		builder.registerTypeHierarchyAdapter(Topology.class, new JsonTopologyAdapter());
		// @formatter:on
		
		return builder;
	}
	
	public static Gson getGson() {
		return getGsonBuilder().create();
	}
	
}