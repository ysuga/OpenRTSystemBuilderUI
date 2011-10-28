/**
 * PropertySettingDialog.java
 *
 * @author Yuki Suga (ysuga.net)
 * @date 2011/10/26
 * @copyright 2011, ysuga.net allrights reserved.
 *
 */
package net.ysuga.rtsbuilder.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTree;

import net.ysuga.corbanaming.ui.GridLayoutPanel;

/**
 *
 * @author ysuga
 *
 */
public class PropertySettingDialog extends JDialog {

	public class PropertyTree extends JTree {
		public PropertyTree() {
			super();
		}
	}
	
	public class PropertyPanel extends GridLayoutPanel {
		public PropertyPanel() {
			super();
		}
	}
	
	private void onOk() {
		
	}
	
	public class ButtonPanel extends GridLayoutPanel {
		public ButtonPanel() {
			super();
			addComponent(0, 0, 0, 0, 10, 1, new JLabel("Press OK if setting is done."));
			addComponent(0, 1, 100, 0, 8, 1, new JLabel(""));
			addComponent(8, 1, 0, 0, 1, 1, new JButton(new AbstractAction("Cancel") {
				public void actionPerformed(ActionEvent e) {
					setVisible(false);
				}
			}));
			addComponent(9, 1, 0, 0, 1, 1, new JButton(new AbstractAction("OK") {
				public void actionPerformed(ActionEvent e) {
					onOk();
				}
			}));
			pack();
		}
	}
	
	private JSplitPane  mainPane;
	private int buttonPanelHeight;
	
	@Override
	public void paint(Graphics g) {
		Dimension sz = getSize();
		mainPane.setDividerLocation(sz.height-buttonPanelHeight);
		super.paint(g);
	}
	
	
	
	public PropertySettingDialog() {
		JPanel contentPane = (JPanel) getContentPane();
		
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, 
				new PropertyTree(), new PropertyPanel());
		mainPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
				splitPane, new ButtonPanel());
		
		contentPane.add(mainPane, BorderLayout.CENTER);
		pack();
		buttonPanelHeight = 100;
	}
}
