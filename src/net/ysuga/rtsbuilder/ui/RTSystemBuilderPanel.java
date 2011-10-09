/**
 * StateMachinePanel.java
 *
 * @author Yuki Suga (ysuga.net)
 * @date 2011/08/07
 * @copyright 2011, ysuga.net allrights reserved.
 *
 */
package net.ysuga.rtsbuilder.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.xml.parsers.ParserConfigurationException;

import net.ysuga.rtsbuilder.RTSystemBuilder;
import net.ysuga.rtsbuilder.ui.pyio.PAIOComponentCreationDialog;
import net.ysuga.rtsbuilder.ui.shape.ComponentPopupMenu;
import net.ysuga.rtsbuilder.ui.shape.ConnectorShape;
import net.ysuga.rtsbuilder.ui.shape.RTSystemShape;
import net.ysuga.rtsbuilder.ui.shape.RTSystemShapeBuilder;
import net.ysuga.rtsystem.profile.Component;
import net.ysuga.rtsystem.profile.DataPort;
import net.ysuga.rtsystem.profile.PortConnector;
import net.ysuga.rtsystem.profile.RTSObject;
import net.ysuga.rtsystem.profile.RTSystemProfile;

import org.xml.sax.SAXException;

/**
 * 
 * <div lang="ja"> StateMachine�N���X��\������ъǗ����邽�߂̃p�l���N���X�D
 * 
 * </div> <div lang="en"> Panel Class for controlling StateMachine class </div>
 * 
 * @author ysuga
 * 
 * @see StateMachine
 */
public class RTSystemBuilderPanel extends JPanel {

	public final static int EDIT_NORMAL = 0;
	public final static int EDIT_TRANSITION = 1;
	public static final int EDIT_CONNECTION = 2;

	public int editMode = EDIT_NORMAL;

	
	public int getEditMode() {
		return editMode;
	}

	public void setEditMode(int mode) {
		editMode = mode;
	}

	/**
	 * File Extention of FSM
	 */
	// public static final String FSM = "fsm";

	/**
	 * StateMachine class object.
	 */
	private RTSystemProfile rtSystemProfile;

	// private //StateMachineExecutionThread stateMachineExecutionThread;

	/**
	 * 
	 * <div lang="ja"> StateMachine�̎擾
	 * 
	 * @return Panel���ێ�����StateMachine </div> <div lang="en"> getter for
	 *         StateMachine owned by this panel.
	 * @return </div>
	 */
	public RTSystemProfile getRTSystemProfile() {
		return rtSystemProfile;
	}

	/**
	 * StateMachineShape. if repainted, automatically re-constructed. Do not use
	 * to store any datas in this object because it will be destroyed and
	 * reconstructed if repainted.
	 */
	private RTSystemShape rtSystemShape;

	/**
	 * 
	 * getStateMachineShape <div lang="ja">
	 * 
	 * @return </div> <div lang="en"> getter function for StateMachine shape
	 * @return </div>
	 */
	public RTSystemShape getRTSystemShape() {
		return rtSystemShape;
	}

	/**
	 * selected state (state can be selected by clicking)
	 */
	private RTSObject selectedRTSObject;

	/**
	 * 
	 * getSelectedState <div lang="ja"> �I�𒆂�State�̎擾
	 * 
	 * @return �I�𒆂�State.�Ȃ����null </div> <div lang="en"> getter function for
	 *         selected state
	 * @return State object. If not selected, null will be returned. </div>
	 */
	final public RTSObject getSelectedRTSObject() {
		return selectedRTSObject;
	}

	/**
	 * 
	 * setSelectedState <div lang="ja"> �I�𒆂�State�̐ݒ�D�����I�ɑI�𒆂�Transition��null�ɂȂ�D
	 * 
	 * @param state
	 *            </div> <div lang="en"> Setting selected state. Selected
	 *            Transition will be null automatically.
	 * @param state
	 *            </div>
	 */
	final public void setSelectedRTSObjectt(RTSObject comp) {
		selectedConnector = null;
		selectedRTSObject = comp;
	}

	/**
	 * Selected Transition
	 */
	private PortConnector selectedConnector;

	/**
	 * 
	 * getSelectedTransition <div lang="ja">
	 * 
	 * @return </div> <div lang="en"> getter function for selected transition
	 *         object.
	 * @return selected transition object. If no transition is selected, null
	 *         will be returned. </div>
	 */
	public PortConnector getSelectedConnector() {
		return selectedConnector;
	}

