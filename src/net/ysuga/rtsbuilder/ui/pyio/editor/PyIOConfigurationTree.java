/**
 * PyIOConfigurationTree.java
 *
 * @author Yuki Suga (ysuga.net)
 * @date 2011/10/09
 * @copyright 2011, ysuga.net allrights reserved.
 *
 */
package net.ysuga.rtsbuilder.ui.pyio.editor;

import java.util.Set;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import net.ysuga.rtsystem.profile.DataPort;
import net.ysuga.rtsystem.profile.PAIOComponent;

/**
 * 
 * @author ysuga
 * 
 */
public class PyIOConfigurationTree extends JTree {

	private DefaultMutableTreeNode rootNode;
	private DefaultMutableTreeNode dataInPortNode;
	private DefaultMutableTreeNode dataOutPortNode;
	private DefaultMutableTreeNode servicePortNode;

	/**
	 * Constructor
	 * 
	 * @param component
	 */
	public PyIOConfigurationTree(PAIOComponent component) {
		super();
		rootNode = new DefaultMutableTreeNode("DataFlowComponent");

		DefaultTreeModel model = (DefaultTreeModel) getModel();
		model.setRoot(rootNode);
		dataInPortNode = new DefaultMutableTreeNode("DataInPort");
		dataOutPortNode = new DefaultMutableTreeNode("DataOutPort");
		servicePortNode = new DefaultMutableTreeNode("ServicePort");
		rootNode.add(dataInPortNode);
		rootNode.add(dataOutPortNode);
		rootNode.add(servicePortNode);
		for (DataPort dataPort : (Set<DataPort>) component.dataPortSet) {
			if (dataPort.getDirection() == DataPort.DIRECTION_IN) {
				dataInPortNode.add(new DefaultMutableTreeNode(dataPort
						.getPlainName()
						+ ":"
						+ dataPort.getDataType()
						+ ":"
						+ "DataInPort"));

			} else if (dataPort.getDirection() == DataPort.DIRECTION_OUT) {
				dataOutPortNode.add(new DefaultMutableTreeNode(dataPort
						.getPlainName()
						+ ":"
						+ dataPort.getDataType()
						+ ":"
						+ "DataOutPort"));

			} else{
				servicePortNode.add(new DefaultMutableTreeNode(dataPort
						.getPlainName()
						+ ":"
						+ dataPort.getDataType()
						+ ":"
						+ "ServicePort"));

			}
		}
	}

}
