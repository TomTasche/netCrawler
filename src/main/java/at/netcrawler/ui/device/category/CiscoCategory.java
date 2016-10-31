package at.netcrawler.ui.device.category;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

import at.netcrawler.network.manager.DeviceManager;
import at.netcrawler.network.model.CDPNeighbor;
import at.netcrawler.network.model.NetworkDevice;
import at.netcrawler.network.model.extension.CiscoDeviceExtension;


public class CiscoCategory extends Category {
	
	public CiscoCategory() {
		super("Cisco", "Generic");
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Component render(DeviceManager manager, NetworkDevice device) {
		CategoryBuilder builder = new CategoryBuilder();
		if (device.getValue(CiscoDeviceExtension.CDP_NEIGHBORS) != null) {
			List<CDPNeighbor> table = new ArrayList<CDPNeighbor>(
					(List<CDPNeighbor>) device
							.getValue(CiscoDeviceExtension.CDP_NEIGHBORS));
			
			List<String> neighbors = new ArrayList<String>();
			for (CDPNeighbor neighbor : table) {
				neighbors.add(neighbor.getHostname());
			}
			
			builder.addListRow("CDP Neighbours", neighbors);
		}
		
		return builder.build();
	}
	
}
