import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FastCollinearPoints {
	private List<Point> points;
	private int numberOfSegment;
	private List<LineSegment> segmentList;

	public FastCollinearPoints(Point[] points) { // finds all line segments
		if (points == null) {
			throw new IllegalArgumentException();
		}
		for (Point p : points) {
			if (p == null) {
				throw new IllegalArgumentException();
			}
		}
		validateDuplicate(points);
		this.points = new ArrayList<>(Arrays.asList(points)); // containing 4
		// point
	}

	private void validateDuplicate(Point[] points) {
		Arrays.sort(points);
		Point prev = points[0];
		for (int index = 1; index < points.length; index++) {
			if (points[index].compareTo(prev) == 0) {
				throw new IllegalArgumentException();
			}
			prev = points[index];
		}
	}

	public int numberOfSegments() {
		return numberOfSegment; // the number of line segments

	}

	public LineSegment[] segments() {
		if (segmentList != null) {
			return segmentList.toArray(new LineSegment[0]).clone();
		}
		segmentList = new ArrayList<>();
		List<Pair> pairList = new ArrayList<>();
		if (points.size() <= 3) {
			return segmentList.toArray(new LineSegment[0]);
		}
		for (Point current : points) {
			List<Point> newList = new ArrayList<>(points);
			newList.remove(current);
			Collections.sort(newList, current.slopeOrder());
			int initIndex = 0;
			int index = 1;
			int count = 2;
			double slope = current.slopeTo(newList.get(0));
			while (index < newList.size()) {
				double currentSlope = current.slopeTo(newList.get(index));
				if (slope == currentSlope) {
					count++;
				} else if (count >= 4) {
					List<Point> list = new ArrayList<>();
					list.add(current);
					for (int idx = initIndex; idx < index; idx++) {
						list.add(newList.get(idx));
					}
					Collections.sort(list);
					// LineSegment segment = new LineSegment(list.get(0), list.get(list.size() -
					// 1));
					// segmentList.add(segment);
					Pair pair = new Pair(list.get(0), list.get(list.size() - 1));
					pairList.add(pair);
					initIndex = index;
					count = 2;
					slope = currentSlope;
				} else {
					initIndex = index;
					count = 2;
					slope = currentSlope;
				}
				index++;
			}
			if (count >= 4) {
				List<Point> list = new ArrayList<>();
				list.add(current);
				for (int idx = initIndex; idx < index; idx++) {
					list.add(newList.get(idx));
				}
				Collections.sort(list);
				// LineSegment segment = new LineSegment(list.get(0), list.get(list.size() -
				// 1));
				// segmentList.add(segment);
				Pair pair = new Pair(list.get(0), list.get(list.size() - 1));
				pairList.add(pair);
			}
		}
		segmentList = getSegmentList(pairList);
		numberOfSegment = segmentList.size();
		return segmentList.toArray(new LineSegment[0]);
	}

	private List<LineSegment> getSegmentList(List<Pair> pairList) {
		if (pairList.size() == 0) {
			return new ArrayList<>();
		}
		Collections.sort(pairList);
		List<LineSegment> segmentList = new ArrayList<>();
		Pair prev = pairList.get(0);
		segmentList.add(new LineSegment(prev.start, prev.end));
		for (int index = 1; index < pairList.size(); index++) {
			Pair current = pairList.get(index);
			if (current.compareTo(prev) != 0) {
				segmentList.add(new LineSegment(current.start, current.end));
				prev = current;
			}
		}
		return segmentList;
	}

	private class Pair implements Comparable<Pair> {
		public Pair(Point start, Point end) {
			this.start = start;
			this.end = end;
		}

		private Point start;
		private Point end;

		@Override
		public int compareTo(Pair o) {
			int compare = this.start.compareTo(o.start);
			return compare != 0 ? compare : this.end.compareTo(o.end);
		}

	}
}