package net.ysuga.corbanaming.ui;

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
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import net.ysuga.corbanaming.CorbaNamingParser;
import net.ysuga.corbanaming.RTNamingContext;


public class RTSystemTreeView extends JPanel {
	
	static Logger logger = Logger.getLogger("net.ysuga.rtsbuilder.ui"); 
	DefaultMutableTreeNode rootNode;
	JToolBar toolBar;
	
	private RTSTree treeView;

	protected void onConnect() {
		String hostAddress = JOptionPane.showInputDialog(this, "Name Server?");
		if(hostAddress != null) {
			try {
				RTNamingContext nc = CorbaNamingParser.buildRTNamingContext(hostAddress);
				rootNode.add(RTSTreeNodeBuilder.buildRTSTreeNode(nc));
				treeView.invalidate();
				treeView.validate();
				((DefaultTreeModel)treeView.getModel()).reload();
			} catch (Exception ex) {
				logger.warning("Connection to (" + hostAddress + ") failed.");
			}
		}
	}
	
	
	protected void onRefresh() {
		int count = rootNode.getChildCount();
		ArrayList<String> addressList = new ArrayList<String>();
		
		// Store IP addresses of servers.
		for(int i = 0;i < count;i++) {
			String hostAddress = ((DefaultMutableTreeNode)rootNode.getChildAt(i)).toString();
			addressList.add(hostAddress);
		}
		rootNode.removeAllChildren();
		
		for(int i = 0;i < count;i++) {
			try {
				RTNamingContext nc = CorbaNamingParser.buildRTNamingContext(addressList.get(i));
				rootNode.add(RTSTreeNodeBuilder.buildRTSTreeNode(nc));
			} catch (Exception e1) {
				logger.warning("Refreshing Name Server View failed.");
			}
		}
		
		((DefaultTreeModel)treeView.getModel()).reload();
		
	}
	
	
	private void initToolBar() {
		toolBar = new JToolBar();
		toolBar.add(new JButton(new AbstractAction("Connect") {
			public void actionPerformed(ActionEvent e) {
				onConnect();
			}
		}));
		
		toolBar.add(new JButton(new AbstractAction("Refresh") {
			public void actionPerformed(ActionEvent e) {
				onRefresh();
			}
		}));
	}
	
	public RTSystemTreeView() {
		super();
		
		initToolBar();
		treeView = new RTSTree();
				
		rootNode = new DefaultMutableTreeNode("/");
		
		DefaultTreeModel model = (DefaultTreeModel) treeView.getModel();
		model.setRoot(rootNode);
		
		setLayout(new BorderLayout());
		add(BorderLayout.CENTER, treeView);
		add(BorderLayout.NORTH, toolBar);
		
		
		
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
