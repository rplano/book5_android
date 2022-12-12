package algorithms.graph;

import java.util.HashMap;

public class GraphDrawingAlgorithmLinear<E extends Comparable, V> extends
		GraphDrawingAlgorithm<E, V> {

	
	public GraphDrawingAlgorithmLinear(AbstractGraphInterface<E, V> graph) {
		super(graph);
	}

	@Override
	public void execute() {
		positions = new HashMap<Vertex<V>, Point>();
		linearTraverse(graph);
	}
	
	protected void linearTraverse(AbstractGraphInterface<E, V> graph) {
		for (Vertex<V> vertex : graph.vertices()) {
			vertex.setLabel("VISITED");
			visit(vertex);
			incrementHorPos();
		}
	}

}
