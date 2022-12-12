package algorithms.graph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Stack;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 * 
 * TopologocalSort<E,V>
 * 
 * @see Topological sorting, https://en.wikipedia.org/wiki/Topological_sorting
 * @see http://www.geeksforgeeks.org/topological-sorting/
 * 
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class TopologicalSort<E extends Comparable, V> {

	private DiGraphEdgeList<E, V> graph;

	public TopologicalSort(DiGraphEdgeList<E, V> graph) throws Exception {
		if (graph.isConnected()) {// && !graph.hasCycle() ) {
			this.graph = graph;
		} else {
			throw new Exception(
					"Graph must be connected for TopologocalSort to work!");
		}
	}

	/**
	 * Kahn's algorithm.
	 * 
	 * @see www.geeksforgeeks.org/topological-sorting-indegree-based-solution/
	 */
	public Collection<Vertex<V>> sort() throws Exception {
		// fill indegrees of vertices
		Map<Vertex<V>, Integer> inDegree = new HashMap<Vertex<V>, Integer>();
		// Create a queue and enqueue all vertices with indegree 0
		Queue<Vertex<V>> q = new LinkedList<Vertex<V>>();
		for (Vertex<V> vertex : graph.vertices()) {
			int inEdgs = graph.incomingEdges(vertex).size();
			inDegree.put(vertex, inEdgs);
			if (inEdgs == 0) {
				q.add(vertex);
			}
		}

		// Initialize count of visited vertices
		int cnt = 0;

		// store topological ordering of the vertices
		List<Vertex<V>> topOrder = new ArrayList<Vertex<V>>();
		while (!q.isEmpty()) {
			// Extract front of queue
			Vertex<V> u = q.poll();
			// and add it to topological order
			topOrder.add(u);

			// Iterate through all its neighbouring nodes
			// of dequeued node u and decrease their in-degree
			// by 1
			// for (Vertex<V> node : adj[u]) {
			for (EdgeDirected<E> eg : graph.outgoingEdges(u)) {
				Vertex<V> node = (Vertex<V>) eg.getVertexTo();
				int inEdges = inDegree.get(node);
				inDegree.put(node, inEdges - 1);
				// If in-degree becomes zero, add it to queue
				if (inDegree.get(node) == 0) {
					q.add(node);
				}
			}
			cnt++;
		}

		// check if there was a cycle
		if (cnt != graph.size()) {
			throw new Exception(
					"Graph may not have cycles for TopologocalSort to work!");
		}

		return topOrder;
	}

	// The function to do Topological Sort. It uses
	// recursive topologicalSortUtil()

	public Collection<Vertex<V>> sort3() throws Exception {
		Stack<Vertex<V>> stack = new Stack<Vertex<V>>();

		int V = graph.size();

		// Mark all the vertices as not visited
		// boolean visited[] = new boolean[V];
		// for (int i = 0; i < V; i++){
		// visited[i] = false;
		// }
		for (Vertex<V> vertex : graph.vertices()) {
			vertex.setLabel("UNEXPLORED");
		}

		// Call the recursive helper function to store
		// Topological Sort starting from all vertices
		// one by one
		// for (int i = 0; i < V; i++){
		// if (visited[i] == false){
		// topologicalSortUtil(i, visited, stack);
		// }
		// }
		for (Vertex<V> vertex : graph.vertices()) {
			if (vertex.getLabel().equals("UNEXPLORED")) {
				topologicalSortUtil(vertex, stack);
			}
		}

		// Print contents of stack
		List<Vertex<V>> L = new ArrayList<Vertex<V>>();
		while (stack.empty() == false) {
			//System.out.print(stack.pop() + " ");
			L.add(stack.pop());
		}
		return L;
	}

	// A recursive function used by topologicalSort
	private void topologicalSortUtil(Vertex<V> v, Stack<Vertex<V>> stack) {
		v.setLabel("VISITED");
		// Vertex<V> i;

		// Recur for all the vertices adjacent to this
		// vertex
		for (EdgeDirected<E> eg : graph.outgoingEdges(v)) {
			Vertex<V> i = (Vertex<V>) eg.getVertexTo();
			if (i.getLabel().equals("UNEXPLORED")) {
				topologicalSortUtil(i, stack);
			}
		}

		// Push current vertex to stack which stores result
		stack.push(v);
	}

	// // A recursive function used by topologicalSort
	// void topologicalSortUtil(int v, boolean visited[],
	// Stack stack){
	// // Mark the current node as visited.
	// visited[v] = true;
	// Integer i;
	//
	// // Recur for all the vertices adjacent to this
	// // vertex
	// Iterator<Integer> it = adj[v].iterator();
	// while (it.hasNext())
	// {
	// i = it.next();
	// if (!visited[i])
	// topologicalSortUtil(i, visited, stack);
	// }
	//
	// // Push current vertex to stack which stores result
	// stack.push(new Integer(v));
	// }

	/**
	 * Kahn's algorithm.
	 * 
	 * @throws Exception
	 */
	public Collection<Vertex<V>> sort2() throws Exception {
		Map<Vertex<V>, Integer> inDegree = new HashMap<Vertex<V>, Integer>();

		// 1. L ← Empty list that will contain the sorted elements
		List<Vertex<V>> L = new ArrayList<Vertex<V>>();
		// 2. S ← Set of all nodes with no incoming edges
		Stack<Vertex<V>> S = new Stack<Vertex<V>>();
		for (Vertex<V> vertex : graph.vertices()) {
			int inEdgs = graph.incomingEdges(vertex).size();
			inDegree.put(vertex, inEdgs);
			if (inEdgs == 0) {
				S.push(vertex);
			}
		}

		// Initialize count of visited vertices
		int cnt = 0;
		// 3. while S is non-empty do
		while (!S.isEmpty()) {
			// 4. remove a node n from S
			Vertex<V> n = S.pop();
			// 5. add n to tail of L
			L.add(n);

			cnt++;
			// 6. for each node m with an edge e from n to m do
			for (EdgeDirected<E> eg : graph.outgoingEdges(n)) {
				Vertex<V> m = (Vertex<V>) eg.getVertexTo();
				// 7. remove edge e from the graph
				int inEdges = inDegree.get(m);
				inDegree.put(m, inEdges - 1);
				// 8. if m has no other incoming edges then
				if (inDegree.get(m) == 0) {
					// 9. insert m into S
					S.push(m);
				}
			}
		}
		// 10. if graph has edges then
		if (cnt != graph.size()) {
			// 11. return error (graph has at least one cycle)
			throw new Exception(
					"Graph may not have cycles for TopologocalSort to work!");
		}
		// 12. else
		// 13. return L (a topologically sorted order)
		return L;
	}

}
