/**
 * RTSystemShapeBuilder.java
 *
 * @author Yuki Suga (ysuga.net)
 * @date 2011/08/27
 * @copyright 2011, ysuga.net allrights reserved.
 *
 */
package net.ysuga.rtsbuilder.ui.shape;

import java.util.Set;

import net.ysuga.rtsystem.profile.Component;
import net.ysuga.rtsystem.profile.DataPortConnector;
import net.ysuga.rtsystem.profile.PortConnector;
import net.ysuga.rtsystem.profile.Properties;
import net.ysuga.rtsystem.profile.RTSystemProfile;
import net.ysuga.rtsystem.profile.ServicePortConnector;

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
public class RTSystemShapeBuilder {

	/**
	 * buildRTSystemShape
	 * <div lang="ja">
	 * 
	 * @param rtSystemProfile
	 * @return
	 * </div>
	 * <div lang="en">
	 *
	 * @param rtSystemProfile
	 * @return
	 * </div>
	 */
	public static RTSystemShape buildRTSystemShape(
			RTSystemProfile rtSystemProfile) {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		RTSystemShape shape = new RTSystemShape();
		for(Component component : (Set<Component>)rtSystemProfile.componentSet) {
			shape.componentShapeList.add(new ComponentShape(component));
		}
		
		for(DataPortConnector connector : (Set<DataPortConnector>)rtSystemProfile.dataPortConnectorSet) {
			String sourceComponentPathUri =  connector.sourcePort.properties.get(Properties.VALUE);
			String targetComponentPathUri =  connector.targetPort.properties.get(Properties.VALUE);

			PortShape sourcePort = null;
			PortShape targetPort = null;
			for(ComponentShape compShape : shape.componentShapeList) {
				if(compShape.getComponent().get(Component.PATH_URI).equals(sourceComponentPathUri)) {
					for(PortShape portShape : (Set<PortShape>)compShape.portShapeSet) {
						if(portShape.getDataPort().get(Component.DataPort.RTS_NAME).equals(connector.sourcePort.get(PortConnector.Port.PORT_NAME))) {
							sourcePort = portShape;
						}
					}
				} else if(compShape.getComponent().get(Component.PATH_URI).equals(targetComponentPathUri)) {
					for(PortShape portShape : (Set<PortShape>)compShape.portShapeSet) {
						if(portShape.getDataPort().get(Component.DataPort.RTS_NAME).equals(connector.targetPort.get(PortConnector.Port.PORT_NAME))) {
							targetPort = portShape;
						}
					}					
				}
			}
					
			shape.connectorShapeList.add(new ConnectorShape(connector, sourcePort, targetPort));
		}
		
		for(ServicePortConnector connector : (Set<ServicePortConnector>)rtSystemProfile.servicePortConnectorSet) {
			String sourceComponentPathUri =  connector.sourcePort.properties.get(Properties.VALUE);
			String targetComponentPathUri =  connector.targetPort.properties.get(Properties.VALUE);

			PortShape sourcePort = null;
			PortShape targetPort = null;
			for(ComponentShape compShape : shape.componentShapeList) {
				if(compShape.getComponent().get(Component.PATH_URI).equals(sourceComponentPathUri)) {
					for(PortShape portShape : (Set<PortShape>)compShape.portShapeSet) {
						if(portShape.getDataPort().get(Component.DataPort.RTS_NAME).equals(connector.sourcePort.get(PortConnector.Port.PORT_NAME))) {
							sourcePort = portShape;
						}
					}
				} else if(compShape.getComponent().get(Component.PATH_URI).equals(targetComponentPathUri)) {
					for(PortShape portShape : (Set<PortShape>)compShape.portShapeSet) {
						if(portShape.getDataPort().get(Component.DataPort.RTS_NAME).equals(connector.targetPort.get(PortConnector.Port.PORT_NAME))) {
							targetPort = portShape;
						}
					}					
				}
			}
					
			shape.connectorShapeList.add(new ConnectorShape(connector, sourcePort, targetPort));
		}
		
		return shape;
	}

}
