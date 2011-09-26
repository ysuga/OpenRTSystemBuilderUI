/**
 * ConnectionDialog.java
 *
 * @author Yuki Suga (ysuga.net)
 * @date 2011/09/02
 * @copyright 2011, ysuga.net allrights reserved.
 *
 */
package net.ysuga.rtsbuilder.ui;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import net.ysuga.rtsystem.profile.Component.DataPort;

/**
 * <div lang="ja">
 *
 * </div>
 * <div lang="en">
 *
 * </div>
 * @author ysuga
 *
 */
public class ConnectionDialog extends JDialog {

	private DataPort sourceDataPort;
	private DataPort targetDataPort;
	/**
	 * <div lang="ja">
	 * コンストラクタ
	 * @param selectedDataPort
	 * @param dataPort
	 * </div>
	 * <div lang="en">
	 * Constructor
	 * @param selectedDataPort
	 * @param dataPort
	 * </div>
	 */
	public ConnectionDialog(DataPort selectedDataPort, DataPort targetDataPort) {
		super();
		this.sourceDataPort = selectedDataPort;
		this.targetDataPort = targetDataPort;
	}

	
	private int exitOption = JOptionPane.CANCEL_OPTION;
	public void onOk() {
		exitOption = JOptionPane.OK_OPTION;
		setVisible(false);
	}
	
	public void onCancel() {
		
		setVisible(false);
	}
	
	
	public int doModal() {
		pack();
		setModal(true);
		setVisible(true);
		return exitOption;
		
	}
}
