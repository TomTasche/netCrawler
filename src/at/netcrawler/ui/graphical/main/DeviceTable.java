package at.netcrawler.ui.graphical.main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import at.andiwand.library.util.comparator.StringLengthComperator;
import at.netcrawler.network.model.NetworkDevice;
import at.netcrawler.network.topology.Topology;
import at.netcrawler.network.topology.TopologyDevice;
import at.netcrawler.ui.graphical.device.DeviceView;


@SuppressWarnings("serial")
public class DeviceTable extends JTable {
	
	private static final Map<String, Boolean> COLUMNS;
	
	static {
		COLUMNS = new HashMap<String, Boolean>();
		COLUMNS.put("Hostname", true);
		COLUMNS.put("Major Capability", true);
		COLUMNS.put("Capabilities", true);
		COLUMNS.put("System", true);
		COLUMNS.put("Connected via", true);
		COLUMNS.put("Management Addresses", true);
		COLUMNS.put("Uptime", true);
	}
	
	Topology topology;
	List<TopologyDevice> devices;
	DeviceModel model;
	
	public DeviceTable() {
		model = new DeviceModel();
		devices = new ArrayList<TopologyDevice>(0);
		
		setModel(model);
		setAutoCreateRowSorter(true);

		TableColumnModel columnModel = getColumnModel();
		for (Entry<String, Boolean> column : COLUMNS.entrySet()) {
			TableColumn tableColumn = new TableColumn();
			tableColumn.setHeaderValue(column.getKey());
			
			columnModel.addColumn(tableColumn);
		}
		
		getTableHeader().addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON3) {
					new ColumnChooser(COLUMNS, e);
				}
			}
		});
		
		addMouseListener(new MouseAdapter() {
			
			@Override
			public synchronized void mouseClicked(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1) {
					int selected = getSelectedRow();
					selected = getRowSorter().convertRowIndexToModel(selected);
					
					TopologyDevice device = devices.get(selected);
					new DeviceView(device.getNetworkDevice());
				}
			}
		});
	}
	
	public synchronized void setTopology(Topology topology) {
		this.topology = topology;
		
		List<TopologyDevice> temp = new ArrayList<TopologyDevice>(
				topology.getVertices());
		Collections.sort(temp, new StringLengthComperator());
		devices = Collections.unmodifiableList(temp);
		
		model.fireTableDataChanged();
	}
	
	private void updateColumns() {
		// TODO
	}
	
	private class DeviceModel extends AbstractTableModel {
		
		@Override
		public int getColumnCount() {
			return getColumnModel().getColumnCount();
		}
		
		@Override
		public synchronized int getRowCount() {
			return devices.size();
		}
		
		@Override
		public synchronized Object getValueAt(int arg0, int arg1) {
			NetworkDevice device = devices.get(arg0).getNetworkDevice();
			
			switch (arg1) {
			case 0:
				return NetworkDeviceHelper.getHostname(device);
				
			case 1:
				return NetworkDeviceHelper.concatCapabilities(device);
				
			case 2:
				return NetworkDeviceHelper.getMajorCapability(device);
				
			case 3:
				return NetworkDeviceHelper.getSystem(device);
				
			case 4:
				return "muss der andi noch implementieren :)";
				
			case 5:
				return NetworkDeviceHelper.getManagementAddresses(device);
				
			case 6:
				return NetworkDeviceHelper.getUptime(device);
				
			default:
				return "Not crawled.";
			}
		}
	}
	
	private class ColumnChooser extends JPopupMenu {
		
		public ColumnChooser(Map<String, Boolean> columns, MouseEvent event) {
			for (Entry<String, Boolean> column : columns.entrySet()) {
				JMenuItem item = createColumnCheckbox(column.getKey(), column.getValue());
				add(item);
			}
			
			addFocusListener(new FocusAdapter() {
				
				@Override
				public void focusLost(FocusEvent e) {
					setVisible(false);
				}
			});
			
			show(DeviceTable.this, event.getX(), event.getY());
		}
		
		
		private JMenuItem createColumnCheckbox(final String column, boolean selected) {
			JMenuItem item = new JMenuItem(column);
			item.setSelected(selected);
			// TODO: item.setSelectedIcon(selectedIcon);
			item.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent arg0) {
					COLUMNS.put(column, !COLUMNS.get(column));
					
					updateColumns();
					
					ColumnChooser.this.setVisible(false);
				}
			});
			
			return item;
		}
	}
}