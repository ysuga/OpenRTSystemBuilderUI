/**
 * RTSystemShape.java
 *
 * @author Yuki Suga (ysuga.net)
 * @date 2011/08/27
 * @copyright 2011, ysuga.net allrights reserved.
 *
 */
package net.ysuga.rtsbuilder.ui.shape;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.swing.JOptionPane;

import net.ysuga.rtsbuilder.ui.ConnectionDialog;
import net.ysuga.rtsystem.profile.Component;
import net.ysuga.rtsystem.profile.DataPort;
import net.ysuga.rtsystem.profile.DataPortConnector;
import net.ysuga.rtsystem.profile.PortConnector;
import net.ysuga.rtsystem.profile.RTSObject;
import net.ysuga.rtsystem.profile.RTSProperties;
import net.ysuga.rtsystem.profile.RTSystemProfile;
import net.ysuga.rtsystem.profile.ServicePortConnector;

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
public class RTSystemShape {
	final public ComponentShape getSelectedComponentShape() {
		for (RTSObjectShape componentShape : componentShapeList) {
			if (componentShape instanceof ComponentShape) {
				if (((ComponentShape) componentShape).isSelected())
					return (ComponentShape) componentShape;
			}
		}
		return null;
	}

	final public ConnectorShape getSelectedConnectorShape() {
		for (ConnectorShape cs : connectorShapeList) {
			if (cs.isSelected()) {
				return cs;
			}
		}
		return null;
	}

	public List<RTSObjectShape> componentShapeList;

	final public ComponentShape getComponentShape(RTSProperties state) {
		for (RTSObjectShape shape : componentShapeList) {
			if (shape instanceof ComponentShape) {
				if (state.equals(((ComponentShape) shape).getRTSObject())) {
					return (ComponentShape) shape;
				}
			}
		}
		return null;
	}

	public ConnectorShapeList connectorShapeList;

	private RTSystemProfile rtSystemProfile;

	/**
	 * <div lang="ja"> �R���X�g���N�^ </div> <div lang="en"> Constructor </div>
	 */
	public RTSystemShape() {
		componentShapeList = new ArrayList<RTSObjectShape>();
		connectorShapeList = new ConnectorShapeList();
	}

	public void draw(Graphics g) {
		for (RTSObjectShape componentShape : componentShapeList) {
			Color oldColor = g.getColor();
			/*
			 * if (rtSystemProfile.getExecutionState() == StateMachine.HALT &&
			 * stateShape.getOwnerState().getInitialStateCondition()
			 * .equals(StateCondition.ACTIVE)) { g.setColor(Color.magenta); }
			 * else if (stateMachine.getExecutionState() != StateMachine.HALT &&
			 * stateShape.getOwnerState().getStateCondition()
			 * .equals(StateCondition.ACTIVE)) { g.setColor(Color.red); }
			 */

			componentShape.draw((Graphics2D) g);
			g.setColor(oldColor);
		}

		for (ConnectorShape transitionShape : connectorShapeList) {
			transitionShape.draw((Graphics2D) g);
		}

		RTSObjectShape shape = getSelectedComponentShape();
		if (shape != null) {
			Color oldColor = g.getColor();
			/*
			 * if (rtSystemProfile.getExecutionState() == StateMachine.HALT &&
			 * stateShape.getOwnerState().getInitialStateCondition()
			 * .equals(StateCondition.ACTIVE)) { g.setColor(Color.magenta); }
			 * else if (stateMachine.getExecutionState() != StateMachine.HALT &&
			 * stateShape.getOwnerState().getStateCondition()
			 * .equals(StateCondition.ACTIVE)) { g.setColor(Color.red); }
			 */

			shape.draw((Graphics2D) g);

			g.setColor(oldColor);
		}

	}

	/**
	 * <div lang="ja">
	 * 
	 * @return </div> <div lang="en">
	 * 
	 * @return </div>
	 */
	public List<RTSObjectShape> getComponentShapeList() {
		return componentShapeList;
	}

	/**
	 * <div lang="ja">
	 * 
	 * @param selectedState
	 *            </div> <div lang="en">
	 * 
	 * @param selectedState
	 *            </div>
	 */
	public void setSelectedComponent(RTSProperties selectedComponent) {
		for (RTSObjectShape shape : componentShapeList) {
			if (shape instanceof ComponentShape) {
				if (((ComponentShape) shape).getRTSObject().equals(
						selectedComponent)) {
					shape.setSelected(true);
				}
			}
		}
	}

	public void setSelectedDataPort(RTSProperties selectedDataPort) {
		for (RTSObjectShape shape : componentShapeList) {
			if (shape instanceof ComponentShape) {
				for (PortShape pshape : (Set<PortShape>) ((ComponentShape) shape).portShapeSet) {
					if (pshape.getDataPort().equals(selectedDataPort)) {
						pshape.setSelected(true);
					}
				}
			}
		}
	}

