package net.ysuga.corbanaming.ui;

import javax.swing.tree.DefaultMutableTreeNode;

import net.ysuga.corbanaming.RTNamingContext;

public class RTSTreeNode extends DefaultMutableTreeNode {

	private RTNamingContext rtNamingContext;
	
	public RTSTreeNode(RTNamingContext rtNamingContext) {
		this.rtNamingContext = rtNamingContext;
	}
	
	public String toString() {
		return rtNamingContext.getName();
	}
}
