package at.rennweg.htl.netcrawler.network.graph;


public class EthernetLink extends PointToPointLink {
	
	private boolean crossover;
	
	
	public EthernetLink(NetworkInterface networkInterfaceA, NetworkInterface networkInterfaceB) {
		super(networkInterfaceA, networkInterfaceB);
	}
	
	
	public boolean isCrossover() {
		return crossover;
	}
	
	public void setCrossover(boolean crossover) {
		this.crossover = crossover;
	}
	
}