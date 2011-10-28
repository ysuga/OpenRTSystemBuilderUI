/**
 * PyIOEditorPane.java
 *
 * @author Yuki Suga (ysuga.net)
 * @date 2011/10/09
 * @copyright 2011, ysuga.net allrights reserved.
 *
 */
package net.ysuga.rtsbuilder.ui.pyio.editor;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.io.IOException;

import javax.swing.JEditorPane;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;

/**
 *
 * @author ysuga
 *
 */
public class PyIOEditorPane extends JEditorPane {

	public PyIOEditorPane() {
		super();

		Font font = new Font(Font.MONOSPACED, Font.PLAIN, 12);
		this.setFont(font);
		
		Document doc = getDocument();
		if (doc instanceof PlainDocument) {
		    doc.putProperty(PlainDocument.tabSizeAttribute, 4);
		}
		
		this.setMinimumSize(new Dimension(100, 100));
	}
	
	
	public PyIOEditorPane(String text) throws IOException {
		this();
	}


	/**
	 * addMethodCode
	 *
	 * @param methodCode
	 * @param point
	 */
	public void addMethodCode(String methodCode, Point point) {
		int offset = this.viewToModel(point);
		StringBuilder strbuf = new StringBuilder( super.getText());
		strbuf.insert(offset, "Hello\n");
		setText(strbuf.toString());
	
	}
}
