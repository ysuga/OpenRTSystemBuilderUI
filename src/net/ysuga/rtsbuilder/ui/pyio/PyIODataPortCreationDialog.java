/**
 * PAIODataPortCreationDialog.java
 *
 * @author Yuki Suga (ysuga.net)
 * @date 2011/10/08
 * @copyright 2011, ysuga.net allrights reserved.
 *
 */
package net.ysuga.rtsbuilder.ui.pyio;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.ysuga.corbanaming.ui.GridLayoutPanel;
import net.ysuga.rtsbuilder.ui.MainFrame;
import net.ysuga.rtsystem.profile.DataPort;

/**
 *
 * @author ysuga
 *
 */
public class PyIODataPortCreationDialog extends JDialog {

	private JTextField portDirectionField;
	private JTextField portNameField;
	private JTextField portTypeField;
	
	public PyIODataPortCreationDialog(String direction) {
		super();
		super.setTitle("\"PyIO\" DataPort Setting");
		portDirectionField = new JTextField(direction);
		portDirectionField.setEditable(false);
		portNameField = new JTextField(direction);
		portTypeField = new JTextField("");
	}

	/**
	 * Constructor
	 * @param dataPort
	 */
	public PyIODataPortCreationDialog(String direction, DataPort dataPort) {
		this(direction);
		this.portNameField.setText(dataPort.getPlainName());
		this.portTypeField.setText(dataPort.getDataType());
	}

	private int exitOption = JOptionPane.CANCEL_OPTION;
	private JButton okButton;
	private JPanel contentPane;

	public void onOk(ActionEvent arg0) {
		exitOption = JOptionPane.OK_OPTION;
		setVisible(false);
	}

	public void onCancel() {

		setVisible(false);
	}
	
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
		
		JLabel mainLabel = new JLabel("Input DataPort Parameter Below");
		Dimension s = mainLabel.getSize();
		mainLabel.setSize(600, s.height);
		panel.addComponent(0, 0, 10, 0, 2, 1, mainLabel);

		int line = 1;
		panel.addComponent(0, line, 0, 0, 1, 1, new JLabel("Direction"));
		panel.addComponent(1, line, 10, 0, 4, 1, portDirectionField);
		line++;
		
		panel.addComponent(0, line, 0, 0, 1, 1, new JLabel("Port Name"));
		panel.addComponent(1, line, 10, 0, 4, 1, portNameField);
		line++;

		panel.addComponent(0, line, 0, 0, 1, 1, new JLabel("Port Type"));
		panel.addComponent(1, line, 10, 0, 4, 1, portTypeField);
		line++;

		
		panel.addComponent(0, line, 10, 0, 2, 1, new JLabel(""));
		panel.addComponent(2, line, 0, 0, 1, 1, new JButton(new AbstractAction("Cancel"){
			public void actionPerformed(ActionEvent e) {
				onCancel();
			}
		}));
		panel.addComponent(3, line, 1, 1, okButton);
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
		portNameField.requestFocusInWindow();
		portNameField.selectAll();
		setVisible(true);
		return exitOption;
	}

	/**
	 * createDataPort
	 *
	 * @return
	 */
	public DataPort createDataPort() {
		DataPort port = new DataPort(portNameField.getText());
		if(portDirectionField.getText().equals("DataInPort")) {
			port.setDirection(port.DIRECTION_IN);
		} else {
			port.setDirection(port.DIRECTION_OUT);
		}
		
		String dataType = portTypeField.getText();
		/**
		if(!dataType.startsWith("IDL")) {
			dataType = "IDL:RTC/" + dataType + ":1.0";
		}
		*/
		port.setDataType(dataType);
		
		return port;
	}

}
