package at.netcrawler.ui.graphical.device.category;

import java.awt.Component;

import at.netcrawler.network.model.NetworkModel;
import at.netcrawler.network.model.extension.CiscoSwitchExtension;


public class CiscoSwitchCategory extends Category {
	
	public CiscoSwitchCategory() {
		super("Cisco", "Switch");
	}
	
	@Override
	public Component render(NetworkModel device) {
		CategoryBuilder builder = new CategoryBuilder();
		builder.addTextRow(
				"Model Number",
				device.getValue(CiscoSwitchExtension.MODEL_NUMBER));
		builder.addTextRow(
				"System Serial Number",
				device.getValue(CiscoSwitchExtension.SYSTEM_SERIAL_NUMBER));
		
		return builder.build();
	}
}
