package at.netcrawler.network.topology.identification;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public final class DeviceIdentificationSet {
	
	private final Map<Class<? extends DeviceIdentification>, DeviceIdentification> identificationMap = new HashMap<Class<? extends DeviceIdentification>, DeviceIdentification>();
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (obj == this) return true;
		
		if (!(obj instanceof DeviceIdentificationSet)) return false;
		DeviceIdentificationSet identificationSet = (DeviceIdentificationSet) obj;
		
		return identificationMap.equals(identificationSet.identificationMap);
	}
	
	@Override
	public int hashCode() {
		return identificationMap.hashCode();
	}
	
	public void addIdentification(DeviceIdentification identification) {
		identificationMap.put(identification.getClass(), identification);
	}
	
	public void removeIdentification(DeviceIdentification identification) {
		identificationMap.remove(identification.getClass());
	}
	
	public boolean contains(DeviceIdentification identification) {
		Class<? extends DeviceIdentification> identificationClass = identification
				.getClass();
		DeviceIdentification deviceIdentificationB = identificationMap
				.get(identificationClass);
		
		if (deviceIdentificationB == null) return false;
		return deviceIdentificationB.equals(identification);
	}
	
	public boolean collides(DeviceIdentificationSet identificationSet) {
		Set<Class<? extends DeviceIdentification>> identificationClasses = new HashSet<Class<? extends DeviceIdentification>>();
		identificationClasses.addAll(identificationMap.keySet());
		identificationClasses.retainAll(identificationSet.identificationMap
				.keySet());
		
		if (identificationClasses.isEmpty()) return false;
		
		for (Class<? extends DeviceIdentification> identificationClass : identificationClasses) {
			DeviceIdentification identificationA = identificationMap
					.get(identificationClass);
			DeviceIdentification identificationB = identificationSet.identificationMap
					.get(identificationClass);
			
			if (identificationA.equals(identificationB)) return true;
		}
		
		return false;
	}
	
}