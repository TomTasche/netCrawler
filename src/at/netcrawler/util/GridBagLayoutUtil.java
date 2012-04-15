package at.netcrawler.util;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JPanel;

public class GridBagLayoutUtil {

	private static final Insets DEFAULT_INSETS = new Insets(0, 5, 5, 5);
	private static final double DEFAULT_WEIGHT = 0.1;

	private GridBagLayout layout;
	private JPanel panel;
	private Insets insets;
	private int columns;
	private double weight;

	private int column;
	private int row;

	public GridBagLayoutUtil(int columns) {
		this.columns = columns;

		layout = new GridBagLayout();
		panel = new JPanel(layout);
		insets = DEFAULT_INSETS;
		weight = DEFAULT_WEIGHT;
		
		column = 0;
		row = 0;
	}

	public GridBagLayoutUtil setInsets(Insets insets) {
		this.insets = insets;

		return this;
	}
	
	public int getRow() {
		return row;
	}
	
	public int getColumn() {
		return column;
	}
	
	public int getColumns() {
		return columns;
	}

	public double getWeight() {
		return weight;
	}
	
	public Insets getInsets() {
		return insets;
	}
	
	public JPanel getPanel() {
		return panel;
	}
	
	public void add(Component component) {
		add(component, insets);
	}

	public void add(Component component, Insets insets) {
		if (component == null) {
			increment(1);
			
			return;
		}
		
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.weightx = weight;
		constraints.gridx = column;
		constraints.gridy = row;
		constraints.insets = insets;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		if (column == 0) {
			constraints.anchor = GridBagConstraints.LINE_START;
		} else if (column == columns) {
			constraints.anchor = GridBagConstraints.LINE_END;
		} else {
			constraints.anchor = GridBagConstraints.CENTER;
		}
		
		add(component, constraints);
	}
	
	public void add(Component component, GridBagConstraints constraints) {
		panel.add(component, constraints);
		
		increment(constraints.gridwidth);
	}

	private void increment(int i) {
		column += i;
		
		if (column >= columns) {
			column = 0;
			row++;
		}
	}
}
