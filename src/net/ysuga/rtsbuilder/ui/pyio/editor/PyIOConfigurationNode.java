/**
 * PyIOConfigurationNode.java
 *
 * @author Yuki Suga (ysuga.net)
 * @date 2011/10/28
 * @copyright 2011, ysuga.net allrights reserved.
 *
 */
package net.ysuga.rtsbuilder.ui.pyio.editor;

import javax.swing.tree.DefaultMutableTreeNode;

import net.ysuga.rtsystem.profile.DataPort;

/**
 * 
 * @author ysuga
 * 
 */
public class PyIOConfigurationNode extends DefaultMutableTreeNode {

	private DataPort dataPort;
	
	public DataPort getDataPort() {
		return dataPort;
	}
	/**
	 * Constructor
	 */
	public PyIOConfigurationNode(DataPort dataPort) {
		super(dataPort.getPlainName() + ":" + dataPort.getDataType() + ":"
				+ "DataInPort");
		this.dataPort = dataPort;
	}

	/**
	 * Constructor
	 * 
	 * @param userObject
	 */
	public PyIOConfigurationNode(Object userObject) {
		super(userObject);
		// TODO 自動生成されたコンストラクター・スタブ
	}

	/**
	 * Constructor
	 * 
	 * @param userObject
	 * @param allowsChildren
	 */
	public PyIOConfigurationNode(Object userObject, boolean allowsChildren) {
		super(userObject, allowsChildren);
		// TODO 自動生成されたコンストラクター・スタブ
	}

}
