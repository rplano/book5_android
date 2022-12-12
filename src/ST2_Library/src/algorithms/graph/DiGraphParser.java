package algorithms.graph;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 * 
 * DiGraphParser
 * 
 * Implements core functionality for parsing directed graphs from String and
 * File.
 * 
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class DiGraphParser extends AbstractGraphParser {
	private DiGraphEdgeList<String, String> graph = new DiGraphEdgeList<String, String>();
	private Map<String, Vertex<String>> vertices = new HashMap<String, Vertex<String>>();

	@Override
	protected void analyze(String line) {
		StringTokenizer st = new StringTokenizer(line, ",>", false);

		String from = st.nextToken().trim();
		if (!vertices.containsKey(from)) {
			Vertex<String> vtx = graph.insertVertex(new Vertex<String>(from));
			vertices.put(from, vtx);
		}

		String to = st.nextToken().trim();
		if (!vertices.containsKey(to)) {
			Vertex<String> vtx = graph.insertVertex(new Vertex<String>(to));
			vertices.put(to, vtx);
		}

		if (st.hasMoreTokens()) {
			String element = st.nextToken().trim();
			graph.insertEdge(vertices.get(from), vertices.get(to),
					element);
		} else {
			graph.insertEdge(vertices.get(from), vertices.get(to), null);
		}
	}

	@Override
	public DiGraphEdgeList<String, String> parseGraph(String line) {
		super.parseString(line);
		return null;
	}

	@Override
	public DiGraphEdgeList<String, String> parseGraph(File file) {
		super.parseTextFile(file);
		return graph;
	}

	public static void main(String[] args) {
		DiGraphEdgeList<String, String> graph = new DiGraphParser()
				.parseGraph(new File("lectures.txt"));

		System.out.println("dfs starting from random beginning:");
		graph.directedDfs(graph.vertices().iterator().next(), new VisitorInterface<Vertex<String>>() {
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
			TopologicalSort<String, String> toso = new TopologicalSort<String, String>(
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
}
