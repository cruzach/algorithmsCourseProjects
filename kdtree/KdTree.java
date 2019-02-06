/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class KdTree {

    private static class Node {
        private Point2D point;      // the point
        private RectHV rectangle;    // the axis-aligned rectangle corresponding to this node
        private Node leftBottom;        // the left/bottom subtree
        private Node rightTop;        // the right/top subtree

        public Node(Point2D inputPoint) {
            this.point = inputPoint;
            this.leftBottom = null;
            this.rightTop = null;
        }

        public double getYCoordinate() {
            return point.y();
        }

        public double getXCoordinate() {
            return point.x();
        }
    }

    private Node root;
    private int sizeCounter;

    // construct an empty set of points
    public KdTree() {
        root = null;
        sizeCounter = 0;
    }

    // is the set empty?
    public boolean isEmpty() {
        return (sizeCounter == 0);
    }

    // number of points in the set
    public int size() {
        return sizeCounter;
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("Null parameter passed to insert().");
        }

        root = insert(root, null, p, 0);
    }

    private Node insert(Node subTreeRoot, Node parent, Point2D p, int level) {
        if (subTreeRoot == null) {
            Node newNode = new Node(p);

            if (parent == null) {
                newNode.rectangle = new RectHV(0.0, 0.0, 1.0, 1.0);
            }
            else {
                decideWhichRectangleToMake(parent, newNode, level);
            }

            sizeCounter++;
            return newNode;
        }

        if (subTreeRoot.point.equals(p)) { // If point exists and is found, return existing point
            return subTreeRoot;
        }

        if (level % 2 == 0) {
            if (p.x() < subTreeRoot.getXCoordinate()) {
                subTreeRoot.leftBottom = insert(subTreeRoot.leftBottom, subTreeRoot, p, level + 1);
            }
            else {
                subTreeRoot.rightTop = insert(subTreeRoot.rightTop, subTreeRoot, p, level + 1);
            }
        }
        else {
            if (p.y() < subTreeRoot.getYCoordinate()) {
                subTreeRoot.leftBottom = insert(subTreeRoot.leftBottom, subTreeRoot, p, level + 1);
            }
            else {
                subTreeRoot.rightTop = insert(subTreeRoot.rightTop, subTreeRoot, p, level + 1);
            }
        }

        return subTreeRoot;
    }

    private void decideWhichRectangleToMake(Node parent, Node newNode, int level) {
        if (level % 2 == 1) {
            if (newNode.point.x() < parent.getXCoordinate()) {
                createRectangleLessThanVerticalDivider(parent, newNode);
            }
            else {
                createRectangleGreaterThanVerticalDivider(parent, newNode);
            }
        }
        else {
            if (newNode.point.y() < parent.getYCoordinate()) {
                createRectangleLessThanHorizontalDivider(parent, newNode);
            }
            else {
                createRectangleGreaterThanHorizontalDivider(parent, newNode);
            }
        }
    }

    private void createRectangleLessThanVerticalDivider(Node parent, Node newNode) {
        newNode.rectangle = new RectHV(parent.rectangle.xmin(), parent.rectangle.ymin(),
                                       parent.getXCoordinate(), parent.rectangle.ymax());
    }

    private void createRectangleGreaterThanVerticalDivider(Node parent, Node newNode) {
        newNode.rectangle = new RectHV(parent.getXCoordinate(), parent.rectangle.ymin(),
                                       parent.rectangle.xmax(), parent.rectangle.ymax());
    }

    private void createRectangleLessThanHorizontalDivider(Node parent, Node newNode) {
        newNode.rectangle = new RectHV(parent.rectangle.xmin(), parent.rectangle.ymin(),
                                       parent.rectangle.xmax(), parent.getYCoordinate());
    }

    private void createRectangleGreaterThanHorizontalDivider(Node parent, Node newNode) {
        newNode.rectangle = new RectHV(parent.rectangle.xmin(), parent.getYCoordinate(),
                                       parent.rectangle.xmax(), parent.rectangle.ymax());
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("Null parameter passed to contains().");
        }
        if (isEmpty()) {
            return false;
        }
        return contains(root, p);
    }


    private boolean contains(Node subTreeRoot, Point2D p) {
        if (subTreeRoot == null) {
            return false;
        }

        if (subTreeRoot.point.equals(p)) {
            return true;
        }

        if (subTreeRoot.leftBottom != null && subTreeRoot.leftBottom.rectangle.contains(p)) {
            return contains(subTreeRoot.leftBottom, p);
        }
        else if (subTreeRoot.rightTop != null && subTreeRoot.rightTop.rectangle.contains(p)) {
            return contains(subTreeRoot.rightTop, p);
        }
        else {
            return false;
        }
    }

    // draw all points to standard draw
    public void draw() {
        StdDraw.clear();
        draw(root, 0);
    }

    private void draw(Node base, int level) {
        if (base != null) {
            draw(base.leftBottom, level + 1);

            StdDraw.setPenRadius();
            if (level % 2 == 0) {
                StdDraw.setPenColor(StdDraw.RED);
                StdDraw.line(base.point.x(), base.rectangle.ymin(), base.point.x(),
                             base.rectangle.ymax());
            }
            else {
                StdDraw.setPenColor(StdDraw.BLUE);
                StdDraw.line(base.rectangle.xmin(), base.point.y(), base.rectangle.xmax(),
                             base.point.y());
            }

            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.setPenRadius(0.01);
            base.point.draw();

            draw(base.rightTop, level + 1);
        }
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException("Null parameter passed to range().");
        }
        Queue<Point2D> pointsInRange = new Queue<Point2D>();

        range(rect, root, pointsInRange);
        return pointsInRange;
    }

    private void range(RectHV searchRectangle, Node base, Queue<Point2D> pointsInRange) {
        if (base != null && searchRectangle.intersects(base.rectangle)) {
            if (searchRectangle.contains(base.point)) {
                pointsInRange.enqueue(base.point);
            }

            range(searchRectangle, base.leftBottom, pointsInRange);
            range(searchRectangle, base.rightTop, pointsInRange);
        }
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("Null parameter passed to nearest().");
        }
        if (isEmpty()) {
            return null;
        }

        Point2D nearestPoint = nearest(p, root, root.point);
        return nearestPoint;
    }

    private Point2D nearest(Point2D queryPoint, Node base, Point2D currentChampion) {
        if (base != null && base.rectangle.distanceSquaredTo(queryPoint) <= currentChampion
                .distanceSquaredTo(queryPoint)) {
            if (base.point.distanceSquaredTo(queryPoint) < currentChampion
                    .distanceSquaredTo(queryPoint)) {
                currentChampion = base.point;
            }

            if (base.leftBottom != null && isQueryOnLeftBottomOfDivider(queryPoint, base)) {
                currentChampion = nearest(queryPoint, base.leftBottom, currentChampion);
                currentChampion = nearest(queryPoint, base.rightTop, currentChampion);
            }
            else {
                currentChampion = nearest(queryPoint, base.rightTop, currentChampion);
                currentChampion = nearest(queryPoint, base.leftBottom, currentChampion);
            }
        }

        return currentChampion;
    }

    private boolean isQueryOnLeftBottomOfDivider(Point2D queryPoint, Node base) {
        return (base.leftBottom.rectangle.contains(queryPoint));
    }


    // unit testing of the methods (optional)
    public static void main(String[] args) {
        // initialize the data structures from file
        String filename = args[0];
        In in = new In(filename);
        KdTree kdtree = new KdTree();

        // StdOut.println(kdtree.size() + " == 0");
        // StdOut.println(kdtree.isEmpty() + " == true");
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            kdtree.insert(p);
        }

        kdtree.draw();

        Point2D testPoint = new Point2D(0.81, 0.30);
        StdOut.println(kdtree.nearest(testPoint));
        /*
        StdOut.println(kdtree.size() + " == 10");
        // StdOut.println(kdtree.isEmpty() + " == false");
        Point2D test1 = new Point2D(0.37, 0.497);
        Point2D test2 = new Point2D(0.564, 0.41);
        Point2D test3 = new Point2D(0.22, 0.577);
        Point2D test4 = new Point2D(0.144, 0.17);
        Point2D test5 = new Point2D(0.08, 0.510);
        Point2D test6 = new Point2D(0.320, 0.70);
        Point2D test7 = new Point2D(0.41, 0.362);
        Point2D test8 = new Point2D(0.862, 0.82);
        Point2D test9 = new Point2D(0.78, 0.725);
        Point2D test10 = new Point2D(0.499, 0.20);


        StdOut.println(kdtree.contains(test1) + " == true");
        StdOut.println(kdtree.contains(test2) + " == true");
        StdOut.println(kdtree.contains(test3) + " == true");
        StdOut.println(kdtree.contains(test4) + " == true");
        StdOut.println(kdtree.contains(test5) + " == true");
        StdOut.println(kdtree.contains(test6) + " == true");
        StdOut.println(kdtree.contains(test7) + " == true");
        StdOut.println(kdtree.contains(test8) + " == true");
        StdOut.println(kdtree.contains(test9) + " == true");
        StdOut.println(kdtree.contains(test10) + " == true");
        */
    }
}
