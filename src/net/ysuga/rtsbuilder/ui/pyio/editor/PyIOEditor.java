/**
 * PyIOEditor.java
 *
 * @author Yuki Suga (ysuga.net)
 * @date 2011/10/09
 * @copyright 2011, ysuga.net allrights reserved.
 *
 */
package net.ysuga.rtsbuilder.ui.pyio.editor;

import java.awt.Point;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;

import net.ysuga.rtsystem.profile.DataPort;
import net.ysuga.rtsystem.profile.PyIOComponent;

/**
 *
 * @author ysuga
 *
 */
public class PyIOEditor extends JFrame {
	
	
	private JSplitPane vSplitPane;
	private JSplitPane hSplitPane;
	
	private PyIOEditorPane onExecuteEditorPane;
	private PyIOEditorPane onActivatedEditorPane;
	private PyIOEditorPane onDeactivatedEditorPane;
	
	private PyIOConfigurationTree pyioConfigurationTree;
	
	public PyIOConfigurationTree getPyIOConfigurationTree() {
		return pyioConfigurationTree;
	}
	
	public PyIOEditorPane getCurrentEditor() {
		return (PyIOEditorPane)((JScrollPane)tabbedPane.getSelectedComponent()).getViewport().getComponent(0);
	}
	
	private JTabbedPane tabbedPane;
	private PyIOMethodTree pyioMethodTree;
	
	public PyIOMethodTree getPyIOMethodTree() {
		return pyioMethodTree;
	}
	
