import javax.swing.JFrame;
import javax.swing.JSplitPane;
import javax.xml.parsers.ParserConfigurationException;

import net.ysuga.corbanaming.ui.RTSystemTreeView;
import net.ysuga.rtsbuilder.ui.RTSystemBuilderPanel;
import net.ysuga.rtsystem.profile.RTSystemProfile;

/**
 * StateMachinePanelTest.java
 *
 * @author Yuki Suga (ysuga.net)
 * @date 2011/08/07
 * @copyright 2011, ysuga.net allrights reserved.
 *
 */

/**
 * @author ysuga
 * 
 */
public class RTSystemBuilderPanelTest  implements Runnable{

	public RTSystemBuilderPanelTest() {

		new MainFrame();
		Thread thread = new Thread(this);
		thread.start();
	}

	RTSystemBuilderPanel panel;
	RTSystemTreeView view;
	JSplitPane scrollPane;
	public class MainFrame extends JFrame {
		public MainFrame() {
			super("RTSystemBuilder Test");

			try {
				panel = new RTSystemBuilderPanel();
				view = new RTSystemTreeView();
				scrollPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, view, panel);
				super.setContentPane(scrollPane);
			} catch (ParserConfigurationException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
			RTSystemProfile profile = panel.getRTSystemProfile();
			
			super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setSize(500, 500);
			setVisible(true);

		}
	}
	
	public void run() {
		while(true) {
			panel.repaint();
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
		}
	}

	static public void main(String[] args) {
		new RTSystemBuilderPanelTest();
	}
}
