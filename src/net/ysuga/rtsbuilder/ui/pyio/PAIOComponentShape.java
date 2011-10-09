/**
 * PAIOComponentShape.java
 *
 * @author Yuki Suga (ysuga.net)
 * @date 2011/10/09
 * @copyright 2011, ysuga.net allrights reserved.
 *
 */
package net.ysuga.rtsbuilder.ui.pyio;

import java.awt.event.MouseEvent;

import net.ysuga.rtsbuilder.ui.RTSystemBuilderPanel;
import net.ysuga.rtsbuilder.ui.shape.ComponentShape;
import net.ysuga.rtsystem.profile.Component;

/**
 *
 * @author ysuga
 *
 */
public class PAIOComponentShape extends ComponentShape {

	/**
	 * Constructor
	 * @param component
	 */
	public PAIOComponentShape(Component component) {
		super(component);
		
	}

	/**
	 * onClicked
	 * @param panel
	 * @param arg0
	 */
	@Override
	public void onClicked(RTSystemBuilderPanel panel, MouseEvent arg0) {
		if (arg0.getButton() == MouseEvent.BUTTON3) { // RightClick
			PAIOComponentPopupMenu popup = new PAIOComponentPopupMenu(panel, this);
			popup.show(arg0.getPoint());
		}

		if (arg0.getButton() == MouseEvent.BUTTON1 && arg0.getClickCount() == 2) { // onDoubleClicked

		}
	}
	
	

}
