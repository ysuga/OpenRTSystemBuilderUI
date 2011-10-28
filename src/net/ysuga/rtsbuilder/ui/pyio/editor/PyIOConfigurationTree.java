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
import net.ysuga.rtsystem.profile.PyIOComponent;

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
	public PyIOConfigurationTree(PyIOComponent component) {
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
				dataInPortNode.add(new PyIOConfigurationNode(dataPort));
			} else if (dataPort.getDirection() == DataPort.DIRECTION_OUT) {
				dataOutPortNode.add(new PyIOConfigurationNode(dataPort));
			} else{
				servicePortNode.add(new PyIOConfigurationNode(dataPort));
			}
		}
		this.expandRow(0);

		this.expandRow(2);

		this.expandRow(1);
	}

}
