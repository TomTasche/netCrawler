package at.netcrawler.ui.graphical.device.category;

import java.awt.Component;
import java.util.ArrayList;
import java.util.Collection;

import at.netcrawler.network.model.NetworkModel;
import at.netcrawler.network.model.extension.RouterExtension;


public class RouterCategory extends Category {
	
	public RouterCategory() {
		super("Router", "Generic");
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Component render(NetworkModel device) {
		CategoryBuilder builder = new CategoryBuilder();
		if (device.getValue(RouterExtension.ROUTING_TABLE) != null) {
			Collection<Object> table = new ArrayList<Object>(
					(Collection<Object>) device
							.getValue(RouterExtension.ROUTING_TABLE));
			
			builder.addListRow("Routing Table", table);
		}
		
		return builder.build();
	}
}
