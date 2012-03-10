package at.netcrawler.ui.graphical.device.category;

import java.awt.Component;

import at.netcrawler.network.manager.DeviceManager;
import at.netcrawler.network.model.NetworkDevice;
import at.netcrawler.network.model.extension.SNMPDeviceExtension;


public class SNMPCategory extends Category {
	
	public SNMPCategory() {
		super("Miscellaneous", "SNMP");
	}
	
	@Override
	public Component render(DeviceManager manager, NetworkDevice device) {
		CategoryBuilder builder = new CategoryBuilder();
		builder.addTextRow(
				"Engine ID", manager, device, SNMPDeviceExtension.ENGINE_ID);
		
		return builder.build();
	}
}
