package net.ysuga.rtsbuilder.ui.shape;


import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

import net.ysuga.rtsbuilder.ui.RTSystemBuilderPanel;
import net.ysuga.rtsystem.profile.RTSObject;


public interface RTSObjectShape {
	
	public void draw(Graphics2D g);

	public boolean contains(Point p);
	
	public Point getCenterPoint();

	public Rectangle getBounds();
		
	public void setSelected(boolean flag);
	
	public boolean isSelected();
	
	public double getX();
	
	public double getY();
	
	public RTSObject getRTSObject();

	/**
	 * onClicked
	 *
	 * @param panel
	 * @param arg0
	 */
	public void onClicked(RTSystemBuilderPanel panel, MouseEvent arg0);

}
