package at.netcrawler.ui.graphical.main;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseEvent;
import java.util.Collection;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

@SuppressWarnings("serial")
public class ColumnChooser extends JPopupMenu {
	
	private final TableColumnModel columnModel;

	public ColumnChooser(Component parent, Collection<String> columns, TableColumnModel columnModel, MouseEvent event) {
		this.columnModel = columnModel;
		
		List<String> visibleColumns = new LinkedList<String>();
		Enumeration<TableColumn> temp = columnModel.getColumns();
		while (temp.hasMoreElements()) {
			visibleColumns.add((String) temp.nextElement().getHeaderValue());
		}
		
		for (String column : columns) {
			JMenuItem item = createColumnCheckbox(column, visibleColumns.contains(column));
			add(item);
		}
		
		addFocusListener(new FocusAdapter() {
			
			@Override
			public void focusLost(FocusEvent e) {
				hideMenu();
			}
		});
		
		show(parent, event.getX(), event.getY());
	}
	
	private void hideMenu() {
		setVisible(false);
	}
	
	private JMenuItem createColumnCheckbox(final String column, boolean selected) {
		JMenuItem item = new JMenuItem(column);
		item.setSelected(selected);
		// TODO: item.setSelectedIcon(selectedIcon);
		item.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					columnModel.removeColumn(columnModel.getColumn(columnModel.getColumnIndex(column)));
				} catch (IllegalArgumentException e) {
					TableColumn tableColumn = new TableColumn();
					tableColumn.setHeaderValue(column);
					columnModel.addColumn(tableColumn);
				}
				
				hideMenu();
			}
		});
		
		return item;
	}
}