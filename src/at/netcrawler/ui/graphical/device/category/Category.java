package at.netcrawler.ui.graphical.device.category;

import java.awt.Component;

import at.netcrawler.network.manager.DeviceManager;
import at.netcrawler.network.model.NetworkDevice;


public abstract class Category {
	
	protected final String category;
	protected final String sub;
	
	public Category(String category, String sub) {
		this.category = category;
		this.sub = sub;
	}
	
	public abstract Component render(DeviceManager manager, NetworkDevice device);
	
	public String getCategory() {
		return category;
	}
	
	public String getSub() {
		return sub;
	}
	
	@Override
	public String toString() {
		return "Category [category=" + category + ", sub=" + sub + "]";
	}
}
