/**
 * MainFrame.java
 *
 * @author Yuki Suga (ysuga.net)
 * @date 2011/09/28
 * @copyright 2011, ysuga.net allrights reserved.
 *
 */
package net.ysuga.rtsbuilder.ui;

import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;

import javax.swing.JFrame;
import javax.swing.JSplitPane;
import javax.xml.parsers.ParserConfigurationException;

import net.ysuga.corbanaming.ui.RTSystemTreeView;
import net.ysuga.rtsystem.profile.RTSystemProfile;

/**
 * 
 * @author ysuga
 * 
 */
public class MainFrame extends JFrame implements Runnable {

	private boolean autoRefresh;

	private long refreshInterval = 500; // milliseconds

	private RTSystemBuilderPanel rtSystemBuilderPanel;
	
	private RTSystemTreeView rtSystemTreeView;
	
	private JSplitPane scrollPane;

	private Thread refreshThread;

	private MainFrameMouseAdapter mouseAdapter;
	/**
	 * Constructor
	 * 
	 * @throws HeadlessException
	 */
	public MainFrame() throws HeadlessException {
		super("RT System Builder");
		try {
			rtSystemBuilderPanel = new RTSystemBuilderPanel();
			rtSystemTreeView = new RTSystemTreeView();
			scrollPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, rtSystemTreeView,
					rtSystemBuilderPanel);
			super.setContentPane(scrollPane);
			mouseAdapter = new MainFrameMouseAdapter(this);
			rtSystemTreeView.getTree().addMouseListener(mouseAdapter);
			rtSystemTreeView.getTree().addMouseMotionListener(mouseAdapter);
			rtSystemBuilderPanel.addMouseListener(mouseAdapter);
			rtSystemBuilderPanel.addMouseMotionListener(mouseAdapter);
			
		} catch (ParserConfigurationException e) {
			// TODO �����������ꂽ catch �u���b�N
			e.printStackTrace();
		}
		RTSystemProfile profile = rtSystemBuilderPanel.getRTSystemProfile();

		refreshThread = new Thread(this);
		refreshThread.start();

		super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(500, 500);
		setVisible(true);
	}

	/**
	 * inherited from Runnable
	 * run
	 */
	public void run() {
		try {
			while (true) {
				if (autoRefresh) {
					rtSystemBuilderPanel.repaint();
				}
				try {
					Thread.sleep(refreshInterval);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

		} catch (Exception e) {
			System.out.println("Exception occurred in refreshing routine:");
			e.printStackTrace();
		}
	}

	/**
	 * @return autoRefresh
	 */
	public final boolean isAutoRefresh() {
		return autoRefresh;
	}

	/**
	 * @param autoRefresh set autoRefresh
	 */
	public final void setAutoRefresh(boolean autoRefresh) {
		this.autoRefresh = autoRefresh;
	}

	/**
	 * @return refreshInterval
	 */
	public final long getRefreshInterval() {
		return refreshInterval;
	}

	/**
	 * @param refreshInterval set refreshInterval
	 */
	public final void setRefreshInterval(long refreshInterval) {
		this.refreshInterval = refreshInterval;
	}

	/**
	 * @return rtSystemBuilderPanel
	 */
	public final RTSystemBuilderPanel getRtSystemBuilderPanel() {
		return rtSystemBuilderPanel;
	}

	/**
	 * @return rtSystemTreeView
	 */
	public final RTSystemTreeView getRtSystemTreeView() {
		return rtSystemTreeView;
	}
}
