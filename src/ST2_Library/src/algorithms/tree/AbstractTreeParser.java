package algorithms.tree;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.StringTokenizer;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 * 
 * AbstractTreeParser
 * 
 * Implements core functionality for parsing trees from String and File.
 * 
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public abstract class AbstractTreeParser {
	protected AbstractNode<String> root;
	protected AbstractNode<String> currentNode;
	protected AbstractNode<String> currentParentNode;

	public AbstractTreeParser() {
		super();
	}
	
	public abstract AbstractTree<String> parseTree(String line);

	public abstract AbstractTree<String> parseTree(File file);

	protected abstract void analyze(String line);

	protected void parseString(String line) {
		analyze(line);
	}

	protected void parseTextFile(BufferedReader br) {
		try {
			//BufferedReader br = new BufferedReader(new FileReader(file));
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

	public String createStringFromTree(AbstractNode<?> node, boolean pretty) {
		if (pretty) {
			return createStringFromTreePretty(node);
		} else {
			return createStringFromTreeUgly(node);
		}
	}

	private String pad = "";

	private String createStringFromTreePretty(AbstractNode<?> node) {
		String tmp = pad + node.getElement().toString();
		if (node.isInternal()) {
			boolean firstTime = true;
			pad += " ";
			for (AbstractNode<?> child : node.getChildren()) {
				if (firstTime) {
					tmp += " {\n"
							+ createStringFromTreePretty((AbstractNode<?>) child);
					firstTime = false;
				} else {
					tmp += ",\n"
							+ createStringFromTreePretty((AbstractNode<?>) child);
				}
			}
			pad = pad.substring(1);
			tmp += "\n" + pad + "}\n" + pad;
		}
		return tmp;
	}

	private String createStringFromTreeUgly(AbstractNode<?> node) {
		String tmp = node.getElement().toString();
		if (node.isInternal()) {
			boolean firstTime = true;
			for (AbstractNode<?> child : node.getChildren()) {
				if (firstTime) {
					tmp += " { "
							+ createStringFromTreeUgly((AbstractNode<?>) child);
					firstTime = false;
				} else {
					tmp += ", "
							+ createStringFromTreeUgly((AbstractNode<?>) child);
				}
			}
			tmp += " }";
		}
		return tmp;
	}
}