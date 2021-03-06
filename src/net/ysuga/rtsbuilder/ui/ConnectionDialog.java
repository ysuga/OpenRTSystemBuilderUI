/**
 * ConnectionDialog.java
 *
 * @author Yuki Suga (ysuga.net)
 * @date 2011/09/02
 * @copyright 2011, ysuga.net allrights reserved.
 *
 */
package net.ysuga.rtsbuilder.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.ysuga.corbanaming.ui.GridLayoutPanel;
import net.ysuga.rtsbuilder.RTSystemBuilder;
import net.ysuga.rtsystem.profile.RTComponent;
import net.ysuga.rtsystem.profile.DataPort;
import net.ysuga.rtsystem.profile.DataPortConnector;
import net.ysuga.rtsystem.profile.Properties;
import net.ysuga.rtsystem.profile.ServicePortConnector;

import org.omg.CosNaming.NamingContextPackage.NotFound;

/**
 * <div lang="ja">
 * 
 * </div> <div lang="en">
 * 
 * </div>
 * 
 * @author ysuga
 * 
 */
public class ConnectionDialog extends JDialog {

	private DataPort sourceDataPort;
	private DataPort targetDataPort;
	private RTComponent sourceComponent;
	private RTComponent targetComponent;

	private boolean validConnection;

	private JTextField connectionIdField;
	private JTextField connectionNameField;

	private String dataType;
	private JTextField dataTypeField;
	private String interfaceName;

	private JComboBox interfaceTypeComboBox;
	private JComboBox dataflowTypeComboBox;
	private JComboBox subscriptionTypeComboBox;

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
	public ConnectionDialog(RTComponent sourceComponent, DataPort sourceDataPort,
			RTComponent targetComponent, DataPort targetDataPort)
			throws Exception {
		super();
		this.sourceDataPort = sourceDataPort;
		this.targetDataPort = targetDataPort;
		this.sourceComponent = sourceComponent;
		this.targetComponent = targetComponent;

		String connectionName = sourceDataPort.get(DataPort.RTS_NAME) + "_"
				+ targetDataPort.get(DataPort.RTS_NAME);
		connectionIdField = new JTextField(connectionName);
		connectionNameField = new JTextField(connectionName);

		validConnection = RTSystemBuilder.isConnectable(sourceComponent,
				sourceDataPort, targetComponent, targetDataPort);
		if (validConnection) {
			// TODO:: ここでPyIOか否かを判定して，PyIOならばデフォルト値の設定はムリーーー
			
			dataType = RTSystemBuilder.getDataType(sourceComponent,
					sourceDataPort);
			interfaceName = RTSystemBuilder.getConnectionServiceInterfaceName(
					sourceComponent, sourceDataPort, targetComponent,
					targetDataPort);

		}
	}

	private int exitOption = JOptionPane.CANCEL_OPTION;
	private JButton okButton;
	private JPanel contentPane;
	private JComboBox pushIntervalComboBox;
	private JTextField pushIntervalTextField;

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
		
		JLabel mainLabel = new JLabel("Input Connection Parameter Below");
		Dimension s = mainLabel.getSize();
		mainLabel.setSize(400, s.height);
		panel.addComponent(0, 0, 10, 0, 2, 1, mainLabel);
		panel.addComponent(0, 1, 0, 0, 1, 1, new JLabel("Connection Id"));
		panel.addComponent(1, 1, 10, 0, 3, 1, connectionIdField);
		panel.addComponent(0, 2, 0, 0, 1, 1, new JLabel("Connection Name"));
		panel.addComponent(1, 2, 10, 0, 3, 1, connectionNameField);

