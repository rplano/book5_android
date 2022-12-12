package algorithms.graph;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 * 
 * GraphEdgeList<E>
 * 
 * A simple implementation of a undirected graph using an underlying edge list
 * data structure.
 * 
 * TODO: getAllComponents for a disconnected graph is missing.
 * 
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class GraphEdgeList<E extends Comparable, V> extends
		AbstractGraphEdgeList<E, V> {

	public GraphEdgeList() {
		// edgeList = new HashSet<AbstractEdge<E>>();
		// vertexList = new HashSet<Vertex<V>>();
		edgeList = new LinkedHashSet<AbstractEdge<E>>();
		vertexList = new LinkedHashSet<Vertex<V>>();
	}

	@Override
	public AbstractEdge<E> insertEdge(Vertex<V> v1, Vertex<V> v2, E element) {
		if (!containsVertex(v1)) {
			insertVertex(v1);
		}
		if (!containsVertex(v2)) {
			insertVertex(v2);
		}
		AbstractEdge<E> edge = new EdgeUnDirected<E>(v1, v2, element);
		edgeList.add(edge);
		return edge;
	}

	@Override
	public String toString() {
		return "GraphEdgeList [edgeList=" + edgeList + ", vertexList="
				+ vertexList + "]";
	}

	private static void testFlights() {
		GraphEdgeList<Integer, String> graph = new GraphEdgeList<Integer, String>();
		Vertex<String> v1 = graph.insertVertex(new Vertex<String>("NUE"));
		Vertex<String> v2 = graph.insertVertex(new Vertex<String>("AMS"));
		Vertex<String> v3 = graph.insertVertex(new Vertex<String>("MAD"));
		AbstractEdge<Integer> e1 = graph.insertEdge(v1, v2, 542);
		AbstractEdge<Integer> e2 = graph.insertEdge(v2, v3, 1462);
		AbstractEdge<Integer> e3 = graph.insertEdge(v1, v3, 1524);

		// graph.dfs();
		// graph.bfs();
		graph.dfs(new VisitorInterface<Vertex<String>>() {
			public void visit(Vertex<?> p) {
				System.out.println(p);
			}
		});
		graph.bfs(new VisitorInterface<Vertex<String>>() {
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

	private static void testShortestPath() {
		GraphEdgeList<Integer, String> graph = new GraphEdgeList<Integer, String>();
		Vertex<String> vA = graph.insertVertex(new Vertex<String>("A"));
		Vertex<String> vB = graph.insertVertex(new Vertex<String>("B"));
		Vertex<String> vC = graph.insertVertex(new Vertex<String>("C"));
		Vertex<String> vD = graph.insertVertex(new Vertex<String>("D"));
		Vertex<String> vE = graph.insertVertex(new Vertex<String>("E"));
		Vertex<String> vF = graph.insertVertex(new Vertex<String>("F"));

		AbstractEdge<Integer> e1 = graph.insertEdge(vA, vB, 8);
		AbstractEdge<Integer> e2 = graph.insertEdge(vA, vC, 2);
		AbstractEdge<Integer> e3 = graph.insertEdge(vA, vD, 4);
		AbstractEdge<Integer> e4 = graph.insertEdge(vB, vC, 7);
		AbstractEdge<Integer> e5 = graph.insertEdge(vB, vE, 2);
		AbstractEdge<Integer> e6 = graph.insertEdge(vC, vD, 1);
		AbstractEdge<Integer> e7 = graph.insertEdge(vC, vE, 3);
		AbstractEdge<Integer> e8 = graph.insertEdge(vC, vF, 9);
		AbstractEdge<Integer> e9 = graph.insertEdge(vD, vF, 5);

		graph.dfs(new VisitorInterface<Vertex<String>>() {
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

		try {
			Dijkstra<Integer, String> dijK = new Dijkstra<Integer, String>(
					graph);
			System.out.println("A -> B: " + dijK.shortestPath(vA, vB));
			System.out.println("A -> C: " + dijK.shortestPath(vA, vC));
			System.out.println("A -> D: " + dijK.shortestPath(vA, vD));
			System.out.println("A -> E: " + dijK.shortestPath(vA, vE));
			System.out.println("A -> F: " + dijK.shortestPath(vA, vF));

			Map<Vertex<String>, Integer> dists = dijK.getAllDistances(vA);
			for (Vertex<String> vx : dists.keySet()) {
				System.out.println("A -> " + vx.getElement() + ": "
						+ dists.get(vx));
			}

			// fastest route from B to A:
			System.out.println("Fastest route from A to B: ");
			Map<Vertex<String>, Vertex<String>> predcrs = dijK
					.getAllPredecessors(vA);
			Vertex<String> vTmp = vB;
			while (vTmp != vA) {
				System.out.print(vTmp.getElement() + " -> ");
				vTmp = predcrs.get(vTmp);
			}
			System.out.println(vTmp.getElement());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private static void testPrim() {
		GraphEdgeList<Integer, String> graph = new GraphEdgeList<Integer, String>();

		// from https://de.wikipedia.org/wiki/Algorithmus_von_Prim
		Vertex<String> vA = graph.insertVertex(new Vertex<String>("A"));
		Vertex<String> vB = graph.insertVertex(new Vertex<String>("B"));
		Vertex<String> vC = graph.insertVertex(new Vertex<String>("C"));
		Vertex<String> vD = graph.insertVertex(new Vertex<String>("D"));
		Vertex<String> vE = graph.insertVertex(new Vertex<String>("E"));
		Vertex<String> vF = graph.insertVertex(new Vertex<String>("F"));
		Vertex<String> vG = graph.insertVertex(new Vertex<String>("G"));

		AbstractEdge<Integer> e1 = graph.insertEdge(vA, vB, 7);
		AbstractEdge<Integer> e2 = graph.insertEdge(vA, vD, 5);
		AbstractEdge<Integer> e3 = graph.insertEdge(vB, vC, 8);
		AbstractEdge<Integer> e4 = graph.insertEdge(vB, vD, 9);
		AbstractEdge<Integer> e5 = graph.insertEdge(vB, vE, 7);
		AbstractEdge<Integer> e6 = graph.insertEdge(vC, vE, 5);
		AbstractEdge<Integer> e7 = graph.insertEdge(vD, vE, 15);
		AbstractEdge<Integer> e8 = graph.insertEdge(vD, vF, 6);
		AbstractEdge<Integer> e9 = graph.insertEdge(vE, vF, 8);
		AbstractEdge<Integer> e10 = graph.insertEdge(vE, vG, 9);
		AbstractEdge<Integer> e11 = graph.insertEdge(vF, vG, 11);

		try {
			Prim<Integer, String> prim = new Prim<Integer, String>(graph);

			System.out.println("Minimum spanning tree using Prim: ");
			Map<Vertex<String>, Vertex<String>> tree = prim.findMSP();
			for (Vertex<String> vx : tree.keySet()) {
				System.out.println(vx.getElement() + " -> " + tree.get(vx));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void testKruskal() {
		GraphEdgeList<Integer, String> graph = new GraphEdgeList<Integer, String>();

		// from https://de.wikipedia.org/wiki/Algorithmus_von_Prim
		Vertex<String> vA = graph.insertVertex(new Vertex<String>("A"));
		Vertex<String> vB = graph.insertVertex(new Vertex<String>("B"));
		Vertex<String> vC = graph.insertVertex(new Vertex<String>("C"));
		Vertex<String> vD = graph.insertVertex(new Vertex<String>("D"));
		Vertex<String> vE = graph.insertVertex(new Vertex<String>("E"));
		Vertex<String> vF = graph.insertVertex(new Vertex<String>("F"));
		Vertex<String> vG = graph.insertVertex(new Vertex<String>("G"));

		AbstractEdge<Integer> e1 = graph.insertEdge(vA, vB, 7);
		AbstractEdge<Integer> e2 = graph.insertEdge(vA, vD, 5);
		AbstractEdge<Integer> e3 = graph.insertEdge(vB, vC, 8);
		AbstractEdge<Integer> e4 = graph.insertEdge(vB, vD, 9);
		AbstractEdge<Integer> e5 = graph.insertEdge(vB, vE, 7);
		AbstractEdge<Integer> e6 = graph.insertEdge(vC, vE, 5);
		AbstractEdge<Integer> e7 = graph.insertEdge(vD, vE, 15);
		AbstractEdge<Integer> e8 = graph.insertEdge(vD, vF, 6);
		AbstractEdge<Integer> e9 = graph.insertEdge(vE, vF, 8);
		AbstractEdge<Integer> e10 = graph.insertEdge(vE, vG, 9);
		AbstractEdge<Integer> e11 = graph.insertEdge(vF, vG, 11);

		System.out.println(graph);
		System.out.println("Size: " + graph.size());

		try {
			Kruskal<Integer, String> kruskal = new Kruskal<Integer, String>(
					graph);

			System.out.println("Minimum spanning tree using Kruskal: ");
			Set<AbstractEdge<Integer>> edgs = kruskal.findMSP();
			for (AbstractEdge<Integer> edge : edgs) {
				Vertex<String>[] vtcs = (Vertex<String>[]) edge.getVertices();
				System.out.println(vtcs[0] + " -> " + vtcs[1]);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		testFlights();
		// testShortestPath();
		// testPrim();
		//testKruskal();
	}
}
