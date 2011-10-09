package net.ysuga.rtsbuilder.ui;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Set;
import java.util.logging.Logger;

import javax.swing.ToolTipManager;

import net.ysuga.rtsbuilder.ui.shape.ComponentShape;
import net.ysuga.rtsbuilder.ui.shape.ConnectorShape;
import net.ysuga.rtsbuilder.ui.shape.PortShape;
import net.ysuga.rtsbuilder.ui.shape.RTSObjectShape;
import net.ysuga.rtsystem.profile.Component;
import net.ysuga.rtsystem.profile.DataPort;
import net.ysuga.rtsystem.profile.PortConnector;

/**
 * @author ysuga
 * 
 */
public class RTSystemPanelMouseAdapter implements MouseListener,
		MouseMotionListener {

	private static Logger logger = Logger
			.getLogger(RTSystemPanelMouseAdapter.class.getName());

	private RTSystemBuilderPanel panel;

	private Point selectOffset;

	public void setSelectedOffset(Point point) {
		selectOffset = point;
	}

	public Point getSelectOffset() {
		return selectOffset;
	}

	public RTSystemPanelMouseAdapter(RTSystemBuilderPanel panel) {
		this.panel = panel;
	}

	/**
	 * 
	 * mouseClicked
	 * 
	 * @param arg0
	 */
	public void mouseClicked(MouseEvent arg0) {
		logger.entering(this.getClass().getName(), "mouseClicked", arg0);
		try {
			if (panel.getEditMode() == RTSystemBuilderPanel.EDIT_NORMAL) {
				RTSObjectShape objectShape = panel.getRTSystemShape()
						.getContainingRTSObject(arg0.getPoint());
				if (objectShape != null) {
					objectShape.onClicked(panel, arg0);
					return;
				}

				ConnectorShape connectorShape = panel.getRTSystemShape()
						.getContainingConnector(arg0.getPoint());
				if (connectorShape != null) {
					connectorShape.onClicked(panel, arg0);

					if (arg0.getButton() == MouseEvent.BUTTON3) { // RightClick
						panel.getConnectorPopupMenu().show(panel,
								arg0.getPoint());
					}

					return;
				}
				if (arg0.getButton() == MouseEvent.BUTTON3) { // RightClick
					panel.getPopupMenu().show(panel, arg0.getPoint());
				}
			}
			panel.setSelectedRTSObjectt(null);
			panel.setEditMode(RTSystemBuilderPanel.EDIT_NORMAL);
			panel.repaint();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void mouseEntered(MouseEvent arg0) {
	}

	public void mouseExited(MouseEvent arg0) {

	}

	public void mousePressed(MouseEvent arg0) {
		logger.entering(this.getClass().getName(), "mousePressed", arg0);
		try {
			if (panel.getEditMode() == RTSystemBuilderPanel.EDIT_NORMAL) {
				PortShape portShape = panel.getRTSystemShape()
						.getContainingPortShape(arg0.getPoint());
				if (portShape != null) {
					panel.setSelectedRTSObjectt(portShape.getComponentShape()
							.getRTSObject());
					panel.setSelectedPort(portShape.getDataPort());
					panel.setEditMode(RTSystemBuilderPanel.EDIT_CONNECTION);
					return;
				}
				
				ConnectorShape connectorShape = panel.getRTSystemShape()
						.getContainingConnector(arg0.getPoint());
				if (connectorShape != null) {
					connectorShape.setSelected(true);
					panel.setSelectedConnector(connectorShape.getConnector());
					connectorShape.onMousePressed(arg0);
					return;
				}

				RTSObjectShape objectShape = panel.getRTSystemShape()
						.getContainingRTSObject(arg0.getPoint());
				if (objectShape != null) {
					panel.setSelectedRTSObjectt(objectShape.getRTSObject());
					int dx = (int) (arg0.getPoint().x - objectShape.getX());
					int dy = (int) (arg0.getPoint().y - objectShape.getY());
					setSelectedOffset(new Point(dx, dy));
					panel.repaint();
					return;
				}

				
				if (arg0.getButton() == MouseEvent.BUTTON3) { // RightClick
					panel.getPopupMenu().show(panel, arg0.getPoint());
				}

			}

			panel.setSelectedRTSObjectt(null);
			panel.setEditMode(RTSystemBuilderPanel.EDIT_NORMAL);
			panel.repaint();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void mouseDragged(MouseEvent arg0) {
		if (panel.getEditMode() == RTSystemBuilderPanel.EDIT_NORMAL) {
			if (panel.getSelectedRTSObject() != null) {
				int x = arg0.getPoint().x - getSelectOffset().x;
				int y = arg0.getPoint().y - getSelectOffset().y;
				if (x < 0)
					x = 0;
				if (y < 0)
					y = 0;
				panel.getSelectedRTSObject().setLocation(new Point(x, y));

				Set<PortConnector> connectorSet = panel.getRTSystemProfile()
						.getOwnedConnectorSet(panel.getSelectedRTSObject());
				for (PortConnector portConnector : connectorSet) {
					ConnectorShape connectorShape = panel.getRTSystemShape()
							.getConnectorShape(portConnector);
					if (connectorShape != null)
						connectorShape.autoPivot();
				}
			}

			if (panel.getSelectedConnector() != null) {
				panel.getRTSystemShape().getSelectedConnectorShape()
						.onMouseDragged(arg0);
			}
		} else if (panel.getEditMode() == RTSystemBuilderPanel.EDIT_CONNECTION) {
			panel.setMousePosition(arg0.getPoint());
			panel.repaint();
		}
		panel.repaint();
	}

	public void mouseReleased(MouseEvent arg0) {
		logger.entering(this.getClass().getName(), "mouseReleased", arg0);
		try {
			panel.setSelectedPivot(null);
			if (panel.getEditMode() == RTSystemBuilderPanel.EDIT_CONNECTION) {

				PortShape portShape = panel.getRTSystemShape()
						.getContainingPortShape(arg0.getPoint());
				if (portShape != null) {

					panel.getRTSystemShape().connect(
							panel.getSelectedDataPort(),
							portShape.getDataPort());
					panel.refresh();

				}
				panel.setEditMode(RTSystemBuilderPanel.EDIT_NORMAL);
			}

			panel.repaint();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void mouseMoved(MouseEvent arg0) {
		if (panel.getEditMode() == RTSystemBuilderPanel.EDIT_NORMAL) {
			PortShape portShape = panel.getRTSystemShape().getContainingPortShape(arg0.getPoint());
			if(portShape != null) {
				panel.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
				ToolTipManager.sharedInstance().setEnabled(true);
				String str = portShape.getDataPort().get(DataPort.RTS_NAME) + ":" + portShape.getDataPort().getDataType();
				panel.setToolTipText(str);
				return;
			}
			
			RTSObjectShape componentShape = panel.getRTSystemShape().getContainingRTSObject(arg0.getPoint());
			if(componentShape != null) {
				panel.setCursor(new Cursor(Cursor.HAND_CURSOR));
				ToolTipManager.sharedInstance().setEnabled(true);
				String str = componentShape.getRTSObject().get(Component.PATH_URI);
				panel.setToolTipText(str);
				return;
			}
		}
		panel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		ToolTipManager.sharedInstance().setEnabled(false);
	}
}
