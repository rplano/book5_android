package algorithms.graph;

public class Vertex<E> {

	private E element;
	private String label;

	public Vertex(E element) {
		this.element = element;
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

	@Override
	public String toString() {
		return "Vertex [element=" + element + ", label=" + label + "]";
	}
}
