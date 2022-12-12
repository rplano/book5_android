package algorithms.graph;


/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 * 
 * AbstractEdge<E>
 * 
 * A simple implementation of the edge of a graph.
 * 
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public abstract class AbstractEdge<E extends Comparable> implements Comparable<AbstractEdge<E>> {

	protected E element;
	protected String label;
	protected Vertex<?>[] vertices;

	public AbstractEdge(Vertex<?> v1, Vertex<?> v2, E element) {
		this.element = element;
		vertices = new Vertex<?>[2];
		vertices[0] = v1;
		vertices[1] = v2;
	}

	public E getElement() {
		return element;
	}

	public void setElement(E element) {
		this.element = element;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public Vertex<?>[] getVertices() {
		return vertices;
	}

	@Override
	public int compareTo(AbstractEdge<E> e) {
		return this.element.compareTo(e.element);
	}
}