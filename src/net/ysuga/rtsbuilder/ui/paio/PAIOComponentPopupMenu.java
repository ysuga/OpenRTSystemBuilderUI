/**
 * PAIOComponentPopupMenu.java
 *
 * @author Yuki Suga (ysuga.net)
 * @date 2011/10/09
 * @copyright 2011, ysuga.net allrights reserved.
 *
 */
package net.ysuga.rtsbuilder.ui.paio;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import net.ysuga.rtsbuilder.ui.RTSystemBuilderPanel;
import net.ysuga.rtsbuilder.ui.shape.ComponentPopupMenu;
import net.ysuga.rtsbuilder.ui.shape.ComponentShape;
import net.ysuga.rtsystem.profile.Component;
import net.ysuga.rtsystem.profile.Location;
import net.ysuga.rtsystem.profile.RTSObject;

/**
 *
 * @author ysuga
 *
 */
public class PAIOComponentPopupMenu extends ComponentPopupMenu {

	final private ComponentShape component;
	final RTSystemBuilderPanel panel;
	/**
	 * Constructor
	 * @param panel
	 * @param component
	 */
	public PAIOComponentPopupMenu(RTSystemBuilderPanel panel,
			ComponentShape component) {
		super(panel, component);
		this.component = component;
		this.panel = panel;
		
		JMenuItem settingMenuItem = new JMenuItem(
				new AbstractAction("Setting") {
					public void actionPerformed(ActionEvent arg0) {
						onSetting();
					}
				});
		popupMenu.add(settingMenuItem);
	}

	private void onSetting() {
		RTSObject rtsObj = component.getRTSObject();
		String x = component.getRTSObject().location.get(Location.RTS_EXT_X);
		String y = component.getRTSObject().location.get(Location.RTS_EXT_Y);
		Point point = new Point(Integer.parseInt(x), Integer.parseInt(y));
		
		panel.getRTSystemProfile().componentSet.remove(component.getRTSObject());
		PAIOComponentCreationDialog dialog = new PAIOComponentCreationDialog();
		dialog.setRTSObject(rtsObj);
//		Point point = popupMenu.getLocation();
		if(dialog.doModal() == JOptionPane.OK_OPTION) {
			try {
				Component component = dialog.createComponent();
				component.setLocation(point);
				panel.getRTSystemProfile().addComponent(component);
				panel.refresh();
			} catch (IOException e) {
				e.printStackTrace();
			} 
		}
	}
}
