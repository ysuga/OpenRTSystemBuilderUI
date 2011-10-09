/**
 * ComponentPopupMenu.java
 *
 * @author Yuki Suga (ysuga.net)
 * @date 2011/08/27
 * @copyright 2011, ysuga.net allrights reserved.
 *
 */
package net.ysuga.rtsbuilder.ui.shape;

import java.awt.Point;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;

import net.ysuga.rtsbuilder.RTSystemBuilder;
import net.ysuga.rtsbuilder.ui.RTSystemBuilderPanel;
import net.ysuga.rtsystem.profile.Component;

/**
 * <div lang="ja">
 * 
 * </div> <div lang="en">
 * 
 * </div>
 * 
 * @author ysuga
 * 
 */
public class ComponentPopupMenu {

	protected JPopupMenu popupMenu;
	private java.awt.Component panel;
	private Component component;

	/**
	 * <div lang="ja"> �R���X�g���N�^
	 * 
	 * @param rtSystemBuilderPanel
	 *            </div> <div lang="en"> Constructor
	 * @param rtSystemBuilderPanel
	 *            </div>
	 */
	public ComponentPopupMenu(RTSystemBuilderPanel panel,
			ComponentShape component) {
		this.panel = panel;
		this.component = (Component) component.getRTSObject();
		popupMenu = new JPopupMenu();

		JMenuItem refreshMenuItem = new JMenuItem(
				new AbstractAction("Refresh") {
					public void actionPerformed(ActionEvent arg0) {
						onRefresh();
					}
				});
		popupMenu.add(refreshMenuItem);

		popupMenu.add(new JSeparator());

		JMenuItem activateMenuItem = new JMenuItem(new AbstractAction(
				"Activate") {
			public void actionPerformed(ActionEvent arg0) {
				onActivate();
			}
		});
		popupMenu.add(activateMenuItem);

		JMenuItem deactivateMenuItem = new JMenuItem(new AbstractAction(
				"Dectivate") {
			public void actionPerformed(ActionEvent arg0) {
				onDeactivate();
			}
		});
		popupMenu.add(deactivateMenuItem);

		JMenuItem resetMenuItem = new JMenuItem(new AbstractAction("Reset") {
			public void actionPerformed(ActionEvent arg0) {
				onReset();
			}
		});
		popupMenu.add(resetMenuItem);
		popupMenu.add(new JSeparator());

		popupMenu.add(new JSeparator());
		JMenuItem settingMenuItem = new JMenuItem(
				new AbstractAction("Configure") {
					public void actionPerformed(ActionEvent arg0) {

					}
				});
		popupMenu.add(settingMenuItem);
	}

	/**
	 * <div lang="ja">
	 * 
	 * @param point
	 *            </div> <div lang="en">
	 * 
	 * @param point
	 *            </div>
	 */
	public void show(Point point) {

		popupMenu.show(panel, point.x, point.y);
	}

	private void onActivate() {
		try {
			RTSystemBuilder.activateComponent((Component) component);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(panel, "Failed to find RTComponent");
			e.printStackTrace();
		}
	}

	private void onDeactivate() {
		try {
			RTSystemBuilder.deactivateComponent((Component) component);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(panel, "Failed to find RTComponent");
			e.printStackTrace();
		}
	}

	private void onReset() {
		try {
			RTSystemBuilder.resetComponent((Component) component);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(panel, "Failed to find RTComponent");
			e.printStackTrace();
		}
	}

	private void onRefresh() {
		try {
			RTSystemBuilder.findComponent((Component) component);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(panel, "Failed to find RTComponent");
			e.printStackTrace();
		}
	}
}