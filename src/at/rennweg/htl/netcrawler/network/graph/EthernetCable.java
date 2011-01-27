package at.rennweg.htl.netcrawler.network.graph;

import java.util.Set;


public class EthernetCable extends NetworkSharedCable {
	
	private boolean crossover;
	
	
	public EthernetCable(NetworkInterface... networkInterfaces) {
		super(networkInterfaces);
	}
	public EthernetCable(Set<NetworkInterface> networkInterfaces) {
		super(networkInterfaces);
	}
	
	
	public boolean isCrossover() {
		return crossover;
	}
	
	public void setCrossover(boolean crossover) {
		this.crossover = crossover;
	}
	
}