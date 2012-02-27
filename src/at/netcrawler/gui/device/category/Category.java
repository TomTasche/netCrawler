package at.netcrawler.gui.device.category;

import java.awt.Component;

import at.netcrawler.network.model.NetworkModel;


public abstract class Category {
	
	protected final String category;
	protected final String sub;
	
	public Category(String category, String sub) {
		this.category = category;
		this.sub = sub;
	}
	
	public abstract Component render(NetworkModel device);
	
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
