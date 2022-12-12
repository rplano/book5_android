package algorithms.graph;

public class EdgeUnDirected<E extends Comparable> extends AbstractEdge<E> {

	public EdgeUnDirected(Vertex<?> v1, Vertex<?> v2, E element) {
		super(v1,v2,element);
	}

	@Override
	public String toString() {
		return "EdgeUnDirected [element=" + element + ", label=" + label + ", vertices=["
				+ vertices[0] + ", " + vertices[1] + "]]";
	}

}
