package algorithms.graph;

import java.util.Collection;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 * 
 * AbstractGraphInterface<E>
 * 
 * A simple interface of a generic Graph.
 * 
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public interface AbstractGraphInterface<E extends Comparable, V> {

	public abstract int size();

	public abstract Collection<AbstractEdge<E>> edges();

	public abstract Collection<Vertex<V>> vertices();

	public abstract Vertex<V> insertVertex(Vertex<V> vertex);

	public abstract AbstractEdge<E> insertEdge(Vertex<V> v1, Vertex<V> v2,
			E element);

	public abstract void replaceElement(Vertex<V> vertex, V element);

	public abstract void replaceElement(AbstractEdge<E> edge, E element);

	/**
	 * removes vertex and all it incident edges
	 */
	public abstract Vertex<V> removeVertex(Vertex<V> vertex);

	public abstract AbstractEdge<E> removeEdge(AbstractEdge<E> edge);

	public abstract boolean containsVertex(Vertex<V> vertex);

	public abstract Vertex<V> findVertex(V element);
	
	public abstract boolean containsEdge(AbstractEdge<E> edge);

	public abstract AbstractEdge<E> findEdge(E element);
		
	
	/**
	 * an edge is incident on a vertex, if one of its vertices is that vertex
	 */
	public abstract Collection<AbstractEdge<E>> incidentEdges(Vertex<V> vertex);

	public abstract Vertex<V> opposite(Vertex<V> vertex, AbstractEdge<E> edge);

	public abstract Vertex<V>[] endVertices(AbstractEdge<E> edge);

	public abstract boolean areAdjacent(Vertex<V> vertex1, Vertex<V> vertex2);

	public abstract boolean isTree();

	/**
	 * start at one vertex, do a bfs and check if all vertices were visited
	 */
	public abstract boolean isConnected();

	public abstract void bfs(VisitorInterface<?> visitor);

	public abstract void bfs(Vertex<V> vertex, VisitorInterface<?> visitor);

	public abstract void dfs(VisitorInterface<?> visitor);
	
	public abstract void dfs(Vertex<V> vertex, VisitorInterface<?> visitor);

	/**
	 * uses dfs like traversal
	 */
	public abstract boolean hasCycle() throws Exception;

}