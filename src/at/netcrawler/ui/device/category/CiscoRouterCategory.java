package at.netcrawler.ui.device.category;

import java.awt.Component;

import at.netcrawler.network.manager.DeviceManager;
import at.netcrawler.network.model.NetworkDevice;
import at.netcrawler.network.model.extension.CiscoRouterExtension;


public class CiscoRouterCategory extends Category {
	
	public CiscoRouterCategory() {
		super("Cisco", "Router");
	}
	
	@Override
	public Component render(DeviceManager manager, NetworkDevice device) {
		CategoryBuilder builder = new CategoryBuilder();
		builder.addTextRow("Processor Board ID", manager, device,
				CiscoRouterExtension.PROCESSOR_BOARD_ID);
		
		return builder.build();
	}
}
