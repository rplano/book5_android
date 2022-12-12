package algorithms.graph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 * 
 * GraphDrawingAlgorithmBFSMostImportant
 * 
 * Traverses a graph using a bfs traversal, but starting from the most important vertex.
 * 
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class GraphDrawingAlgorithmBFSMostImportant<E extends Comparable, V>
		extends GraphDrawingAlgorithm<E, V> {

	public GraphDrawingAlgorithmBFSMostImportant(
			AbstractGraphInterface<E, V> graph) {
		super(graph);
	}

	@Override
	public void execute() {
		positions = new HashMap<Vertex<V>, Point>();
		Vertex<V> mostImportantVertex = (Vertex<V>) findMostImportantVertex(graph);
		bfsTraversal(graph,mostImportantVertex);
	}

	private void bfsTraversal(AbstractGraphInterface<E, V> graph,
			Vertex<V> startVertex) {
		for (Vertex<V> vertex : graph.vertices()) {
			vertex.setLabel("UNEXPLORED");
		}
		for (AbstractEdge<E> edge : graph.edges()) {
			edge.setLabel("UNEXPLORED");
		}
		if (startVertex.getLabel().equals("UNEXPLORED")) {
			bfsIterate(startVertex);
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
