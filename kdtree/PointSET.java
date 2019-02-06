/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdDraw;

public class PointSET {

    private SET<Point2D> pointsInSquare;

    // construct an empty set of points
    public PointSET() {
        pointsInSquare = new SET<Point2D>();
    }

    // is the set empty?
    public boolean isEmpty() {
        return pointsInSquare.isEmpty();
    }

    // number of points in the set
    public int size() {
        return pointsInSquare.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("Null parameter passed to insert().");
        }


        pointsInSquare.add(p);
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("Null parameter passed to contains().");
        }


        return pointsInSquare.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        for (Point2D point : pointsInSquare) {
            point.draw();
        }
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException("Null parameter passed to range().");
        }

        Stack<Point2D> pointsInSearchRectangle = new Stack<Point2D>();

        for (Point2D point : pointsInSquare) {
            if (rect.contains(point)) {
                pointsInSearchRectangle.push(point);
            }
        }

        return pointsInSearchRectangle;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("Null parameter passed to nearest().");
        }
        double smallestDistance = 0;
        Point2D nearestNeighbor = null;

        for (Point2D point : pointsInSquare) {
            if (nearestNeighbor == null || p.distanceSquaredTo(point) < smallestDistance) {
                nearestNeighbor = point;
                smallestDistance = p.distanceSquaredTo(point);
            }
        }

        return nearestNeighbor;
    }


    // unit testing of the methods (optional)
    public static void main(String[] args) {

        // initialize the data structures from file
        String filename = args[0];
        In in = new In(filename);
        PointSET brute = new PointSET();

        // StdOut.println(brute.size() + " == 0");
        // StdOut.println(brute.isEmpty() + " == true");
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            brute.insert(p);
        }

        // StdOut.println(brute.isEmpty() + " == false");
        // brute.draw();
        // Point2D test1 = new Point2D(0.5, 0.5);
        // StdOut.println(brute.nearest(test1) + " is nearest to (0.5, 0.5)");

        // Point2D test2 = new Point2D(0.143, 0.179);
        // StdOut.println(brute.contains(test2) + " == false");

        // TEST RANGE CAPABILITIES
        /*
        RectHV fullRange = new RectHV(0.0, 0.0, 1.0, 1.0);

        double xmin = 0.2;
        double ymin = 0.2;
        double xmax = 0.7;
        double ymax = 0.7;

        RectHV test3 = new RectHV(xmin, ymin, xmax, ymax);
        StdDraw.setPenColor(StdDraw.BLUE);
        StdDraw.setPenRadius(0.01);
        test3.draw();
        for (Point2D point : brute.range(fullRange)) {
            StdDraw.setPenColor(StdDraw.BLACK);
            point.draw();
        }

        for (Point2D point : brute.range(test3)) {
            StdDraw.setPenColor(StdDraw.RED);
            point.draw();
        }
        */
    }
}
