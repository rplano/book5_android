package algorithms.graph;

public class EdgeDirected<E extends Comparable> extends AbstractEdge<E> {

	public EdgeDirected(Vertex<?> v1, Vertex<?> v2, E element) {
		super(v1, v2, element);
	}

	public Vertex<?> getVertexFrom() {
		return vertices[0];
	}

	public Vertex<?> getVertexTo() {
		return vertices[1];
	}

	@Override
	public String toString() {
		return "EdgeDirected [element=" + element + ", label=" + label
				+ ", vertexFrom=" + vertices[0] + ", vertexTo=" + vertices[1]
				+ "]";
	}

}
