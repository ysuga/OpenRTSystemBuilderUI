/**
 * PAIODataPortCreationDialog.java
 *
 * @author Yuki Suga (ysuga.net)
 * @date 2011/10/08
 * @copyright 2011, ysuga.net allrights reserved.
 *
 */
package net.ysuga.rtsbuilder.ui.paio;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.ysuga.corbanaming.ui.GridLayoutPanel;
import net.ysuga.rtsystem.profile.Component;
import net.ysuga.rtsystem.profile.DataPort;

/**
 *
 * @author ysuga
 *
 */
public class PAIODataPortCreationDialog extends JDialog {

	private JTextField portDirectionField;
	private JTextField portNameField;
	private JTextField portTypeField;
	
	public PAIODataPortCreationDialog(String direction) {
		super();

		portDirectionField = new JTextField(direction);
		portDirectionField.setEditable(false);
		portNameField = new JTextField(direction);
		portTypeField = new JTextField("");
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

		int line = 0;
		panel.addComponent(0, line, 0, 0, 1, 1, new JLabel("Direction"));
		panel.addComponent(1, line, 10, 0, 4, 1, portDirectionField);
		line++;
		
		panel.addComponent(0, line, 0, 0, 1, 1, new JLabel("Port Name"));
		panel.addComponent(1, line, 10, 0, 4, 1, portNameField);
		line++;

		panel.addComponent(0, line, 0, 0, 1, 1, new JLabel("Port Type"));
		panel.addComponent(1, line, 10, 0, 4, 1, portTypeField);
		line++;

		
		contentPane.add(okButton, BorderLayout.SOUTH);
		okButton.setRequestFocusEnabled(true);

	}
	
	public int doModal() {
		initPanel();
		pack();
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
		port.setDataType(portTypeField.getText());
		
		return port;
	}

}
