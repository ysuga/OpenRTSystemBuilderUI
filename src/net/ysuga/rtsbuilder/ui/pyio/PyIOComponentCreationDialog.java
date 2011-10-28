/**
 * PAIOComponentCreationDialog.java
 *
 * @author Yuki Suga (ysuga.net)
 * @date 2011/10/07
 * @copyright 2011, ysuga.net allrights reserved.
 *
 */
package net.ysuga.rtsbuilder.ui.pyio;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.ysuga.corbanaming.ui.GridLayoutPanel;
import net.ysuga.rtsbuilder.ui.MainFrame;
import net.ysuga.rtsystem.profile.DataPort;
import net.ysuga.rtsystem.profile.ExecutionContext;
import net.ysuga.rtsystem.profile.PyIOComponent;
import net.ysuga.rtsystem.profile.RTComponent;

/**
 * 
 * @author ysuga
 * 
 */
public class PyIOComponentCreationDialog extends JDialog {

	private JTextField moduleNameField;
	// private JTextField moduleDescField;
	private JTextField nameServerField;
	private JTextField namingContextField;
	private JTextField executionRateField;

	private JTextField moduleVendorField;
	private JTextField moduleVersionField;
	private JTextField moduleCategoryField;

	private JComboBox dataInPortComboBox;
	private JComboBox dataOutPortComboBox;
	private JComboBox servicePortComboBox;
	
	private String onExecuteCode;
	private String onActivatedCode;
	private String onDeactivatedCode;

	// private JTextField moduleCompTypeField;
	// private JTextField moduleActTypeField;
	// private JTextField moduleMaxInstanceField;

	public PyIOComponentCreationDialog() {
		this("ModuleName", "localhost", "%n.rtc", "100", "ModuleVendor", "1.0", "Category");
	}
	
	/**
	 * <div lang="ja"> �R���X�g���N�^
	 * 
	 * @param sourceDataPort
	 * @param dataPort
	 *            </div> <div lang="en"> Constructor
	 * @param sourceDataPort
	 * @param dataPort
	 *            </div>
	 * @throws Exception
	 */
	public PyIOComponentCreationDialog(String moduleName, String nameServer,
			String namingContext, String executionRate, String vendor, String version, String category) {
		super();

		moduleNameField = new JTextField(moduleName);
		nameServerField = new JTextField(nameServer);
		namingContextField = new JTextField(namingContext);
		namingContextField.setEditable(false);
		executionRateField = new JTextField(executionRate);
		// moduleDescField = new JTextField("Module Description");
		moduleVendorField = new JTextField(vendor);
		moduleVersionField = new JTextField(version);
		moduleCategoryField = new JTextField(category);

		dataInPortComboBox = new JComboBox();
		dataOutPortComboBox = new JComboBox();
		servicePortComboBox = new JComboBox();

		dataPortList = new ArrayList<DataPort>();
	}

	public PyIOComponentCreationDialog(PyIOComponent component) {
		this(component.getModuleName(), component.getNameServer(), component.getNamingContext(), 
				component.getExecutionRate(), component.getVendor(), component.getVersion(), component.getCategory());
		
		for(DataPort dataPort : (Set<DataPort>)component.dataPortSet) {
			if(dataPort.getDirection() == DataPort.DIRECTION_IN) {
				this.dataInPortComboBox.addItem(dataPort.getPlainName());
			} else if (dataPort.getDirection() == DataPort.DIRECTION_OUT) {
				this.dataOutPortComboBox.addItem(dataPort.getPlainName());
			} else {
				this.servicePortComboBox.addItem(dataPort.getPlainName());
			}
			this.dataPortList.add(dataPort);
		}
		
		onExecuteCode = component.getOnExecuteCode();
		onActivatedCode = component.getOnActivatedCode();
		onDeactivatedCode = component.getOnDeactivatedCode();
	}
	
	
	private int exitOption = JOptionPane.CANCEL_OPTION;
	private JButton okButton;
	private JPanel contentPane;
	private List<DataPort> dataPortList;

