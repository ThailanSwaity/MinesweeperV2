package display;


import java.awt.Polygon;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author thaiboman
 */
public class PolyCircle extends Polygon {
    
    public double radius;
    
    public PolyCircle(double radius, int points) {
        this.radius = radius;
        double inc = 2 * Math.PI / points;
        double x;
        double y;
        for (int i = 0; i < points; i++) {
            x = radius * Math.cos(inc * (i + 1));
            y = radius * Math.sin(inc * (i + 1));
            this.addPoint((int)x, (int)y);
        }
    }
    
    public void addPoint() {
        PolyCircle poly = new PolyCircle(radius, this.npoints + 1);
        this.npoints = poly.npoints;
        this.xpoints = poly.xpoints;
        this.ypoints = poly.ypoints;
    }
    
    public void setX(double x) {
        int[] x1 = this.xpoints;
        double inc = 2 * Math.PI / 2;
        
        for (int i = 0; i < x1.length; i++) {
            x1[i] = (int)(radius * Math.cos(inc * (i + 1)));
        }
    }
    
    public void setY(double y) {
        int[] y1 = this.ypoints;
        double inc = 2 * Math.PI / 2;
        
        for (int i = 0; i < y1.length; i++) {
            y1[i] = (int)(radius * Math.sin(inc * (i + 1)));
        }
    }
    
}