		if (dataType != null) {
			dataTypeField = new JTextField(dataType);
			dataTypeField.setEditable(false);

			interfaceTypeComboBox = new JComboBox();
			interfaceTypeComboBox.addItem("corba_cdr");

			dataflowTypeComboBox = new JComboBox();
			dataflowTypeComboBox.addItem("push");
			dataflowTypeComboBox.addItem("pull");

			subscriptionTypeComboBox = new JComboBox();
			subscriptionTypeComboBox.addItem("flush");
			subscriptionTypeComboBox.addItem("new");
			subscriptionTypeComboBox.addItem("periodic");
			subscriptionTypeComboBox.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					if (subscriptionTypeComboBox.getSelectedItem().equals(
							"periodic")) {
						pushIntervalTextField.setEditable(true);
					} else {
						pushIntervalTextField.setEditable(false);
					}
				}

			});

			pushIntervalTextField = new JTextField("1000.0");
			pushIntervalTextField.setEditable(false);

			panel.addComponent(0, 3, 0, 0, 1, 1, new JLabel("Data Type"));
			panel.addComponent(1, 3, 10, 0, 3, 1, dataTypeField);
			panel.addComponent(0, 4, 0, 0, 1, 1, new JLabel("Interface Type"));
			panel.addComponent(1, 4, 10, 0, 3, 1, interfaceTypeComboBox);
			panel.addComponent(0, 5, 0, 0, 1, 1, new JLabel("Dataflow Type"));
			panel.addComponent(1, 5, 10, 0, 3, 1, dataflowTypeComboBox);
			panel.addComponent(0, 6, 0, 0, 1, 1,
					new JLabel("Subscription Type"));
			panel.addComponent(1, 6, 10, 0, 3, 1, subscriptionTypeComboBox);
			panel.addComponent(0, 7, 0, 0, 1, 1, new JLabel("Push Rate"));
			panel.addComponent(1, 7, 10, 0, 3, 1, pushIntervalTextField);
		} else {
			panel.addComponent(0, 3, 0, 0, 1, 1, new JLabel("Interface Type"));
			panel.addComponent(1, 3, 10, 0, 3, 1, new JLabel(interfaceName));
		}

		contentPane.add(okButton, BorderLayout.SOUTH);
		okButton.setRequestFocusEnabled(true);

	}

	public int doModal() {
		if (!validConnection) {
			return exitOption;
		}
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

	public DataPortConnector createDataPortConnector() {
		DataPortConnector connector;
		if (subscriptionTypeComboBox.getSelectedItem().equals("periodic")) {
			connector = new DataPortConnector(connectionIdField.getText(),
					connectionNameField.getText(), dataType,
					(String) interfaceTypeComboBox.getSelectedItem(),
					(String) dataflowTypeComboBox.getSelectedItem(),
					(String) subscriptionTypeComboBox.getSelectedItem(),
					pushIntervalTextField.getText());
		} else {
			connector = new DataPortConnector(connectionIdField.getText(),
					connectionNameField.getText(), dataType,
					(String) interfaceTypeComboBox.getSelectedItem(),
					(String) dataflowTypeComboBox.getSelectedItem(),
					(String) subscriptionTypeComboBox.getSelectedItem());
		}
		
		connector.sourcePort = connector.new Port(this.sourceDataPort.get(DataPort.RTS_NAME),
				this.sourceComponent.get(RTComponent.INSTANCE_NAME), this.sourceComponent.get(RTComponent.ID));
		connector.sourcePort.properties = new Properties("COMPONENT_PATH_ID", sourceComponent.get(RTComponent.PATH_URI));
		connector.targetPort = connector.new Port(this.targetDataPort.get(DataPort.RTS_NAME),
				this.targetComponent.get(RTComponent.INSTANCE_NAME), this.targetComponent.get(RTComponent.ID));
		connector.targetPort.properties = new Properties("COMPONENT_PATH_ID", targetComponent.get(RTComponent.PATH_URI));
		
		return connector;
	}
	/**
	 * createServicePortConnector
	 *
	 * @return
	 */
	public ServicePortConnector createServicePortConnector() {
		ServicePortConnector connector;
			connector = new ServicePortConnector(connectionIdField.getText(),
					connectionNameField.getText());
			connector.sourcePort = connector.new Port(this.sourceDataPort.get(DataPort.RTS_NAME),
					this.sourceComponent.get(RTComponent.INSTANCE_NAME), this.sourceComponent.get(RTComponent.ID));
			connector.sourcePort.properties = new Properties("COMPONENT_PATH_ID", sourceComponent.get(RTComponent.PATH_URI));
			connector.targetPort = connector.new Port(this.targetDataPort.get(DataPort.RTS_NAME),
					this.targetComponent.get(RTComponent.INSTANCE_NAME), this.targetComponent.get(RTComponent.ID));
			connector.targetPort.properties = new Properties("COMPONENT_PATH_ID", targetComponent.get(RTComponent.PATH_URI));
		return  connector;
	}
	/**
	 * isDataPortConnection
	 *
	 * @return
	 * @throws Exception 
	 */
	public boolean isDataPortConnection() throws Exception {
		if(!RTSystemBuilder.isConnectable(sourceComponent,
				sourceDataPort, targetComponent, targetDataPort)) {
			return false;
		}
		if(dataType == null) {
			return false;
		}
		return true;
	}

	/**
	 * isServicePortConnection
	 *
	 * @return
	 * @throws Exception 
	 */
	public boolean isServicePortConnection() throws Exception {
		if(!RTSystemBuilder.isConnectable(sourceComponent,
				sourceDataPort, targetComponent, targetDataPort)) {
			return false;
		}
		if(dataType == null) {
			return true;
		}
		return false;
	}


}
