package at.netcrawler.network.model;

import java.util.Map;
import java.util.Set;


public interface NetworkModelExtensionSet {
	
	public Class<? extends NetworkModel> getExtendedClass();
	public Set<? extends NetworkModelExtensionSet> getSuperExtensionSets();
	
	public Map<String, Class<?>> getTypeMap();
	
}