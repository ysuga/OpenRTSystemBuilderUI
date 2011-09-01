package net.ysuga.rtsbuilder.ui.shape;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import net.ysuga.rtsystem.profile.PivotList;

public class PivottedNamedArrow {
	
	private PivotList pivotList;
	private NamedLine namedArrow;
	
	public void setSelected(boolean b) {
		namedArrow.setSelected(b);
	}
	
	public boolean isSelected() {
		return namedArrow.isSelected();
	}
	
	private Point startPoint;
	
	public Point getStartPoint() {
		return startPoint;
	}
	
	
	private Point endPoint;
	
	public Point getEndPoint() {
		return endPoint;
	}
	

	public PivottedNamedArrow(String name, Point startPoint, Point endPoint, PivotList pivotList) {
		this.startPoint = startPoint;
		this.endPoint   = endPoint;
		if(pivotList == null) {
			this.pivotList = new PivotList();
		} else {
			this.pivotList = pivotList;
		}
		
		if(pivotList.size() != 0) {
			Point p = pivotList.get(pivotList.size()-1);
			namedArrow = new NamedLine(name, p.x, p.y, endPoint.x, endPoint.y, closed);
		}else {
			
			namedArrow = new NamedLine(name, startPoint.x, startPoint.y, endPoint.x, endPoint.y, closed);
		}		
	}

	
	static final public boolean lineContains(int x1, int y1, int x2, int y2, Point p) {
		double w = 10;
		double x = p.x;
		double y = p.y;
		
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
	
	
	/**
	 * 最初のPivotを返す．
	 * Pivotをひとつも持っていなければ，終点を返すので注意．
	 * @return
	 * @return Point
	 */
	final public Point getFirstPivotPoint() {
		if(pivotList.size() == 0) {
			return endPoint;
		}
		return pivotList.get(0);
	}
	
	/**
	 * 最後のPIVOT位置を返す．
	 * Pivotをひとつも持っていなければ始点を返すので注意．
	 * @return
	 * @return Point
	 */
	final public Point getLastPivotPoint() {
		if(pivotList.size() == 0) {
			return startPoint;
		}
		return pivotList.get(pivotList.size()-1);
	}
	
	boolean closed = false;
	
	public boolean contains(Point point) {
		boolean flag = false;
		Point oldPoint = startPoint;
		for(Point p : pivotList) {
			flag |= lineContains(oldPoint.x, oldPoint.y, p.x, p.y, point);
			oldPoint = p;
		}
		flag |= lineContains(oldPoint.x, oldPoint.y, endPoint.x, endPoint.y, point);
		flag |= namedArrow.contains(point);
		return flag;
	}

	public void addPivot(Point p) {
		pivotList.add(p);
	}

	public void draw(Graphics2D g) {
		Color oldColor = g.getColor();
		g.setColor(lineColor);
		Point oldPoint = startPoint;
		for(Point p : pivotList) {
			g.drawLine(oldPoint.x, oldPoint.y, p.x, p.y);
			oldPoint = p;
		}
		
		if(this.isSelected()) {
			double w = 6;
			g.draw(new Rectangle2D.Double(startPoint.x-w/2, startPoint.y-w/2, w, w));
			for(Point p : pivotList) {
				Rectangle2D rect = new Rectangle2D.Double(p.x-w/2, p.y-w/2, w, w);
				g.draw(rect);
			}
		}
		g.setColor(oldColor);
		namedArrow.draw(g);
	}

	public Rectangle getBounds() {
		return null;
	}

	public double getCenterX() {
		return namedArrow.getCenterX();
	}

	public double getCenterY() {
		return namedArrow.getCenterY();
	}

	public int getPivotNum() {
		return pivotList.size();
	}

	public ArrayList<Point> getPivotList() {
		return pivotList;
	}
	
	/**
	 * @return lineColor
	 */
	public Color getLineColor() {
		return lineColor;
	}

	/**
	 * @param lineColor セットする lineColor
	 */
	public void setLineColor(Color lineColor) {
		this.lineColor = lineColor;
		namedArrow.setLineColor(lineColor);
	}
	
	public void setTextColor(Color textColor) {
		namedArrow.setTextColor(textColor);
	}

	private Color lineColor;
	
}
