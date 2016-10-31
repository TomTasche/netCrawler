package at.netcrawler.ui.component;

import at.andiwand.library.component.GenericGraphViewerVertexFactory;
import at.netcrawler.network.topology.TopologyDevice;


public class TopologyViewerDeviceFactory extends
		GenericGraphViewerVertexFactory<TopologyDevice, TopologyViewerDevice> {
	
	public TopologyViewerDeviceFactory() {
		super(TopologyDevice.class, TopologyViewerDevice.class);
	}
	
	@Override
	protected TopologyViewerDevice buildVertexGeneric(TopologyDevice device) {
		return new TopologyViewerDevice(device);
	}
	
}