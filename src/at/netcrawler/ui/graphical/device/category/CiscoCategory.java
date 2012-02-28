package at.netcrawler.ui.graphical.device.category;

import java.awt.Component;
import java.util.ArrayList;
import java.util.Collection;

import at.netcrawler.network.model.NetworkModel;
import at.netcrawler.network.model.extension.CiscoDeviceExtension;


public class CiscoCategory extends Category {
	
	public CiscoCategory() {
		super("Cisco", "Generic");
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Component render(NetworkModel device) {
		CategoryBuilder builder = new CategoryBuilder();
		if (device.getValue(CiscoDeviceExtension.CDP_NEIGHBORS) != null) {
			Collection<Object> table = new ArrayList<Object>((Collection<Object>) device.getValue(CiscoDeviceExtension.CDP_NEIGHBORS));
			
			builder.addListRow(
					"CDP Neighbours", table);
		}
		
		return builder.build();
	}
}
