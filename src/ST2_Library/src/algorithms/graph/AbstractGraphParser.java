package algorithms.graph;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 * 
 * AbstractGraphParser
 * 
 * Implements core functionality for parsing directed and undirected graphs from String and
 * File.
 * 
 * TODO: export to Trivial Graph Format, https://en.wikipedia.org/wiki/Trivial_Graph_Format
 * 
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public abstract class AbstractGraphParser {

	public AbstractGraphParser() {
		super();
	}

	public abstract AbstractGraphEdgeList<String, String> parseGraph(String line);

	public abstract AbstractGraphEdgeList<String, String> parseGraph(File file);
	
	protected abstract void analyze(String line);

	protected void parseString(String line) {
		analyze(line);
	}

	protected void parseTextFile(BufferedReader br) {
		try {
			while (true) {
				String line = br.readLine();
				if (line == null)
					break;
				if (!line.startsWith("#")) { // ignore comments
					analyze(line);
				}
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	protected void parseTextFile(File file) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			while (true) {
				String line = br.readLine();
				if (line == null)
					break;
				if (!line.startsWith("#")) { // ignore comments
					analyze(line);
				}
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String createStringFromGraph(AbstractGraphInterface<?,?> graph, boolean pretty) {
		if (pretty) {
			return createStringFromGraphPretty(graph);
		} else {
			return createStringFromGraphUgly(graph);
		}
	}
	
	private String createStringFromGraphPretty(AbstractGraphInterface<?,?> graph) {
		String tmp = "";
		for (AbstractEdge<?> edge : graph.edges()) {
			Vertex<String>[] vtcs = (Vertex<String>[]) edge.getVertices();
			if (edge.getElement()!=null) {
				tmp += vtcs[0].getElement() +"-"+vtcs[1].getElement() +"/" +edge.getElement() +",";				
			} else {
				tmp += vtcs[0].getElement() +"-"+vtcs[1].getElement() +",";				
			}
		}
		return tmp;
	}
	
	// basically creates an edge list
	private String createStringFromGraphUgly(AbstractGraphInterface<?,?> graph) {
		String tmp = "";
		for (AbstractEdge<?> edge : graph.edges()) {
			Vertex<String>[] vtcs = (Vertex<String>[]) edge.getVertices();
			if (edge.getElement()!=null) {
				tmp += vtcs[0].getElement() +", "+vtcs[1].getElement() +", " +edge.getElement() +"\n";				
			} else {
				tmp += vtcs[0].getElement() +", "+vtcs[1].getElement() +", 0\n";				
			}
		}
		return tmp;
	}
}
