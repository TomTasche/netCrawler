package at.netcrawler.ui.graphical.device.category;

import java.awt.Component;
import java.util.Collection;

import at.netcrawler.network.model.NetworkDevice;
import at.netcrawler.network.model.NetworkModel;


public class DeviceCategory extends Category {
	
	public DeviceCategory() {
		super("System", "Generic");
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Component render(NetworkModel device) {
		CategoryBuilder builder = new CategoryBuilder();
		builder.addTextRow(
				"Hostname", (String) device.getValue(NetworkDevice.HOSTNAME));
		builder.addTextRow(
				"System", (String) device.getValue(NetworkDevice.SYSTEM_STRING));
		builder.addListRow("Management Address",
				(Collection<Object>) device
						.getValue(NetworkDevice.MANAGEMENT_ADDRESSES));
		builder.addTextRow(
				"Major Capability",
				device.getValue(NetworkDevice.MAJOR_CAPABILITY));
		builder.addListRow("Capabilites",
				(Collection<Object>) device
						.getValue(NetworkDevice.CAPABILITIES));
		// TODO: interfaces shouldn't be displayed in this category, although
		// they're declared in NetworkDevice
		
		if (device.getValue(NetworkDevice.UPTIME) != null) {
			float uptime = (Long) device.getValue(NetworkDevice.UPTIME) / 1000 / 60 / 60;
			builder.addTextRow(
					"Uptime", uptime);
		}
		
		return builder.build();
	}
}