	public void onOk(ActionEvent arg0) {
		exitOption = JOptionPane.OK_OPTION;
		setVisible(false);
	}

	public void onCancel() {

		setVisible(false);
	}

	/**
	 * 
	 * 
	 * @return void
	 */
	private void initPanel() {
		okButton = new JButton(new AbstractAction("OK") {
			public void actionPerformed(ActionEvent arg0) {
				onOk(arg0);
			}
		});

		contentPane = (JPanel) getContentPane();
		GridLayoutPanel panel = new GridLayoutPanel();
		contentPane.add(panel, BorderLayout.CENTER);

		contentPane.add(Box.createVerticalStrut(20), BorderLayout.NORTH); 
		contentPane.add(Box.createVerticalStrut(20), BorderLayout.SOUTH); 
		contentPane.add(Box.createHorizontalStrut(20), BorderLayout.WEST); 
		contentPane.add(Box.createHorizontalStrut(20), BorderLayout.EAST); 
		
		int line = 0;
		
		panel.addComponent(0, line, 10, 0, 5, 1, new JLabel("Input PyIO Component Initial Parameters."));
		line++;
		
		panel.addComponent(0, line, 0, 0, 1, 1, new JLabel("Module Name"));
		panel.addComponent(1, line, 10, 0, 4, 1, moduleNameField);
		line++;

		panel.addComponent(0, line, 0, 0, 1, 1, new JLabel("NameServer URI"));
		panel.addComponent(1, line, 10, 0, 4, 1, nameServerField);
		line++;

		panel.addComponent(0, line, 0, 0, 1, 1, new JLabel("Naming Context"));
		panel.addComponent(1, line, 10, 0, 4, 1, namingContextField);
		line++;

		panel.addComponent(0, line, 0, 0, 1, 1, new JLabel("Execution Rate"));
		panel.addComponent(1, line, 10, 0, 4, 1, executionRateField);
		line++;

		panel.addComponent(0, line, 0, 0, 1, 1, new JLabel("Vendor"));
		panel.addComponent(1, line, 10, 0, 4, 1, moduleVendorField);
		line++;

		panel.addComponent(0, line, 0, 0, 1, 1, new JLabel("Version"));
		panel.addComponent(1, line, 10, 0, 4, 1, moduleVersionField);
		line++;

		panel.addComponent(0, line, 0, 0, 1, 1, new JLabel("Category"));
		panel.addComponent(1, line, 10, 0, 4, 1, moduleCategoryField);
		line++;

		panel.addComponent(0, line, 0, 0, 1, 1, new JLabel("DataInPort"));
		panel.addComponent(1, line, 10, 0, 1, 1, dataInPortComboBox);
		panel.addComponent(2, line, 0, 0, 1, 1, new JButton(new AbstractAction(
				"Add") {
			public void actionPerformed(ActionEvent e) {
				onAddDataInPort();
			}
		}));
		panel.addComponent(3, line, 0, 0, 1, 1, new JButton(new AbstractAction(
				"Del") {
			public void actionPerformed(ActionEvent e) {
				onDelDataInPort();
			}
		}));
		panel.addComponent(4, line, 0, 0, 1, 1, new JButton(new AbstractAction(
				"Edit") {
			public void actionPerformed(ActionEvent e) {
				onEditDataInPort();
			}
		}));
		line++;

		panel.addComponent(0, line, 0, 0, 1, 1, new JLabel("DataOutPort"));
		panel.addComponent(1, line, 10, 0, 1, 1, dataOutPortComboBox);
		panel.addComponent(2, line, 0, 0, 1, 1, new JButton(new AbstractAction(
				"Add") {
			public void actionPerformed(ActionEvent e) {
				onAddDataOutPort();
			}
		}));
		panel.addComponent(3, line, 0, 0, 1, 1, new JButton(new AbstractAction(
				"Del") {
			public void actionPerformed(ActionEvent e) {
				onDelDataOutPort();
			}
		}));
		panel.addComponent(4, line, 0, 0, 1, 1, new JButton(new AbstractAction(
				"Edit") {
			public void actionPerformed(ActionEvent e) {
				onEditDataOutPort();
			}
		}));
		line++;

		panel.addComponent(0, line, 0, 0, 1, 1, new JLabel("ServicePort"));
		panel.addComponent(1, line, 10, 0, 1, 1, servicePortComboBox);
		panel.addComponent(2, line, 0, 0, 1, 1, new JButton(new AbstractAction(
				"Add") {
			public void actionPerformed(ActionEvent e) {
				onAddServicePort();
			}
		}));
		panel.addComponent(3, line, 0, 0, 1, 1, new JButton(new AbstractAction(
				"Del") {
			public void actionPerformed(ActionEvent e) {
				onDelServicePort();
			}
		}));
		panel.addComponent(4, line, 0, 0, 1, 1, new JButton(new AbstractAction(
				"Edit") {
			public void actionPerformed(ActionEvent e) {
				onEditServicePort();
			}
		}));
		line++;

		panel.addComponent(0, line, 0, 0, 1, 1, new JLabel(""));
		panel.addComponent(1, line, 10, 0, 1, 1,Box.createHorizontalStrut(60));
		panel.addComponent(2, line, 0, 0, 1, 1, new JLabel(""));
		panel.addComponent(3, line, 0, 0, 1, 1,new JButton(new AbstractAction("Cancel") {
			public void actionPerformed(ActionEvent e) {
				onCancel();
			}
		}));
		panel.addComponent(4, line, 0, 0, 1, 1,new JButton(new AbstractAction("OK") {
			public void actionPerformed(ActionEvent e) {
				onOk(e);
			}
		}));
		
		okButton.setRequestFocusEnabled(true);

	}

