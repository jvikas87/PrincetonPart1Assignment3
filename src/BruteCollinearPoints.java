import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BruteCollinearPoints {
	private Point[] points;
	private int numberOfSegment;
	private List<LineSegment> segmentList;

	public BruteCollinearPoints(Point[] points) { // finds all line segments
		if (points == null) {
			throw new IllegalArgumentException();
		}
		for (Point p : points) {
			if (p == null) {
				throw new IllegalArgumentException();
			}
		}
		for (int outer = 0; outer < points.length; outer++) {
			for (int inner = outer + 1; inner < points.length; inner++) {
				if (points[outer].compareTo(points[inner]) == 0) {
					throw new IllegalArgumentException();
				}
			}
		}
		this.points = points.clone(); // containing 4 points
	}

	public int numberOfSegments() {
		return numberOfSegment; // the number of line segments

	}

	public LineSegment[] segments() {
		if (segmentList != null) {
			return segmentList.toArray(new LineSegment[0]).clone();
		}
		int len = points.length;
		segmentList = new ArrayList<>();
		for (int a = 0; a < len - 3; a++) {
			Point first = points[a];
			for (int b = a + 1; b < len - 2; b++) {
				Point second = points[b];
				double slopeOne = first.slopeTo(second);
				for (int c = b + 1; c < len - 1; c++) {
					Point third = points[c];
					double slopeTwo = first.slopeTo(third);
					for (int d = c + 1; d < len; d++) {
						Point fourth = points[d];
						double slopeThree = first.slopeTo(fourth);
						if (slopeOne == slopeTwo && slopeTwo == slopeThree) {
							List<Point> pList = new ArrayList<>();
							pList.add(first);
							pList.add(second);
							pList.add(third);
							pList.add(fourth);
							Collections.sort(pList);
							segmentList.add(new LineSegment(pList.get(0), pList.get(3)));
							numberOfSegment++;
						}
					}
				}
			}
		}
		return segmentList.toArray(new LineSegment[0]);
		// the line segments
	}
}