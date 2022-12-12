package algorithms.graph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 * 
 * AbstractGraphEdgeList<E>
 * 
 * A simple implementation of a graph using an underlying edge list data
 * structure.
 * 
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public abstract class AbstractGraphEdgeList<E extends Comparable, V> implements
		AbstractGraphInterface<E, V> {

	protected Collection<AbstractEdge<E>> edgeList;
	protected Collection<Vertex<V>> vertexList;

	public AbstractGraphEdgeList() {
		super();
	}

	@Override
	public int size() {
		return vertexList.size();
	}

	@Override
	public Collection<AbstractEdge<E>> edges() {
		return edgeList;
	}

	@Override
	public Collection<Vertex<V>> vertices() {
		return vertexList;
	}

	@Override
	public Vertex<V> insertVertex(Vertex<V> vertex) {
		vertexList.add(vertex);
		return vertex;
	}

	@Override
	public void replaceElement(Vertex<V> vertex, V element) {
		vertex.setElement(element);
	}

	@Override
	public void replaceElement(AbstractEdge<E> edge, E element) {
		edge.setElement(element);
	}

	@Override
	public Vertex<V> removeVertex(Vertex<V> vertex) {
		Collection<AbstractEdge<E>> edges = incidentEdges(vertex);
		for (AbstractEdge<E> edge : edges) {
			removeEdge(edge);
		}
		vertexList.remove(vertex);
		return vertex;
	}

	@Override
	public AbstractEdge<E> removeEdge(AbstractEdge<E> edge) {
		edgeList.remove(edge);
		return edge;
	}

	@Override
	public boolean containsVertex(Vertex<V> vertex) {
		return vertexList.contains(vertex);
	}

	@Override
	public Vertex<V> findVertex(V element) {
		for (Vertex<V> vertex : vertexList) {
			if (vertex.getElement().equals(element)) {
				return vertex;
			}
		}
		return null;
	}

	@Override
	public boolean containsEdge(AbstractEdge<E> edge) {
		return edgeList.contains(edge);
	}

	@Override
	public AbstractEdge<E> findEdge(E element) {
		for (AbstractEdge<E> edge : edgeList) {
			if (edge.getElement().equals(element)) {
				return edge;
			}
		}
		return null;
	}

	@Override
	public Collection<AbstractEdge<E>> incidentEdges(Vertex<V> vertex) {
		List<AbstractEdge<E>> el = new ArrayList<AbstractEdge<E>>();
		for (AbstractEdge<E> edge : edgeList) {
			Vertex<V>[] verts = (Vertex<V>[]) edge.getVertices();
			for (int i = 0; i < verts.length; i++) {
				if (verts[i].equals(vertex)) {
					el.add(edge);
				}
			}
		}
		return el;
	}

	@Override
	public Vertex<V> opposite(Vertex<V> vertex, AbstractEdge<E> edge) {
		Vertex<V>[] verts = (Vertex<V>[]) edge.getVertices();
		if (verts[0].equals(vertex)) {
			return verts[1];
		} else if (verts[1].equals(vertex)) {
			return verts[0];
		} else {
			return null;
		}
	}

	@Override
	public Vertex<V>[] endVertices(AbstractEdge<E> edge) {
		return (Vertex<V>[]) edge.getVertices();
	}

	@Override
	public boolean areAdjacent(Vertex<V> vertex1, Vertex<V> vertex2) {
		Collection<AbstractEdge<E>> edges = incidentEdges(vertex1);
		for (AbstractEdge<E> edge : edges) {
			if (opposite(vertex1, edge).equals(vertex2)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean isTree() {
		try {
			if (isConnected() && !hasCycle()) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean isConnected() {
		for (Vertex<V> vertex : vertexList) {
			vertex.setLabel("UNEXPLORED");
		}
		for (AbstractEdge<E> edge : edgeList) {
			edge.setLabel("UNEXPLORED");
		}
		// start with one vertex:
		final Collection<Vertex<V>> vertices = new HashSet<Vertex<V>>();
		Vertex<V> vertex = vertexList.iterator().next();
		bfsIterate(vertex, new VisitorInterface<Vertex<String>>() {
			public void visit(Vertex<?> p) {
				vertices.add((Vertex<V>) p);
			}
		});
		if (vertices.size() == this.size()) {
			return true;
		}
		return false;
	}

	@Override
	public void bfs(VisitorInterface<?> visitor) {
		for (Vertex<V> vertex : vertexList) {
			vertex.setLabel("UNEXPLORED");
		}
		for (AbstractEdge<E> edge : edgeList) {
			edge.setLabel("UNEXPLORED");
		}
		for (Vertex<V> vertex : vertexList) {
			if (vertex.getLabel().equals("UNEXPLORED")) {
				bfsIterate(vertex, visitor);
			}
		}
	}

	@Override
	public void bfs(Vertex<V> startVertex, VisitorInterface<?> visitor) {
		for (Vertex<V> vertex : vertexList) {
			vertex.setLabel("UNEXPLORED");
		}
		for (AbstractEdge<E> edge : edgeList) {
			edge.setLabel("UNEXPLORED");
		}
		if (startVertex.getLabel().equals("UNEXPLORED")) {
			bfsIterate(startVertex, visitor);
		}
	}

	private void bfsIterate(Vertex<V> vertex, VisitorInterface<?> visitor) {
		List<Vertex<V>>[] L = new List[40]; // better to use List of List

		int i = 0;
		L[i] = new ArrayList<Vertex<V>>();
		L[i].add(vertex);

		vertex.setLabel("VISITED");
		visitor.visit(vertex);

		while (!L[i].isEmpty()) {
			L[i + 1] = new ArrayList<Vertex<V>>();

			for (Vertex<V> v : L[i]) {
				for (AbstractEdge<E> e : incidentEdges(v)) {
					if (e.getLabel().equals("UNEXPLORED")) {

						Vertex<V> w = opposite(v, e);
						if (w.getLabel().equals("UNEXPLORED")) {
							e.setLabel("DISCOVERY");

							w.setLabel("VISITED");
							visitor.visit(w);

							L[i + 1].add(w);

						} else {
							e.setLabel("CROSS");
						}
					}
				}
			}
			i++;
		}
	}

	@Override
	public void dfs(VisitorInterface<?> visitor) {
		for (Vertex<V> vertex : vertexList) {
			vertex.setLabel("UNEXPLORED");
		}
		for (AbstractEdge<E> edge : edgeList) {
			edge.setLabel("UNEXPLORED");
		}
		for (Vertex<V> vertex : vertexList) {
			if (vertex.getLabel().equals("UNEXPLORED")) {
				dfsRecurse(vertex, visitor);
			}
		}
	}

	@Override
	public void dfs(Vertex<V> startVertex, VisitorInterface<?> visitor) {
		for (Vertex<V> vertex : vertexList) {
			vertex.setLabel("UNEXPLORED");
		}
		for (AbstractEdge<E> edge : edgeList) {
			edge.setLabel("UNEXPLORED");
		}
		if (startVertex.getLabel().equals("UNEXPLORED")) {
			dfsRecurse(startVertex, visitor);
		}
	}

	private void dfsRecurse(Vertex<V> vertex, VisitorInterface<?> visitor) {
		vertex.setLabel("VISITED");
		// visit(vertex);
		visitor.visit(vertex);
		for (AbstractEdge<E> edge : incidentEdges(vertex)) {
			if (edge.getLabel().equals("UNEXPLORED")) {
				Vertex<V> w = opposite(vertex, edge);
				if (w.getLabel().equals("UNEXPLORED")) {
					edge.setLabel("DISCOVERY");
					dfsRecurse(w, visitor);
				} else {
					edge.setLabel("BACK");
				}
			}
		}
	}

	@Override
	public boolean hasCycle() throws Exception {
		if (isConnected()) {
			for (Vertex<V> vertex : vertexList) {
				vertex.setLabel("UNEXPLORED");
			}
			for (AbstractEdge<E> edge : edgeList) {
				edge.setLabel("UNEXPLORED");
			}
			return detectCycle(vertexList.iterator().next());
		} else {
			throw new Exception("Graph is not connected!");
		}
	}

	/**
	 * if we have a BACK edge, we have a cycle
	 */
	private boolean detectCycle(Vertex<V> vertex) {
		vertex.setLabel("VISITED");
		// visit(vertex);
		for (AbstractEdge<E> edge : incidentEdges(vertex)) {
			if (edge.getLabel().equals("UNEXPLORED")) {
				Vertex<V> w = opposite(vertex, edge);
				if (w.getLabel().equals("UNEXPLORED")) {
					edge.setLabel("DISCOVERY");
					return detectCycle(w);
				} else {
					edge.setLabel("BACK");
					return true;
				}
			}
		}
		return false;
	}

}