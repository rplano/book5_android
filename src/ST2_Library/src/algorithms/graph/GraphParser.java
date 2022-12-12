package algorithms.graph;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;

import algorithms.tree.AbstractNode;
import algorithms.tree.OrderedNode;
import algorithms.tree.OrderedTree;
import algorithms.tree.OrderedTreeParser;
import algorithms.tree.VisitorInterface;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 * 
 * GraphParser
 * 
 * Implements core functionality for parsing graphs from String and File.
 * 
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class GraphParser extends AbstractGraphParser {

	private GraphEdgeList<String, String> graph;
	private Map<String, Vertex<String>> vertices;

	public GraphParser() {
		super();
		graph = new GraphEdgeList<String, String>();
		vertices = new HashMap<String, Vertex<String>>();
	}


	@Override
	protected void analyze(String line) {
		StringTokenizer st = new StringTokenizer(line, ",", false);

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

	/**
	 * without edges: "u-v,v-w,w-x,x-y,y-z"
	 * with edges: "U-V-a,V-W-b,W-X-e,X-U-f,V-X-g,W-Z-c,Z-Z-h,Z-W-d"
	 */
	private void analyzeString(String line) {

		StringTokenizer st = new StringTokenizer(line, ",", false);
		while (st.hasMoreTokens()) {
			String pair = st.nextToken();
			StringTokenizer st2 = new StringTokenizer(pair, "-", false);

			String from = st2.nextToken().trim();
			if (!vertices.containsKey(from)) {
				Vertex<String> vtx = graph
						.insertVertex(new Vertex<String>(from));
				vertices.put(from, vtx);
			}

			String to = st2.nextToken().trim();
			if (!vertices.containsKey(to)) {
				Vertex<String> vtx = graph.insertVertex(new Vertex<String>(to));
				vertices.put(to, vtx);
			}

			String edge = null;
			if (st2.hasMoreTokens()) {
				edge = st2.nextToken().trim();
			}
			graph.insertEdge(vertices.get(from), vertices.get(to), edge);
		}
	}

	@Override
	public GraphEdgeList<String, String> parseGraph(String line) {
		analyzeString(line);
		return graph;
	}

	@Override
	public GraphEdgeList<String, String> parseGraph(File file) {
		super.parseTextFile(file);
		return graph;
	}
	
	/**
	 * Always insert root as first node.
	 * @param line
	 * @return
	 */
	public GraphEdgeList<String, String> parseTree(String line) {
		OrderedTree<String> tree = new OrderedTreeParser().parseTree(line);

		AbstractNode<String> root = tree.root();
		preOrder(root);
		
		return graph;
	}

	private void preOrder(AbstractNode<String> node) {
		//visit(node);
		String parentName = ((OrderedNode<String>)node).getElement();
		Vertex<String> vtx = graph.insertVertex(new Vertex<String>(parentName));
		vertices.put(parentName, vtx);
		
		if (node.getChildren() != null) {
			for (AbstractNode<String> child : node.getChildren()) {
				preOrder(child);
				String childName = ((OrderedNode<String>)child).getElement();
				graph.insertEdge(vertices.get(parentName), vertices.get(childName), null);
			}
		}
	}


	public static void main(String[] args) {
		// testFileParser();
		//testStringParser();
		testTreeParser();
	}

	private static void testTreeParser() {
		String str = "A{B{C,D},E{F,G,H},I}";
		GraphEdgeList<String, String> graph =  new GraphParser().parseTree(str);
		System.out.println(graph.vertices());
		System.out.println(graph);
		System.out.println("Size: " + graph.size());
		System.out
		.println(new GraphParser().createStringFromGraph(graph, true));		
	}


	private static void testStringParser() {
		//String edges = "joe-food,joe-dog";
		 String edges = "joe-food,joe-dog,joe-tea,joe-cat,joe-table,table-plate,plate-food,food-mouse,food-dog,mouse-cat,table-cup,cup-tea,dog-cat,cup-spoon,plate-fork,dog-flea1,dog-flea2,flea1-flea2,plate-knife";
		// String edges
		// ="zero-one,one-two,two-three,three-four,four-five,five-six,six-seven,seven-zero";
		// String edges
		// ="zero-one,zero-two,zero-three,zero-four,zero-five,zero-six,zero-seven,zero-eight,zero-nine,one-ten,two-twenty,three-thirty,four-fourty,five-fifty,six-sixty,seven-seventy,eight-eighty,nine-ninety,ten-twenty/80,twenty-thirty/80,thirty-fourty/80,fourty-fifty/80,fifty-sixty/80,sixty-seventy/80,seventy-eighty/80,eighty-ninety/80,ninety-ten/80,one-two/30,two-three/30,three-four/30,four-five/30,five-six/30,six-seven/30,seven-eight/30,eight-nine/30,nine-one/30";
		// String edges
		// ="a1-a2,a2-a3,a3-a4,a4-a5,a5-a6,b1-b2,b2-b3,b3-b4,b4-b5,b5-b6,c1-c2,c2-c3,c3-c4,c4-c5,c5-c6,x-a1,x-b1,x-c1,x-a6,x-b6,x-c6";

		GraphParser gp = new GraphParser();
		GraphEdgeList<String, String> graph = gp.parseGraph(edges);

		//System.out.println(gp.vertices);
		System.out.println(graph.vertices());
		System.out.println(graph);
		System.out.println("Size: " + graph.size());

		System.out
				.println(new GraphParser().createStringFromGraph(graph, true));
		System.out.println(new GraphParser()
				.createStringFromGraph(graph, false));

	}

	private static void testFileParser() {
		GraphEdgeList<String, String> graph = new GraphParser()
				.parseGraph(new File("kruskal.txt")); // "flights.txt",
														// "shortest_path.txt"

		// graph.dfs(new VisitorInterface<Vertex<String>>() {
		// public void visit(Vertex<?> p) {
		// System.out.println(p);
		// }
		// });
		// graph.bfs(new VisitorInterface<Vertex<String>>() {
		// public void visit(Vertex<?> p) {
		// System.out.println(p);
		// }
		// });

		System.out.println(graph);
		System.out.println("Size: " + graph.size());
		System.out.println("isConnected: " + graph.isConnected());
		System.out.println("isTree: " + graph.isTree());
		try {
			System.out.println("hasCycle: " + graph.hasCycle());
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out
				.println(new GraphParser().createStringFromGraph(graph, true));
		System.out.println(new GraphParser()
				.createStringFromGraph(graph, false));
	}
}
