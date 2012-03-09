package at.netcrawler.component;

import java.awt.Graphics;
import java.awt.Point;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import at.andiwand.library.component.GraphViewerEdge;
import at.andiwand.library.component.GraphViewerVertex;
import at.netcrawler.network.CableType;
import at.netcrawler.network.model.NetworkCable;
import at.netcrawler.network.model.NetworkModelAdapter;
import at.netcrawler.network.topology.TopologyCable;


public class TopologyViewerCable extends GraphViewerEdge {
	
	private static final Map<CableType, CablePainter> PAINTER_MAP = new HashMap<CableType, CablePainter>() {
		private static final long serialVersionUID = 6309138456376713160L;
		
		{
			put(null, new UnknownCablePainter());
		}
	};
	
	private class CableTypeAdapter extends NetworkModelAdapter {
		public void valueChanged(String key, Object value, Object oldValue) {
			if (!key.equals(NetworkCable.TYPE)) return;
			setCableType((CableType) value);
		}
	}
	
	private final Map<CableType, CablePainter> painterMap = new HashMap<CableType, CablePainter>(
			PAINTER_MAP);
	
	private final NetworkCable networkCable;
	private CableType cableType;
	private CablePainter painter;
	
	public TopologyViewerCable(TopologyCable cable,
			Set<GraphViewerVertex> vertices) {
		super(cable, vertices);
		
		networkCable = cable.getNetworkCable();
		setCableType((CableType) networkCable.getValue(NetworkCable.TYPE));
		networkCable.addListener(new CableTypeAdapter());
	}
	
	@SuppressWarnings("unchecked")
	public Set<TopologyViewerDevice> getDevices() {
		return (Set<TopologyViewerDevice>) (Set<?>) super.getVertices();
	}
	
	public TopologyCable getTopologyCable() {
		return (TopologyCable) getEdge();
	}
	
	public NetworkCable getNetworkCable() {
		return networkCable;
	}
	
	public CablePainter getPainter(CableType cableType) {
		CablePainter painter = painterMap.get(cableType);
		if (painter == null) painter = painterMap.get(null);
		return painter;
	}
	
	private void setCableType(CableType cableType) {
		this.cableType = cableType;
		painter = getPainter(cableType);
		
		fireRepaint();
	}
	
	public void setPainter(CableType cableType, CablePainter painter) {
		painterMap.put(
				cableType, painter);
		setCableType(this.cableType);
	}
	
	@Override
	public void paint(Graphics g) {
		painter.paint(
				g, this);
	}
	
	@Override
	public boolean intersects(Point p) {
		return false;
	}
	
}