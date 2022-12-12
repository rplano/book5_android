package algorithms.graph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 * 
 * GraphDrawingAlgorithmBFS
 * 
 * Traverses a graph using a bfs traversal.
 * 
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class GraphDrawingAlgorithmBFS<E extends Comparable, V> extends
		GraphDrawingAlgorithm<E, V> {

	
	public GraphDrawingAlgorithmBFS(AbstractGraphInterface<E, V> graph) {
		super(graph);
	}

	@Override
	public void execute() {
		positions = new HashMap<Vertex<V>, Point>();
		bfsTraversal(graph);
	}

	/**
	 * simple bfs traversal.
	 */
	protected void bfsTraversal(AbstractGraphInterface<E, V> graph) {
		for (Vertex<V> vertex : graph.vertices()) {
			vertex.setLabel("UNEXPLORED");
		}
		for (AbstractEdge<E> edge : graph.edges()) {
			edge.setLabel("UNEXPLORED");
		}
		for (Vertex<V> vertex : graph.vertices()) {
			if (vertex.getLabel().equals("UNEXPLORED")) {
				bfsIterate(vertex);
			}
		}
	}


	private void bfsIterate(Vertex<V> vertex) {
		List<Vertex<V>>[] L = new List[40]; // better to use List of List

		int i = 0;
		L[i] = new ArrayList<Vertex<V>>();
		L[i].add(vertex);

		vertex.setLabel("VISITED");
		visit(vertex);
		incrementHorPos();

		while (!L[i].isEmpty()) {
			L[i + 1] = new ArrayList<Vertex<V>>();

			for (Vertex<V> v : L[i]) {
				Collection<AbstractEdge<E>> incidentEdges = graph
						.incidentEdges(v);
				for (AbstractEdge<E> e : incidentEdges) {
					if (e.getLabel().equals("UNEXPLORED")) {

						Vertex<V> w = graph.opposite(v, e);
						if (w.getLabel().equals("UNEXPLORED")) {
							e.setLabel("DISCOVERY");

							w.setLabel("VISITED");
							visit(w);
							incrementHorPos();

							L[i + 1].add(w);

						} else {
							e.setLabel("CROSS");
						}
					}
				}
			}
			i++;
			vertPosMax = vertPos;
			vertPos++;
			horPos = 0;
		}

	}
	
}
