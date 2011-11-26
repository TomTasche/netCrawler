package at.netcrawler.network.model;

import java.util.Map;
import java.util.Set;


public interface NetworkModelExtension {
	
	public Class<? extends NetworkModel> getExtendedModelClass();
	public Set<? extends NetworkModelExtension> getRequiredExtensions();
	
	public Map<String, Class<?>> getExtensionTypeMap();
	
	public boolean isExtensionSupported(NetworkModel model);
	
}