	public int doModal() {
		initPanel();
		pack();
		Point mainLocation = MainFrame.getInstance().getLocation();
		Dimension mainSize = MainFrame.getInstance().getSize();
		Dimension thisSize = getSize();
		super.setLocation(mainLocation.x+mainSize.width/2-thisSize.width/2, mainLocation.y + mainSize.height/2 - thisSize.height/2);
		setModal(true);
		setVisible(true);
		return exitOption;
	}

	/**
	 * createComponent
	 * 
	 * @return
	 * @throws IOException
	 */
	public RTComponent createComponent() throws IOException {
		String Id = "RTC:" + moduleVendorField.getText() + ":"
				+ moduleCategoryField.getText() + ":"
				+ moduleNameField.getText() + ":"
				+ moduleVersionField.getText();
		String pathUri = nameServerField.getText();
		if (!pathUri.endsWith("/")) {
			pathUri = pathUri + "/";
		}
		pathUri = pathUri + moduleNameField.getText() + "0.rtc";
		String instanceName = this.moduleNameField.getText() + "0";
		PyIOComponent component = new PyIOComponent(
				instanceName, pathUri, Id);

		component.executionContextSet.add(new ExecutionContext("0",
				"rtsExt:execution_context_ext", "PERIODIC",
				this.executionRateField.getText()));
		
		for (DataPort port : dataPortList) {
			//String remappedName = instanceName + "." + port.getPlainName();
			String remappedName =  "." + port.getPlainName(); // Port Naming Bug. This will be fixed in 1.1 Release.
			port.put(DataPort.RTS_NAME, remappedName);
			component.dataPortSet.add(port);
		}
		
		component.setPeriodicRate(this.executionRateField.getText());
		component.setNamingContext(this.namingContextField.getText());
		component.setNameServers(this.nameServerField.getText());
		
		if(onExecuteCode != null) {
			component.setOnExecuteCode(onExecuteCode);
		}
		if(onActivatedCode != null) {
			component.setOnActivatedCode(onActivatedCode);
		}
		if(onDeactivatedCode != null) {
			component.setOnDeactivatedCode(onDeactivatedCode);
		}
		return component;
	}

