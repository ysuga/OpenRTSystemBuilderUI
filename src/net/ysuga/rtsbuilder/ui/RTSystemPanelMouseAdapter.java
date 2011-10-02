package net.ysuga.rtsbuilder.ui;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Set;

import javax.swing.JOptionPane;

import net.ysuga.rtsbuilder.ui.shape.ComponentShape;
import net.ysuga.rtsbuilder.ui.shape.ConnectorShape;
import net.ysuga.rtsbuilder.ui.shape.PivottedNamedArrow;
import net.ysuga.rtsbuilder.ui.shape.PortShape;
import net.ysuga.rtsystem.profile.Component;
import net.ysuga.rtsystem.profile.DataPortConnector;
import net.ysuga.rtsystem.profile.PivotList;
import net.ysuga.rtsystem.profile.RTSProperties;
import net.ysuga.rtsystem.profile.ServicePortConnector;

/**
 * @author ysuga
 * 
 */
public class RTSystemPanelMouseAdapter implements MouseListener,
		MouseMotionListener {

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

	public void mouseClicked(MouseEvent arg0) {
		// panel.setSelectedState(null);
		if (panel.getEditMode() == RTSystemBuilderPanel.EDIT_NORMAL) {
			for (ComponentShape componentShape : panel.getRTSystemShape()
					.getComponentShapeList()) {
				if (componentShape.contains(arg0.getPoint())) {
					panel.setSelectedComponent(componentShape.getComponent());
					int dx = (int) (arg0.getPoint().x - componentShape.getX());
					int dy = (int) (arg0.getPoint().y - componentShape.getY());
					setSelectedOffset(new Point(dx, dy));
					ComponentShape.onClicked(panel, arg0);
					return;
				}
			}

			for (ConnectorShape shape : panel.getRTSystemShape()
					.getConnectorShapeList()) {
				if (shape.contains(arg0.getPoint())) {
					shape.setSelected(true);
					panel.setSelectedConnector(shape.getConnector());

					if (arg0.getButton() == MouseEvent.BUTTON3) { // RightClick
						panel.getConnectorPopupMenu().show(panel,
								arg0.getPoint());
					}

					shape.onMousePressed(arg0);
					/*
					 * if (arg0.getButton() == MouseEvent.BUTTON1 &&
					 * arg0.getClickCount() == 2) { PivotList list =
					 * shape.getConnector().getPivotList(); Point nearestPivot =
					 * null; Point mouse = arg0.getPoint(); for (Point pivot :
					 * list) { int dx = pivot.x - mouse.x; int dy = pivot.y -
					 * mouse.y; double distance = Math.sqrt(dx * dx + dy * dy);
					 * if (distance < 10) { nearestPivot = pivot; }
					 * 
					 * } if (nearestPivot != null) { list.remove(nearestPivot);
					 * } }
					 */
					return;
				}
			}
		}

		panel.setSelectedComponent(null);
		if (arg0.getButton() == MouseEvent.BUTTON3) { // RightClick
			panel.getPopupMenu().show(panel, arg0.getPoint());
		}
		panel.repaint();
	}

	public void mouseEntered(MouseEvent arg0) {
	}

	public void mouseExited(MouseEvent arg0) {

	}

	public void mousePressed(MouseEvent arg0) {
		RTSProperties selectedComponent = panel.getSelectedComponent();
		// panel.setSelectedState(null);
		for (ComponentShape componentShape : panel.getRTSystemShape()
				.getComponentShapeList()) {
			if (componentShape.contains(arg0.getPoint())) { // If Mouse Click is
															// on
				// State....
				if (panel.getEditMode() == RTSystemBuilderPanel.EDIT_NORMAL) {
					panel.setSelectedComponent(componentShape.getComponent());
					int dx = (int) (arg0.getPoint().x - componentShape.getX());
					int dy = (int) (arg0.getPoint().y - componentShape.getY());
					setSelectedOffset(new Point(dx, dy));
				}

				/**
				 * else if (panel.getEditMode() ==
				 * RTSystemBuilderPanel.EDIT_TRANSITION) {
				 * 
				 * if (stateShape.getComponent().getName().equals("start")) {
				 * JOptionPane.showMessageDialog(panel,
				 * "Start State cannot be a target."); } else {
				 * TransitionSettingDialog dialog = new TransitionSettingDialog(
				 * panel, null);
				 * dialog.setSourceStateName(selectedState.getName());
				 * dialog.setTargetStateName(stateShape.getState() .getName());
				 * panel.setSelectedState(stateShape.getState()); if
				 * (dialog.doModal() == AbstractStateSettingDialog.OK_OPTION) {
				 * try { dialog.createTransition(); } catch
				 * (InvalidConnectionException e) { // TODO �����������ꂽ catch
				 * �u���b�N e.printStackTrace(); Object obj =
				 * "InvalidConnectionException";
				 * JOptionPane.showMessageDialog(panel, obj); } panel.repaint();
				 * } } panel.setEditMode(StateMachinePanel.EDIT_NORMAL); }
				 */
				panel.repaint();

				return;
			}
			for (PortShape portShape : (Set<PortShape>) componentShape.portShapeSet) {
				if (portShape.contains(arg0.getPoint())) {

					panel.setSelectedComponent(componentShape.getComponent());
					panel.setSelectedPort(portShape.getDataPort());
					panel.setEditMode(RTSystemBuilderPanel.EDIT_CONNECTION);

					return;
				}
			}
		}
		panel.setSelectedComponent(null);

		for (ConnectorShape connectorShape : panel.getRTSystemShape()
				.getConnectorShapeList()) {
			if (connectorShape.contains(arg0.getPoint())) {
				// If mouse click is on Transition ....
				connectorShape.setSelected(true);
				panel.setSelectedConnector(connectorShape.getConnector());

				if (arg0.getButton() == MouseEvent.BUTTON1) {
					connectorShape.onMousePressed(arg0);
					/**
					 * Point mp = arg0.getPoint(); panel.setSelectedPivot(null);
					 * for (Point p
					 * :connectorShape.getConnector().getPivotList()) { double
					 * dist = Math.sqrt((p.x - mp.x) * (p.x - mp.x) + (p.y -
					 * mp.y) * (p.y - mp.y)); if (dist < 10) {
					 * panel.setSelectedPivot(p); } } if
					 * (panel.getSelectedPivot() == null) {
					 * panel.setSelectedPivot(new Point(mp.x, mp.y));
					 * 
					 * Point oldPoint =
					 * connectorShape.getArrow().getStartPoint(); for (int i =
					 * 0; i <
					 * connectorShape.getConnector().getPivotList().size(); i++)
					 * { Point p =
					 * ((List<Point>)connectorShape.getArrow().getPivotList
					 * ()).get(i);
					 * 
					 * if (PivottedNamedArrow.lineContains(oldPoint.x,
					 * oldPoint.y, p.x, p.y, mp)) {
					 * connectorShape.getConnector().getPivotList() .add(i,
					 * panel.getSelectedPivot()); panel.repaint(); return; }
					 * 
					 * oldPoint = p; }
					 * connectorShape.getConnector().getPivotList()
					 * .add(panel.getSelectedPivot()); }
					 */
				} else if (arg0.getButton() == MouseEvent.BUTTON3) {
					panel.setEditMode(RTSystemBuilderPanel.EDIT_NORMAL);
					panel.getConnectorPopupMenu().show(panel, arg0.getPoint());
					panel.repaint();
					return;
				}
			} // If mouse click is on Transition ....
		}

		if (arg0.getButton() == MouseEvent.BUTTON3) { // RightClick
			panel.getPopupMenu().show(panel, arg0.getPoint());
		}

		if (panel.getEditMode() == RTSystemBuilderPanel.EDIT_CONNECTION) {
			panel.setEditMode(RTSystemBuilderPanel.EDIT_NORMAL);
		}

		// panel.setSelectedState(null);
		panel.repaint();

	}

	public void mouseReleased(MouseEvent arg0) {
		/**
		 * ModelDiagramElement fromElement = null; for
		 * (ModelDiagramElementHolder modelDiagramElementHolder :
		 * modelDiagramElementHolderList) { Set<String> keySet =
		 * modelDiagramElementHolder.keySet(); for(String key : keySet) {
		 * ModelDiagramElement elem = modelDiagramElementHolder.get(key);
		 * if(elem.isRButtonCapturing() || elem.isLButtonCapturing()) {
		 * elem.onMouseReleased(arg0); fromElement = elem; // repaint(); } } }
		 * 
		 * 
		 * 
		 * for (ModelDiagramElementHolder modelDiagramElementHolder :
		 * modelDiagramElementHolderList) { ModelDiagramElement
		 * modelDiagramElement = modelDiagramElementHolder
		 * .getContainingObject(arg0); if (modelDiagramElement != null) {
		 * if(fromElement != null) { modelDiagramElement.onMouseDroped(arg0,
		 * fromElement); } } } repaint();
		 */

		for (ConnectorShape transitionShape : panel.getRTSystemShape()
				.getConnectorShapeList()) {
			if (panel.getSelectedConnector() != null
					&& transitionShape.contains(arg0.getPoint())) {
				panel.setSelectedConnector(transitionShape.getConnector());

				Point prevPoint, nextPoint;

				PivotList pivotList = transitionShape.getConnector()
						.getPivotList();
				int selectedIndex = pivotList.indexOf(panel.getSelectedPivot());
				if (selectedIndex < 0) {
					continue;
				} else if (selectedIndex == 0) { // firstPivot
					prevPoint = transitionShape.getArrow().getStartPoint();
				} else {
					prevPoint = pivotList.get(selectedIndex - 1);
				}

				if (selectedIndex == pivotList.size() - 1) { // lastPivot
					nextPoint = transitionShape.getArrow().getEndPoint();
				} else {
					nextPoint = pivotList.get(selectedIndex + 1);
				}

				if (PivottedNamedArrow.lineContains(prevPoint.x, prevPoint.y,
						nextPoint.x, nextPoint.y, panel.getSelectedPivot())) {
					pivotList.remove(panel.getSelectedPivot());
				}

				panel.setSelectedPivot(null);
			}
		}
		if (panel.getEditMode() == RTSystemBuilderPanel.EDIT_CONNECTION) {

			for (ComponentShape componentShape : panel.getRTSystemShape()
					.getComponentShapeList()) {
				for (PortShape portShape : (Set<PortShape>) componentShape.portShapeSet) {
					if (portShape.contains(arg0.getPoint())) {
						try {
							ConnectionDialog dialog = new ConnectionDialog(
									panel.getSelectedComponent(),
									panel.getSelectedDataPort(),
									componentShape.getComponent(),
									portShape.getDataPort());

							if (dialog.doModal() == JOptionPane.OK_OPTION) {
								if(dialog.isDataPortConnection()) {
									DataPortConnector connector = dialog.createDataPortConnector();
									panel.getRTSystemProfile().addDataPortConnector(connector); 						
								} else if(dialog.isServicePortConnection()){
									ServicePortConnector connector = dialog.createServicePortConnector();
									panel.getRTSystemProfile().addServicePortConnector(connector);
								}
								panel.refresh();
							}
						} catch (Exception e) {
							
							e.printStackTrace();
						}
					}
				}
			}
			panel.setEditMode(RTSystemBuilderPanel.EDIT_NORMAL);
		}

		panel.repaint();
	}

	public void mouseDragged(MouseEvent arg0) {
		if (panel.getEditMode() == RTSystemBuilderPanel.EDIT_CONNECTION) {
			panel.setMousePosition(arg0.getPoint());
			panel.repaint();

		} else {
			if (panel.getSelectedComponent() != null) {
				int x = arg0.getPoint().x - getSelectOffset().x;
				int y = arg0.getPoint().y - getSelectOffset().y;
				if (x < 0)
					x = 0;
				if (y < 0)
					y = 0;
				panel.getSelectedComponent().setLocation(new Point(x, y));
				for (DataPortConnector con : (Set<DataPortConnector>) panel
						.getRTSystemProfile().dataPortConnectorSet) {
					if (con.getSourceComponentPathUri().equals(
							panel.getSelectedComponent()
									.get(Component.PATH_URI))) {
						for (ConnectorShape cs : panel.getRTSystemShape().connectorShapeList) {
							if (cs.getConnector().equals(con)) {
								cs.autoPivot();
							}
						}
					}
					if (con.getTargetComponentPathUri().equals(
							panel.getSelectedComponent()
									.get(Component.PATH_URI))) {
						for (ConnectorShape cs : panel.getRTSystemShape().connectorShapeList) {
							if (cs.getConnector().equals(con)) {
								cs.autoPivot();
							}
						}
					}
				}
			}

			// for(TransitionShape transitionShape :
			// panel.getStateMachineShape().getTransitionShapeList()) {
			// if(transitionShape.contains(arg0.getPoint())) {
			if (panel.getSelectedConnector() != null) {
				Point mp = arg0.getPoint();
				int x = mp.x;
				int y = mp.y;
				if (x < 0)
					x = 0;
				if (y < 0)
					y = 0;
				/*
				 * if (panel.getSelectedPivot() != null) {
				 * panel.getSelectedPivot().setLocation(x, y); }
				 */
				panel.getRTSystemShape().getSelectedConnectorShape()
						.onMouseDragged(arg0);
			}
		}
		panel.repaint();
	}

	public void mouseMoved(MouseEvent arg0) {
		if (panel.getEditMode() == RTSystemBuilderPanel.EDIT_TRANSITION) {
			// panel.repaint();
			panel.setMousePosition(arg0.getPoint());
			panel.repaint();
		}
	}
}
