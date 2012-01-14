package at.netcrawler.network.topology;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import at.andiwand.library.math.graph.AbstractHypergraph;


public abstract class NetworkTopology extends
		AbstractHypergraph<TopologyDevice, TopologyCable> {
	
	@Override
	public final int getVertexCount() {
		return getDeviceCount();
	}
	
	public int getDeviceCount() {
		return super.getVertexCount();
	}
	
	@Override
	public final int getEdgeCount() {
		return getCableCount();
	}
	
	public int getCableCount() {
		return super.getVertexCount();
	}
	
	@Override
	public final Set<TopologyDevice> getVertices() {
		return getDevices();
	}
	
	public abstract Set<TopologyDevice> getDevices();
	
	@Override
	public final Collection<TopologyCable> getEdges() {
		return getCables();
	}
	
	public abstract Set<TopologyCable> getCables();
	
	public abstract Map<TopologyInterface, TopologyCable> getConnectionMap();
	
	@Override
	public final boolean addVertex(TopologyDevice vertex) {
		return addDevice(vertex);
	}
	
	public abstract boolean addDevice(TopologyDevice device);
	
	@Override
	public final boolean addEdge(TopologyCable edge) {
		return addCable(edge);
	}
	
	public abstract boolean addCable(TopologyCable cable);
	
	@Override
	public final boolean removeVertex(TopologyDevice vertex) {
		return removeDevice(vertex);
	}
	
	public abstract boolean removeDevice(TopologyDevice device);
	
	@Override
	public final boolean removeEdge(TopologyCable edge) {
		return removeCable(edge);
	}
	
	public abstract boolean removeCable(TopologyCable cable);
	
	@Override
	public final boolean removeAllEdges(TopologyCable edge) {
		return removeAllCables(edge);
	}
	
	public boolean removeAllCables(TopologyCable cable) {
		return super.removeAllEdges(cable);
	}
	
}