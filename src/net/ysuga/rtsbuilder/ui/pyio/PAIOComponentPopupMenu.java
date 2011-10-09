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
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import net.ysuga.rtsbuilder.ui.RTCTemplateWrapper;
import net.ysuga.rtsbuilder.ui.RTSystemBuilderPanel;
import net.ysuga.rtsbuilder.ui.pyio.editor.PyIOEditor;
import net.ysuga.rtsbuilder.ui.shape.ComponentPopupMenu;
import net.ysuga.rtsystem.profile.Component;
import net.ysuga.rtsystem.profile.DataPort;
import net.ysuga.rtsystem.profile.Location;
import net.ysuga.rtsystem.profile.PAIOComponent;

/**
 * 
 * @author ysuga
 * 
 */
public class PAIOComponentPopupMenu extends ComponentPopupMenu {

	final private PAIOComponentShape component;
	final RTSystemBuilderPanel panel;

	/**
	 * Constructor
	 * 
	 * @param panel
	 * @param component
	 */
	public PAIOComponentPopupMenu(RTSystemBuilderPanel panel,
			PAIOComponentShape component) {
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

		JMenuItem editMenuItem = new JMenuItem(new AbstractAction("Edit Code") {
			public void actionPerformed(ActionEvent arg0) {
				onEdit();
			}
		});
		popupMenu.add(editMenuItem);

		JMenuItem executeMenuItem = new JMenuItem(
				new AbstractAction("Execute") {
					public void actionPerformed(ActionEvent arg0) {
						onExecute();
					}
				});
		popupMenu.add(executeMenuItem);
	}

	private void onSetting() {
		PAIOComponent rtsObj = (PAIOComponent) component.getRTSObject();

		String x = component.getRTSObject().location.get(Location.RTS_EXT_X);
		String y = component.getRTSObject().location.get(Location.RTS_EXT_Y);
		Point point = new Point(Integer.parseInt(x), Integer.parseInt(y));

		PAIOComponentCreationDialog dialog = new PAIOComponentCreationDialog(
				rtsObj);
		// Point point = popupMenu.getLocation();
		if (dialog.doModal() == JOptionPane.OK_OPTION) {
			try {
				panel.getRTSystemProfile().componentSet.remove(component
						.getRTSObject());
				Component component = dialog.createComponent();
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
				(PAIOComponent) this.component.getRTSObject());
		editor.setVisible(true);
	}

	private void onExecute() {
		PAIOComponent pyio = (PAIOComponent)component.getRTSObject();
		String language = "python";
		String moduleName = pyio.getModuleName();
		String moduleDesc = "";
		String moduleVendor = pyio.getVendor();
		String moduleVersion= pyio.getVersion();
		String moduleCompType = "DataFlowComponent";
		String moduleActType = "PERIODIC";
		String moduleCategory = pyio.getCategory();
		String moduleMaxInstance = "16";
		
		HashMap<String, String> inportMap = new HashMap<String, String>();
		HashMap<String, String> outportMap = new HashMap<String, String>();
		HashMap<String, String> servicePortMap = new HashMap<String, String>();
		for(DataPort dataPort: (Set<DataPort>)pyio.dataPortSet) {
			if(dataPort.getDirection() == DataPort.DIRECTION_IN) {
				inportMap.put(dataPort.getPlainName(), dataPort.getDataType());
			} else if(dataPort.getDirection() == DataPort.DIRECTION_OUT) {
				outportMap.put(dataPort.getPlainName(), dataPort.getDataType());
			} else {
				//inportMap.put(dataPort.getDataType(), dataPort.getPlainName());
			}
		}
		
		
		File rtcDir = new File("rtc/" + moduleName);
		int i = 2;
		while(rtcDir.exists()) {
			rtcDir = new File("rtc/" + moduleName + "(" + i + ")");	
			i++;
		}
		rtcDir.mkdirs();
		
		
		try {
			RTCTemplateWrapper.generate(rtcDir, language, moduleName, moduleDesc,
					moduleVendor, moduleVersion, moduleCompType, moduleActType, moduleCategory, moduleMaxInstance,
					inportMap, outportMap);
		} catch (Exception e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		
	}
}