	/**
	 * <div lang="ja">
	 * 
	 * @param transition
	 *            </div> <div lang="en">
	 * 
	 * @param transition
	 *            </div>
	 */
	public void setSelectedConnector(PortConnector connector) {
		selectedConnector = connector;
		selectedRTSObject = null;
	}

	/**
	 * Selected Pivot
	 */
	private Point selectedPivot;

	public void setSelectedPivot(Point p) {
		selectedPivot = p;
	}

	public Point getSelectedPivot() {
		return selectedPivot;
	}

	//private ComponentPopupMenu componentPopupMenu;

	//public ComponentPopupMenu getRTComponentPopupMenu() {
	//	return componentPopupMenu;
	//}

	private RTSystemPanelPopupMenu rtSystemPanelPopupMenu;
	private ConnectorPopupMenu connectorPopupMenu;
	private File currentFile;

	/**
	 * 
	 * <div lang="ja"> �R���X�g���N�^
	 * 
	 * @param stateMachine
	 *            </div> <div lang="en"> Constructor
	 * @param stateMachine
	 *            </div>
	 * @throws ParserConfigurationException
	 */
	public RTSystemBuilderPanel() throws ParserConfigurationException {
		this.rtSystemProfile = createRTSystemProfile("new state machine");
		this.rtSystemShape = buildRTSystemShape(rtSystemProfile);

		RTSystemPanelMouseAdapter adapter = new RTSystemPanelMouseAdapter(this);
		super.addMouseListener(adapter);
		super.addMouseMotionListener(adapter);

		//this.componentPopupMenu = new ComponentPopupMenu(this);
		this.connectorPopupMenu = new ConnectorPopupMenu(this);
		this.rtSystemPanelPopupMenu = new RTSystemPanelPopupMenu(this);
		
		/**
		 * GuardSettingDialogFactoryManager .add(new
		 * AndGuardSettingDialogFactory()); GuardSettingDialogFactoryManager
		 * .add(new ExorGuardSettingDialogFactory());
		 * GuardSettingDialogFactoryManager.add(new
		 * OrGuardSettingDialogFactory()); GuardSettingDialogFactoryManager
		 * .add(new NotGuardSettingDialogFactory());
		 * 
		 * GuardSettingDialogFactoryManager .add(new
		 * DelayGuardSettingDialogFactory()); GuardSettingDialogFactoryManager
		 * .add(new NullGuardSettingDialogFactory());
		 */
	}

	/**
	 * createRTSystemProfile <div lang="ja">
	 * 
	 * @param string
	 * @return </div> <div lang="en">
	 * 
	 * @param string
	 * @return </div>
	 */
	public RTSystemProfile createRTSystemProfile(String string) {
		return new RTSystemProfile(string, "default vendor", "default version");
	}

	public RTSystemShape buildRTSystemShape(RTSystemProfile rtSystemProfile) {
		
		return new RTSystemShapeBuilder().buildRTSystemShape(rtSystemProfile);
	}
	/**
	 * 
	 * <div lang="ja">
	 * 
	 * @param g
	 *            </div> <div lang="en">
	 * @param g
	 *            </div>
	 */
	@Override
	public void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		Dimension d = this.getSize();
		Rectangle2D.Double r = new Rectangle2D.Double(0, 0, d.width, d.height);
		g2d.setColor(Color.white);
		g2d.fill(r);
		g2d.setColor(Color.black);

		this.rtSystemShape = buildRTSystemShape(rtSystemProfile);
		rtSystemShape.setSelectedComponent(selectedRTSObject);
		rtSystemShape.setSelectedConnector(selectedConnector);
		rtSystemShape.setSelectedDataPort(selectedDataPort);
		rtSystemShape.draw(g);

