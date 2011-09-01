/**
 * StateMachinePopupMenu.java
 *
 * @author Yuki Suga (ysuga.net)
 * @date 2011/08/09
 * @copyright 2011, ysuga.net allrights reserved.
 *
 */
package net.ysuga.rtsbuilder.ui;

import java.awt.Component;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;


/**
 * 
 * <div lang="ja">
 * StateMachinePanelクラス用のポップアップメニューおよびその処理．
 * 
 * ポップアップ関連の処理はできるだけここに記述するようにしています．
 * </div>
 * <div lang="en">
 *
 * </div>
 * @author ysuga
 *
 */
public class RTSystemPanelPopupMenu {

	/**
	 * ポップアップメニュー本体
	 */
	private JPopupMenu popupMenu;
	
	/**
	 * パネル本体
	 */
	private RTSystemBuilderPanel panel;
	
	/**
	 * メニューの位置を保存しておくバッファ
	 */
	private Point location;
	
	/**
	 * 
	 * <div lang="ja">
	 * メニューの位置の取得
	 * @return メニューの表示された位置
	 * </div>
	 * <div lang="en">
	 *
	 * @return
	 * </div>
	 */
	public Point getLocation() {
		return location;
	}
	
	/**
	 * 新規ステート追加のためのアクションクラス
	 * @author ysuga
	 *
	 */
	class AddNewStateAction extends AbstractAction {
		private String kind;
		public AddNewStateAction(String title, String kind) {
			super(title);
			this.kind = kind;
		}
		
		public void actionPerformed(ActionEvent arg0) {
			/**
			ComponentSettingDialogFactory factory = ComponentSettingDialogFactoryManager.getInstance().get(kind);
			AbstractComponentSettingDialog dialog = factory.createStateSettingDialog(panel, null);
			if(dialog.doModal() == AbstractStateSettingDialog.OK_OPTION) {
				State state = dialog.buildState();
				try {
					state.setLocation(getLocation());
					panel.getStateMachine().add(state);
					panel.repaint();
				} catch (InvalidStateNameException e) {
					JOptionPane.showMessageDialog(null, (Object)"Invalid State Name", "Exception", JOptionPane.OK_OPTION);
				}
			}
			*/
		}	
	}
	
	private JMenuItem buildConnectionMenuItem;
	private JMenuItem refreshMenuItem;
	private JMenuItem activateMenuItem; 
	private JMenuItem deactivateMenuItem; 
	private JMenuItem resetMenuItem; 
	private JMenuItem newMenuItem;
	private JMenuItem openMenuItem;
	private JMenuItem saveMenuItem;
	private JMenuItem saveAsMenuItem;
	/**
	 * <div lang="ja">
	 * コンストラクタ
	 * </div>
	 * <div lang="en">
	 * Constructor
	 * </div>
	 */
	public RTSystemPanelPopupMenu(final RTSystemBuilderPanel panel) {
		this.panel = panel;
	}
	
	
	
