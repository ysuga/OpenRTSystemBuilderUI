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
import net.ysuga.rtsystem.profile.Connector;
import net.ysuga.rtsystem.profile.Properties;
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
		
		for(Connector connector : (Set<Connector>)rtSystemProfile.connectorSet) {
			String sourceComponentPathUri =  connector.sourceDataPort.properties.get(Properties.VALUE);
			String targetComponentPathUri =  connector.targetDataPort.properties.get(Properties.VALUE);

			PortShape sourcePort = null;
			PortShape targetPort = null;
			for(ComponentShape compShape : shape.componentShapeList) {
				if(compShape.getComponent().get(Component.PATH_URI).equals(sourceComponentPathUri)) {
					for(PortShape portShape : (Set<PortShape>)compShape.portShapeSet) {
						if(portShape.getDataPort().get(Component.DataPort.RTS_NAME).equals(connector.sourceDataPort.get(Connector.DataPort.PORT_NAME))) {
							sourcePort = portShape;
						}
					}
				} else if(compShape.getComponent().get(Component.PATH_URI).equals(targetComponentPathUri)) {
					for(PortShape portShape : (Set<PortShape>)compShape.portShapeSet) {
						if(portShape.getDataPort().get(Component.DataPort.RTS_NAME).equals(connector.targetDataPort.get(Connector.DataPort.PORT_NAME))) {
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