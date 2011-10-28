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
import net.ysuga.rtsbuilder.ui.shape.RTComponentShape;
import net.ysuga.rtsystem.profile.RTComponent;

/**
 *
 * @author ysuga
 *
 */
public class PyIOComponentShape extends RTComponentShape {

	/**
	 * Constructor
	 * @param component
	 */
	public PyIOComponentShape(RTComponent component) {
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
			PyIOComponentPopupMenu popup = new PyIOComponentPopupMenu(panel, this);
			popup.show(arg0.getPoint());
		}

		if (arg0.getButton() == MouseEvent.BUTTON1 && arg0.getClickCount() == 2) { // onDoubleClicked

		}
	}
	
	

}
