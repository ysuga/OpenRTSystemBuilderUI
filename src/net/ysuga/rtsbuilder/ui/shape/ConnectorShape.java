/**
 * ConnectorShape.java
 *
 * @author Yuki Suga (ysuga.net)
 * @date 2011/08/27
 * @copyright 2011, ysuga.net allrights reserved.
 *
 */
package net.ysuga.rtsbuilder.ui.shape;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

import net.ysuga.rtsystem.profile.Connector;
import net.ysuga.rtsystem.profile.PivotList;
import net.ysuga.rtsystem.profile.RTSProperties;

/**
 * <div lang="ja">
 *
 * </div>
 * <div lang="en">
 *
 * </div>
 * @author ysuga
 *
 */
public class ConnectorShape  {
	private Connector connector;
	
	public Connector getConnector() {
		return connector;
	}
	
	boolean selected;
	
	PortShape sourcePortShape;
	PortShape targetPortShape;
	
	private PivottedNamedArrow arrow;
	
	private PivotList pivotList;
	
	public PivotList getPivotList() {
		return connector.getPivotList();
		// return pivotList;
	}
	/**
	 * <div lang="ja">
	 * コンストラクタ
	 * </div>
	 * <div lang="en">
	 * Constructor
	 * </div>
	 */
	public ConnectorShape(Connector connector, PortShape sourcePortShape, PortShape targetPortShape) {
		this.connector = connector;
		this.sourcePortShape = sourcePortShape;
		this.targetPortShape = targetPortShape;
		
		this.pivotList = new PivotList();
		initShape();
		
		if(getPivotList().size() == 0) {
			autoPivot();
		}
		///autoPivot();
	}
	
	private void initShape() {
		Point fromPoint = sourcePortShape.getCenterPoint();
		Point distPoint = targetPortShape.getCenterPoint();

		Rectangle2D sourceRect = sourcePortShape.getBounds();
		Rectangle2D targetRect = targetPortShape.getBounds();

		Point lastPivot;
		Point firstPivot;
		setArrow(new PivottedNamedArrow( connector.get(Connector.NAME), sourcePortShape.getCenterPoint(),
				targetPortShape.getCenterPoint(), getPivotList()));

		if(getPivotList().size() != 0) { 
			firstPivot = getArrow().getFirstPivotPoint();
			lastPivot = getArrow().getLastPivotPoint();
		}else if (sourceRect.intersects(targetRect)) { 
			// 重なっていると厄介なので，強制的にPIVOTを追加．
			getArrow().addPivot(new Point((int) getArrow().getCenterX() - 40,
					(int) getArrow().getCenterY() - 40));
			getArrow().addPivot(new Point((int) getArrow().getCenterX() + 40,
					(int) getArrow().getCenterY() - 40));
			firstPivot = getArrow().getFirstPivotPoint();
			lastPivot = getArrow().getLastPivotPoint();
			
		} else {
			firstPivot = distPoint;
			lastPivot = fromPoint;
		}

		Line2D tempLineFrom = new Line2D.Double(fromPoint, firstPivot);
		Line2D tempLineTo = new Line2D.Double(lastPivot, distPoint);
		Point fromEdge = detectEdge(sourceRect, tempLineFrom);
		Point toEdge = detectEdge(targetRect, tempLineTo);

		if (fromEdge != null && toEdge != null) {
			setArrow(new PivottedNamedArrow(connector.get(Connector.NAME), 
					fromEdge, toEdge, getPivotList()));
		}
	}
	
	

	public void draw(Graphics2D g) {

		initShape();
		
		Color lineColor;
		switch(connector.getState()) {
		case RTSProperties.ONLINE_ACTIVE:
			lineColor = Color.green;
			sourcePortShape.setBackground(Color.green);
			targetPortShape.setBackground(Color.green);
			break;
		case RTSProperties.OFFLINE:
		default:
			lineColor = Color.black;
			sourcePortShape.setBackground(Color.white);
			targetPortShape.setBackground(Color.white);
			break;		
		}
		
		getArrow().setLineColor(lineColor);
		getArrow().setSelected(isSelected());
		getArrow().draw(g);
		
		sourcePortShape.draw(g);
		targetPortShape.draw(g);
		
		// TODO:
		// transition.setPivotList(arrow.getPivotList());

		/*
		for (ModelShape shape : getShapeList()) {
			shape.draw(g);
		}
		*/
	}
	
