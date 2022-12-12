package algorithms.tree;

import java.io.BufferedReader;
import java.io.File;
import java.util.StringTokenizer;

public class BinaryTreeParser extends AbstractTreeParser {

	private boolean firstToken = true;
	
	public BinaryTreeParser() {
		super();
		firstToken = true;
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
					firstToken = true;
					break;
				case "}":
					currentNode = currentParentNode;
					currentParentNode = (BinaryNode<String>) currentParentNode
							.getParent();
					break;
				case ",":
					firstToken = false;
					break;
				default:
					if (root == null) {
						root = new BinaryNode<String>(token);
						currentNode = root;
					} else {
						currentNode = new BinaryNode<String>(token);
						if (firstToken) {
							((BinaryNode<String>) currentParentNode)
									.setLeft((BinaryNode<String>) currentNode);
						} else {
							((BinaryNode<String>) currentParentNode)
									.setRight((BinaryNode<String>) currentNode);
						}
					}
					break;
				}
			}
		}
	}

	@Override
	public BinaryTree<String> parseTree(String line) {
		super.parseString(line);
		return new BinaryTree<String>((BinaryNode<String>) root);
	}

	@Override
	public BinaryTree<String> parseTree(BufferedReader br) {
		super.parseTextFile(br);
		return new BinaryTree<String>((BinaryNode<String>) root);
	}

	@Override
	public BinaryTree<String> parseTree(File file) {
		super.parseTextFile(file);
		return new BinaryTree<String>((BinaryNode<String>) root);
	}

	public static void main(String[] args) {
		String tmp = "+{*{2,-{a,1}},*{3,b}}";
		// String tree = "+{,2}";
		// String tree = "+{2,}";
		BinaryTree<String> tree = new BinaryTreeParser().parseTree(tmp);
		
//		BinaryTree<String> tree = new BinaryTreeParser().parseTree(new File(
//				"eat.txt"));
		// "eat.txt", "math.txt", "pilot.txt", "sort_algorithm.txt", "type_face.txt", "visualization_method.txt", "fibonacci.txt"

		System.out.println(tree);
		System.out.println("size: " + tree.size());
		System.out.println("height: " + tree.height());
		System.out.println(tree.elements());

		// new TreePrinter().prettyPrintSimpleVertical(tree);
		// new TreePrinter().prettyPrintSimpleHorizontal(tree);

		System.out.println(new BinaryTreeParser().createStringFromTree(
				tree.root, true));
	}
}
