package at.netcrawler.ui.graphical;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumnModel;

import at.andiwand.library.math.graph.Edge;
import at.andiwand.library.math.graph.GraphListener;
import at.andiwand.library.util.comparator.ObjectStringComparator;
import at.netcrawler.network.model.NetworkDevice;
import at.netcrawler.network.topology.Topology;
import at.netcrawler.network.topology.TopologyDevice;
import at.netcrawler.util.NetworkDeviceHelper;


@SuppressWarnings("serial")
public class DeviceTableModel extends AbstractTableModel implements
		GraphListener {
	
	private static final Map<String, NetworkDeviceDataAccessor> ACCESSOR_FOR_NAME;
	
	static {
		ACCESSOR_FOR_NAME = new HashMap<String, DeviceTableModel.NetworkDeviceDataAccessor>();
		
		ACCESSOR_FOR_NAME.put(
				"Hostname", new NetworkDeviceDataAccessor() {
					
					@Override
					public String get(NetworkDevice device) {
						return NetworkDeviceHelper.getHostname(device);
					}
				});
		ACCESSOR_FOR_NAME.put(
				"Major Capability", new NetworkDeviceDataAccessor() {
					
					@Override
					public String get(NetworkDevice device) {
						return NetworkDeviceHelper.getMajorCapability(device);
					}
				});
		ACCESSOR_FOR_NAME.put(
				"Capabilities", new NetworkDeviceDataAccessor() {
					
					@Override
					public String get(NetworkDevice device) {
						return NetworkDeviceHelper.concatCapabilities(device);
					}
				});
		ACCESSOR_FOR_NAME.put(
				"System", new NetworkDeviceDataAccessor() {
					
					@Override
					public String get(NetworkDevice device) {
						return NetworkDeviceHelper.getSystem(device);
					}
				});
		ACCESSOR_FOR_NAME.put(
				"Connected via", new NetworkDeviceDataAccessor() {
					
					@Override
					public String get(NetworkDevice device) {
						return NetworkDeviceHelper.getConnectedVia(device);
					}
				});
		ACCESSOR_FOR_NAME.put(
				"Management Addresses", new NetworkDeviceDataAccessor() {
					
					@Override
					public String get(NetworkDevice device) {
						return NetworkDeviceHelper
								.getManagementAddresses(device);
					}
				});
		ACCESSOR_FOR_NAME.put(
				"Uptime", new NetworkDeviceDataAccessor() {
					
					@Override
					public String get(NetworkDevice device) {
						return NetworkDeviceHelper.getUptime(device);
					}
				});
	}
	
	public static Collection<String> getColumnNames() {
		return Collections.unmodifiableCollection(ACCESSOR_FOR_NAME.keySet());
	}
	
	// private final JTable table;
	private final TableColumnModel columnModel;
	private List<TopologyDevice> devices;
	
	public DeviceTableModel(JTable table) {
		// this.table = table;
		this.columnModel = table.getColumnModel();
		this.devices = new ArrayList<TopologyDevice>();
	}
	
	public synchronized void setTopology(Topology topology) {
		List<TopologyDevice> temp = new ArrayList<TopologyDevice>(
				topology.getVertices());
		Collections.sort(
				temp, new ObjectStringComparator());
		devices = Collections.unmodifiableList(temp);
		
		fireTableDataChanged();
		
		topology.addListener(this);
	}
	
	@Override
	public int getColumnCount() {
		synchronized (columnModel) {
			return columnModel.getColumnCount();
		}
	}
	
	@Override
	public synchronized int getRowCount() {
		return devices.size();
	}
	
	@Override
	public void edgeAdded(Edge edge) {
		fireTableDataChanged();
	}
	
	@Override
	public void edgeRemoved(Edge edge) {
		fireTableDataChanged();
	}
	
	@Override
	public void vertexAdded(Object vertex) {
		fireTableDataChanged();
	}
	
	@Override
	public void vertexRemoved(Object vertex) {
		fireTableDataChanged();
	}
	
	@Override
	public synchronized Object getValueAt(int arg0, int arg1) {
		// NetworkDevice device =
		// devices.get(table.convertRowIndexToModel(arg0)).getNetworkDevice();
		NetworkDevice device = devices.get(
				arg0).getNetworkDevice();
		
		// String column = (String)
		// columnModel.getColumn(table.convertColumnIndexToModel(arg1)).getHeaderValue();
		String column;
		synchronized (columnModel) {
			column = (String) columnModel.getColumn(
					arg1).getHeaderValue();
		}
		
		if (ACCESSOR_FOR_NAME.containsKey(column)) {
			return ACCESSOR_FOR_NAME.get(
					column).get(
					device);
		} else {
			return "Not crawled.";
		}
	}
	
	public synchronized List<TopologyDevice> getDevices() {
		return Collections.unmodifiableList(devices);
	}
	
	private static interface NetworkDeviceDataAccessor {
		
		public String get(NetworkDevice device);
	}
}