	final static Point detectEdge(Rectangle2D box, Line2D line) {
		double x1 = line.getX1();
		double x2 = line.getX2();
		double y1 = line.getY1();
		double y2 = line.getY2();

		double xx = x1;
		double yy = y1;

		double u1 = box.getX();
		double u2 = box.getX() + box.getWidth();
		double v1 = box.getY();
		double v2 = box.getY() + box.getHeight();

		double a = (y2 - y1) / (x2 - x1);
		double v = 0, u = 0;

		// u1 should be less than u2
		if (u1 >= u2) {
			double buf = u1;
			u1 = u2;
			u2 = buf;
		}
		// x1 should be less than x2
		if (x1 >= x2) {
			double buf = x1;
			x1 = x2;
			x2 = buf;
		}
		// v1 should be less than v2
		if (v1 >= v2) {
			double buf = v1;
			v1 = v2;
			v2 = buf;
		}
		// y1 should be less than y2
		if (y1 >= y2) {
			double buf = y1;
			y1 = y2;
			y2 = buf;
		}

		if (u1 >= x1 && u1 <= x2) {
			// x = u1
			v = a * (u1 - xx) + yy;
			if (v >= v1 && v <= v2) {
				return new Point((int) u1, (int) v);
			}
		}

		if (u2 >= x1 && u2 <= x2) {
			// x = u2
			v = a * (u2 - xx) + yy;
			if (v >= v1 && v <= v2) {
				return new Point((int) u2, (int) v);
			}
		}

		if (v1 >= y1 && v1 <= y2) {
			// y = v1
			u = (v1 - yy) / a + xx;
			if (u >= u1 && u <= u2) {
				return new Point((int) u, (int) v1);
			}
		}

		if (v2 >= y1 && v2 <= y2) {
			// y = v2
			u = (v2 - yy) / a + xx;
			if (u >= u1 && u <= u2) {
				return new Point((int) u, (int) v2);
			}
		}

		return null;
	}

	/**
	 * <div lang="ja">
	 *
	 * @param x
	 * @param y
	 * @return
	 * </div>
	 * <div lang="en">
	 *
	 * @param x
	 * @param y
	 * @return
	 * </div>
	 */
	public boolean contains(Point point) {
		return getArrow().contains(point);
	}

	/**
	 * @param arrow the arrow to set
	 */
	public void setArrow(PivottedNamedArrow arrow) {
		this.arrow = arrow;
	}

	/**
	 * @return the arrow
	 */
	public PivottedNamedArrow getArrow() {
		return arrow;
	}
	
	//private Point selectedPivot;
	
	/**
	 * @return selectedPivot
	 */
	public final Point getSelectedPivot() {
		return connector.getSelectedPivot();
	}
	/**
	 * @param selectedPivot セットする selectedPivot
	 */
	public final void setSelectedPivot(Point selectedPivot) {
		//this.selectedPivot = selectedPivot;
		connector.setSelectedPivot(selectedPivot);
	}
	
	public void onMousePressed(MouseEvent arg0) {
		System.out.println("Mouse Pressed");
		Point mp = arg0.getPoint();
		setSelectedPivot(null);
		double nearestDistance = Double.MAX_VALUE;
		//PivotList pivotList = transition.getPivotList();
		for(Point p : getPivotList()) {
			double dist = Math.sqrt((p.x - mp.x) * (p.x - mp.x) + (p.y - mp.y) * (p.y - mp.y));
			if(dist < nearestDistance) {
				setSelectedPivot(p);
				nearestDistance = dist;
			}
			/**
			double dist = Math.sqrt((p.x - mp.x) * (p.x - mp.x) + (p.y - mp.y) * (p.y - mp.y));
			if(dist < 10) {
				selectedPivot = p;
			}*/
			
		}
		/*
		if(selectedPivot == null) {
			System.out.println("selectedPivot is null");
			
			selectedPivot = new Point(mp.x, mp.y);
			
			Point oldPoint = arrow.getStartPoint();
			for(int i = 0;i < connector.getPivotList().size();i++) {
				Point p = connector.getPivotList().get(i);
				if(PivottedNamedArrow.lineContains(oldPoint.x, oldPoint.y, p.x, p.y, mp)) {
					connector.getPivotList().add(i, selectedPivot);
					return;
				}
				oldPoint = p;
			}
			connector.getPivotList().add(selectedPivot);
		}*/
	//	System.out.println("Connector("+this.toString()+") selectedPivot=" + selectedPivot);
	}
	
	public void onMouseDragged(MouseEvent arg0) {
		Point mp = arg0.getPoint();
					//pivotList.remove(selectedPivot);
	//	System.out.println("Connector("+this.toString()+") mp="+selectedPivot);
		for(Point p : getPivotList()) {
			System.out.println("p="+p);
			if(getSelectedPivot() != null && getSelectedPivot().equals(p)) {
				int index = getPivotList().indexOf(p);
				System.out.println("onDragged(" + index + ")");
				switch(index) {
				case 0:
					getPivotList().get(0).setLocation(mp.x, getPivotList().get(0).y);
					getPivotList().get(1).setLocation(mp.x, getPivotList().get(1).y);
					break;
				case 1:
					getPivotList().get(0).setLocation(mp.x, getPivotList().get(0).y);
					getSelectedPivot().setLocation(mp.x, mp.y);
					getPivotList().get(2).setLocation(getPivotList().get(2).x, mp.y);
					break;
				case 2:
					getPivotList().get(3).setLocation(mp.x, getPivotList().get(3).y);
					getSelectedPivot().setLocation(mp.x, mp.y);
					getPivotList().get(1).setLocation(getPivotList().get(1).x, mp.y);
					break;					
				case 3:
					getPivotList().get(3).setLocation(mp.x, getPivotList().get(3).y);
					getPivotList().get(2).setLocation(mp.x, getPivotList().get(2).y);
					break;
				default:
					break;
				}
			break;
			}
		}
	}
	
