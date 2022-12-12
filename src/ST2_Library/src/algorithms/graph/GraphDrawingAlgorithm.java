package algorithms.graph;

import java.util.HashMap;

public abstract class GraphDrawingAlgorithm<E extends Comparable, V> {

	public static final int TRAVERSAL_LINEAR = 3;
	public static final int TRAVERSAL_DFS = 0;
	public static final int TRAVERSAL_DFS_MOST_IMPORTANT = 6;
	public static final int TRAVERSAL_BFS = 1;
	public static final int TRAVERSAL_BFS_MOST_IMPORTANT = 5;
	public static final int TRAVERSAL_BFS_RADIAL = 2;
	public static final int TRAVERSAL_SPRING_FORCE = 4;

	protected int horPos = 0;
	protected int vertPos = 0;
	protected int horPosMax = 0;
	protected int vertPosMax = 0;
	
	protected AbstractGraphInterface<E, V> graph;
	protected HashMap<Vertex<V>, Point> positions;


	public GraphDrawingAlgorithm(AbstractGraphInterface<E, V> graph) {
		this.graph = graph;
	}
	
	public HashMap<Vertex<V>, Point> getPositions() {
		return positions;
	}

	public int getHorPosMax() {
		return horPosMax;
	}

	public int getVertPosMax() {
		return vertPosMax;
	}

	protected void visit(Vertex<V> p) {
		positions.put(p, new Point(horPos + 1, vertPos + 1));
	}

	protected void incrementHorPos() {
		horPos++;
		if (horPos > horPosMax) {
			horPosMax = horPos;
		}
	}
	
	public abstract void execute();
	


	protected void recalculateMinAndMax(HashMap<Vertex<V>, Point> positions) {
		int horMin = Integer.MAX_VALUE;
		int vertMin = Integer.MAX_VALUE;
		int horMax = Integer.MIN_VALUE;
		int vertMax = Integer.MIN_VALUE;

		// find min and max
		for (Vertex<V> vrtx : positions.keySet()) {
			Point p = positions.get(vrtx);
			if (p.x < horMin) {
				horMin = p.x;
			}
			if (p.x > horMax) {
				horMax = p.x;
			}
			if (p.y < vertMin) {
				vertMin = p.y;
			}
			if (p.y > vertMax) {
				vertMax = p.y;
			}
		}

		// re-assign max
		horPosMax = horMax - horMin + 1;
		vertPosMax = vertMax - vertMin + 1;

		// move vertices
		for (Vertex<V> vrtx : positions.keySet()) {
			Point p = positions.get(vrtx);
			positions.put(vrtx, new Point(p.x - horMin + 1, p.y - vertMin + 1));
		}
	}

	protected Vertex<String> findMostImportantVertex(
			AbstractGraphInterface<E, V> graph) {
		Vertex<String> importantVertex = null;
		for (Vertex<V> vertex : graph.vertices()) {
			int nrEdges = graph.incidentEdges(vertex).size();
			//System.out.println(vertex.getElement() + ":" + nrEdges);
			if (importantVertex == null
					|| nrEdges > graph.incidentEdges((Vertex<V>) importantVertex).size()) {
				importantVertex = (Vertex<String>) vertex;
			}
		}
		return importantVertex;
	}

	protected Vertex<String> findLeastImportantVertex(
			AbstractGraphInterface<Integer, String> graph) {
		Vertex<String> importantVertex = null;
		for (Vertex<String> vertex : graph.vertices()) {
			int nrEdges = graph.incidentEdges(vertex).size();
			//System.out.println(vertex.getElement() + ":" + nrEdges);
			if (importantVertex == null
					|| nrEdges < graph.incidentEdges(importantVertex).size()) {
				importantVertex = vertex;
			}
		}
		return importantVertex;
	}
}
