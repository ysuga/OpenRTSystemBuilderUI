/**
 * PyIOEditor.java
 *
 * @author Yuki Suga (ysuga.net)
 * @date 2011/10/09
 * @copyright 2011, ysuga.net allrights reserved.
 *
 */
package net.ysuga.rtsbuilder.ui.pyio.editor;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;

import net.ysuga.rtsystem.profile.PAIOComponent;

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
	
	private JTabbedPane tabbedPane;
	private PyIOMethodTree pyioMethodTree;
	
	private PAIOComponent component;
	
	
	
	
	public PyIOEditor(PAIOComponent component) {
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
}
