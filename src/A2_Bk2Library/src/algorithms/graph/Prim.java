package algorithms.graph;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 * 
 * Prim<E,V>
 * 
 * A simple implementation of Prim's algorithm based on pseudo code from
 * Wikipedia. Not the most efficient, but does the job.
 * 
 * @see https://de.wikipedia.org/wiki/Algorithmus_von_Prim
 * 
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class Prim<E extends Comparable, V> {

	private AbstractGraphInterface<E, V> graph;

	private Map<Vertex<V>, Integer> wert;

	public Prim(AbstractGraphInterface<E, V> graph) throws Exception {
		if (graph.isConnected()) {
			this.graph = graph;
		} else {
			throw new Exception("Graph must be connected for Prim to work!");
		}
	}

	public Map<Vertex<V>, Vertex<V>> findMSP() {
		Map<Vertex<V>, Vertex<V>> pi = new HashMap<Vertex<V>, Vertex<V>>();
		wert = new HashMap<Vertex<V>, Integer>();
		// V_G: Knotenmenge von G
		// w: Gewichtsfunktion für Kantenlänge
		// Q: Prioritätswarteschlange
		// π[u]: Elternknoten von Knoten u im Spannbaum
		// Adj[u]: Adjazenzliste von u (alle Nachbarknoten)
		// wert[u]: Abstand von u zum entstehenden Spannbaum

		// r: Startknoten (r ∈ V_G)
		Vertex<V> r = graph.vertices().iterator().next();
		for (Vertex<V> u : graph.vertices()) {
			if (u.getElement().equals("D")) {
				r = u;
			}
		}
		System.out.println("D="+r.getElement());
		// 05 wert[r] <- 0
		wert.put(r, 0);

		// 01 Q <- V_G //Initialisierung
		Comparator<Vertex<V>> comparator = new VertexComparator();
		Queue<Vertex<V>> Q = new PriorityQueue<Vertex<V>>(graph.vertices()
				.size(), comparator);

		// 02 für alle u ∈ Q
		for (Vertex<V> u : graph.vertices()) {
			// 7 if v ≠ source
			if (u != r) {
				// 03 wert[u] <- ∞
				wert.put(u, Integer.MAX_VALUE);

				// 04 π[u] <- 0
				pi.put(u, null);
			}
			// 11 Q.add_with_priority(v, dist[v])
			Q.add(u);
		}

		Vertex<V> u;
		// 06 solange Q ≠ nothing
		// 07 u <- extract_min(Q)
		while ((u = Q.poll()) != null) {
			// 08 für alle v ∈ Adj[u]
			for (AbstractEdge<E> edge : graph.incidentEdges(u)) {
				Vertex<V> v = graph.opposite(u, edge);
				// 09 wenn v ∈ Q und w(u,v) < wert[v]
				if (Q.contains(v) && (int) edge.getElement() < wert.get(v)) {
					// 10 dann π[v] <- u
					pi.put(v, u);
					// 11 wert[v] <- w(u,v)
					wert.put(v, (int) edge.getElement());
					// funny way of updating prio queue:
					Q.remove(v);
					Q.add(v);
				}
			}
		}
		return pi;
	}

	private class VertexComparator implements Comparator<Vertex<V>> {
		@Override
		public int compare(Vertex<V> v1, Vertex<V> v2) {
			return wert.get(v1) - wert.get(v2);
		}
	}
}
