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

import net.ysuga.rtsbuilder.ui.pyio.PAIOComponentShape;
import net.ysuga.rtsystem.profile.Component;
import net.ysuga.rtsystem.profile.DataPort;
import net.ysuga.rtsystem.profile.DataPortConnector;
import net.ysuga.rtsystem.profile.PortConnector;
import net.ysuga.rtsystem.profile.Properties;
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

		for (Component component : (Set<Component>) rtSystemProfile.componentSet) {
			if (component.get("PAIO") != null) {
				shape.componentShapeList.add(new PAIOComponentShape(component));
			} else {
				shape.componentShapeList.add(new ComponentShape(component));
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
				if (cShape instanceof ComponentShape) {
					ComponentShape compShape = ((ComponentShape) cShape);
					if (compShape.getRTSObject().get(Component.PATH_URI)
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
					} else if (compShape.getRTSObject().get(Component.PATH_URI)
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

			shape.connectorShapeList.add(new ConnectorShape(connector,
					sourcePort, targetPort));
		}

		for (ServicePortConnector connector : (Set<ServicePortConnector>) rtSystemProfile.servicePortConnectorSet) {
			String sourceComponentPathUri = connector.sourcePort.properties
					.get(Properties.VALUE);
			String targetComponentPathUri = connector.targetPort.properties
					.get(Properties.VALUE);

			PortShape sourcePort = null;
			PortShape targetPort = null;
			for (RTSObjectShape cShape : (List<RTSObjectShape>) shape.componentShapeList) {
				if (cShape instanceof ComponentShape) {
					ComponentShape compShape = (ComponentShape) cShape;
					if (compShape.getRTSObject().get(Component.PATH_URI)
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
					} else if (compShape.getRTSObject().get(Component.PATH_URI)
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

			shape.connectorShapeList.add(new ConnectorShape(connector,
					sourcePort, targetPort));
		}

		return shape;
	}

}