	public void onMouseReleased(MouseEvent arg0) {
		Point prevPoint, nextPoint;
		
		PivotList pivotList = connector.getPivotList();
		int selectedIndex = pivotList.indexOf(getSelectedPivot());
		if(selectedIndex == 0) { // firstPivot
			prevPoint = arrow.getStartPoint();
		} else {
			prevPoint = pivotList.get(selectedIndex-1);
		}
		
		if(selectedIndex == pivotList.size()-1) { //lastPivot
			nextPoint = arrow.getEndPoint();
		} else {
			nextPoint = pivotList.get(selectedIndex+1);
		}
		if(PivottedNamedArrow.lineContains(prevPoint.x, prevPoint.y, nextPoint.x, nextPoint.y, getSelectedPivot())) {
		//	pivotList.remove(getSelectedPivot());
		}
		setSelectedPivot(null);
	}

	/**
	 * <div lang="ja">
	 *
	 * @param b
	 * </div>
	 * <div lang="en">
	 *
	 * @param b
	 * </div>
	 */
	public void setSelected(boolean b) {
		selected = b;
	}
	
	public boolean isSelected() {
		return selected;
	}

	private int escape = 40;
	/**
	 * autoPivot
	 * <div lang="ja">
	 * 
	 * </div>
	 * <div lang="en">
	 *
	 * </div>
	 */
	public void autoPivot() {
		connector.getPivotList().clear();
		Point fromPoint = sourcePortShape.getCenterPoint();
		Point distPoint = targetPortShape.getCenterPoint();
		int fromSide = sourcePortShape.getSide();
		int distSide = targetPortShape.getSide();
		
		if(fromSide == PortShape.SIDE_LEFT && distSide == PortShape.SIDE_LEFT) {
			// 2 pivots.
			int pivotX = fromPoint.x < distPoint.x ? fromPoint.x - escape : distPoint.x - escape;
			getPivotList().add(new Point(pivotX, fromPoint.y));
			getPivotList().add(new Point(pivotX, (fromPoint.y + distPoint.y)/2));
			getPivotList().add(new Point(pivotX, (fromPoint.y + distPoint.y)/2));			
			getPivotList().add(new Point(pivotX, distPoint.y));
		} else if (fromSide == PortShape.SIDE_RIGHT && distSide == PortShape.SIDE_RIGHT) {
			// 2 pivots.
			int pivotX = fromPoint.x > distPoint.x ? fromPoint.x + escape : distPoint.x + escape;
			getPivotList().add(new Point(pivotX, fromPoint.y));
			getPivotList().add(new Point(pivotX, (fromPoint.y + distPoint.y)/2));
			getPivotList().add(new Point(pivotX, (fromPoint.y + distPoint.y)/2));
			getPivotList().add(new Point(pivotX, distPoint.y));
		} else if (fromSide == PortShape.SIDE_RIGHT && distSide == PortShape.SIDE_LEFT) {
			if(fromPoint.x + escape > distPoint.x - escape) {
				// 4 pivots
				getPivotList().add(new Point(fromPoint.x + escape, fromPoint.y));
				getPivotList().add(new Point(fromPoint.x + escape, (fromPoint.y+distPoint.y)/2));
				getPivotList().add(new Point(distPoint.x - escape, (fromPoint.y+distPoint.y)/2));
				getPivotList().add(new Point(distPoint.x - escape, distPoint.y));
			} else {
				// 2 pivots.
				int pivotX = (fromPoint.x + distPoint.x) / 2;
				getPivotList().add(new Point(pivotX, fromPoint.y));
				getPivotList().add(new Point(pivotX, (fromPoint.y + distPoint.y)/2));
				getPivotList().add(new Point(pivotX, (fromPoint.y + distPoint.y)/2));
				getPivotList().add(new Point(pivotX, distPoint.y));
			}
		} else if (fromSide == PortShape.SIDE_LEFT && distSide == PortShape.SIDE_RIGHT) {
			if(fromPoint.x - escape < distPoint.x + escape) {
				// 4 pivots
				getPivotList().add(new Point(fromPoint.x - escape, fromPoint.y));
				getPivotList().add(new Point(fromPoint.x - escape, (fromPoint.y+distPoint.y)/2));
				getPivotList().add(new Point(distPoint.x + escape, (fromPoint.y+distPoint.y)/2));
				getPivotList().add(new Point(distPoint.x + escape, distPoint.y));
			} else {
				// 2 pivots.
				int pivotX = (fromPoint.x + distPoint.x) / 2;
				getPivotList().add(new Point(pivotX, fromPoint.y));
				getPivotList().add(new Point(pivotX, (fromPoint.y + distPoint.y)/2));
				getPivotList().add(new Point(pivotX, (fromPoint.y + distPoint.y)/2));
				getPivotList().add(new Point(pivotX, distPoint.y));
			}
		}
		
	}

}
