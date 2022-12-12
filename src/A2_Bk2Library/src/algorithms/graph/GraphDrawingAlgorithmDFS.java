package algorithms.graph;

import java.util.Collection;
import java.util.HashMap;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 * 
 * GraphDrawingAlgorithmDFS
 * 
 * Traverses a graph using a dfs traversal.
 * 
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class GraphDrawingAlgorithmDFS<E extends Comparable, V> extends
		GraphDrawingAlgorithm<E, V> {

	
	public GraphDrawingAlgorithmDFS(AbstractGraphInterface<E, V> graph) {
		super(graph);
	}

	@Override
	public void execute() {
		positions = new HashMap<Vertex<V>, Point>();
		dfsTraversal(graph);
	}

	/**
	 * simple dfs traversal.
	 */
	private void dfsTraversal(AbstractGraphInterface<E, V> graph) {
		for (Vertex<V> vertex : graph.vertices()) {
			vertex.setLabel("UNEXPLORED");
		}
		for (AbstractEdge<E> edge : graph.edges()) {
			edge.setLabel("UNEXPLORED");
		}
		for (Vertex<V> vertex : graph.vertices()) {
			if (vertex.getLabel().equals("UNEXPLORED")) {
				dfsRecurse(vertex, new int[40]);
			}
		}
		horPosMax++;
		vertPosMax++;
	}

	private void dfsRecurse(Vertex<V> vertex, int[] horPosns) {
		vertex.setLabel("VISITED");
		horPos = horPosns[vertPos];
		if (horPos > horPosMax) {
			horPosMax = horPos;
		}
		visit(vertex);
		horPosns[vertPos]++;

		Collection<AbstractEdge<E>> incidentEdges = graph.incidentEdges(vertex);
		for (AbstractEdge<E> edge : incidentEdges) {
			// for (AbstractEdge<E> edge : incidentEdges(vertex)) {
			if (edge.getLabel().equals("UNEXPLORED")) {
				Vertex<V> w = graph.opposite(vertex, edge);
				if (w.getLabel().equals("UNEXPLORED")) {
					edge.setLabel("DISCOVERY");
					vertPos++;
					if (vertPos > vertPosMax) {
						vertPosMax = vertPos;
					}
					dfsRecurse(w, horPosns);
					// horPos = horPosns[vertPos];
					// horPos++;
					vertPos--;
				} else {
					edge.setLabel("BACK");
					// vertPos--;
				}
			}
		}
	}
}
