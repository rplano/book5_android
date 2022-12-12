package algorithms.graph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 * 
 * GraphDrawingAlgorithmSpringForce
 * 
 * It starts using bfs for initial distribution. then it moves nodes away, like
 * they would be repelling electic charges. finally it draws the nodes back
 * together, as if they were connected by rubber bands. It uses a greedy algo to
 * do that, because it always picks the edge that is longest for optimization.
 * 
 * TODO: <br/>
 * - it is better to only try 1000 times but try 10 different versions <br/>
 * - MAX_NR_OF_ANNEALINGS should be set from outside <br/>
 * - after some annealing try to move one random vertex to some random position <br/>
 * - after some annealing one may make the grid finer, and allow for small
 * adjustments <br/>
 * 
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class GraphDrawingAlgorithmSpringForce<E extends Comparable, V> extends
		GraphDrawingAlgorithmBFS<E, V> {

	private final int MAX_NR_OF_ANNEALINGS = 1000;

	public GraphDrawingAlgorithmSpringForce(AbstractGraphInterface<E, V> graph) {
		super(graph);
	}

	@Override
	public void execute() {
		positions = new HashMap<Vertex<V>, Point>();
		bfsTraversal(graph);

		simulatedAnnealingEnergy();
		simulatedAnnealingSpring();

		recalculateMinAndMax(positions);
	}

	public void simulatedAnnealingEnergy() {
		recalculateMinAndMax(positions);
		moveVerticesEnergy();
	}

	/**
	 * Imagine at the vertices being electric charges repelling each other.
	 */
	private void moveVerticesEnergy() {
		final int FDGE = 5;
		// init force field
		Point[][] forceField = new Point[horPosMax + FDGE][vertPosMax + FDGE];
		for (int i = 0; i < horPosMax + FDGE; i++) {
			for (int j = 0; j < vertPosMax + FDGE; j++) {
				forceField[i][j] = new Point();
			}
		}
		// calc force field
		for (Vertex<V> vrtx : positions.keySet()) {
			Point p = positions.get(vrtx);
			for (int i = 0; i < horPosMax + FDGE; i++) {
				for (int j = 0; j < vertPosMax + FDGE; j++) {
					int dx = i - (p.x + 2);
					int dy = j - (p.y + 2);
					double len = dx * dx + dy * dy;
					if (len > 0) {
						forceField[i][j].x += dx / len;
					}
					if (len > 0) {
						forceField[i][j].y += dy / len;
					}
				}
			}
		}
		// visualizeForceField(forceField);
		// go through all points and move in direction of field
		for (Vertex<V> vrtx : positions.keySet()) {
			Point p = positions.get(vrtx);
			Point field = forceField[p.x + 2][p.y + 2];
			int dx = (int) Math.signum(field.x);
			int dy = (int) Math.signum(field.y);
			Point np = new Point(p.x + dx, p.y + dy);
			// note: it is possible for tow vertices to be in the same
			// position!!!
			Vertex<V> v2 = findVertexAtPoint(np, positions);
			if (v2 == null) {
				positions.put(vrtx, np);
			}
		}
	}

	private void visualizeForceField(Point[][] forceField) {
		final int FDGE = 5;
		for (int i = 0; i < horPosMax + FDGE; i++) {
			for (int j = 0; j < vertPosMax + FDGE; j++) {
				int dx = forceField[i][j].x;
				int dy = forceField[i][j].y;
				System.out.print(padWithSpaces("" + dx, 3));
			}
			System.out.println();
		}
		System.out.println();

		for (int i = 0; i < horPosMax + FDGE; i++) {
			for (int j = 0; j < vertPosMax + FDGE; j++) {
				int dx = forceField[i][j].x;
				int dy = forceField[i][j].y;
				System.out.print(padWithSpaces("" + dy, 3));
			}
			System.out.println();
		}
	}

	private String padWithSpaces(String s, int len) {
		while (s.length() < len) {
			s = " " + s;
		}
		return s;
	}

	/**
	 * rubber bands. basically try ten times and pick the best.
	 */
	public void simulatedAnnealingSpring() {
		TreeMap<Double, HashMap<Vertex<V>, Point>> costPositionsMap = new TreeMap<Double, HashMap<Vertex<V>, Point>>();
		for (int i = 0; i < 10; i++) {
			HashMap<Vertex<V>, Point> positions4 = simulatedAnnealingSpringDoIt();
			double cost = calculateCostSpring(positions4);
			System.out.println("cost: " + cost);
			costPositionsMap.put(cost, positions4);
		}
		positions = costPositionsMap.values().iterator().next();
		double cost = calculateCostSpring(positions);
		System.out.println("final cost: " + cost);
	}

	private HashMap<Vertex<V>, Point> simulatedAnnealingSpringDoIt() {
		HashMap<Vertex<V>, Point> positions3 = clonePositions(positions);
		recalculateMinAndMax(positions3);
		int annealingCounter = 0;
		double lengthOfLongestEdge = Integer.MAX_VALUE;
		while ((lengthOfLongestEdge >= 2)
				&& (annealingCounter < MAX_NR_OF_ANNEALINGS)) {
			annealingCounter++;
			HashMap<Vertex<V>, Point> copyPositions = clonePositions(positions3);

			double costOld = calculateCostSpring(positions3);
			// System.out.print(annealingCounter + ", costOld: " + costOld);

			AbstractEdge<E> longestEdge = findLongestEdge(positions3);
			lengthOfLongestEdge = lengthOfEdge(longestEdge, positions3);
			moveVerticesRandomly(longestEdge, copyPositions);

			double costNew = calculateCostSpring(copyPositions);
			// System.out.println(", costNew: " + costNew);

			if (costNew <= costOld) {
				positions3 = copyPositions;
			}
		}
		return positions3;
	}

	private void moveVerticesRandomly(AbstractEdge<E> longestEdge,
			HashMap<Vertex<V>, Point> positns) {
		Vertex<V>[] vtcs = (Vertex<V>[]) longestEdge.getVertices();
		moveVertexRandomly(vtcs[0], positns);
		moveVertexRandomly(vtcs[1], positns);
	}

	private void moveVertexRandomly(Vertex<V> vrtx,
			HashMap<Vertex<V>, Point> positns) {
		Point p1 = positns.get(vrtx);
		int dx = (int) (Math.random() * 3) - 1;
		int dy = (int) (Math.random() * 3) - 1;
		Point p2 = new Point(p1.x + dx, p1.y + dy);
		moveVertexFromTo(vrtx, p2, positns);
	}

	private void moveVertexFromTo(Vertex<V> v1, Point newPos,
			HashMap<Vertex<V>, Point> positns) {
		Vertex<V> v2 = findVertexAtPoint(newPos, positns);
		if (v2 == null) {
			positns.put(v1, newPos);
		} else {
			Point p1 = positns.get(v1);
			positns.put(v2, p1);
			positns.put(v1, newPos);
		}
	}

	private double calculateCostSpring(HashMap<Vertex<V>, Point> positns) {
		return costRubberBand(positns);
	}

	/**
	 * Imagine vertices being connected by rubber bands, the shorter the bands,
	 * the lower the cost.
	 */
	private double costRubberBand(HashMap<Vertex<V>, Point> positns) {
		double cost = 0;
		Collection<AbstractEdge<E>> edges = graph.edges();
		for (AbstractEdge<E> edge : edges) {
			Vertex<V>[] vtcs = (Vertex<V>[]) edge.getVertices();
			Point p1 = positns.get(vtcs[0]);
			Point p2 = positns.get(vtcs[1]);
			cost += p1.distance(p2);
		}
		return cost;
	}

	private AbstractEdge<E> findLongestEdge(HashMap<Vertex<V>, Point> positions) {
		AbstractEdge<E> longestEdge = null;
		double maxLength = 0;
		Collection<AbstractEdge<E>> edges = graph.edges();
		for (AbstractEdge<E> edge : edges) {
			if (longestEdge == null) {
				longestEdge = edge;
			}
			double length = lengthOfEdge(edge, positions);
			if (length > maxLength) {
				maxLength = length;
				longestEdge = edge;
			}
		}
		// System.out.print(", maxLength = " + maxLength);
		return longestEdge;
	}

	private double lengthOfEdge(AbstractEdge<E> edge,
			HashMap<Vertex<V>, Point> positions) {
		Vertex<V>[] vtcs = (Vertex<V>[]) edge.getVertices();
		Point p1 = positions.get(vtcs[0]);
		Point p2 = positions.get(vtcs[1]);
		double length = p1.distance(p2);
		return length;
	}

	private HashMap<Vertex<V>, Point> clonePositions(
			HashMap<Vertex<V>, Point> positions) {
		HashMap<Vertex<V>, Point> copyPositions;
		copyPositions = new HashMap<Vertex<V>, Point>();
		for (Vertex<V> vrtx : positions.keySet()) {
			copyPositions.put(vrtx, new Point(positions.get(vrtx)));
		}
		return copyPositions;
	}

	private Vertex<V> findVertexAtPoint(Point p2,
			HashMap<Vertex<V>, Point> positns) {
		for (Vertex<V> vrtx : positns.keySet()) {
			Point p3 = positns.get(vrtx);
			if (p2.equals(p3)) {
				return vrtx;
			}
		}
		return null;
	}

}
