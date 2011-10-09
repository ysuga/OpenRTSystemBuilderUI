/**
 * ComponentShape.java
 *
 * @author Yuki Suga (ysuga.net)
 * @date 2011/08/27
 * @copyright 2011, ysuga.net allrights reserved.
 *
 */
package net.ysuga.rtsbuilder.ui.shape;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.HashSet;
import java.util.Set;

import net.ysuga.rtsbuilder.ui.RTSystemBuilderPanel;
import net.ysuga.rtsystem.profile.Component;
import net.ysuga.rtsystem.profile.DataPort;
import net.ysuga.rtsystem.profile.Location;
import net.ysuga.rtsystem.profile.RTSObject;
import net.ysuga.rtsystem.profile.RTSProperties;

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
public class ComponentShape implements RTSObjectShape {

	private NamedBox box;
	private Component component;

	int boxwidth = 80;
	int boxheight = 40;

	public Set<PortShape> portShapeSet;

	public int getNumRightSidePort() {
		int sum = 0;
		for (PortShape shape : portShapeSet) {
			if (shape.side == PortShape.SIDE_RIGHT)
				sum++;
		}
		return sum;
	}

	public int getNumLeftSidePort() {
		int sum = 0;
		for (PortShape shape : portShapeSet) {
			if (shape.side == PortShape.SIDE_LEFT)
				sum++;
		}
		return sum;
	}

	/**
	 * <div lang="ja"> �R���X�g���N�^ </div> <div lang="en"> Constructor </div>
	 */
	public ComponentShape(Component component) {
		int x = Integer.parseInt(component.location.get(Location.RTS_EXT_X));
		int y = Integer.parseInt(component.location.get(Location.RTS_EXT_Y));
		Point p = new Point(x, y);
		box = new NamedBox(component.get(Component.INSTANCE_NAME), p.x, p.y,
				boxwidth, boxheight);
		this.component = component;

		portShapeSet = new HashSet<PortShape>();
		for (DataPort dataPort : (Set<DataPort>) component.dataPortSet) {
			portShapeSet.add(new PortShape(dataPort, this));
		}
		int rightSidePort = getNumRightSidePort();
		int leftSidePort = getNumLeftSidePort();
		int maxPortInOneSide = rightSidePort > leftSidePort ? rightSidePort
				: leftSidePort;
		int suitableHeight = maxPortInOneSide * 15 + 5;
		if (boxheight < suitableHeight) {
			boxheight = suitableHeight;
			box = new NamedBox(component.get(Component.INSTANCE_NAME), p.x,
					p.y, boxwidth, boxheight);
		}

	}

	/**
	 * getComponent <div lang="ja">
	 * 
	 * @return </div> <div lang="en">
	 * 
	 * @return </div>
	 */
	public RTSObject getRTSObject() {
		return component;
	}

	/**
	 * onClicked <div lang="ja">
	 * 
	 * @param panel
	 * @param arg0
	 *            </div> <div lang="en">
	 * 
	 * @param panel
	 * @param arg0
	 *            </div>
	 */
	public void onClicked(RTSystemBuilderPanel panel, MouseEvent arg0) {
		if (arg0.getButton() == MouseEvent.BUTTON3) { // RightClick
			ComponentPopupMenu popup = new ComponentPopupMenu(panel, this);
			popup.show(arg0.getPoint());
		}

		if (arg0.getButton() == MouseEvent.BUTTON1 && arg0.getClickCount() == 2) { // onDoubleClicked

		}
	}

	/**
	 * <div lang="ja">
	 * 
	 * @param g
	 *            </div> <div lang="en">
	 * @param g
	 *            </div>
	 */
	public void draw(Graphics2D g) {
		Color textColor;
		Color bgColor;
		Color boxColor;
		switch (getRTSObject().getState()) {
		case RTSProperties.OFFLINE:
			textColor = Color.black;
			bgColor = Color.white;
			boxColor = Color.black;
			break;
		case RTSProperties.ONLINE_ACTIVE:
			textColor = Color.black;
			bgColor = Color.green;
			boxColor = Color.black;
			break;
		case RTSProperties.ONLINE_INACTIVE:
			textColor = Color.white;
			bgColor = Color.blue;
			boxColor = Color.black;
			break;
		case RTSProperties.ONLINE_CREATED:
			textColor = Color.white;
			bgColor = Color.black;
			boxColor = Color.black;
			break;
		case RTSProperties.ONLINE_ERROR:
			textColor = Color.white;
			bgColor = Color.red;
			boxColor = Color.black;
			break;
		case RTSProperties.ONLINE_UNKNOWN:
		default:
			textColor = Color.red;
			bgColor = Color.white;
			boxColor = Color.black;
			break;
		}
		box.setTextColor(textColor);
		box.setBgColor(bgColor);
		box.setBoxColor(boxColor);
		box.draw(g);
		for (PortShape port : portShapeSet) {
			port.draw(g);
		}
	}

	/**
	 * <div lang="ja">
	 * 
	 * @param p
	 * @return </div> <div lang="en">
	 * @param p
	 * @return </div>
	 */
	public boolean contains(Point p) {
		boolean flag = box.contains(p);
		return flag;
	}

	/**
	 * <div lang="ja">
	 * 
	 * @return </div> <div lang="en">
	 * 
	 * @return </div>
	 */
	public Point getCenterPoint() {
		return new Point((int) box.getCenterX(), (int) box.getCenterY());
	}

	/**
	 * <div lang="ja">
	 * 
	 * @return </div> <div lang="en">
	 * @return </div>
	 */
	@Override
	public Rectangle getBounds() {
		return box.getBounds();
	}

	/**
	 * <div lang="ja">
	 * 
	 * @param flag
	 *            </div> <div lang="en">
	 * @param flag
	 *            </div>
	 */
	@Override
	public void setSelected(boolean flag) {
		box.setSelected(flag);
	}

	/**
	 * <div lang="ja">
	 * 
	 * @return </div> <div lang="en">
	 * @return </div>
	 */
	@Override
	public boolean isSelected() {
		return box.isSelected();
	}

	/**
	 * <div lang="ja">
	 * 
	 * @return </div> <div lang="en">
	 * @return </div>
	 */
	public double getX() {
		return box.x;
	}

	/**
	 * <div lang="ja">
	 * 
	 * @return </div> <div lang="en">
	 * @return </div>
	 */
	public double getY() {
		return box.y;
	}

}
