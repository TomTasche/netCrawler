package at.netcrawler.ui.graphical.main;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collection;

import javax.swing.JTable;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import at.netcrawler.network.topology.Topology;
import at.netcrawler.network.topology.TopologyDevice;


@SuppressWarnings("serial")
public class DeviceTable extends JTable {
	
	private static final Collection<String> COLUMNS;
	
	static {
		COLUMNS = DeviceTableModel.getColumnNames();
	}
	
	private final DeviceTableModel model;
	private final GUI gui;
	
	public DeviceTable(GUI gui) {
		this.gui = gui;
		
		model = new DeviceTableModel(this);
		
		setModel(model);
		setAutoCreateRowSorter(true);
		
		TableColumnModel columnModel = getColumnModel();
		for (String column : COLUMNS) {
			TableColumn tableColumn = new TableColumn(columnModel.getColumnCount());
			tableColumn.setHeaderValue(column);
			
			columnModel.addColumn(tableColumn);
		}
		
		getTableHeader().setReorderingAllowed(false);
		getTableHeader().addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON3) {
					// TODO:
					// new ColumnChooser(DeviceTable.this, COLUMNS, e);
				}
			}
		});
		
		addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseClicked(MouseEvent e) {
				int selected = getSelectedRow();
				if (selected < 0) return;
				
				selected = getRowSorter().convertRowIndexToModel(selected);
				
				TopologyDevice device = model.getDevices().get(selected);
				DeviceTable.this.gui.handleMouse(e, device);
			}
		});
	}
	
	public void setTopology(Topology topology) {
		model.setTopology(topology);
	}
}