	private void onAddDataInPort() {
		PyIODataPortCreationDialog dialog = new PyIODataPortCreationDialog(
				"DataInPort");
		if (dialog.doModal() == JOptionPane.OK_OPTION) {
			DataPort dataPort = dialog.createDataPort();
			this.dataPortList.add(dataPort);
			dataInPortComboBox.addItem(dataPort.get(DataPort.RTS_NAME));
			refresh();
		}
	}

	/**
	 * refresh
	 *
	 */
	private void refresh() {
		setVisible(false);
		pack();
		setVisible(true);
	}

	private void onDelDataInPort() {
		String name = (String)dataInPortComboBox.getSelectedItem();
		int index = dataInPortComboBox.getSelectedIndex();
		for(DataPort dataPort : dataPortList) {
			if(dataPort.get(DataPort.RTS_NAME).equals(name)) {
				dataInPortComboBox.removeItemAt(index);
				dataPortList.remove(dataPort);
				refresh();
				return;
			}
		}
	}

	private void onEditDataInPort() {
		String name = (String)dataInPortComboBox.getSelectedItem();
		int index = dataInPortComboBox.getSelectedIndex();
		for(DataPort dataPort : dataPortList) {
			if(dataPort.get(DataPort.RTS_NAME).equals(name)) {
				PyIODataPortCreationDialog dialog = new PyIODataPortCreationDialog("DataInPort", dataPort);
				if(dialog.doModal() == JOptionPane.OK_OPTION) {
					dataPortList.remove(dataPort);
					dataInPortComboBox.removeItemAt(index);
					DataPort newDataPort = dialog.createDataPort();
					dataInPortComboBox.insertItemAt(newDataPort.get(DataPort.RTS_NAME), index);
					dataPortList.add(newDataPort);
					refresh();
					return;
				}
			}
		}
	}

	private void onAddDataOutPort() {
		PyIODataPortCreationDialog dialog = new PyIODataPortCreationDialog(
				"DataOutPort");
		if (dialog.doModal() == JOptionPane.OK_OPTION) {
			DataPort dataPort = dialog.createDataPort();
			this.dataPortList.add(dataPort);
			dataOutPortComboBox.addItem(dataPort.get(DataPort.RTS_NAME));
			refresh();
		}
	}

	private void onDelDataOutPort() {
		String name = (String)dataInPortComboBox.getSelectedItem();
		int index = dataInPortComboBox.getSelectedIndex();
		for(DataPort dataPort : dataPortList) {
			if(dataPort.get(DataPort.RTS_NAME).equals(name)) {
				dataOutPortComboBox.removeItemAt(index);
				dataPortList.remove(dataPort);
				refresh();
				return;
			}
		}
	}

	private void onEditDataOutPort() {
		String name = (String)dataOutPortComboBox.getSelectedItem();
		int index = dataOutPortComboBox.getSelectedIndex();
		for(DataPort dataPort : dataPortList) {
			if(dataPort.get(DataPort.RTS_NAME).equals(name)) {
				PyIODataPortCreationDialog dialog = new PyIODataPortCreationDialog("DataOutPort", dataPort);
				if(dialog.doModal() == JOptionPane.OK_OPTION) {
					dataPortList.remove(dataPort);
					dataOutPortComboBox.removeItemAt(index);
					DataPort newDataPort = dialog.createDataPort();
					dataOutPortComboBox.insertItemAt(newDataPort.get(DataPort.RTS_NAME), index);
					dataPortList.add(newDataPort);
					refresh();
					return;
				}
			}
		}
	}

	private void onAddServicePort() {

	}

	private void onDelServicePort() {

	}

	private void onEditServicePort() {

	}

}