	private PyIOComponent component;
	
	
	
	
	public PyIOEditor(PyIOComponent component) {
		super();
		this.component = component;
		
		String onExecuteCode = component.getOnExecuteCode();
		String onActivatedCode = component.getOnActivatedCode();
		String onDeactivatedCode = component.getOnDeactivatedCode();
		
		
		setTitle("PyIO Editor (" + component.getModuleName() + ")");
		onExecuteEditorPane = new PyIOEditorPane();
		onExecuteEditorPane.setText(onExecuteCode);
		onActivatedEditorPane = new PyIOEditorPane();
		onActivatedEditorPane.setText(onActivatedCode);
		onDeactivatedEditorPane = new PyIOEditorPane();
		onDeactivatedEditorPane.setText(onDeactivatedCode);
		
		pyioConfigurationTree = new PyIOConfigurationTree(component);
		pyioMethodTree = new PyIOMethodTree(component);
		
		tabbedPane = new JTabbedPane();
		tabbedPane.add("onExecute", new JScrollPane(onExecuteEditorPane, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS));
		tabbedPane.add("onActivated",  new JScrollPane(onActivatedEditorPane, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS));
		tabbedPane.add("onDeactivated",  new JScrollPane(onDeactivatedEditorPane, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS));
		
		vSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, pyioMethodTree, pyioConfigurationTree);
		hSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, tabbedPane, vSplitPane);
		setContentPane(hSplitPane);
		
		mouseAdapter = new PyIOEditorMouseAdapter(this);
		tabbedPane.addMouseListener(mouseAdapter);
		pyioConfigurationTree.addMouseListener(mouseAdapter);
		tabbedPane.addMouseMotionListener(mouseAdapter);
		pyioConfigurationTree.addMouseMotionListener(mouseAdapter);

		
		super.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		super.addWindowListener(new WindowAdapter() {

			/**
			 * windowClosing
			 * @param e
			 */
			@Override
			public void windowClosing(WindowEvent e) {
				updatePyIOCode();
				setVisible(false);
				super.windowClosing(e);
			}
			
		});
	}
	
	
	@Override
	public void setVisible(boolean flag) {
		if(flag) {
			setSize(800, 600);
	
			vSplitPane.setDividerLocation(300);
			hSplitPane.setDividerLocation(600);
		}
		super.setVisible(flag);
	}
	
	
	public void updatePyIOCode() {
		component.setOnExecuteCode(onExecuteEditorPane.getText());
	}

	PyIOEditorMouseAdapter mouseAdapter;
	
	String getBasicDataTypeCodeForInPort(String portName) {
		String methodCode = "\n" + PyIOComponent.indent + "if self._"+portName+"In.isNew():\n"
		 + PyIOComponent.indent  + PyIOComponent.indent + "data = self._"+portName+"In.read()\n"
		 + PyIOComponent.indent  + PyIOComponent.indent + "#print data.data";
		return methodCode;
	}
	
	String getVelocity2DCodeForInPort(String portName) {
		String methodCode = "\n" + PyIOComponent.indent + "if self._"+portName+"In.isNew():\n"
		 + PyIOComponent.indent  + PyIOComponent.indent + "data = self._"+portName+"In.read()\n"
		 + PyIOComponent.indent  + PyIOComponent.indent + "#print data.data.vx\n"
		 + PyIOComponent.indent  + PyIOComponent.indent + "#print data.data.vy\n"
		 + PyIOComponent.indent  + PyIOComponent.indent + "#print data.data.va\n";
		return methodCode;
	}

	String getPose2DCodeForInPort(String portName) {
		String methodCode = "\n" + PyIOComponent.indent + "if self._"+portName+"In.isNew():\n"
		 + PyIOComponent.indent  + PyIOComponent.indent + "data = self._"+portName+"In.read()\n"
		 + PyIOComponent.indent  + PyIOComponent.indent + "#print data.data.position.x\n"
		 + PyIOComponent.indent  + PyIOComponent.indent + "#print data.data.position.y\n"
		 + PyIOComponent.indent  + PyIOComponent.indent + "#print data.data.heading\n";
		return methodCode;
	}
	
	String getBasicDataTypeCodeForOutPort(String portName) {
		String methodCode = "\n" + PyIOComponent.indent +"print self._d_"+ portName + ".data = []\n"
		 + PyIOComponent.indent +"self._"+portName+"Out.write()\n";
		
		return methodCode;
	}
	
	String getVelocity2DCodeForOutPort(String portName) {
		String methodCode = "\n" + PyIOComponent.indent + "vx = 0.0\n"
			+ PyIOComponent.indent + "vy = 0.0\n"
			+ PyIOComponent.indent + "va = 0.0\n"
			+ PyIOComponent.indent +"self._d_"+ portName + ".data = RTC.Velocity2D(vx, vy, va)\n"
			+ PyIOComponent.indent +"self._"+portName+"Out.write()\n";
		return methodCode;
	}

	String getPose2DCodeForOutPort(String portName) {
		String methodCode = "\n" + PyIOComponent.indent + "x = 0.0\n"
			+ PyIOComponent.indent + "y = 0.0\n"
			+ PyIOComponent.indent + "heading = 0.0\n"
			+ PyIOComponent.indent + "position = RTC.Point2D(x, y)\n"
			+ PyIOComponent.indent + "self._d_"+ portName + ".data = RTC.Pose2D(position, heading)\n"
			+ PyIOComponent.indent + "self._"+portName+"Out.write()\n";
		
		return methodCode;
	}
	
	
	/**
	 * addConfigurationMedthodOnEditor
	 *
	 * @param selectedNode
	 * @param point
	 */
	public void addConfigurationMedthodOnEditor(
			PyIOConfigurationNode selectedNode, Point point) {
		DataPort dataPort = selectedNode.getDataPort();
		if(dataPort.getDirection() == DataPort.DIRECTION_IN) {
			
			String methodCode = null;
			if(dataPort.getDataType().equals("TimedVelocity2D")) {
				methodCode = getVelocity2DCodeForInPort(dataPort.getPlainName());
			} else if(dataPort.getDataType().equals("TimedPose2D")) {
				methodCode = getPose2DCodeForInPort(dataPort.getPlainName());
			} else {
				methodCode = getBasicDataTypeCodeForInPort(dataPort.getPlainName());
			}
			
			JScrollPane c = (JScrollPane)tabbedPane.getSelectedComponent();
			PyIOEditorPane selectedPane = (PyIOEditorPane)(((JScrollPane)c).getViewport().getComponent(0));
			selectedPane.addMethodCode(methodCode, point);
		} else if(dataPort.getDirection() == DataPort.DIRECTION_OUT) {
			String methodCode = null;
			String dataType = dataPort.getDataType();
			if(dataType.equals("TimedVelocity2D")) {
				methodCode = getVelocity2DCodeForOutPort(dataPort.getPlainName());
			} else if(dataPort.getDataType().equals("TimedPose2D")) {
				methodCode = getPose2DCodeForOutPort(dataPort.getPlainName());
			} else {
				methodCode = getBasicDataTypeCodeForOutPort(dataPort.getPlainName());
			}
	
			JScrollPane c = (JScrollPane)tabbedPane.getSelectedComponent();
			PyIOEditorPane selectedPane = (PyIOEditorPane)(((JScrollPane)c).getViewport().getComponent(0));
			selectedPane.addMethodCode(methodCode, point);
		}
	}
}
