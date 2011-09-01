/**
 * ConnectorPopupMenu.java
 *
 * @author Yuki Suga (ysuga.net)
 * @date 2011/08/27
 * @copyright 2011, ysuga.net allrights reserved.
 *
 */
package net.ysuga.rtsbuilder.ui;

import java.awt.Point;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import net.ysuga.rtsbuilder.ui.shape.ConnectorShape;
import net.ysuga.rtsystem.profile.Connector;

/**
 * <div lang="ja">
 *
 * </div>
 * <div lang="en">
 *
 * </div>
 * @author ysuga
 *
 */
public class ConnectorPopupMenu {

	private RTSystemBuilderPanel panel;
	private JPopupMenu popupMenu;
	/**
	 * <div lang="ja">
	 * コンストラクタ
	 * @param rtSystemBuilderPanel
	 * </div>
	 * <div lang="en">
	 * Constructor
	 * @param rtSystemBuilderPanel
	 * </div>
	 */
	public ConnectorPopupMenu(RTSystemBuilderPanel rtSystemBuilderPanel) {
		this.panel = rtSystemBuilderPanel;
	}

	/**
	 * show
	 * <div lang="ja">
	 * 
	 * @param panel
	 * @param point
	 * </div>
	 * <div lang="en">
	 *
	 * @param panel
	 * @param point
	 * </div>
	 */
	public void show(RTSystemBuilderPanel panel, Point point) {
		popupMenu = new JPopupMenu();
		JMenuItem refreshMenuItem = new JMenuItem(
				new AbstractAction("Refresh") {
					public void actionPerformed(ActionEvent arg0) {
						onRefresh();
					}
				});
		popupMenu.add(refreshMenuItem);
		JMenuItem autoPivotMenuItem = new JMenuItem(
				new AbstractAction("Auto Pivot") {
					public void actionPerformed(ActionEvent arg0) {
						onAutoPivot();
					}
				});
		popupMenu.add(autoPivotMenuItem);
		
		popupMenu.show(panel, point.x, point.y);
	}

	private void onRefresh() {
		
	}
	
	private void onAutoPivot() {
		//Connector connector = panel.getSelectedConnector();
		ConnectorShape cs = panel.getRTSystemShape().getSelectedConnectorShape();
		if(cs != null) {
			cs.autoPivot();
			panel.repaint();
		}
	}
}
