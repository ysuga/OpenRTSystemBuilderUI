/**
 * MainFrameMouseAdapter.java
 *
 * @author Yuki Suga (ysuga.net)
 * @date 2011/09/28
 * @copyright 2011, ysuga.net allrights reserved.
 *
 */
package net.ysuga.rtsbuilder.ui.pyio.editor;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import net.ysuga.corbanaming.ui.RTSTreeNode;

/**
 * 
 * @author ysuga
 * 
 */
public class PyIOEditorMouseAdapter implements MouseMotionListener, MouseListener {

	private PyIOEditor pyIOEditor;
	private DefaultMutableTreeNode selectedNode;
	private Cursor cursor;

	/**
	 * Constructor
	 * @param mainFrame2
	 */
	public PyIOEditorMouseAdapter(PyIOEditor pyIOEditor) {
		File file = new File("paper_cursor.gif");
		try {
			img = ImageIO.read(file);
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		Point hotSpot = new Point(0, 0);
		String name  = "test-cursor";
		Toolkit kit = pyIOEditor.getToolkit();
		cursor = kit.createCustomCursor(img, hotSpot, name);
		
		this.pyIOEditor = pyIOEditor;
	}
	Image img;
	
	final public boolean contains(Component c, MouseEvent e) {
		Point mousePoint = e.getLocationOnScreen();
		Point componentOrigin = c.getLocationOnScreen();
		Point mouseEventPoint = new Point(mousePoint.x - componentOrigin.x, mousePoint.y - componentOrigin.y);
		return c.contains(mouseEventPoint);
	}
	
	
	/**
	 * 
	 * mouseDragged
	 * 
	 * @param e
	 */
	public void mouseDragged(MouseEvent e) {
		if(selectedNode != null) {
			pyIOEditor.setCursor(cursor);
		}
	}

	/**
	 * mouseMoved
	 * 
	 * @param e
	 */
	public void mouseMoved(MouseEvent e) {
		// TODO 自動生成されたメソッド・スタブ

	}

	/**
	 * mouseClicked
	 * 
	 * @param e
	 */
	public void mouseClicked(MouseEvent e) {
		// TODO 自動生成されたメソッド・スタブ
		//System.out.println("Clicked2!");
	
	}

	/**
	 * mousePressed
	 * 
	 * @param e
	 */
	@Override
	public void mousePressed(MouseEvent e) {
		if (e.getSource() == pyIOEditor.getPyIOConfigurationTree()) {
			JTree tree = pyIOEditor.getPyIOConfigurationTree();
			///TreeModel model = tree.getModel();

			TreePath path = tree.getPathForLocation(e.getPoint().x,
					e.getPoint().y);
			tree.setSelectionPath(path);
			if (path != null) {
				Object obj = path.getLastPathComponent();
				if (obj instanceof DefaultMutableTreeNode) {
					selectedNode = (DefaultMutableTreeNode) obj;
					//((RTSTreeNode) obj).onClicked(e);
				}
			}
		}// else if(e.getSource() == pyIOEditor.getRtSystemBuilderPanel()){
			//System.out.println("Builder");
		//}
	}

	/**
	 * mouseReleased
	 * 
	 * @param e
	 */
	public void mouseReleased(MouseEvent e) {
		if (e.getSource() == pyIOEditor.getPyIOConfigurationTree()) {
			if (selectedNode != null) {
				
				if(contains(pyIOEditor.getCurrentEditor(), e)) {
					try {
						Point mousePoint = e.getLocationOnScreen();
						Point origin = pyIOEditor.getCurrentEditor().getLocationOnScreen();
						if(selectedNode instanceof PyIOConfigurationNode) {
							pyIOEditor.addConfigurationMedthodOnEditor((PyIOConfigurationNode)selectedNode, new Point(mousePoint.x - origin.x, mousePoint.y - origin.y));
						}
					} catch (Exception e1) {
						// TODO 自動生成された catch ブロック
						e1.printStackTrace();
					}
					pyIOEditor.repaint();
				}
			}
		}
		selectedNode = null;
		pyIOEditor.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
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
