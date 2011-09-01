package net.ysuga.rtsbuilder.ui.shape;

import java.awt.Dimension;
import java.awt.geom.Rectangle2D;

public class ShapeEdgeDetector {
	static Dimension detectEdge(Rectangle2D box, Rectangle2D line) {
		double x1 = line.getX();
		double x2 = line.getX() + line.getWidth();
		double y1 = line.getY();
		double y2 = line.getY() + line.getHeight();

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
				return new Dimension((int) u1, (int) v);
			}
		}

		if (u2 >= x1 && u2 <= x2) {
			// x = u2
			v = a * (u2 - xx) + yy;
			if (v >= v1 && v <= v2) {
				return new Dimension((int) u2, (int) v);
			}
		}

		if (v1 >= y1 && v1 <= y2) {
			// y = v1
			u = (v1 - yy) / a + xx;
			if (u >= u1 && u <= u2) {
				return new Dimension((int) u, (int) v1);
			}
		}

		if (v2 >= y1 && v2 <= y2) {
			// y = v2
			u = (v2 - yy) / a + xx;
			if (u >= u1 && u <= u2) {
				return new Dimension((int) u, (int) v2);
			}
		}

		return null;
	}
	
	static public void rotate(double[] p, double th) {
		double[] q = new double[2];
		q[0] = p[0]; q[1] = p[1];
		double c = Math.cos(th);
		double s = Math.sin(th);
		p[0] = c * q[0] + -s * q[1];
		p[1] = s * q[0] + c * q[1];
	}
}
