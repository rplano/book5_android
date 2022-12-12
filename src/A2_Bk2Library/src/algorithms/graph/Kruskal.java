package algorithms.graph;

import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

import algorithms.graph.DisjointSet.DT;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 * 
 * Kruskal<E,V>
 * 
 * A simple implementation of Kruskal's algorithm based on pseudo code from
 * Wikipedia. Not the most efficient, but does the job.
 * 
 * @see Kruskal's algorithm, https://en.wikipedia.org/wiki/Kruskal's_algorithm
 * 
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class Kruskal<E extends Comparable, V> {

	private AbstractGraphInterface<E, V> graph;

	public Kruskal(AbstractGraphInterface<E, V> graph) throws Exception {
		if (graph.isConnected()) {
			this.graph = graph;
		} else {
			throw new Exception("Graph must be connected for Kruskal to work!");
		}
	}

	public Set<AbstractEdge<E>> findMSP() {
		DisjointSet<Vertex<V>> dus = new DisjointSet<Vertex<V>>();
		// 1 A = ∅
		Set<AbstractEdge<E>> A = new HashSet<AbstractEdge<E>>();

		// 2 foreach v ∈ G.V:
		for (Vertex<V> v : graph.vertices()) {
			// 3 MAKE-SET(v)
			DT<Vertex<V>> dt = new DT<Vertex<V>>(v);
			dus.makeSet(dt);
		}

		// init a prio queue
		Queue<AbstractEdge<E>> Q = new PriorityQueue<AbstractEdge<E>>(graph
				.edges().size());
		for (AbstractEdge<E> edge : graph.edges()) {
			Q.add(edge);
		}

		// 4 foreach (u, v) in G.E ordered by weight(u, v), increasing:
		//for (EdgeDirected<E> edge : graph.edges()) {
		while (A.size() < graph.size()-1) {
			AbstractEdge<E> edge = Q.poll();
			Vertex<V>[] vtcs = (Vertex<V>[]) edge.getVertices();
			DT<Vertex<V>> u = dus.getDT(vtcs[0]);
			DT<Vertex<V>> v = dus.getDT(vtcs[1]);

			// 5 if FIND-SET(u) ≠ FIND-SET(v):
			if (dus.find(u) != dus.find(v)) {
				// 6 A = A ∪ {(u, v)}
				A.add(edge);
				// 7 UNION(u, v)
				dus.union(u, v);
			}
		}
		// 8 return A
		return A;
	}

}
