package algorithms.tree;

import java.io.File;
import java.util.StringTokenizer;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 * 
 * OrderedTreeParser
 * 
 * Implements core functionality for parsing trees from String and File.
 * 
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class OrderedTreeParser  extends AbstractTreeParser {
	
	public OrderedTreeParser() {
		super();
	}
	
	@Override
	protected void analyze(String line) {
		
		StringTokenizer st = new StringTokenizer(line, "{},", true);
		while (st.hasMoreTokens()) {
			String token = st.nextToken().trim();
			if (token.length() > 0) {
				// System.out.println(token);
				switch (token) {
				case "{":
					currentParentNode = currentNode;
					
					break;
				case "}":
					currentNode = currentParentNode;
					currentParentNode = (OrderedNode<String>) currentParentNode
							.getParent();
					break;
				case ",":

					break;
				default:
					if (root == null) {
						root = new OrderedNode<String>(token);
						currentNode = root;
					} else {
						currentNode = new OrderedNode<String>(token);
						((OrderedNode<String>) currentParentNode).addChild((OrderedNode<String>) currentNode);
					}
					break;
				}
			}
		}
	}


	@Override
	public OrderedTree<String> parseTree(String line) {
		super.parseString(line);
		return new OrderedTree<String>((OrderedNode<String>) root);
	}

	@Override
	public OrderedTree<String> parseTree(File file) {
		super.parseTextFile(file);
		return new OrderedTree<String>((OrderedNode<String>) root);		
	}

	public static void main(String[] args) {
		OrderedTree<String> tree = new OrderedTreeParser()
				.parseTree(new File("adam.txt"));
		// "adam.txt", "abraham.txt", "pilot.txt", "book.txt", "university.txt", "camels_and_whales.txt"

//		System.out.println(tree);
//		System.out.println("size: "+tree.size());
//		System.out.println("height: "+tree.height());
//		System.out.println(tree.elements());

		//new TreePrinter().prettyPrintSimpleVertical(tree);
		//new TreePrinter().prettyPrintSimpleHorizontal(tree);
		
		System.out.println(new OrderedTreeParser().createStringFromTree(tree.root, true));
	}
}
