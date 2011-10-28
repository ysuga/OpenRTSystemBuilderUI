/**
 * RTSystemShapeBuilder.java
 *
 * @author Yuki Suga (ysuga.net)
 * @date 2011/08/27
 * @copyright 2011, ysuga.net allrights reserved.
 *
 */
package net.ysuga.rtsbuilder.ui.shape;

import java.util.List;
import java.util.Set;

import net.ysuga.rtsbuilder.ui.pyio.PyIOComponentShape;
import net.ysuga.rtsystem.profile.RTComponent;
import net.ysuga.rtsystem.profile.DataPort;
import net.ysuga.rtsystem.profile.DataPortConnector;
import net.ysuga.rtsystem.profile.PortConnector;
import net.ysuga.rtsystem.profile.Properties;
import net.ysuga.rtsystem.profile.PyIOComponent;
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
public class RTSystemShapeBuilder {

	public RTSystemShapeBuilder() {
	}

	public RTSystemShape createRTSystemShape() {
		return new RTSystemShape();
	}

	/**
	 * buildRTSystemShape <div lang="ja">
	 * 
	 * @param rtSystemProfile
	 * @return </div> <div lang="en">
	 * 
	 * @param rtSystemProfile
	 * @return </div>
	 */
	public RTSystemShape buildRTSystemShape(RTSystemProfile rtSystemProfile) {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		RTSystemShape shape = createRTSystemShape();
		shape.setRTSystem(rtSystemProfile);

		for (RTComponent component : (Set<RTComponent>) rtSystemProfile.componentSet) {
			if (component.get(PyIOComponent.PYIO) != null) {
				shape.componentShapeList.add(new PyIOComponentShape(component));
			} else {
				shape.componentShapeList.add(new RTComponentShape(component));
			}
		}

		for (DataPortConnector connector : (Set<DataPortConnector>) rtSystemProfile.dataPortConnectorSet) {
			String sourceComponentPathUri = connector.sourcePort.properties
					.get(Properties.VALUE);
			String targetComponentPathUri = connector.targetPort.properties
					.get(Properties.VALUE);

			PortShape sourcePort = null;
			PortShape targetPort = null;
			for (RTSObjectShape cShape : (List<RTSObjectShape>) shape.componentShapeList) {
				if (cShape instanceof RTComponentShape) {
					RTComponentShape compShape = ((RTComponentShape) cShape);
					if (compShape.getRTSObject().get(RTComponent.PATH_URI)
							.equals(sourceComponentPathUri)) {
						for (PortShape portShape : (Set<PortShape>) compShape.portShapeSet) {
							if (portShape
									.getDataPort()
									.get(DataPort.RTS_NAME)
									.equals(connector.sourcePort
											.get(PortConnector.Port.PORT_NAME))) {
								sourcePort = portShape;
							}
						}
					} else if (compShape.getRTSObject().get(RTComponent.PATH_URI)
							.equals(targetComponentPathUri)) {
						for (PortShape portShape : (Set<PortShape>) compShape.portShapeSet) {
							if (portShape
									.getDataPort()
									.get(DataPort.RTS_NAME)
									.equals(connector.targetPort
											.get(PortConnector.Port.PORT_NAME))) {
								targetPort = portShape;
							}
						}
					}

				}
			}

			if(sourcePort != null && targetPort != null) {
			shape.connectorShapeList.add(new ConnectorShape(connector,
					sourcePort, targetPort));
			}
		}

		for (ServicePortConnector connector : (Set<ServicePortConnector>) rtSystemProfile.servicePortConnectorSet) {
			String sourceComponentPathUri = connector.sourcePort.properties
					.get(Properties.VALUE);
			String targetComponentPathUri = connector.targetPort.properties
					.get(Properties.VALUE);

			PortShape sourcePort = null;
			PortShape targetPort = null;
			for (RTSObjectShape cShape : (List<RTSObjectShape>) shape.componentShapeList) {
				if (cShape instanceof RTComponentShape) {
					RTComponentShape compShape = (RTComponentShape) cShape;
					if (compShape.getRTSObject().get(RTComponent.PATH_URI)
							.equals(sourceComponentPathUri)) {
						for (PortShape portShape : (Set<PortShape>) compShape.portShapeSet) {
							if (portShape
									.getDataPort()
									.get(DataPort.RTS_NAME)
									.equals(connector.sourcePort
											.get(PortConnector.Port.PORT_NAME))) {
								sourcePort = portShape;
							}
						}
					} else if (compShape.getRTSObject().get(RTComponent.PATH_URI)
							.equals(targetComponentPathUri)) {
						for (PortShape portShape : (Set<PortShape>) compShape.portShapeSet) {
							if (portShape
									.getDataPort()
									.get(DataPort.RTS_NAME)
									.equals(connector.targetPort
											.get(PortConnector.Port.PORT_NAME))) {
								targetPort = portShape;
							}
						}
					}
				}
			}

			if(sourcePort != null && targetPort != null) {
				shape.connectorShapeList.add(new ConnectorShape(connector,
					sourcePort, targetPort));
			}
		}

		return shape;
	}

}