	/**
	 * <div lang="ja">
	 *
	 * @param point
	 * </div>
	 * <div lang="en">
	 *
	 * @param point
	 * </div>
	 */
	public void show(Component component, Point point) {
		popupMenu = new JPopupMenu();
		refreshMenuItem = new JMenuItem(new AbstractAction("Refresh All") {
			public void actionPerformed(ActionEvent e) {
				onRefresh();
			}
		});
		popupMenu.add(refreshMenuItem);
		popupMenu.add(new JSeparator());
		
		buildConnectionMenuItem = new JMenuItem(new AbstractAction("Restore All Connection") {
			public void actionPerformed(ActionEvent e) {
				onRestoreConnection();
			}
		});
		popupMenu.add(buildConnectionMenuItem);

		activateMenuItem = new JMenuItem(new AbstractAction("All Activate") {
			public void actionPerformed(ActionEvent e) {
				onActivate();
			}
		});
		popupMenu.add(activateMenuItem);
		
		deactivateMenuItem = new JMenuItem(new AbstractAction("All Deactivate") {
			public void actionPerformed(ActionEvent e) {
				onDeactivate();
			}
		});
		popupMenu.add(deactivateMenuItem);
		
		resetMenuItem = new JMenuItem(new AbstractAction("All Reset") {
			public void actionPerformed(ActionEvent e) {
				onReset();
			}
		});
		popupMenu.add(resetMenuItem);
		
		/*

		
		if(panel.getRTSystemProfile().getExecutionState() != RTSystem.HALT) {
			startMenuItem.setEnabled(false);
		}
		
		suspendMenuItem = new JMenuItem(new AbstractAction("Suspend") {
			public void actionPerformed(ActionEvent e) {
				onSuspend();
			}
		});
		
		if(panel.getStateMachine().getExecutionState() != StateMachine.OPERATING) {
			suspendMenuItem.setEnabled(false);
		}
		resumeMenuItem = new JMenuItem(new AbstractAction("Resume") {
			public void actionPerformed(ActionEvent e) {
				onResume();
			}

		});
		if(panel.getStateMachine().getExecutionState() != StateMachine.SUSPEND) {
			resumeMenuItem.setEnabled(false);
		}
		
		stopMenuItem = new JMenuItem(new AbstractAction("Stop") {
			public void actionPerformed(ActionEvent e) {
				onStop();
			}

		});
		if(panel.getStateMachine().getExecutionState() != StateMachine.OPERATING) {
			stopMenuItem.setEnabled(false);
		}
		
		popupMenu.add(startMenuItem);
		popupMenu.add(suspendMenuItem);
		popupMenu.add(resumeMenuItem);
		popupMenu.add(stopMenuItem);

		popupMenu.add(new JSeparator());
		*/
		
		popupMenu.add(new JSeparator());
		JMenuItem addComponentMenuItem = new JMenuItem(new AbstractAction("Add Component") {
			public void actionPerformed(ActionEvent e) {
				/*
				Component component = new StartState();
				try {
					state.setLocation(getLocation());
					panel.getStateMachine().add(state);
					panel.repaint();
				} catch (InvalidStateNameException ex) {
					JOptionPane.showMessageDialog(null, (Object)"Invalid State Name", "Exception", JOptionPane.OK_OPTION);
				}
				*/
			}
		});
		/*
		if(panel.getStateMachine().getState(StateMachineTagNames.START) != null) {
			addStartMenuItem.setEnabled(false);
		}
		*/
		popupMenu.add(addComponentMenuItem);
		
		/*
		JMenuItem addExitMenuItem = new JMenuItem(new AbstractAction("Add Exit State") {
			public void actionPerformed(ActionEvent e) {
				State state = new ExitState();
				try {
					state.setLocation(getLocation());
					panel.getStateMachine().add(state);
					panel.repaint();
				} catch (InvalidStateNameException ex) {
					JOptionPane.showMessageDialog(null, (Object)"Invalid State Name", "Exception", JOptionPane.OK_OPTION);
				}
			}
		});
		if(panel.getStateMachine().getState(StateMachineTagNames.EXIT) != null) {
			addExitMenuItem.setEnabled(false);
		}
		popupMenu.add(addExitMenuItem);
		*/
		/**
		Set<String> kindSet = StateSettingDialogFactoryManager.getInstance().getKindList();
		for(String kind : kindSet) {
			JMenuItem addNewMenuItem = new JMenuItem(
					new AddNewStateAction("Add New State(" + kind + ")", kind));
			popupMenu.add(addNewMenuItem);
		}
		*/
		popupMenu.add(new JSeparator());
		
		newMenuItem = new JMenuItem(new AbstractAction("New") {
			public void actionPerformed(ActionEvent e) {
				onNew();
			}
		});

		openMenuItem = new JMenuItem(new AbstractAction("Open") {
			public void actionPerformed(ActionEvent e) {
				onOpen();
			}
		});
		saveMenuItem = new JMenuItem(new AbstractAction("Save"){
			public void actionPerformed(ActionEvent e) {
				onSave();
			}
		});
		saveAsMenuItem = new JMenuItem(new AbstractAction("Save As..."){
			public void actionPerformed(ActionEvent e) {
				onSaveAs();
			}
		});
		popupMenu.add(newMenuItem);
		popupMenu.add(openMenuItem);
		popupMenu.add(saveMenuItem);
		popupMenu.add(saveAsMenuItem);
				
		location = point;
		popupMenu.show(component, point.x, point.y);
	}

	/**
	 * 
	 * onNew
	 * <div lang="ja">
	 * 
	 * </div>
	 * <div lang="en">
	 *
	 * </div>
	 */
	public void onNew() {
		panel.createRTSystemProfile("");
	}
	
	/**
	 * 
	 * open
	 * <div lang="ja">
	 * ファイル選択ダイアログを表示させて，StateMachineを開きます．
	 * </div>
	 * <div lang="en">
	 *
	 * </div>
	 */
	public void onOpen() {
		panel.showOpenFileDialog();
	}
	
	public void onSave() {
		panel.save();
	}
	
	public void onSaveAs() {
		panel.showSaveFileDialog();
	}
	
	public void onRefresh() {
		panel.refresh();
	}
	
	public void onActivate() {
		panel.activate();
	}
	
	public void onDeactivate() {
		panel.deactivate();
	}
	
	public void onReset() {
		panel.reset();
	}
	
	private void onRestoreConnection() {
		panel.restoreConnection();
	}
}
