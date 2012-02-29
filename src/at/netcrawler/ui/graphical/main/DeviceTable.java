package at.netcrawler.ui.graphical.main;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collection;

import javax.swing.JTable;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import at.netcrawler.network.topology.Topology;
import at.netcrawler.network.topology.TopologyDevice;
import at.netcrawler.ui.graphical.device.DeviceView;


@SuppressWarnings("serial")
public class DeviceTable extends JTable {
	
	private static final Collection<String> COLUMNS;
	
	static {
		COLUMNS = DeviceModel.getColumnNames();
	}
	
	private final DeviceModel model;
	
	public DeviceTable() {
		model = new DeviceModel(this, getColumnModel());
		
		setModel(model);
		setAutoCreateRowSorter(true);
		
		TableColumnModel columnModel = getColumnModel();
		for (String column : COLUMNS) {
			TableColumn tableColumn = new TableColumn();
			tableColumn.setHeaderValue(column);
			
			columnModel.addColumn(tableColumn);
		}
		
		getTableHeader().addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON3) {
					new ColumnChooser(DeviceTable.this, COLUMNS, getColumnModel(), e);
				}
			}
		});
		
		addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1) {
					int selected = getSelectedRow();
					selected = getRowSorter().convertRowIndexToModel(selected);
					
					TopologyDevice device = model.getDevices().get(selected);
					new DeviceView(device.getNetworkDevice());
				}
			}
		});
	}
	
	public void setTopology(Topology topology) {
		model.setTopology(topology);
	}
}