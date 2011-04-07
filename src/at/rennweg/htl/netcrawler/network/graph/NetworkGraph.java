package at.rennweg.htl.netcrawler.network.graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import at.andiwand.library.math.graph.AbstractGraph;
import at.andiwand.library.math.graph.GraphListener;
import at.andiwand.library.math.graph.ListenableGraph;



public class NetworkGraph extends AbstractGraph<NetworkDevice, NetworkLink> implements ListenableGraph<NetworkDevice, NetworkLink> {
	
	private Set<NetworkDevice> vertices;
	private List<NetworkLink> cables;
	
	private List<GraphListener<NetworkDevice, NetworkLink>> listeners;
	
	
	
	
	public NetworkGraph() {
		vertices = new HashSet<NetworkDevice>();
		cables = new ArrayList<NetworkLink>();
		
		listeners = new ArrayList<GraphListener<NetworkDevice, NetworkLink>>();
	}
	
	
	
	
	public int getEdgeCount(NetworkLink edge) {
		int result = 0;
		
		for (NetworkLink e : cables) {
			if (e.equals(edge)) result++;
		}
		
		return result;
	}
	
	public int getCableCount(NetworkLink cable) {
		return getCableCount(cable);
	}
	
	
	@Override
	public Set<NetworkDevice> getVertices() {
		return Collections.unmodifiableSet(vertices);
	}
	
	@Override
	public List<NetworkLink> getEdges() {
		return Collections.unmodifiableList(cables);
	}
	
	public List<NetworkLink> getCables() {
		return getEdges();
	}
	
	
	public Set<NetworkLink> getConnectedCables(NetworkDevice networkDevice) {
		Set<NetworkLink> result = new HashSet<NetworkLink>();
		
		for (NetworkLink cable : cables) {
			if (cable.getConnectedVertices().contains(networkDevice))
				result.add(cable);
		}
		
		return result;
	}
	
	
	public Set<NetworkInterface> getAviableInterfaces(NetworkDevice networkDevice) {
		Set<NetworkInterface> result = networkDevice.getInterfaces();
		Set<NetworkLink> cables = getConnectedCables(networkDevice);
		
		for (NetworkLink cable : cables) {
			result.removeAll(cable.getConnectedInterfaces());
		}
		
		return result;
	}
	
	public boolean isInterfaceAviable(NetworkInterface networkInterface) {
		for (NetworkLink cable : cables) {
			if (cable.getConnectedInterfaces().contains(networkInterface))
				return false;
		}
		
		return true;
	}
	
	@Override
	public boolean isSimple() {
		//TODO: implement
		return false;
	}
	
	
	
	
	@Override
	public boolean addVertex(NetworkDevice vertex) {
		boolean result = vertices.add(vertex);
		
		if (result) {
			for (GraphListener<NetworkDevice, NetworkLink> listener : listeners) {
				listener.vertexAdded(vertex);
			}
		}
		
		return result;
	}
	
	@Override
	public boolean addEdge(NetworkLink edge) {
		if (!vertices.containsAll(edge.getConnectedVertices())) return false;
		
		for (NetworkInterface networkInterface : edge.getConnectedInterfaces()) {
			if (!isInterfaceAviable(networkInterface)) return false;
		}
		
		boolean result = cables.add(edge);
		
		if (result) {
			for (GraphListener<NetworkDevice, NetworkLink> listener : listeners) {
				listener.edgeAdded(edge);
			}
		}
		
		return result;
	}
	
	public boolean addCable(NetworkLink cable) {
		return addEdge(cable);
	}
	
	
	@Override
	public void addListener(GraphListener<NetworkDevice, NetworkLink> listener) {
		listeners.add(listener);
	}
	
	
	
	@Override
	public boolean removeVertex(NetworkDevice vertex) {
		boolean result = vertices.remove(vertex);
		
		for (NetworkLink cable : getConnectedCables(vertex)) {
			removeEdge(cable);
		}
		
		for (GraphListener<NetworkDevice, NetworkLink> listener : listeners) {
			listener.vertexRemoved(vertex);
		}
		
		return result;
	}
	
	@Override
	public boolean removeEdge(NetworkLink edge) {
		boolean result = cables.remove(edge);
		
		if (result) {
			for (GraphListener<NetworkDevice, NetworkLink> listener : listeners) {
				listener.edgeRemoved(edge);
			}
		}
		
		return result;
	}
	
	@Override
	public boolean removeAllEdges(NetworkLink edge) {
		boolean result = false;
		
		List<NetworkLink> edgesCopy = getEdges();
		for (NetworkLink edgeItem : edgesCopy) {
			if (edgeItem.equals(edge)) result |= removeEdge(edgeItem);
		}
		
		return result;
	}
	
	
	@Override
	public void removeListener(GraphListener<NetworkDevice, NetworkLink> listener) {
		listeners.remove(listener);
	}
	
}