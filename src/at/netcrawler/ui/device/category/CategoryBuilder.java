package at.netcrawler.ui.device.category;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Collection;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import at.netcrawler.network.manager.DeviceManager;
import at.netcrawler.network.model.NetworkDevice;


public class CategoryBuilder {
	
	private static final Insets DEFAULT_INSETS = new Insets(0, 5, 5, 5);
	
	JPanel panel;
	int rows;
	
	public CategoryBuilder() {
		panel = new JPanel();
		panel.setLayout(new java.awt.GridBagLayout());
	}
	
	public void addTextRow(String name, final DeviceManager manager,
			NetworkDevice device, final String identifier) {
		Object temp = device.getValue(identifier);
		if (temp == null) return;
		String data = temp.toString();
		
		addTextRow(name, data, new CategoryCallback() {
			
			@Override
			public void save(Object value) {
				try {
					manager.setValue(identifier, value);
				} catch (IOException e) {
					e.printStackTrace();
					
					JOptionPane.showMessageDialog(panel, e
							.getLocalizedMessage(), "Error setting value",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
	}
	
	public void addTextRow(String name, Object data) {
		addTextRow(name, data, null);
	}
	
	public void addTextRow(String name, Object data,
			final CategoryCallback callback) {
		if (data == null) return;
		
		JLabel label = new JLabel();
		final JTextField textField = new JTextField();
		final JButton editButton = new JButton();
		
		label.setText(name + ":");
		GridBagConstraints gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
		gridBagConstraints.gridy = rows;
		gridBagConstraints.insets = DEFAULT_INSETS;
		panel.add(label, gridBagConstraints);
		
		textField.setText(data.toString());
		textField.setEditable(false);
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.CENTER;
		gridBagConstraints.weightx = 0.1;
		gridBagConstraints.gridy = rows;
		gridBagConstraints.insets = DEFAULT_INSETS;
		panel.add(textField, gridBagConstraints);
		
		if (callback != null) {
			editButton.setText("E");
			gridBagConstraints = new java.awt.GridBagConstraints();
			gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
			gridBagConstraints.gridy = rows;
			gridBagConstraints.insets = DEFAULT_INSETS;
			panel.add(editButton, gridBagConstraints);
			editButton.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent arg0) {
					boolean enabled = textField.isEditable();
					
					editButton.setText(!enabled ? "S" : "E");
					textField.setEditable(!enabled);
					
					if (enabled) callback.save(textField.getText());
				}
			});
		}
		
		rows++;
	}
	
	public void addListRow(String name, Collection<? extends Object> data) {
		if (data == null) return;
		
		JLabel jLabel = new javax.swing.JLabel();
		JList jList = new JList(collectionToArray(data));
		
		jLabel.setText(name + ":");
		GridBagConstraints gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
		gridBagConstraints.gridy = rows;
		gridBagConstraints.insets = DEFAULT_INSETS;
		panel.add(jLabel, gridBagConstraints);
		
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
		gridBagConstraints.weightx = 0.1;
		gridBagConstraints.gridy = rows;
		gridBagConstraints.insets = DEFAULT_INSETS;
		panel.add(jList, gridBagConstraints);
		
		rows++;
	}
	
	public void addCustomRow(String name, Component data) {
		if (data == null) return;
		
		JLabel jLabel = new javax.swing.JLabel();
		
		jLabel.setText(name + ":");
		GridBagConstraints gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
		gridBagConstraints.gridy = rows;
		gridBagConstraints.insets = DEFAULT_INSETS;
		panel.add(jLabel, gridBagConstraints);
		
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
		gridBagConstraints.weightx = 0.1;
		gridBagConstraints.gridy = rows;
		gridBagConstraints.insets = DEFAULT_INSETS;
		panel.add(data, gridBagConstraints);
		
		rows++;
	}
	
	public JPanel build() {
		GridBagConstraints gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints.weightx = 0.1;
		gridBagConstraints.weighty = 0.1;
		gridBagConstraints.gridy = rows;
		gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
		panel.add(new JPanel(), gridBagConstraints);
		
		return panel;
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
	
	public static interface CategoryCallback {
		
		public void save(Object value);
	}
	
}
