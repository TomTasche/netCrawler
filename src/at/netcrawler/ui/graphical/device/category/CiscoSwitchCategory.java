package at.netcrawler.ui.graphical.device.category;

import java.awt.Component;

import at.netcrawler.network.manager.DeviceManager;
import at.netcrawler.network.model.NetworkDevice;
import at.netcrawler.network.model.extension.CiscoSwitchExtension;


public class CiscoSwitchCategory extends Category {
	
	public CiscoSwitchCategory() {
		super("Cisco", "Switch");
	}
	
	@Override
	public Component render(DeviceManager manager, NetworkDevice device) {
		CategoryBuilder builder = new CategoryBuilder();
		builder.addTextRow(
				"Model Number", manager, device, CiscoSwitchExtension.MODEL_NUMBER);
		builder.addTextRow(
				"System Serial Number", manager, device, CiscoSwitchExtension.SYSTEM_SERIAL_NUMBER);
		
		return builder.build();
	}
}
