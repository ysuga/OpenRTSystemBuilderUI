/**
 * RTSTreeMouseAdapter.java
 *
 * @author Yuki Suga (ysuga.net)
 * @date 2011/09/27
 * @copyright 2011, ysuga.net allrights reserved.
 *
 */
package net.ysuga.corbanaming.ui;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.tree.TreePath;

/**
 * 
 * @author ysuga
 * 
 */
public class RTSTreeMouseAdapter implements MouseListener, MouseMotionListener {

	private RTSTree rtSystemTree;
	private RTSTreeNode selectedNode;

	/**
	 * Constructor
	 * 
	 * @param rtSystemTreeView
	 */
	public RTSTreeMouseAdapter(RTSTree rtSystemTree) {
		this.rtSystemTree = rtSystemTree;
	}

	/**
	 * mouseDragged
	 * 
	 * @param e
	 */
	public void mouseDragged(MouseEvent e) {
	}

	/**
	 * mouseMoved
	 * 
	 * @param e
	 */
	public void mouseMoved(MouseEvent e) {

	}

	/**
	 * mouseClicked
	 * 
	 * @param e
	 */
	public void mouseClicked(MouseEvent e) {
		System.out.println("Clicked.");
		this.rtSystemTree.getModel();
		TreePath path = rtSystemTree.getPathForLocation(e.getPoint().x, e.getPoint().y);
		rtSystemTree.setSelectionPath(path);
		if (path != null) {
			Object obj = path.getLastPathComponent();
			if (obj instanceof RTSTreeNode) {
				selectedNode = (RTSTreeNode) obj;
				((RTSTreeNode) obj).onClicked(e);
			}
		}
	}

	/**
	 * mousePressed
	 * 
	 * @param e
	 */
	public void mousePressed(MouseEvent e) {
		System.out.println("Pressed");
		this.rtSystemTree.getModel();
		TreePath path = rtSystemTree.getPathForLocation(e.getPoint().x, e.getPoint().y);
		rtSystemTree.setSelectionPath(path);
		if (path != null) {
			Object obj = path.getLastPathComponent();
			if (obj instanceof RTSTreeNode) {
				selectedNode = (RTSTreeNode) obj;
				((RTSTreeNode) obj).onPressed(e);
			}
		}
	}

	/**
	 * mouseReleased
	 * 
	 * @param e
	 */
	public void mouseReleased(MouseEvent e) {
		// TODO 自動生成されたメソッド・スタブ

	}

	/**
	 * mouseEntered
	 * 
	 * @param e
	 */
	public void mouseEntered(MouseEvent e) {
		// TODO 自動生成されたメソッド・スタブ

	}

	/**
	 * mouseExited
	 * 
	 * @param e
	 */
	public void mouseExited(MouseEvent e) {
		// TODO 自動生成されたメソッド・スタブ

	}

}
