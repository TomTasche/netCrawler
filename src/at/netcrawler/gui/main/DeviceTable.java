package at.netcrawler.gui.main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import at.netcrawler.network.model.NetworkDevice;
import at.netcrawler.network.topology.Topology;
import at.netcrawler.network.topology.TopologyDevice;


@SuppressWarnings("serial")
public class DeviceTable extends JTable {
	
	private static final String[] COLUMN_NAMES = new String[] {"Hostname",
			"Uptime", "Capabilities"};
	
	Topology topology;
	List<TopologyDevice> devices;
	DeviceModel model;
	
	public DeviceTable() {
		model = new DeviceModel();
		
		setModel(model);
	}
	
	public synchronized void setTopology(Topology topology) {
		this.topology = topology;
		
		List<TopologyDevice> temp = new ArrayList<TopologyDevice>(
				topology.getVertices());
		devices = Collections.unmodifiableList(temp);
		
		model.fireTableDataChanged();
	}
	
	private class DeviceModel extends AbstractTableModel {
		
		@Override
		public int getColumnCount() {
			return 3;
		}
		
		@Override
		public synchronized int getRowCount() {
			return devices.size();
		}
		
		@Override
		public String getColumnName(int column) {
			return COLUMN_NAMES[column];
		}
		
		@Override
		public synchronized Object getValueAt(int arg0, int arg1) {
			TopologyDevice device = devices.get(arg0);
			
			switch (arg1) {
			case 0:
				return device.getHostname();
				
			case 1:
				return device.getNetworkDevice().getValue(
						NetworkDevice.UPTIME);
				
			case 2:
				return NetworkDeviceHelper.concatCapabilities(device
						.getNetworkDevice());
				
			default:
				return "Not crawled.";
			}
		}
	}
}