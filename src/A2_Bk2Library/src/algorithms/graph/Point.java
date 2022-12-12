package algorithms.graph;

public class Point {
	public int x;
	public int y;

	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public Point() {
		this.x = 0;
		this.y = 0;
	}

	public Point(Point p) {
		this.x = p.x;
		this.y = p.y;
	}

	public double distance(Point p) {
		double dx = x - p.x;
		double dy = y - p.y;
		return Math.sqrt(dx * dx + dy * dy);
	}

}
