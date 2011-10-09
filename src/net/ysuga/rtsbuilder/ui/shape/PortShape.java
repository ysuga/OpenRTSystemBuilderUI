/**
 * PortShape.java
 *
 * @author Yuki Suga (ysuga.net)
 * @date 2011/08/28
 * @copyright 2011, ysuga.net allrights reserved.
 *
 */
package net.ysuga.rtsbuilder.ui.shape;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;

import net.ysuga.rtsystem.profile.DataPort;

/**
 * <div lang="ja">
 * 
 * </div> <div lang="en">
 * 
 * </div>
 * 
 * @author ysuga
 * 
 */
public class PortShape  {

	private DataPort dataPort;

	public DataPort getDataPort() {
		return dataPort;
	}

	private PortShapeBase shape;
	
	public PortShapeBase getShape() {
		return shape;
	}

	public final static int SIDE_RIGHT = 0;
	public final static int SIDE_LEFT = 1;
	public int side;
	private ComponentShape componentShape;
	
	public int getSide() {
		return side;
	}

	public class PortShapeBase extends Polygon {
		private Color bgColor = Color.white;

		private Color fgColor = Color.black;

		/**
		 * @return bgColor
		 */
		public final Color getBgColor() {
			return bgColor;
		}

		/**
		 * @param bgColor
		 *            �Z�b�g���� bgColor
		 */
		public final void setBgColor(Color bgColor) {
			this.bgColor = bgColor;
		}

		/**
		 * @return fgColor
		 */
		public final Color getFgColor() {
			return fgColor;
		}

		/**
		 * @param fgColor
		 *            �Z�b�g���� fgColor
		 */
		public final void setFgColor(Color fgColor) {
			this.fgColor = fgColor;
		}

		public PortShapeBase(int[] x, int[] y, int pointnum) {
			super(x, y, pointnum);
		}

		public void draw(Graphics2D g) {
			Color oldColor = g.getColor();

			g.setColor(bgColor);
			g.fill(this);
			g.setColor(fgColor);
			g.draw(this);
			g.setColor(oldColor);
		}
	}

	public class InPortShape extends PortShapeBase {

		public InPortShape(int x, int y, int w, int h) {
			super(new int[] { x, x + w, x + w, x, x + w / 3 }, new int[] { y,
					y, y + h, y + h, y + h / 2 }, 5);
		}

	}

	public class OutPortShape extends PortShapeBase {

		public OutPortShape(int x, int y, int w, int h) {
			super(new int[] { x, x + w * 2 / 3, x + w, x + w * 2 / 3, x },
					new int[] { y, y, y + h / 2, y + h, y + h }, 5);
		}
	}

	public class UnknownPortShape extends PortShapeBase {
		public UnknownPortShape(int x, int y, int w, int h) {
			super(new int[] { x+w/3, x+w*2/3, x+w, x+w*2/3, x+w/3, x },
					new int[] { y, y, y+h/2, y+h, y+h, y+h/2 }, 6);
		}
	}
	
	
	/**
	 * <div lang="ja"> �R���X�g���N�^ </div> <div lang="en"> Constructor </div>
	 * 
	 * @param dataPort
	 */
	public PortShape(DataPort dataPort, ComponentShape component) {
		this.setComponentShape(component);
		this.dataPort = dataPort;
		Rectangle rect = component.getBounds();

		int numLeftSidePort = component.getNumLeftSidePort();
		int numRightSidePort = component.getNumRightSidePort();

		switch (dataPort.getDirection()) {
		case DataPort.DIRECTION_IN:
			shape = new InPortShape(rect.x - 10, rect.y + 5 + numLeftSidePort
					* (10 + 5), 15, 10);
			side = SIDE_LEFT;
			break;
		case DataPort.DIRECTION_OUT:
			shape = new OutPortShape(rect.x + rect.width - 5, rect.y + 5
					+ numRightSidePort * (10 + 5), 15, 10);
			side = SIDE_RIGHT;
			break;
		case DataPort.SERVICE_PROVIDER:
			
			break;
		case DataPort.DIRECTION_UNKNOWN:
		default:
			if (numLeftSidePort <= numRightSidePort) {
				shape = new UnknownPortShape(rect.x - 10, rect.y + 5
						+ numLeftSidePort * (10 + 5), 15, 10);
				side = SIDE_LEFT;
			} else {
				shape = new UnknownPortShape(rect.x + rect.width - 5, rect.y
						+ 5 + numRightSidePort * (10 + 5), 15, 10);
				side = SIDE_RIGHT;
			}
			break;
		}
	}

	/**
	 * <div lang="ja">
	 * 
	 * @param g
	 *            </div> <div lang="en">
	 * @param g
	 *            </div>
	 */
	public void draw(Graphics2D g) {
		shape.draw(g);
	}
	
	public void setBackground(Color c) {
		shape.setBgColor(c);
	}
	
	public void setForeground(Color c) {
		shape.setFgColor(c);
	}

	/**
	 * <div lang="ja">
	 * 
	 * @param p
	 * @return </div> <div lang="en">
	 * @param p
	 * @return </div>
	 */
	public boolean contains(Point p) {
		return this.shape.contains(p);
	}

	/**
	 * <div lang="ja">
	 * 
	 * @return </div> <div lang="en">
	 * @return </div>
	 */
	public Point getCenterPoint() {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		return new Point((int) shape.getBounds().getCenterX(), (int) shape
				.getBounds().getCenterY());
	}

	/**
	 * <div lang="ja">
	 * 
	 * @return </div> <div lang="en">
	 * @return </div>
	 */
	public Rectangle getBounds() {
		return shape.getBounds();
	}

	boolean selected;
	/**
	 * <div lang="ja">
	 * 
	 * @param flag
	 *            </div> <div lang="en">
	 * @param flag
	 *            </div>
	 */
	public void setSelected(boolean flag) {
		selected = flag;
	}

	/**
	 * <div lang="ja">
	 * 
	 * @return </div> <div lang="en">
	 * @return </div>
	 */
	public boolean isSelected() {
		return selected;
	}

	/**
	 * <div lang="ja">
	 * 
	 * @return </div> <div lang="en">
	 * @return </div>
	 */
	public double getX() {
		return shape.getBounds().getX();
	}

	/**
	 * <div lang="ja">
	 * 
	 * @return </div> <div lang="en">
	 * @return </div>
	 */
	public double getY() {
		return shape.getBounds().getY();
	}

	/**
	 * @return componentShape
	 */
	public ComponentShape getComponentShape() {
		return componentShape;
	}

	/**
	 * @param componentShape set componentShape
	 */
	public void setComponentShape(ComponentShape componentShape) {
		this.componentShape = componentShape;
	}



}
