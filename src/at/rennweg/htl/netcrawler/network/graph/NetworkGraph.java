package at.rennweg.htl.netcrawler.network.graph;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import at.andiwand.library.math.graph.AbstractGraph;
import at.andiwand.library.math.graph.GraphListener;
import at.andiwand.library.math.graph.ListenableGraph;



public class NetworkGraph extends AbstractGraph<NetworkDevice, NetworkCable> implements ListenableGraph<NetworkDevice, NetworkCable> {
	
	private Set<NetworkDevice> vertices;
	private List<NetworkCable> cables;
	
	private List<GraphListener<NetworkDevice, NetworkCable>> listeners;
	
	
	
	
	public NetworkGraph() {
		vertices = new HashSet<NetworkDevice>();
		cables = new ArrayList<NetworkCable>();
		
		listeners = new ArrayList<GraphListener<NetworkDevice, NetworkCable>>();
	}
	
	
	
	
	public int getEdgeCount(NetworkCable edge) {
		int result = 0;
		
		for (NetworkCable e : cables) {
			if (e.equals(edge)) result++;
		}
		
		return result;
	}
	
	public int getCableCount(NetworkCable cable) {
		return getCableCount(cable);
	}
	
	
	@Override
	public Set<NetworkDevice> getVertices() {
		return new HashSet<NetworkDevice>(vertices);
	}
	
	@Override
	public List<NetworkCable> getEdges() {
		return new ArrayList<NetworkCable>(cables);
	}
	
	public List<NetworkCable> getCables() {
		return getEdges();
	}
	
	
	@Override
	public Set<NetworkDevice> getConnectedVertices(NetworkCable cable) {
		return cable.getConnectedVertices();
	}
	
	public Set<NetworkCable> getConnectedCables(NetworkDevice networkDevice) {
		Set<NetworkCable> result = new HashSet<NetworkCable>();
		
		for (NetworkCable cable : cables) {
			if (cable.getConnectedVertices().contains(networkDevice))
				result.add(cable);
		}
		
		return result;
	}
	
	
	public Set<NetworkInterface> getAviableInterfaces(NetworkDevice networkDevice) {
		Set<NetworkInterface> result = networkDevice.getInterfaces();
		Set<NetworkCable> cables = getConnectedCables(networkDevice);
		
		for (NetworkCable cable : cables) {
			result.removeAll(cable.getConnectedInterfaces());
		}
		
		return result;
	}
	
	public boolean isInterfaceAviable(NetworkInterface networkInterface) {
		for (NetworkCable cable : cables) {
			if (cable.getConnectedInterfaces().contains(networkInterface))
				return false;
		}
		
		return true;
	}
	
	
	
	
	@Override
	public boolean addVertex(NetworkDevice vertex) {
		boolean result = vertices.add(vertex);
		
		if (result) {
			for (GraphListener<NetworkDevice, NetworkCable> listener : listeners) {
				listener.vertexAdded(vertex);
			}
		}
		
		return result;
	}
	
	@Override
	public boolean addEdge(NetworkCable edge) {
		if (!vertices.containsAll(edge.getConnectedVertices())) return false;
		
		for (NetworkInterface networkInterface : edge.getConnectedInterfaces()) {
			if (!isInterfaceAviable(networkInterface)) return false;
		}
		
		boolean result = cables.add(edge);
		
		if (result) {
			for (GraphListener<NetworkDevice, NetworkCable> listener : listeners) {
				listener.edgeAdded(edge);
			}
		}
		
		return result;
	}
	
	public boolean addCable(NetworkCable cable) {
		return addEdge(cable);
	}
	
	
	@Override
	public void addListener(GraphListener<NetworkDevice, NetworkCable> listener) {
		listeners.add(listener);
	}
	
	
	
	@Override
	public boolean removeVertex(NetworkDevice vertex) {
		boolean result = vertices.remove(vertex);
		
		for (NetworkCable cable : getConnectedCables(vertex)) {
			removeEdge(cable);
		}
		
		for (GraphListener<NetworkDevice, NetworkCable> listener : listeners) {
			listener.vertexRemoved(vertex);
		}
		
		return result;
	}
	
	@Override
	public boolean removeEdge(NetworkCable edge) {
		boolean result = cables.remove(edge);
		
		if (result) {
			for (GraphListener<NetworkDevice, NetworkCable> listener : listeners) {
				listener.edgeRemoved(edge);
			}
		}
		
		return result;
	}
	
	@Override
	public boolean removeAllEdges(NetworkCable edge) {
		boolean result = false;
		
		List<NetworkCable> edgesCopy = getEdges();
		for (NetworkCable edgeItem : edgesCopy) {
			if (edgeItem.equals(edge)) result |= removeEdge(edgeItem);
		}
		
		return result;
	}
	
	
	@Override
	public void removeListener(GraphListener<NetworkDevice, NetworkCable> listener) {
		listeners.remove(listener);
	}
	
}