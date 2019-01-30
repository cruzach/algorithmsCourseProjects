/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;

public class FastCollinearPoints {

    private ArrayList<LineSegment> lineSegments;
    private int lineSegmentCount;

    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points) {
        if (points == null) {
            throw new java.lang.IllegalArgumentException("Cannot input array equal to 'null'.");
        }

        // Check for null values, sort array, and copy array
        Point[] sortedPoints = new Point[points.length];
        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) {
                throw new java.lang.IllegalArgumentException(
                        "Array element of 'null' encountered.");
            }
            for (int j = 0; j < points.length; j++) {
                if (i != j && points[i].equals(points[j])) {
                    throw new java.lang.IllegalArgumentException("Repeated point found");
                }
            }
            sortedPoints[i] = points[i];
        }

        lineSegments = new ArrayList<LineSegment>();

        lineSegmentCount = 0;
        double previousSlope = 0;
        int high;
        int low;

        // Loop through all points to check collinearity using them(points[outerloop]) as anchors
        for (int outerLoop = 0; outerLoop < points.length; outerLoop++) {
            // sort inner loop by the slope the points make with anchor point
            Arrays.sort(sortedPoints, points[outerLoop].slopeOrder());

            // Maintain array indices throughout loop
            high = 0; // current index evaluated
            low = 0;  // earliest index with equal slope to current index

            for (int innerLoop = 0; innerLoop < sortedPoints.length; innerLoop++) {
                // first iteration
                if (high - low == 0) {
                    previousSlope = points[outerLoop].slopeTo(sortedPoints[high]);
                    high++;
                }
                // if slope is same as previous point, simply incremement
                else if ((points[outerLoop].slopeTo(sortedPoints[high]) - previousSlope) == 0) {
                    high++;
                }
                // if slope is not equal to last, check if enough points to make line
                // and check if points go in ascending order
                else {
                    if (high - low >= 3 && points[outerLoop].compareTo(sortedPoints[low]) < 0) {
                        lineSegments
                                .add(new LineSegment(points[outerLoop], sortedPoints[high - 1]));
                        lineSegmentCount++;
                    }
                    // then iterate to next point
                    previousSlope = points[outerLoop].slopeTo(sortedPoints[high]);
                    low = high;
                    high++;
                }
            }
        }
    }

    // the number of line segments
    public int numberOfSegments() {
        return lineSegmentCount;
    }

    // the line segments
    public LineSegment[] segments() {
        LineSegment[] segmentArray = new LineSegment[lineSegmentCount];
        for (int i = 0; i < lineSegmentCount; i++) {
            segmentArray[i] = lineSegments.get(i);
        }
        return segmentArray;
    }

    public static void main(String[] args) {
        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
