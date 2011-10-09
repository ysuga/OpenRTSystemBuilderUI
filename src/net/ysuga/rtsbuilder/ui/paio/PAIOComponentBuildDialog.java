/**
 * PAIOComponentCreationDialog.java
 *
 * @author Yuki Suga (ysuga.net)
 * @date 2011/10/07
 * @copyright 2011, ysuga.net allrights reserved.
 *
 */
package net.ysuga.rtsbuilder.ui.paio;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.ysuga.corbanaming.ui.GridLayoutPanel;
import net.ysuga.rtsystem.profile.Component;

/**
 * 
 * @author ysuga
 * 
 */
public class PAIOComponentBuildDialog extends JDialog {

	private JTextField moduleNameField;
	private JTextField moduleDescField;
	private JTextField moduleVendorField;
	private JTextField moduleVersionField;
	private JTextField moduleCategoryField;
	private JTextField moduleCompTypeField;
	private JTextField moduleActTypeField;
	private JTextField moduleMaxInstanceField;

	/**
	 * <div lang="ja"> �R���X�g���N�^
	 * 
	 * @param sourceDataPort
	 * @param dataPort
	 *            </div> <div lang="en"> Constructor
	 * @param sourceDataPort
	 * @param dataPort
	 *            </div>
	 * @throws Exception
	 */
	public PAIOComponentBuildDialog() {
		super();

		moduleNameField = new JTextField("ModuleName");
		moduleDescField = new JTextField("Module Description");
		moduleVendorField = new JTextField("Module Vendor");
		moduleVersionField = new JTextField("1.0");
		moduleCategoryField = new JTextField("Category");
		moduleCompTypeField = new JTextField("DataFlowComponent");
		moduleActTypeField = new JTextField("PERIODIC");
		moduleMaxInstanceField = new JTextField("10");
	}

	private int exitOption = JOptionPane.CANCEL_OPTION;
	private JButton okButton;
	private JPanel contentPane;
	private JComboBox pushIntervalComboBox;
	private JTextField pushIntervalTextField;

	public void onOk(ActionEvent arg0) {
		exitOption = JOptionPane.OK_OPTION;
		setVisible(false);
	}

	public void onCancel() {

		setVisible(false);
	}

	/**
	 * 
	 * 
	 * @return void
	 */
	private void initPanel() {
		okButton = new JButton(new AbstractAction("OK") {
			public void actionPerformed(ActionEvent arg0) {
				onOk(arg0);
			}
		});

		contentPane = (JPanel) getContentPane();
		GridLayoutPanel panel = new GridLayoutPanel();
		contentPane.add(panel, BorderLayout.CENTER);

		int line = 0;
		panel.addComponent(0, line, 0, 0, 1, 1, new JLabel("Name"));
		panel.addComponent(1, line, 10, 0, 3, 1, moduleNameField);
		line++;
		
		panel.addComponent(0, line, 0, 0, 1, 1, new JLabel("Description"));
		panel.addComponent(1, line, 10, 0, 3, 1, moduleDescField);
		line++;
		
		panel.addComponent(0, line, 0, 0, 1, 1, new JLabel("Vendor"));
		panel.addComponent(1, line, 10, 0, 3, 1, moduleVendorField);
		line++;
		
		panel.addComponent(0, line, 0, 0, 1, 1, new JLabel("Version"));
		panel.addComponent(1, line, 10, 0, 3, 1, moduleVersionField);
		line++;
		
		panel.addComponent(0, line, 0, 0, 1, 1, new JLabel("Category"));
		panel.addComponent(1, line, 10, 0, 3, 1, moduleCategoryField);
		line++;
		
		panel.addComponent(0, line, 0, 0, 1, 1, new JLabel("Type"));
		panel.addComponent(1, line, 10, 0, 3, 1, moduleCompTypeField);
		line++;
		
		panel.addComponent(0, line, 0, 0, 1, 1, new JLabel("Activity Type"));
		panel.addComponent(1, line, 10, 0, 3, 1, moduleActTypeField);
		line++;
		
		panel.addComponent(0, line, 0, 0, 1, 1, new JLabel("Max Instance"));
		panel.addComponent(1, line, 10, 0, 3, 1, moduleMaxInstanceField);
		line++;
		
		contentPane.add(okButton, BorderLayout.SOUTH);
		okButton.setRequestFocusEnabled(true);

	}

	public int doModal() {
		initPanel();
		pack();
		setModal(true);
		setVisible(true);
		return exitOption;
	}

	/**
	 * createComponent
	 * 
	 * @return
	 */
	public Component createComponent() {
		
		File rtcDir = new File("rtc/" + moduleNameField.getText());
		int i = 2;
		while(rtcDir.exists()) {
			rtcDir = new File("rtc/" + moduleNameField.getText() + "(" + i + ")");	
			i++;
		}
		rtcDir.mkdirs();

		
		List<String> arg = new ArrayList<String>();
		arg.add("C:/Python26/python");
		arg.add("\"C:/Program Files (x86)/OpenRTM-aist/1.0/utils/rtc-template/rtc-template.py\"");
		arg.add("--backend=python");
		arg.add("--module-name=" + moduleNameField.getText());
		arg.add("--module-desc=\"" + moduleDescField.getText()+"\"");
		arg.add("--module-vendor=\"" + moduleVendorField.getText()+"\"");
		arg.add("--module-version="+moduleVersionField.getText());
		arg.add("--module-comp-type=" + moduleCompTypeField.getText());
		arg.add("--module-act-type=" + moduleActTypeField.getText());
		arg.add("--module-category="+moduleCategoryField.getText());
		arg.add("--module-max-inst="+moduleMaxInstanceField.getText());
		
		ProcessBuilder pb = new ProcessBuilder(arg);
		pb.directory(rtcDir);
		try {
			Process p = pb.start();
			p.waitFor();
			p.destroy();
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}

		File backupFile = new File(rtcDir.getAbsolutePath() + "/"+moduleNameField.getText() + ".bak" + ".py");
		File componentFile = new File(rtcDir.getAbsolutePath() + "/"+moduleNameField.getText() + ".py");
		componentFile.renameTo(backupFile);
		File newComponentFile = new File(rtcDir.getAbsolutePath() + "/"+moduleNameField.getText() + ".py");
		
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(
					new FileInputStream(backupFile)));
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(newComponentFile)));
			
			String str;
			while ((str = br.readLine()) != null) {
				if (str.equals("import OpenRTM")) {
					bw.write("import OpenRTM_aist as OpenRTM\n");
				} else {
					bw.write(str + "\n");
				}
			}
			bw.flush();
			br.close();
			bw.close();
			if(!backupFile.delete()) {
				
				
				System.out.println("Failed to delete.");
			}
		} catch (FileNotFoundException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}

		return null;
	}

}
