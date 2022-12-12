package algorithms.graph;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 * 
 * Dijkstra<E,V>
 * 
 * A simple implementation of the Dijkstra algorithm based on pseudo code from
 * Wikipedia. Not the most efficient, but does the job.
 * 
 * @see Dijkstra's algorithm,
 *      https://en.wikipedia.org/wiki/Dijkstra%27s_algorithm
 * 
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class Dijkstra<E extends Comparable, V> {

	private AbstractGraphInterface<E, V> graph;

	private Map<Vertex<V>, Integer> distances;
	private Map<Vertex<V>, Vertex<V>> predecessors;

	public Dijkstra(AbstractGraphInterface<E, V> graph) throws Exception {
		if (graph.isConnected()) {
			this.graph = graph;			
		} else {
			throw new Exception("Graph must be connected for Dijkstra to work!");
		}
	}

	public int shortestPath(Vertex<V> source, Vertex<V> dest) {
		executeDijkstra(source);
		return distances.get(dest);
	}

	public Map<Vertex<V>, Integer> getAllDistances(Vertex<V> source) {
		executeDijkstra(source);
		return distances;
	}

	public Map<Vertex<V>, Vertex<V>> getAllPredecessors(Vertex<V> source) {
		executeDijkstra(source);
		return predecessors;
	}

	// 1 function Dijkstra(Graph, source):
	private void executeDijkstra(Vertex<V> source) {
		distances = new HashMap<Vertex<V>, Integer>();
		predecessors = new HashMap<Vertex<V>, Vertex<V>>();

		// 2 dist[source] ← 0 // Initialization
		distances.put(source, 0);

		// 4 create vertex set Q
		Comparator<Vertex<V>> comparator = new VertexComparator();
		Queue<Vertex<V>> Q = new PriorityQueue<Vertex<V>>(graph.vertices()
				.size(), comparator);

		// 6 for each vertex v in Graph:
		for (Vertex<V> vertex : graph.vertices()) {
			// 7 if v ≠ source
			if (vertex != source) {
				// 8 dist[v] ← INFINITY // Unknown distance from source to v
				distances.put(vertex, Integer.MAX_VALUE);
				// 9 prev[v] ← UNDEFINED // Predecessor of v
				predecessors.put(vertex, null);
			}
			// 11 Q.add_with_priority(v, dist[v])
			Q.add(vertex);
		}

		Vertex<V> u;
		// 14 while Q is not empty: // The main loop
		// 15 u ← Q.extract_min() // Remove and return best vertex
		while ((u = Q.poll()) != null) {
			// 16 for each neighbor v of u: // only v that is still in Q
			for (AbstractEdge<E> edge : graph.incidentEdges(u)) {
				Vertex<V> v = graph.opposite(u, edge);
				if (Q.contains(v)) { // only v that is still in Q

					// 17 alt ← dist[u] + length(u, v)
					int alt = distances.get(u) + (int) edge.getElement();
					// 18 if alt < dist[v]
					if (alt < distances.get(v)) {
						// 19 dist[v] ← alt
						distances.put(v, alt);
						// 20 prev[v] ← u
						predecessors.put(v, u);
						// 21 Q.decrease_priority(v, alt)
						Q.remove(v);
						Q.add(v);
					}
				}
			}
		}

		// 23 return dist[], prev[]
	}

	private class VertexComparator implements Comparator<Vertex<V>> {
		@Override
		public int compare(Vertex<V> v1, Vertex<V> v2) {
			return distances.get(v1) - distances.get(v2);
		}
	}
}
