package algorithms.graph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 * 
 * DiGraphEdgeList<E>
 * 
 * A simple implementation of a directed graph using an underlying edge list
 * data structure.
 * 
 * TODO: the isTree() method may not work properly for DiGraphs.
 * 
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class DiGraphEdgeList<E extends Comparable, V> extends
		AbstractGraphEdgeList<E, V> {

	public DiGraphEdgeList() {
		edgeList = new HashSet<AbstractEdge<E>>();
		vertexList = new HashSet<Vertex<V>>();
	}

	@Override
	public AbstractEdge<E> insertEdge(Vertex<V> v1, Vertex<V> v2, E element) {
		if (!containsVertex(v1)) {
			insertVertex(v1);
		}
		if (!containsVertex(v2)) {
			insertVertex(v2);
		}
		AbstractEdge<E> edge = new EdgeDirected<E>(v1, v2, element);
		edgeList.add(edge);
		return edge;
	}

	@Override
	public String toString() {
		return "DiGraphEdgeList [edgeList=" + edgeList + ", vertexList="
				+ vertexList + "]";
	}

	@Override
	public void bfs(VisitorInterface<?> visitor) {
		// TODO: does nothing, should be overriden!!!
	}

	@Override
	public void dfs(VisitorInterface<?> visitor) {
		// TODO: does nothing, should be overriden!!!
	}

	public void directedDfs(Vertex<V> start, VisitorInterface<?> visitor) {
		for (Vertex<V> vertex : vertexList) {
			vertex.setLabel("UNEXPLORED");
		}

		directedDfsRecursive(start, visitor);
	}

	private void directedDfsRecursive(Vertex<V> vertex,
			VisitorInterface<?> visitor) {
		vertex.setLabel("VISITED");
		visitor.visit(vertex);
		for (EdgeDirected<E> edge : outgoingEdges(vertex)) {
			Vertex<V> w = opposite(vertex, edge);
			if (w.getLabel().equals("UNEXPLORED")) {
				directedDfsRecursive(w, visitor);
			}
		}
	}

	public Collection<EdgeDirected<E>> outgoingEdges(Vertex<V> vertex) {
		List<EdgeDirected<E>> el = new ArrayList<EdgeDirected<E>>();
		for (AbstractEdge<E> edge : edgeList) {
			EdgeDirected<E> dirEdge = (EdgeDirected<E>) edge;
			if (dirEdge.getVertexFrom() == vertex) {
				el.add(dirEdge);
			}
		}
		return el;
	}

	public Collection<EdgeDirected<E>> incomingEdges(Vertex<V> vertex) {
		List<EdgeDirected<E>> el = new ArrayList<EdgeDirected<E>>();
		for (AbstractEdge<E> edge : edgeList) {
			EdgeDirected<E> dirEdge = (EdgeDirected<E>) edge;
			if (dirEdge.getVertexTo() == vertex) {
				el.add(dirEdge);
			}
		}
		return el;
	}

	// show there is no escape from Island of Death
	private static void testPirateIsland() {
		DiGraphEdgeList<String, String> graph = new DiGraphEdgeList<String, String>();

		Vertex<String> v1 = graph.insertVertex(new Vertex<String>(
				"Pirate Island"));
		Vertex<String> v2 = graph.insertVertex(new Vertex<String>(
				"Island of Death"));
		Vertex<String> v3 = graph.insertVertex(new Vertex<String>(
				"Treasure Island"));

		AbstractEdge<String> e1 = graph.insertEdge(v1, v2, "");
		AbstractEdge<String> e2 = graph.insertEdge(v1, v3, "");
		AbstractEdge<String> e3 = graph.insertEdge(v3, v1, "");

		// graph.dfs();
		// graph.bfs();
		System.out.println("Starting from 'Island of Death':");
		graph.directedDfs(v2, new VisitorInterface<Vertex<String>>() {
			public void visit(Vertex<?> p) {
				System.out.println(p);
			}
		});

		System.out.println("Starting from 'Pirate Island':");
		graph.directedDfs(v1, new VisitorInterface<Vertex<String>>() {
			public void visit(Vertex<?> p) {
				System.out.println(p);
			}
		});

		System.out.println("Starting from 'Treasure Island':");
		graph.directedDfs(v3, new VisitorInterface<Vertex<String>>() {
			public void visit(Vertex<?> p) {
				System.out.println(p);
			}
		});

		System.out.println(graph);
		System.out.println("Size: " + graph.size());
		System.out.println("isConnected: " + graph.isConnected());
		System.out.println("isTree: " + graph.isTree());
		try {
			System.out.println("hasCycle: " + graph.hasCycle());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void testTimePlan() {
		DiGraphEdgeList<Integer, String> graph = new DiGraphEdgeList<Integer, String>();
		// project with five milestones
		Vertex<String> v10 = graph.insertVertex(new Vertex<String>("10"));
		Vertex<String> v20 = graph.insertVertex(new Vertex<String>("20"));
		Vertex<String> v30 = graph.insertVertex(new Vertex<String>("30"));
		Vertex<String> v40 = graph.insertVertex(new Vertex<String>("40"));
		Vertex<String> v50 = graph.insertVertex(new Vertex<String>("50"));

		AbstractEdge<Integer> e1 = graph.insertEdge(v10, v20, 4);
		AbstractEdge<Integer> e2 = graph.insertEdge(v10, v30, 3);
		AbstractEdge<Integer> e3 = graph.insertEdge(v20, v50, 3);
		AbstractEdge<Integer> e4 = graph.insertEdge(v30, v40, 1);
		AbstractEdge<Integer> e5 = graph.insertEdge(v30, v50, 3);
		AbstractEdge<Integer> e6 = graph.insertEdge(v40, v50, 3);

		// try {
		// System.out.println("hasCycle: " + graph.hasCycle());
		// } catch (Exception e) {
		// e.printStackTrace();
		// }

		try {
			TopologicalSort<Integer, String> toso = new TopologicalSort<Integer, String>(
					graph);
			System.out.println("\n1. sort: ");
			for (Vertex<String> vertex : toso.sort()) {
				System.out.print(vertex.getElement() + " -> ");
			}
			System.out.println("\n2. sort: ");
			for (Vertex<String> vertex : toso.sort2()) {
				System.out.print(vertex.getElement() + " -> ");
			}
			System.out.println("\n3. sort: ");
			for (Vertex<String> vertex : toso.sort3()) {
				System.out.print(vertex.getElement() + " -> ");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 4 -> 5 -> 2 -> 0 -> 3 -> 1
	private static void testTopologicalSort() {
		DiGraphEdgeList<Integer, String> graph = new DiGraphEdgeList<Integer, String>();

		Vertex<String> v0 = graph.insertVertex(new Vertex<String>("0"));
		Vertex<String> v1 = graph.insertVertex(new Vertex<String>("1"));
		Vertex<String> v2 = graph.insertVertex(new Vertex<String>("2"));
		Vertex<String> v3 = graph.insertVertex(new Vertex<String>("3"));
		Vertex<String> v4 = graph.insertVertex(new Vertex<String>("4"));
		Vertex<String> v5 = graph.insertVertex(new Vertex<String>("5"));

		AbstractEdge<Integer> e1 = graph.insertEdge(v5, v2, 1);
		AbstractEdge<Integer> e2 = graph.insertEdge(v5, v0, 1);
		AbstractEdge<Integer> e3 = graph.insertEdge(v4, v0, 1);
		AbstractEdge<Integer> e4 = graph.insertEdge(v4, v1, 1);
		AbstractEdge<Integer> e5 = graph.insertEdge(v2, v3, 1);
		AbstractEdge<Integer> e6 = graph.insertEdge(v3, v1, 1);

		try {
			TopologicalSort<Integer, String> toso = new TopologicalSort<Integer, String>(
					graph);
			System.out.println("First sort: ");
			Collection<Vertex<String>> verts = toso.sort();
			for (Vertex<String> vertex : verts) {
				System.out.print(vertex.getElement() + " -> ");
			}

			System.out.println("\nSecond sort: ");
			Collection<Vertex<String>> verts2 = toso.sort2();
			for (Vertex<String> vertex : verts2) {
				System.out.print(vertex.getElement() + " -> ");
			}

			System.out.println("\nThird sort: ");
			Collection<Vertex<String>> verts3 = toso.sort3();
			for (Vertex<String> vertex : verts3) {
				System.out.print(vertex.getElement() + " -> ");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		testPirateIsland();
		// testTimePlan();
		// testTopologicalSort();
	}
}
