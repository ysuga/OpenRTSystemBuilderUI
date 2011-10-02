package net.ysuga.corbanaming.ui;

import java.awt.event.MouseEvent;

import javax.swing.JOptionPane;
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
	
	public String getFullPath() {
		if(this.isRoot()) {
			return toString();
		}
		
		StringBuilder nameBuffer = new StringBuilder();
		DefaultMutableTreeNode nodeBuffer = this;
		while(true) {
			nameBuffer.insert(0, nodeBuffer.toString());
			nodeBuffer = (DefaultMutableTreeNode)nodeBuffer.getParent();
			if(nodeBuffer.isRoot()) { 
				break;
			}
			nameBuffer.insert(0,  "/");
		}
		return nameBuffer.toString();
	}

	/**
	 * onClicked
	 *
	 * @param e
	 */
	public void onClicked(MouseEvent e) {
		if(this.isLeaf()) {
			//JOptionPane.showMessageDialog(null, getFullPath());
		}
	}

	/**
	 * onPressed
	 *
	 * @param e
	 */
	public void onPressed(MouseEvent e) {
		// TODO 自動生成されたメソッド・スタブ
		
	}
}