	/**
	 * <div lang="ja">
	 * 
	 * @param selectedState
	 *            </div> <div lang="en">
	 * 
	 * @param selectedState
	 *            </div>
	 */
	public void setSlectedTransition(PortConnector selectedConnector) {
		for (ConnectorShape shape : connectorShapeList) {
			if (shape.getConnector() == selectedConnector) {
				shape.setSelected(true);
			}
		}
	}

	/**
	 * <div lang="ja">
	 * 
	 * @return </div> <div lang="en">
	 * 
	 * @return </div>
	 */
	public ConnectorShapeList getConnectorShapeList() {
		return connectorShapeList;
	}

	/**
	 * <div lang="ja">
	 * 
	 * @param selectedTransition
	 *            </div> <div lang="en">
	 * 
	 * @param selectedTransition
	 *            </div>
	 */
	public void setSelectedConnector(PortConnector selectedConnector) {
		for (ConnectorShape connectorShape : connectorShapeList) {
			if (connectorShape.getConnector().equals(selectedConnector)) {
				connectorShape.setSelected(true);
			}
		}
	}

	/**
	 * setStateMachine <div lang="ja">
	 * 
	 * @param stateMachine
	 *            </div> <div lang="en">
	 * 
	 * @param stateMachine
	 *            </div>
	 */
	public void setRTSystem(RTSystemProfile rtSystemProfile) {
		this.rtSystemProfile = rtSystemProfile;
	}

	/**
	 * getSelectedDataPortShape <div lang="ja">
	 * 
	 * @return </div> <div lang="en">
	 * 
	 * @return </div>
	 */
	public PortShape getSelectedDataPortShape() {
		for (RTSObjectShape compShape : componentShapeList) {
			if (compShape instanceof ComponentShape) {
				for (PortShape portShape : (Set<PortShape>) ((ComponentShape) compShape).portShapeSet) {
					if (portShape.isSelected()) {
						return portShape;
					}
				}
			}
		}
		return null;
	}

	/**
	 * @return rtSystemProfile
	 */
	public final RTSystemProfile getRtSystemProfile() {
		return rtSystemProfile;
	}

	/**
	 * getContainingRTSObject
	 * 
	 * @param point
	 * @return
	 */
	public RTSObjectShape getContainingRTSObject(Point point) {
		for (RTSObjectShape componentShape : getComponentShapeList()) {
			if (componentShape.contains(point)) {
				return componentShape;
			}
		}
		return null;
	}

	/**
	 * getContainingConnector
	 * 
	 * @param point
	 * @return
	 */
	public ConnectorShape getContainingConnector(Point point) {
		for (ConnectorShape shape : getConnectorShapeList()) {
			if (shape.contains(point)) {
				return shape;
			}
		}
		return null;
	}

	/**
	 * getContainingPortShape
	 * 
	 * @param point
	 * @return
	 */
	public PortShape getContainingPortShape(Point point) {
		for (RTSObjectShape componentShape : getComponentShapeList()) {
			if (componentShape instanceof ComponentShape) {
				for (PortShape portShape : (Set<PortShape>) ((ComponentShape) componentShape).portShapeSet) {
					if (portShape.contains(point)) {
						return portShape;
					}
				}
			}
		}
		return null;
	}

	/**
	 * getConnectorShape
	 * 
	 * @param portConnector
	 * @return
	 */
	public ConnectorShape getConnectorShape(PortConnector portConnector) {
		for (ConnectorShape cs : connectorShapeList) {
			if (cs.getConnector() == portConnector) {
				return cs;
			}
		}
		return null;
	}

	/**
	 * connect
	 * 
	 * @param selectedDataPort
	 * @param dataPort
	 * @throws Exception 
	 */
	public void connect(RTSObject source, RTSObject target) throws Exception {
		if ((source instanceof DataPort) && (target instanceof DataPort)) {
			Component sourceComponent = this.getRtSystemProfile().getOwner(
					(DataPort) source);
			Component targetComponent = this.getRtSystemProfile().getOwner(
					(DataPort) target);
			ConnectionDialog dialog = new ConnectionDialog(sourceComponent,
					(DataPort) source, targetComponent, (DataPort) target);
			if (dialog.doModal() == JOptionPane.OK_OPTION) {
				if (dialog.isDataPortConnection()) {
					DataPortConnector connector = dialog
							.createDataPortConnector();
					getRtSystemProfile().addDataPortConnector(connector);
				} else if (dialog.isServicePortConnection()) {
					ServicePortConnector connector = dialog
							.createServicePortConnector();
					getRtSystemProfile().addServicePortConnector(connector);
				}
			}
		}
	}

}
