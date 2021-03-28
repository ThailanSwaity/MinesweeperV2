/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package display;

import java.awt.Point;
import java.awt.Polygon;

/**
 *
 * @author thaiboman
 */
public class VectorShape extends Polygon {

    protected double radius;

    protected double angle;

    protected Point origin;

    public VectorShape() {
        this(new Point(0, 0));
    }

    public VectorShape(Point origin) {
        this.origin = origin;
    }

    public VectorShape(Point origin, double angle) {
        this.origin = origin;
        this.angle = angle;
    }

    /**
     *
     * @param a Matrix of variables
     * @param b Answer to equations
     * @return Values of variables
     */
    public double[] cramersLaw(double[] a, double[] b) {
        if (b.length > 2 || b.length < 2) {
            return null;
        }
        if (a.length > 4 || a.length > 4) {
            return null;
        }
        double[] x = new double[2];
        double[] i = inverse(a);
        x[0] = i[0] * b[0] + i[1] * b[1];
        x[1] = i[2] * b[0] + i[3] * b[1];
        return x;
    }

    public double[] inverse(double[] a) {
        double[] i = new double[a.length];
        double b = 1 / det(a);
        i[0] = b * a[3];
        i[1] = b * -a[1];
        i[2] = b * -a[2];
        i[3] = b * a[0];
        return i;
    }

    public double det(double[] a) {
        if (a.length < 4) {
            return 0;
        } else if (a.length > 4) {
            return 0;
        }
        double d = a[0] * a[3] - a[1] * a[2];
        return d;
    }

    /**
     * Checks two vectors based on the starting positions and the corresponding
     * line vectors.
     *
     * @param a Position vector 1
     * @param u Line vector 1
     * @param b Position vector 2
     * @param v Line vector 2
     * @return boolean stating whether or not the vectors intersect with each
     * other
     */
    public boolean isIntersecting(double[] a, double[] u, double[] b, double[] v) {
        double[] w = new double[2];
        w[0] = a[0] - b[0];
        w[1] = a[1] - b[1];
//        System.out.println("w[0] " + b[0] + " - " + a[0]);
//        System.out.println("w[1] " + b[1] + " - " + a[1]);
//        System.out.println(w[0]);
//        System.out.println(w[1]);
        double[] x = new double[4];
        x[0] = v[0];
        x[2] = v[1];
        x[1] = -u[0];
        x[3] = -u[1];
//        System.out.println(Arrays.toString(x));
        double[] an = cramersLaw(x, w);
        if (an[0] > 1 || an[1] > 1) {
            return false;
        } else if (an[0] < 0 || an[1] < 1) {
            return false;
        }
        double[] p = new double[2];
        p[0] = a[0] + u[0] * an[1];
        p[1] = a[1] + u[1] * an[1];
        double[] q = new double[2];
        q[0] = b[0] + v[0] * an[0];
        q[1] = b[1] + v[1] * an[0];
//        System.out.println("s: " + an[0]);
//        System.out.println("t: " + an[1]);
//        System.out.println("P: " + p[0] + ", " + p[1]);
//        System.out.println("Q: " + q[0] + ", " + q[1]);
        return q[0] == p[0] && q[1] == p[1];
    }

    public boolean intersecting(VectorShape ve) {
        double[] a = new double[2];
        double[] u = new double[2];
        double[] b = new double[2];
        double[] v = new double[2];
        for (int i = 0; i < this.npoints; i++) {
            a[0] = this.xpoints[i];
            a[1] = this.ypoints[i];
            if (i != this.npoints) {
                u[0] = -a[0] + this.xpoints[i + 1];
                u[1] = -a[1] + this.ypoints[i + 1];
            } else {
                u[0] = -a[0] + this.xpoints[0];
                u[1] = -a[1] + this.ypoints[0];
            }
            for (int j = 0; j < ve.npoints; j++) {
                b[0] = ve.xpoints[j];
                b[1] = ve.ypoints[j];
                if (j != ve.npoints) {
                    v[0] = -b[0] + ve.xpoints[j + 1];
                    v[1] = -b[1] + ve.ypoints[j + 1];
                } else {
                    v[0] = -b[0] + ve.xpoints[0];
                    v[1] = -b[1] + ve.ypoints[0];
                }
                if (isIntersecting(a, u, b, v)) return true;
            }
        }
        return false;
    }

    /**
     * Shape presets
     *
     * @param radius
     * @param points
     */
    public void setToCircle(double radius, int points) {
        Polygon shape = new Polygon();
        double inc = 2 * Math.PI / points;
        double x;
        double y;
        for (int i = 0; i < points; i++) {
            x = radius * Math.cos(inc * (i + 1)) + origin.x;
            y = radius * Math.sin(inc * (i + 1)) + origin.y;
            shape.addPoint((int) x, (int) y);
        }
        this.radius = radius;
        setAsShape(shape);
    }

    public void setAsShape(Polygon poly) {
        this.npoints = poly.npoints;
        this.xpoints = poly.xpoints;
        this.ypoints = poly.ypoints;
    }

    public void setOrigin(Point origin) {
        this.origin = origin;
    }

    public Point getOrigin() {
        return origin;
    }

    public void moveTo(Point coord) {
        double x = coord.x - origin.x;
        double y = coord.y - origin.y;

        Polygon poly = new Polygon();
        for (int i = 0; i < this.npoints; i++) {
            poly.addPoint((int) (this.xpoints[i] + x), (int) (this.ypoints[i] + y));
        }
        setAsShape(poly);

        origin = coord;
    }

    public void setRadius(double radius) {
        this.setToCircle(radius, this.npoints);
    }

    public double getRadius() {
        return radius;
    }

    public void setRotation(double angle) {
        double x;
        double y;

        for (int i = 0; i < npoints; i++) {
            x = this.xpoints[i] - origin.x;
            y = this.ypoints[i] - origin.y;

        }
    }

}
