/**
 * PAIOComponentCreationDialog.java
 *
 * @author Yuki Suga (ysuga.net)
 * @date 2011/10/07
 * @copyright 2011, ysuga.net allrights reserved.
 *
 */
package net.ysuga.rtsbuilder.ui.paio;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.ysuga.corbanaming.ui.GridLayoutPanel;
import net.ysuga.rtsystem.profile.Component;
import net.ysuga.rtsystem.profile.DataPort;
import net.ysuga.rtsystem.profile.ExecutionContext;
import net.ysuga.rtsystem.profile.PAIOComponent;
import net.ysuga.rtsystem.profile.RTSObject;

/**
 * 
 * @author ysuga
 * 
 */
public class PAIOComponentCreationDialog extends JDialog {

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

	// private JTextField moduleCompTypeField;
	// private JTextField moduleActTypeField;
	// private JTextField moduleMaxInstanceField;

	public PAIOComponentCreationDialog() {
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
	public PAIOComponentCreationDialog(String moduleName, String nameServer,
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

	public PAIOComponentCreationDialog(RTSObject rtsObj) {
		
	}
	
	/**
	 * setRTSObject
	 *
	 * @param rtsObj
	 */
	public void setRTSObject(RTSObject rtsObj) {
		// TODO 自動生成されたメソッド・スタブ
		
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

		int line = 0;
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

		contentPane.add(okButton, BorderLayout.SOUTH);
		okButton.setRequestFocusEnabled(true);

	}

	public int doModal() {
		initPanel();
		pack();
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
	public Component createComponent() throws IOException {
		String Id = "RTC:" + moduleVendorField.getText() + ":"
				+ moduleCategoryField.getText() + ":"
				+ moduleNameField.getText() + ":"
				+ moduleVersionField.getText();
		String pathUri = nameServerField.getText();
		if (!pathUri.endsWith("/")) {
			pathUri = pathUri + "/";
		}
		pathUri = pathUri + moduleNameField.getText() + "0.rtc";
		PAIOComponent component = new PAIOComponent(
				this.moduleNameField.getText() + "0", pathUri, Id);

		component.executionContextSet.add(new ExecutionContext("0",
				"rtsExt:execution_context_ext", "PERIODIC",
				this.executionRateField.getText()));
		
		for (DataPort port : dataPortList) {
			component.dataPortSet.add(port);
		}

		return component;
	}

	private void onAddDataInPort() {
		PAIODataPortCreationDialog dialog = new PAIODataPortCreationDialog(
				"DataInPort");
		if (dialog.doModal() == JOptionPane.OK_OPTION) {
			DataPort dataPort = dialog.createDataPort();
			this.dataPortList.add(dataPort);
			setVisible(false);
			dataInPortComboBox.addItem(dataPort.get(DataPort.RTS_NAME));
			pack();
			setVisible(true);
		}
	}

	private void onDelDataInPort() {

	}

	private void onEditDataInPort() {

	}

	private void onAddDataOutPort() {
		PAIODataPortCreationDialog dialog = new PAIODataPortCreationDialog(
				"DataOutPort");
		if (dialog.doModal() == JOptionPane.OK_OPTION) {
			DataPort dataPort = dialog.createDataPort();
			this.dataPortList.add(dataPort);
			setVisible(false);
			dataOutPortComboBox.addItem(dataPort.get(DataPort.RTS_NAME));
			pack();
			setVisible(true);
		}
	}

	private void onDelDataOutPort() {

	}

	private void onEditDataOutPort() {

	}

	private void onAddServicePort() {

	}

	private void onDelServicePort() {

	}

	private void onEditServicePort() {

	}

}
