package at.netcrawler.gui.device.category;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.lang.reflect.Array;
import java.util.Collection;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class CategoryBuilder {
	
	JPanel jPanel;
	
	int rows;
	
	
	public CategoryBuilder() {
		jPanel = new JPanel();
		jPanel.setLayout(new java.awt.GridBagLayout());
	}
	
	
	public void addTextRow(String name, Object data) {
		if (data == null) return;
		
		JLabel jLabel = new javax.swing.JLabel();
		JTextField jTextField = new javax.swing.JTextField();
		
		jLabel.setText(name + ":");
		GridBagConstraints gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
		gridBagConstraints.gridy = rows;
		jPanel.add(jLabel, gridBagConstraints);
		
		jTextField.setText(data.toString());
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
		gridBagConstraints.weightx = 0.1;
		gridBagConstraints.gridy = rows;
		jPanel.add(jTextField, gridBagConstraints);
		
		rows++;
	}
	
	public void addListRow(String name, Collection<Object> data) {
		if (data == null) return;
		
		JLabel jLabel = new javax.swing.JLabel();
		JList jList = new JList(collectionToArray(data));
		jList.setEnabled(false);
		
		jLabel.setText(name + ":");
		GridBagConstraints gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
		gridBagConstraints.gridy = rows;
		jPanel.add(jLabel, gridBagConstraints);
		
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
		gridBagConstraints.weightx = 0.1;
		gridBagConstraints.gridy = rows;
		jPanel.add(jList, gridBagConstraints);
		
		rows++;
	}
	
	public void addCustomRow(String name, Component data) {
		if (data == null) return;
		
		JLabel jLabel = new javax.swing.JLabel();
		
		jLabel.setText(name + ":");
		GridBagConstraints gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
		gridBagConstraints.gridy = rows;
		jPanel.add(jLabel, gridBagConstraints);
		
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
		gridBagConstraints.weightx = 0.1;
		gridBagConstraints.gridy = rows;
		jPanel.add(data, gridBagConstraints);
		
		rows++;
	}
	
	public JPanel build() {
		if (rows == 0) return jPanel;
		
//		Border border = BorderFactory.createLineBorder(Color.DARK_GRAY);
//		jPanel.setBorder(border);
		
		return jPanel;
	}
	
	@SuppressWarnings("unchecked")
	private static <T> T[] collectionToArray(Collection<T> collection) {
		Class<T> clazz = (Class<T>) collection.iterator().next().getClass();
		
		T[] array = (T[]) Array.newInstance(clazz, collection.size());
		
		int i = 0;
		for (T item : collection) {
			array[i] = item;
			
			i++;
		}
		
		return array;
	}
}
