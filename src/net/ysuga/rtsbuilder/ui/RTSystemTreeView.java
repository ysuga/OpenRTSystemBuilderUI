package net.ysuga.rtsbuilder.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

public class RTSystemTreeView extends JPanel {
	
	static Logger logger = Logger.getLogger("org.ysuga_net.rt.system.gui"); 
	DefaultMutableTreeNode rootNode;
	JToolBar toolBar;
	
	private TreeView treeView;
	class TreeView extends JTree {
		public TreeView() {
			super();	
		}
	}
	
	
	public RTSystemTreeView() {
		super();
		treeView = new TreeView();
		toolBar = new JToolBar();
		
		
		rootNode = new DefaultMutableTreeNode("RT System");
		
		DefaultTreeModel model = (DefaultTreeModel) treeView.getModel();
		model.setRoot(rootNode);
		
		setLayout(new BorderLayout());
		add(BorderLayout.CENTER, treeView);
		add(BorderLayout.NORTH, toolBar);
		
		toolBar.add(new JButton(new AbstractAction("Connect") {
			public void actionPerformed(ActionEvent e) {
				String str = JOptionPane.showInputDialog(this, "Name Server?");
				if(str != null) {
					try {
						StringTokenizer tokenizer2 = new StringTokenizer(str, ":");
						if (tokenizer2.countTokens() == 1) {
							// No TCP Port is defained.
							str = str + ":2809";
						}
						rootNode.add(RTSTreeNode.create(str));
						treeView.invalidate();
						treeView.validate();
						((DefaultTreeModel)treeView.getModel()).reload();
					} catch (Exception ex) {
						logger.warning("Connection to (" + str + ") failed.");
					}
				}
			}
		}));
		
		toolBar.add(new JButton(new AbstractAction("Refresh") {
			public void actionPerformed(ActionEvent e) {
				int count = rootNode.getChildCount();
				ArrayList<String> addressList = new ArrayList<String>();
				
				// Store IP addresses of servers.
				for(int i = 0;i < count;i++) {
					String address = ((DefaultMutableTreeNode)rootNode.getChildAt(i)).toString();
					addressList.add(address);
				}
				rootNode.removeAllChildren();
				
				
				for(int i = 0;i < count;i++) {
					try {
						rootNode.add(RTSTreeNode.create(addressList.get(i)));
					} catch (Exception e1) {
						logger.warning("Refreshing Name Server View failed.");
					}
				}
				
				((DefaultTreeModel)treeView.getModel()).reload();
				
			}
		}));
		
		/*
		String hostAddress = "localhost:2809";

		try {
			rootNode.add(RTSTreeNode.create(hostAddress));
		} catch (Exception e) {
			logger.warning(e.getLocalizedMessage());
			
		}
		*/
		this.setPreferredSize(new Dimension(1400, 1200));
	}

}
