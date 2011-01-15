package at.rennweg.htl.netcrawler.network.graph;

import java.util.Set;


public class EthernetCable<V extends NetworkDevice> extends NetworkSharedCable<V> {
	
	private boolean crossover;
	
	
	public EthernetCable(V... networkDevices) {
		super(networkDevices);
	}
	public EthernetCable(Set<V> networkDevices) {
		super(networkDevices);
	}
	
	
	public boolean isCrossover() {
		return crossover;
	}
	
	public void setCrossover(boolean crossover) {
		this.crossover = crossover;
	}
	
}