package at.netcrawler.gui.device.category;

import java.awt.Component;

import at.netcrawler.network.model.NetworkModel;
import at.netcrawler.network.model.extension.SNMPDeviceExtension;

public class SNMPCategory extends Category {
	
	public SNMPCategory() {
		super("Miscellaneous", "SNMP");
	}
	
	
	@Override
	public Component render(NetworkModel device) {
		CategoryBuilder builder = new CategoryBuilder();
		builder.addTextRow("Engine ID", device.getValue(SNMPDeviceExtension.ENGINE_ID));
		
		return builder.build();
	}
}
