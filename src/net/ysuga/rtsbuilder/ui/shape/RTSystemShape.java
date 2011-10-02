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
import java.util.Set;

import net.ysuga.rtsystem.profile.PortConnector;
import net.ysuga.rtsystem.profile.RTSProperties;
import net.ysuga.rtsystem.profile.RTSystemProfile;

/**
 * <div lang="ja">
 *
 * </div>
 * <div lang="en">
 *
 * </div>
 * @author ysuga
 *
 */
public class RTSystemShape {
	final public ComponentShape getSelectedComponentShape() {
		for (ComponentShape componentShape : componentShapeList) {
			if (componentShape.isSelected())
				return componentShape;
		}
		return null;
	}
	
	final public ConnectorShape getSelectedConnectorShape() {
		for(ConnectorShape cs : connectorShapeList) {
			if(cs.isSelected()) {
				return cs;
			}
		}
		return null;
	}

	public ComponentShapeList componentShapeList;

	final public ComponentShape getComponentShape(RTSProperties state) {
		for (ComponentShape shape : componentShapeList) {
			if (state.equals(shape.getComponent())) {
				return (ComponentShape) shape;
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
		componentShapeList = new ComponentShapeList();
		connectorShapeList = new ConnectorShapeList();
	}

	public void draw(Graphics g) {
		for (ModelShape componentShape : componentShapeList) {
			Color oldColor = g.getColor();
			/*
			if (rtSystemProfile.getExecutionState() == StateMachine.HALT
					&& stateShape.getOwnerState().getInitialStateCondition()
							.equals(StateCondition.ACTIVE)) {
				g.setColor(Color.magenta);
			} else if (stateMachine.getExecutionState() != StateMachine.HALT
					&& stateShape.getOwnerState().getStateCondition()
							.equals(StateCondition.ACTIVE)) {
				g.setColor(Color.red);
			}*/

			componentShape.draw((Graphics2D) g);
			g.setColor(oldColor);
		}

		for (ConnectorShape transitionShape : connectorShapeList) {
			transitionShape.draw((Graphics2D) g);
		}

		ModelShape shape = getSelectedComponentShape();
		if (shape != null) {
			Color oldColor = g.getColor();
			/*
			if (rtSystemProfile.getExecutionState() == StateMachine.HALT
					&& stateShape.getOwnerState().getInitialStateCondition()
							.equals(StateCondition.ACTIVE)) {
				g.setColor(Color.magenta);
			} else if (stateMachine.getExecutionState() != StateMachine.HALT
					&& stateShape.getOwnerState().getStateCondition()
							.equals(StateCondition.ACTIVE)) {
				g.setColor(Color.red);
			}*/

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
	public ComponentShapeList getComponentShapeList() {
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
		for (ComponentShape shape : componentShapeList) {
			if (shape.getComponent().equals(selectedComponent)) {
				shape.setSelected(true);
			}
		}
	}

	
	public void setSelectedDataPort(RTSProperties selectedDataPort) {
		for (ComponentShape shape : componentShapeList) {
			for(PortShape pshape : (Set<PortShape>)shape.portShapeSet) {
				if(pshape.getDataPort().equals(selectedDataPort)) {
					pshape.setSelected(true);
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
	 * getSelectedDataPortShape
	 * <div lang="ja">
	 * 
	 * @return
	 * </div>
	 * <div lang="en">
	 *
	 * @return
	 * </div>
	 */
	public PortShape getSelectedDataPortShape() {
		for(ComponentShape compShape : this.componentShapeList) {
			for(PortShape portShape: (Set<PortShape>)compShape.portShapeSet) {
				if(portShape.isSelected()) {
					return portShape;
				}
			}
		}
		return null;
	}

}
