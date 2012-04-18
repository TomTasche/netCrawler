package at.netcrawler.ui.device.category;

import java.awt.Component;
import java.util.Collection;

import at.netcrawler.network.manager.DeviceManager;
import at.netcrawler.network.model.NetworkDevice;


public class DeviceCategory extends Category {
	
	public DeviceCategory() {
		super("System", "Generic");
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Component render(DeviceManager manager, NetworkDevice device) {
		CategoryBuilder builder = new CategoryBuilder();
		builder.addTextRow("Hostname", manager, device, NetworkDevice.HOSTNAME);
		builder.addTextRow("System", manager, device,
				NetworkDevice.SYSTEM_DESCRIPTION);
		builder.addListRow("Management Address", (Collection<Object>) device
				.getValue(NetworkDevice.MANAGEMENT_ADDRESSES));
		builder.addTextRow("Major Capability", manager, device,
				NetworkDevice.MAJOR_CAPABILITY);
		// builder.addListRow("Capabilites", (Collection<Object>) device
		// .getValue(NetworkDevice.CAPABILITIES));
		// TODO: interfaces shouldn't be displayed in this category, although
		// they're declared in NetworkDevice
		
		if (device.getValue(NetworkDevice.UPTIME) != null) {
			float uptime = (Long) device.getValue(NetworkDevice.UPTIME) / 1000 / 60 / 60;
			builder.addTextRow("Uptime", uptime);
		}
		
		return builder.build();
	}
	
}
