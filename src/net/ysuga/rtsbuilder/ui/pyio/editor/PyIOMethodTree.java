/**
 * PyIOMethodTree.java
 *
 * @author Yuki Suga (ysuga.net)
 * @date 2011/10/09
 * @copyright 2011, ysuga.net allrights reserved.
 *
 */
package net.ysuga.rtsbuilder.ui.pyio.editor;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import net.ysuga.rtsystem.profile.PAIOComponent;

/**
 *
 * @author ysuga
 *
 */
public class PyIOMethodTree extends JTree {

	private DefaultMutableTreeNode rootNode;

	/**
	 * Constructor
	 * @param component
	 */
	public PyIOMethodTree(PAIOComponent component) {
		super();
		rootNode = new DefaultMutableTreeNode("DataFlowComponent");
		
		DefaultTreeModel model = (DefaultTreeModel) getModel();
		model.setRoot(rootNode);
		rootNode.add(new DefaultMutableTreeNode("onExecute"));
		rootNode.add(new DefaultMutableTreeNode("onActivated"));
		rootNode.add(new DefaultMutableTreeNode("onDeactivated"));
	}

}