		if (getEditMode() == EDIT_CONNECTION && mousePosition != null) {
			DataPort dataPort = getSelectedDataPort();
			if (dataPort != null) {
				Point p = rtSystemShape.getSelectedDataPortShape().getCenterPoint();
				g2d.drawLine(p.x, p.y, mousePosition.x, mousePosition.y);
			}
		}
	}

	/**
	 * <div lang="ja">
	 * 
	 * @return </div> <div lang="en">
	 * 
	 * @return </div>
	 */
	public RTSystemPanelPopupMenu getPopupMenu() {
		return rtSystemPanelPopupMenu;
	}

	/**
	 * <div lang="ja">
	 * 
	 * @return </div> <div lang="en">
	 * 
	 * @return </div>
	 */
	public ConnectorPopupMenu getConnectorPopupMenu() {
		return connectorPopupMenu;
	}

	class FSMFileFilter extends javax.swing.filechooser.FileFilter {
		public boolean accept(java.io.File f) {
			if (f.isDirectory())
				return true;
			String name = f.getName().toLowerCase();
			if (name.endsWith("xml"))
				return true;
			return false;
		}

		public java.lang.String getDescription() {
			return "*." + "xml";
		}
	}

	private final File openOpenFileDialog() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fileChooser.setDialogTitle("Open file");
		fileChooser.setFileFilter(new FSMFileFilter());
		int ret = fileChooser.showOpenDialog(this);
		if (ret != JFileChooser.APPROVE_OPTION) {
			return null;
		}
		File file = fileChooser.getSelectedFile();
		if (!file.exists()) {
			JOptionPane.showMessageDialog(this, "File not exists.");
			return null;
		}
		if (!file.getName().endsWith("xml")) {
			JOptionPane.showMessageDialog(this, "File is not available.");
			return null;
		}

		RTSystemProfile oldRTSystemProfile = this.rtSystemProfile;
		try {
			this.rtSystemProfile = createRTSystemProfile(file);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Invalid FSM file.");
			this.rtSystemProfile = oldRTSystemProfile;
			e.printStackTrace();
			return null;
		}
		repaint();
		return file;
	}

	/**
	 * createRTSystemProfile <div lang="ja">
	 * 
	 * @param file
	 * @return </div> <div lang="en">
	 * 
	 * @param file
	 * @return </div>
	 * @throws IOException
	 * @throws SAXException
	 * @throws ParserConfigurationException
	 */
	private RTSystemProfile createRTSystemProfile(File file)
			throws ParserConfigurationException, SAXException, IOException {
		return new RTSystemProfile(file);
	}

	public final void showOpenFileDialog() {
		File file = openOpenFileDialog();
		if (file == null) {
			JOptionPane.showMessageDialog(this, "Open is canceled.");
		} else {
			JOptionPane.showMessageDialog(this, "Open is succeeded.");
			this.currentFile = file;
		}
	}

	public final void showSaveFileDialog() {
		File file = openSaveFileDialog();
		if (file == null) {
			JOptionPane.showMessageDialog(this, "Save is canceled.");
		} else {
			JOptionPane.showMessageDialog(this, "Save is succeeded.");
			this.currentFile = file;
		}
	}

	public final File openSaveFileDialog() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fileChooser.setDialogTitle("Select File");
		fileChooser.setFileFilter(new FSMFileFilter());
		if (fileChooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) {
			return null;
		}
		File file = fileChooser.getSelectedFile();
		if (!file.getName().endsWith("xml")) {
			file = new File(file.getAbsolutePath() + "." + "xml");
		}

		if (file.exists()) {
			if (JOptionPane.showConfirmDialog(this, "Overwrite?") == JOptionPane.NO_OPTION) {
				return null;
			}
		}

		try {
			this.rtSystemProfile.save(file);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Failed to save.");
			e.printStackTrace();
			return null;
		}
		return file;
	}

	/**
	 * createNewStateMachine <div lang="ja">
	 * 
	 * </div> <div lang="en">
	 * 
	 * </div>
	 */
	public void createNewRTSystemProfile() {

		String name = JOptionPane.showInputDialog(this,
				"Input New State Machine Name");
		if (name != null) {
			rtSystemProfile = createRTSystemProfile("new state machine");
		} else {
			JOptionPane.showMessageDialog(this,
					"Creating New State Machine is canceled.");
		}
		repaint();
	}

	/**
	 * start <div lang="ja">
	 * 
	 * </div> <div lang="en">
	 * 
	 * </div>
	 */
	public void start() {
		// stateMachineExecutionThread = new
		// StateMachineExecutionThread(stateMachine);
		// stateMachineExecutionThread.startExecution();
	}

	/**
	 * stop <div lang="ja">
	 * 
	 * </div> <div lang="en">
	 * 
	 * </div>
	 */
	public void stop() {
		// stateMachineExecutionThread.stopExecution();
		// stateMachineExecutionThread = null;
		// stateMachine.reset();
	}

	/**
	 * suspend <div lang="ja">
	 * 
	 * </div> <div lang="en">
	 * 
	 * </div>
	 */
	public void suspend() {
		// stateMachineExecutionThread.suspendExecution();
	}

	/**
	 * resume <div lang="ja">
	 * 
	 * </div> <div lang="en">
	 * 
	 * </div>
	 */
	public void resume() {
		// stateMachineExecutionThread.resumeExecution();
	}

	public boolean isSuspend() {
		return false;
		// return stateMachineExecutionThread.isSuspend();
	}

	private Point mousePosition;

	/**
	 * setMousePosition <div lang="ja">
	 * 
	 * @param point
	 *            </div> <div lang="en">
	 * 
	 * @param point
	 *            </div>
	 */
	public void setMousePosition(Point point) {
		mousePosition = point;
	}

	/**
	 * save <div lang="ja">
	 * 
	 * </div> <div lang="en">
	 * 
	 * </div>
	 */
	public void save() {
		if (currentFile == null) {
			showSaveFileDialog();
		} else {
			try {
				this.rtSystemProfile.save(currentFile);
			} catch (Exception e) {
				JOptionPane.showMessageDialog(this, "Failed to save.");
				e.printStackTrace();
				JOptionPane.showMessageDialog(this, "Fail to save to file.");
			}
		}
	}

	/**
	 * connect <div lang="ja">
	 * 
	 * </div> <div lang="en">
	 * 
	 * </div>
	 */
	public void refresh() {
		try {
			RTSystemBuilder.searchRTCs(this.rtSystemProfile);
			RTSystemBuilder.searchConnections(this.rtSystemProfile);
			for(ConnectorShape shape : rtSystemShape.connectorShapeList) {
				shape.autoPivot();
			}
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "Connecting was failed.");
		}
	}
	
	

	/**
	 * activate <div lang="ja">
	 * 
	 * </div> <div lang="en">
	 * 
	 * </div>
	 */
	public void activate() {
		try {
			RTSystemBuilder.activateRTCs(this.rtSystemProfile);
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "Connecting was failed.");
		}
	}

	/**
	 * deactivate <div lang="ja">
	 * 
	 * </div> <div lang="en">
	 * 
	 * </div>
	 */
	public void deactivate() {
		try {
			RTSystemBuilder.deactivateRTCs(this.rtSystemProfile);
		} catch (Exception ex) {

			JOptionPane.showMessageDialog(this, "Connecting was failed.");
		}
	}

	/**
	 * reset <div lang="ja">
	 * 
	 * </div> <div lang="en">
	 * 
	 * </div>
	 */
	public void reset() {
		try {
			RTSystemBuilder.resetRTCs(this.rtSystemProfile);
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "Connecting was failed.");
		}
		repaint();
	}

	/**
	 * restoreConnection
	 * <div lang="ja">
	 * 
	 * </div>
	 * <div lang="en">
	 *
	 * </div>
	 */
	public void restoreConnection() {
		try {
			RTSystemBuilder.buildConnection(this.rtSystemProfile);
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "Connecting was failed.");
		}
		repaint();
	}

	private DataPort selectedDataPort;
	/**
	 * setSelectedPort
	 * <div lang="ja">
	 * 
	 * @param dataPort
	 * </div>
	 * <div lang="en">
	 *
	 * @param dataPort
	 * </div>
	 */
	public void setSelectedPort(DataPort dataPort) {
		selectedDataPort = dataPort;
	}
	
	public DataPort getSelectedDataPort() {
		return selectedDataPort;
	}

	/**
	 * addRTComponentOnEditor
	 *
	 * @param fullPath
	 * @param point
	 * @throws Exception 
	 */
	public void addRTComponentOnEditor(String fullPath, Point point) throws Exception {
		Component component = RTSystemBuilder.createComponent(fullPath);
		component.setLocation(point);
		this.rtSystemProfile.addComponent(component);
		RTSystemBuilder.findComponent(component);
	}

	/**
	 * addComponent
	 * @param point 
	 *
	 */
	public void addComponent(Point point) {
		PAIOComponentCreationDialog dialog = new PAIOComponentCreationDialog();
		if(dialog.doModal() == JOptionPane.OK_OPTION) {
			try {
				Component component = dialog.createComponent();
				component.setLocation(point);
				this.getRTSystemProfile().addComponent(component);
				refresh();
			} catch (IOException e) {
				e.printStackTrace();
			} 
		}
	}
}
