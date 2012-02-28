package at.netcrawler.ui.graphical.device.category;

import java.awt.Component;

import at.netcrawler.network.model.NetworkModel;
import at.netcrawler.network.model.extension.CiscoRouterExtension;


public class CiscoRouterCategory extends Category {
	
	public CiscoRouterCategory() {
		super("Cisco", "Router");
	}
	
	@Override
	public Component render(NetworkModel device) {
		CategoryBuilder builder = new CategoryBuilder();
		builder.addTextRow(
				"Processor Board ID",
				device.getValue(CiscoRouterExtension.PROCESSOR_BOARD_ID));
		
		return builder.build();
	}
}
