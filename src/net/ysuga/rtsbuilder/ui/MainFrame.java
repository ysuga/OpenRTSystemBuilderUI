/**
 * MainFrame.java
 *
 * @author Yuki Suga (ysuga.net)
 * @date 2011/09/28
 * @copyright 2011, ysuga.net allrights reserved.
 *
 */
package net.ysuga.rtsbuilder.ui;

import java.awt.BorderLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.xml.parsers.ParserConfigurationException;

import net.ysuga.corbanaming.ui.RTSystemTreeView;
import net.ysuga.rtsbuilder.RTSystemBuilder;
import net.ysuga.rtsystem.profile.PyIOComponent;
import net.ysuga.rtsystem.profile.RTComponent;
import net.ysuga.rtsystem.profile.RTSystemProfile;

/**
 * 
 * @author ysuga
 * 
 */
public class MainFrame extends JFrame implements Runnable {

	static Logger logger = Logger.getLogger(MainFrame.class.getName())
			;
	private boolean autoRefresh;

	private long refreshInterval = 1000; // milliseconds

	protected RTSystemBuilderPanel rtSystemBuilderPanel;

	protected RTSystemTreeView rtSystemTreeView;

	protected JSplitPane horizontalSplitPane;

	protected JSplitPane verticalSplitPane;

	protected JTabbedPane tabbedView;

	private Thread refreshThread;

	private MainFrameMouseAdapter mouseAdapter;
	
	private JToolBar toolBar;

	public RTSystemBuilderPanel createRTSystemBuilderPanel()
			throws ParserConfigurationException {
		return new RTSystemBuilderPanel();
	}

	static private MainFrame instance;
	
	static public MainFrame getInstance() {
		return instance;
	}
	
	static public MainFrame start() {
		MainProperty.init();
		instance = new MainFrame();
		return instance;
	}
	
	/**
	 * Constructor
	 * 
	 * @throws HeadlessException
	 */
	MainFrame() throws HeadlessException {
		super("RT System Builder");
		try {
			getContentPane().setLayout(new BorderLayout());
			initMenuBar();
			
			
			rtSystemBuilderPanel = createRTSystemBuilderPanel();
			
			RTSProfileHolder.getInstance().add(new RTSystemProfile(RTSProfileHolder.ONLINE, "defaultVendor", "1.0"));
			
			tabbedView = new JTabbedPane();
			tabbedView.add("Logger", new LoggerView("net.ysuga"));
			rtSystemTreeView = new RTSystemTreeView();
			horizontalSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
					rtSystemTreeView, rtSystemBuilderPanel);
			verticalSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
					horizontalSplitPane, tabbedView);
			getContentPane().add(BorderLayout.CENTER, verticalSplitPane);

			mouseAdapter = new MainFrameMouseAdapter(this);
			rtSystemTreeView.getTree().addMouseListener(mouseAdapter);
			rtSystemTreeView.getTree().addMouseMotionListener(mouseAdapter);
			rtSystemBuilderPanel.addMouseListener(mouseAdapter);
			rtSystemBuilderPanel.addMouseMotionListener(mouseAdapter);

			initToolBar();
			
			setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosing(WindowEvent e) {
					onExit();
					super.windowClosing(e);
				}
			});

		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		RTSystemProfile profile = rtSystemBuilderPanel.getRTSystemProfile();

		refreshThread = new Thread(this);
		autoRefresh = true;

		super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

	@Override
	public void setSize(int w, int h) {
		super.setSize(w, h);
		verticalSplitPane.setDividerLocation((int) (h * 0.6));
	}

	private JMenuBar menuBar;
	
	private void initMenuBar() {
		menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		JMenuItem newMenuItem = new JMenuItem(new AbstractAction("New") {
			public void actionPerformed(ActionEvent e) {
				onNew();
			}
		});
		fileMenu.add(newMenuItem);
		JMenuItem saveMenuItem = new JMenuItem(new AbstractAction("Save") {
			public void actionPerformed(ActionEvent e) {
				onSave();
			}
		});
		fileMenu.add(saveMenuItem);
		JMenuItem saveAsMenuItem = new JMenuItem(new AbstractAction(
				"Save As...") {
			public void actionPerformed(ActionEvent e) {
				onSaveAs();
			}
		});
		fileMenu.add(saveAsMenuItem);
		fileMenu.add(new JSeparator());
		JMenuItem exitMenuItem = new JMenuItem(new AbstractAction("Exit") {
			public void actionPerformed(ActionEvent e) {
				onExit();
			}
		});
		fileMenu.add(exitMenuItem);
		menuBar.add(fileMenu);
		
		JMenu settingMenu = new JMenu("Setting");
		JMenuItem propertyMenu = new JMenuItem(new AbstractAction("Property") {
			public void actionPerformed(ActionEvent e) {
				onProperty();
			}
		});
		settingMenu.add(propertyMenu);
		
		menuBar.add(settingMenu);
		
		this.setJMenuBar(menuBar);
	}

	@Override
	public void setVisible(boolean flag) {
		super.setVisible(flag);
		refreshThread.start();
	}

	/**
	 * inherited from Runnable run
	 */
	public void run() {
		System.out.println("RuN");
		try {
			while (true) {
				
				if (autoRefresh && this.isVisible()) {

					try {
						RTSProfileHolder.getInstance().refreshAll();
						//rtSystemBuilderPanel.refresh();
						rtSystemBuilderPanel.downwardSynchronization();
						rtSystemBuilderPanel.upwardSynchronization();	
					} catch(Exception e) {
						//logger.warning("Exception(" + e.getClass().getName() + ") occured in MainFrame.run()");
					}
					
					rtSystemBuilderPanel.repaint();
					rtSystemTreeView.refresh();
				}
				
				for (RTComponent component : (Set<RTComponent>) this.rtSystemBuilderPanel
						.getRTSystemProfile().componentSet) {
					if ("true".equals(component.get(PyIOComponent.PYIO))) {
						// If there is PyIOComponent
						if (((PyIOComponent) component).getLauncher() != null) {
							if (((PyIOComponent) component).getLauncher()
									.isAlive()) {
								// If there is EXECUTING PyIO compnoent...
								boolean windowIsAlreadyOpenFlag = false;
								String instanceName = ((PyIOComponent) component)
										.get(RTComponent.INSTANCE_NAME);

								java.awt.Component[] comps = this.tabbedView
										.getComponents();
								for (int i = 0; i < comps.length; i++) {
									String title = tabbedView.getTitleAt(i);
									if (instanceName.equals(title)) {
										windowIsAlreadyOpenFlag = true;
									}
								}
								if (!windowIsAlreadyOpenFlag) {
									tabbedView.add(instanceName,
											new PyIOLoggerPanel(
													(PyIOComponent) component));
								}
							} else {
								((PyIOComponent) component).setLauncher(null);
							}
						}
					}
				}

				java.awt.Component[] comps = tabbedView.getComponents();
				for (int i = 0; i < comps.length; i++) {
					String title = tabbedView.getTitleAt(i);
					if (!title.equals("Logger")) {
						boolean viewHasCorrespondingPyIOFlag = false;
						for (RTComponent component : (Set<RTComponent>) this.rtSystemBuilderPanel
								.getRTSystemProfile().componentSet) {

							if (title.equals(component
									.get(RTComponent.INSTANCE_NAME))) {
								if(((PyIOComponent)component).getLauncher() != null) {
									viewHasCorrespondingPyIOFlag = true;
								}
							}
						}
						if (!viewHasCorrespondingPyIOFlag) {
							tabbedView.remove(comps[i]);
						}
					}
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
	 * @param autoRefresh
	 *            set autoRefresh
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
	 * @param refreshInterval
	 *            set refreshInterval
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

	private void onNew() {
		
	}

	private void onSave() {

	}
	
	private void onOpen() {
		
	}

	private void onSaveAs() {

	}

	private void onExit() {

	}
	
	private void onActivateAll() {
		
	}
	
	private void onDeactivateAll() {
		
	}
	
	private void onResetAll() {
		
	}
	
	private void onProperty() {
		MainProperty.showSettingDialog();
	}
	
	private void onDownwardSynchronizationAll() {
		try {
			RTSystemBuilder.downwardSynchronization(rtSystemBuilderPanel.getRTSystemProfile());
		} catch (Exception e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}
	
	private void addButtonToToolbar(BufferedImage image, int offset, AbstractAction action, String toolTip) {
		int width = 16;
		BufferedImage sub = image.getSubimage(width*offset, 0, width, width);
		JButton newButton = new JButton(action);
		newButton.setIcon(new ImageIcon(sub));
		newButton.setFocusable(false);
		newButton.setToolTipText(toolTip);
		toolBar.add(newButton);
	}
	
	public void initToolBar() {
		try {
			toolBar = new JToolBar();
			toolBar.setRollover(true);
			getContentPane().add(BorderLayout.NORTH, toolBar);
			BufferedImage image = ImageIO.read(new File("toolbar_icon.gif"));
			BufferedImage sub = image.getSubimage(0, 0, 16, 16);
			setIconImage(sub);
			
			addButtonToToolbar(image, 1, new AbstractAction("") {
				public void actionPerformed(ActionEvent e) {
					onNew();
				}
			}, "New");
			
			addButtonToToolbar(image, 2, new AbstractAction("") {
				public void actionPerformed(ActionEvent e) {
					onOpen();
				}
			}, "Open");
			
			addButtonToToolbar(image, 3, new AbstractAction("") {
				public void actionPerformed(ActionEvent e) {
					onSave();
				}
			}, "Save");
			
			toolBar.addSeparator();
			
			addButtonToToolbar(image, 4, new AbstractAction("") {
				public void actionPerformed(ActionEvent e) {
					onActivateAll();
				}
			}, "Activate All");
			
			addButtonToToolbar(image, 5, new AbstractAction("") {
				public void actionPerformed(ActionEvent e) {
					onDeactivateAll();
				}
			}, "Deactivate All");	
			
			addButtonToToolbar(image, 6, new AbstractAction("") {
				public void actionPerformed(ActionEvent e) {
					onResetAll();
				}
			}, "Reset All");
			
			addButtonToToolbar(image, 7, new AbstractAction("") {
				public void actionPerformed(ActionEvent e) {
					onDownwardSynchronizationAll();
				}
			}, "Synchronize All");
			
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this,
					"Icon file \"logos.gif\" can not be found.");
			e.printStackTrace();
		}
	}
}
