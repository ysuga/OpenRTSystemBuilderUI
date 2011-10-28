/**
 * PyIOLoggerPanel.java
 *
 * @author Yuki Suga (ysuga.net)
 * @date 2011/10/12
 * @copyright 2011, ysuga.net allrights reserved.
 *
 */
package net.ysuga.rtsbuilder.ui;

import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;

import net.ysuga.rtsbuilder.RTSystemBuilder;
import net.ysuga.rtsystem.profile.PyIOComponent;

/**
 *
 * @author ysuga
 *
 */
public class PyIOLoggerPanel extends JPanel implements Runnable{
	JScrollPane scrollPane;
	JTextArea jTextArea;
	PyIOComponent component;
	Process process;
	boolean endflag;
	Thread thread;
	
	JToolBar toolBar;
	/**
	 * Constructor
	 * @param component
	 * @throws IOException 
	 */
	public PyIOLoggerPanel(PyIOComponent component) throws IOException {
		super();
		this.component = component;
		process = component.getLauncher().getProcess();
		jTextArea = new JTextArea();
		scrollPane = new JScrollPane(jTextArea);
		this.setLayout(new BorderLayout());
		this.add(BorderLayout.CENTER, scrollPane);
		initToolBar();

		thread = new Thread(this);
		thread.start();
		
		MouseAdapter adapter = new MouseAdapter() {

			/**
			 * mouseClicked
			 * @param e
			 */
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getButton() == MouseEvent.BUTTON3) {
					showPopup(e.getPoint());
				}
				super.mouseClicked(e);
			}
			
		};
		super.addMouseListener(adapter);
		super.addMouseMotionListener(adapter);
	}
	
	public void initToolBar() throws IOException {
		BufferedImage image = ImageIO.read(new File("pyio_icons.gif"));
		BufferedImage sub = image.getSubimage(0, 0, 16, 16);
		
		toolBar = new JToolBar();
		JButton terminate = new JButton(new AbstractAction("") {
			public void actionPerformed(ActionEvent e) {
				try {
					terminatePyIO();
				} catch (Exception e1) {
					// TODO 自動生成された catch ブロック
					e1.printStackTrace();
				}
			}
		});
		terminate.setIcon(new ImageIcon(sub));
		toolBar.add(terminate);
		
		add(BorderLayout.NORTH, toolBar);
	}

	public void run() {
		BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
		BufferedReader er = new BufferedReader(new InputStreamReader(process.getErrorStream()));
		endflag = false;
		while(!endflag) {
			try {
				if(br.ready()) {
					jTextArea.setText(jTextArea.getText() + br.readLine() + "\n");
				}
				if(er.ready()) {
					jTextArea.setText(jTextArea.getText() + er.readLine() + "\n");
				}
			} catch (IOException e) {
				// TODO 自動生成された catch ブロック
				//e.printStackTrace();
			}
			
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
		}
	}
	

	public void showPopup(Point p) {
		JPopupMenu popup = new JPopupMenu();
		JMenuItem terminateMenuItem = new JMenuItem(new AbstractAction("Terminate") {
			public void actionPerformed(ActionEvent e) {
				try {
					terminatePyIO();
				} catch (Exception e1) {
					// TODO 自動生成された catch ブロック
					e1.printStackTrace();
				}
			}
		});
		popup.add(terminateMenuItem);
		
		popup.show(this, p.x, p.y);
	}
	
	public void terminatePyIO() throws Exception {
		RTSystemBuilder.exitComponent(component);
		process.destroy();
		this.component.setLauncher(null);
	}
}
