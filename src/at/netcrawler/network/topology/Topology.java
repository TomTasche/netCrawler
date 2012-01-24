package at.netcrawler.network.topology;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import at.andiwand.library.math.graph.AbstractHypergraph;
import at.andiwand.library.math.graph.GraphListener;
import at.andiwand.library.math.graph.ListenableGraph;


public abstract class Topology extends
		AbstractHypergraph<TopologyDevice, TopologyCable> implements
		ListenableGraph<TopologyDevice, TopologyCable> {
	
	private final List<TopologyListener> listeners = new ArrayList<TopologyListener>();
	
	public abstract Map<TopologyInterface, TopologyCable> getConnectionMap();
	
	public Set<TopologyCable> getConnectedCables(TopologyDevice device) {
		Set<TopologyCable> result = new HashSet<TopologyCable>();
		Map<TopologyInterface, TopologyCable> connectionMap = getConnectionMap();
		
		for (TopologyInterface interfaze : device.getInterfaces()) {
			TopologyCable cable = connectionMap.get(interfaze);
			result.add(cable);
		}
		
		return result;
	}
	
	@Override
	public void addListener(
			GraphListener<TopologyDevice, TopologyCable> listener) {
		throw new UnsupportedOperationException();
	}
	
	public void addListener(TopologyListener listener) {
		synchronized (listeners) {
			listeners.add(listener);
		}
	}
	
	protected abstract boolean addVertexImpl(TopologyDevice vertex);
	
	@Override
	public final boolean addVertex(TopologyDevice vertex) {
		if (!addVertexImpl(vertex)) return false;
		fireVertexAdded(vertex);
		return true;
	}
	
	protected abstract boolean addEdgeImpl(TopologyCable edge);
	
	@Override
	public final boolean addEdge(TopologyCable edge) {
		if (!addEdgeImpl(edge)) return false;
		fireEdgeAdded(edge);
		return true;
	}
	
	@Override
	public void removeListener(
			GraphListener<TopologyDevice, TopologyCable> listener) {
		throw new UnsupportedOperationException();
	}
	
	public final void removeListener(TopologyListener listener) {
		synchronized (listeners) {
			listeners.remove(listener);
		}
	}
	
	protected abstract boolean removeVertexImpl(TopologyDevice vertex);
	
	@Override
	public final boolean removeVertex(TopologyDevice vertex) {
		if (!removeVertexImpl(vertex)) return false;
		fireVertexRemoved(vertex);
		return true;
	}
	
	protected abstract boolean removeEdgeImpl(TopologyCable edge);
	
	@Override
	public final boolean removeEdge(TopologyCable edge) {
		if (!removeEdgeImpl(edge)) return false;
		fireEdgeRemoved(edge);
		return true;
	}
	
	private void fireVertexAdded(TopologyDevice vertex) {
		synchronized (listeners) {
			for (TopologyListener listener : listeners) {
				listener.vertexAdded(vertex);
			}
		}
	}
	
	private void fireVertexRemoved(TopologyDevice vertex) {
		synchronized (listeners) {
			for (TopologyListener listener : listeners) {
				listener.vertexRemoved(vertex);
			}
		}
	}
	
	private void fireEdgeAdded(TopologyCable edge) {
		synchronized (listeners) {
			for (TopologyListener listener : listeners) {
				listener.edgeAdded(edge);
			}
		}
	}
	
	private void fireEdgeRemoved(TopologyCable edge) {
		synchronized (listeners) {
			for (TopologyListener listener : listeners) {
				listener.edgeRemoved(edge);
			}
		}
	}
	
}