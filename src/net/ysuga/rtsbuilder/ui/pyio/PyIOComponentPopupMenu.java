/**
 * PAIOComponentPopupMenu.java
 *
 * @author Yuki Suga (ysuga.net)
 * @date 2011/10/09
 * @copyright 2011, ysuga.net allrights reserved.
 *
 */
package net.ysuga.rtsbuilder.ui.pyio;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;

import net.ysuga.rtsbuilder.ui.RTSystemBuilderPanel;
import net.ysuga.rtsbuilder.ui.pyio.editor.PyIOEditor;
import net.ysuga.rtsbuilder.ui.shape.ComponentPopupMenu;
import net.ysuga.rtsystem.profile.Location;
import net.ysuga.rtsystem.profile.PyIOComponent;
import net.ysuga.rtsystem.profile.PythonRTCLauncher;
import net.ysuga.rtsystem.profile.RTComponent;

/**
 * 
 * @author ysuga
 * 
 */
public class PyIOComponentPopupMenu extends ComponentPopupMenu {

	final private PyIOComponentShape component;
	final RTSystemBuilderPanel panel;

	/**
	 * Constructor
	 * 
	 * @param panel
	 * @param component
	 */
	public PyIOComponentPopupMenu(RTSystemBuilderPanel panel,
			PyIOComponentShape component) {
		super(panel, component);
		this.component = component;
		this.panel = panel;

		JMenuItem settingMenuItem = new JMenuItem(
				new AbstractAction("PyIO Setting") {
					public void actionPerformed(ActionEvent arg0) {
						onSetting();
					}
				});
		popupMenu.insert(settingMenuItem, 0);

		JMenuItem editMenuItem = new JMenuItem(new AbstractAction("PyIO Edit Code") {
			public void actionPerformed(ActionEvent arg0) {
				onEdit();
			}
		});
		popupMenu.insert(editMenuItem, 1);

		JMenuItem executeMenuItem = new JMenuItem(
				new AbstractAction("PyIO Execute") {
					public void actionPerformed(ActionEvent arg0) {
						onExecute();
					}
				});
		popupMenu.insert(executeMenuItem, 2);
		

		popupMenu.insert(new JSeparator(), 3);
	}

	private void onSetting() {
		PyIOComponent rtsObj = (PyIOComponent) component.getRTSObject();

		String x = component.getRTSObject().location.get(Location.RTS_EXT_X);
		String y = component.getRTSObject().location.get(Location.RTS_EXT_Y);
		Point point = new Point(Integer.parseInt(x), Integer.parseInt(y));

		PyIOComponentCreationDialog dialog = new PyIOComponentCreationDialog(
				rtsObj);
		// Point point = popupMenu.getLocation();
		if (dialog.doModal() == JOptionPane.OK_OPTION) {
			try {
				panel.getRTSystemProfile().componentSet.remove(component
						.getRTSObject());
				RTComponent component = dialog.createComponent();
				component.setLocation(point);
				panel.getRTSystemProfile().addComponent(component);
				panel.refresh();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void onEdit() {
		PyIOEditor editor = new PyIOEditor(
				(PyIOComponent) this.component.getRTSObject());
		editor.setVisible(true);
	}

	private void onExecute() {
		PyIOComponent pyio = (PyIOComponent)component.getRTSObject();
		try {
			PythonRTCLauncher launcher = PyIOBuilder.generateAndExecute("temp", pyio);
			pyio.setLauncher(launcher);
			this.panel.addPyIOLauncher(launcher);
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		
	}
}
