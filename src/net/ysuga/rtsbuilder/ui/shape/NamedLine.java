package net.ysuga.rtsbuilder.ui.shape;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;


public class NamedLine {
	
	Line2D line;
	Polygon triangle;
	
	StringModelView nameModel;
	String name;
	
	private boolean selected;
	final public boolean isSelected() {
		return selected;
	}
	
	final public void setSelected(boolean b) {
		selected = b;
	}
	
	public class StringModelView  {
		private GlyphVector glyphVector;

		final Rectangle2D rectangle;
		String name;
		/**
		 * @return glyphVector
		 */
		public final GlyphVector getGlyphVector() {
			return glyphVector;
		}
	
		
		public StringModelView(String name, double x, double y) {
			this.name = name;
			
			Font font = new Font(Font.SERIF, 0, 16);
			glyphVector = font.createGlyphVector(new FontRenderContext(
					new AffineTransform(), false, false), name);
			
			Rectangle2D visualBounds = glyphVector.getVisualBounds();
			rectangle = new Rectangle2D.Double(x,
					y,
					visualBounds.getWidth(),
					visualBounds.getHeight());
		}


		public boolean contains(Point p) {
			return rectangle.contains(p);
		}


		public void draw(Graphics2D g) {
			g.drawGlyphVector(glyphVector,
					(float)rectangle.getX(),
					(float)(rectangle.getY()
					 + glyphVector.getVisualBounds().getHeight()));
		}

		public Rectangle getBounds() {
			// TODO �����������ꂽ���\�b�h�E�X�^�u
			return rectangle.getBounds();
		}


		public double getCenterX() {
			return rectangle.getCenterX();
		}


		public double getCenterY() {
			return rectangle.getCenterY();
		}
		
	}
	
	
	private ArrayList<Shape> selectedShapeList;
	
	public ArrayList<Shape> getSelectedShapeList() {
		return selectedShapeList;
	}
	
	public NamedLine(String name, double x1, double y1, double x2, double y2, boolean closed) {
		this.name = name;
		this.closed  = closed;
		
		line = new Line2D.Double(x1, y1, x2, y2);
		
		double dx = x2 - x1;
		double dy = y2 - y1;

		double[] p1 = { -16, 8 };
		double[] p2 = { -16, -8 };
		ShapeEdgeDetector.rotate(p1, Math.atan2(dy, dx));
		ShapeEdgeDetector.rotate(p2, Math.atan2(dy, dx));

		int[] xpoints = new int[3];
		xpoints[0] = (int)x2;
		xpoints[1] = (int)(x2 + p1[0]);
		xpoints[2] = (int)(x2 + p2[0]);

		int[] ypoints = new int[3];
		ypoints[0] = (int)(y2);
		ypoints[1] = (int)(y2 + p1[1]);
		ypoints[2] = (int)(y2 + p2[1]);
		triangle = new Polygon(xpoints, ypoints, 3);
		
		
		selectedShapeList = new ArrayList<Shape>();
		double w = 6;
		selectedShapeList.add(new Rectangle2D.Double(x1-w/2, y1-w/2, w, w));
		selectedShapeList.add(new Rectangle2D.Double(x2-w/2, y2-w/2, w, w));
		
		double x = getCenterX();
		double y = getCenterY()+ 10;
		
		this.nameModel = new StringModelView(name, x, y);
	}

	public boolean contains(Point p) {
		if(nameModel.contains(p)) {
			return true;
		}
		
		double w = 10;
		double x = p.x;
		double y = p.y;
		double x1 = line.getX1();
		double y1 = line.getY1();
		double x2 = line.getX2();
		double y2 = line.getY2();

		if (x > x1+w && x > x2+w)  {
			return false;
		} else if(x < x1-w && x < x2-w) {
			return false;
		}
		
		if (y > y1+w && y > y2+w) {
			return false;
		} else if(y < y1-w && y < y2-w) {
			return false;
		}
		
		double a = (y2 - y1);
		double b = (x1 - x2);
		double c = x2 * y1 - x1 * y2;
		double d = Math.abs(a * x + b * y + c) / Math.sqrt(a * a + b * b);

		if (d <= w)
			return true;
		return false;	
	}

	boolean closed = false;
	
	private Color lineColor = Color.black;
	/**
	 * @return lineColor
	 */
	public final Color getLineColor() {
		return lineColor;
	}

	/**
	 * @param lineColor �Z�b�g���� lineColor
	 */
	public final void setLineColor(Color lineColor) {
		this.lineColor = lineColor;
	}

	/**
	 * @return textColor
	 */
	public final Color getTextColor() {
		return textColor;
	}

	/**
	 * @param textColor �Z�b�g���� textColor
	 */
	public final void setTextColor(Color textColor) {
		this.textColor = textColor;
	}

	private Color textColor = Color.black;
	
	public void draw(Graphics2D g) {
		Color oldColor = g.getColor();
		
		g.setColor(lineColor);
		g.draw(line);
		if(isSelected()) {
			for(Shape selectedShape : getSelectedShapeList()) {
				g.draw(selectedShape);
			}
		}
		/**
		if(closed) {
			g.fill(triangle);
		} else {
			g.drawLine(triangle.xpoints[0], triangle.ypoints[0], triangle.xpoints[1], triangle.ypoints[1]);
			g.drawLine(triangle.xpoints[0], triangle.ypoints[0], triangle.xpoints[2], triangle.ypoints[2]);
		}
		*/
		g.setColor(textColor);
		nameModel.draw(g);
		
		g.setColor(oldColor);

	}


	public double getCenterX() {
		return (line.getX2() + line.getX1())/2;
	}

	public double getCenterY() {
		return (line.getY2() + line.getY1())/2;
	}
	
	public void setTextLocation(int x, int y) {
		this.nameModel = new StringModelView(name, x, y);
	}
}
