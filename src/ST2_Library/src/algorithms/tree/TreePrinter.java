package algorithms.tree;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 * 
 * TreePrinter<E>
 * 
 * Really ugly code, don't look at it.
 * 
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class TreePrinter {

	// this is a little hack, but it does the job...
	private String[][] tmp;
	private int vertPos = 0;
	private int horPos = 0;

	/**
	 * Is simple, works o.k.
	 * 
	 * @param tree
	 * @return
	 */
	public String[][] prettyPrintSimple(AbstractTree<?> tree) {

		if (tree instanceof BinaryTree<?>) {
			// for binary trees we know the max width
			int height = tree.height();
			int maxWidth = (int) Math.pow(2, (height - 1));
			tmp = new String[height][maxWidth + 1];
		} else {
			// for arbitrary trees we have to guess
			tmp = new String[tree.height() + 1][tree.height() * 10];
		}

		if (tree instanceof BinaryTree<?>) {
			inOrder((BinaryNode<?>) tree.root());
		} else {
			inOrder(tree.root());
		}
		return tmp;
	}

	public void prettyPrintSimpleVertical(AbstractTree<?> tree) {
		prettyPrintSimple(tree);

		for (int i = 0; i < tmp.length; i++) { // 4
			for (int j = 0; j < tmp[0].length; j++) { // 7
				if (tmp[i][j] != null) {
					System.out.print(tmp[i][j] + " ");
				} else {
					System.out.print("  ");
				}
			}
			System.out.println();
		}
	}

	// inspired by binary tree inorder
	private void inOrder(AbstractNode<?> node) {
		// left
		if (node.getChildren() != null && node.getChildren().size() > 0) {
			int size = node.getChildren().size();
			Iterator<?> iter = node.getChildren().iterator();

			// visit first half children
			int i = 0;
			while (i < size / 2) {
				vertPos++;
				AbstractNode<?> child = (AbstractNode<?>) iter.next();
				inOrder(child);
				vertPos--;
				i++;
			}
		}

		// visit
		tmp[vertPos][horPos] = node.getElement().toString();
		horPos++;

		// right
		if (node.getChildren() != null && node.getChildren().size() > 0) {
			int size = node.getChildren().size();
			Iterator<?> iter = node.getChildren().iterator();
			// move to previous position
			for (int i = 0; i < size / 2; i++) {
				iter.next();
			}

			// visit remaining children,
			vertPos++;
			while (iter.hasNext()) {
				AbstractNode<?> child = (AbstractNode<?>) iter.next();
				inOrder(child);
			}
			vertPos--;
		}
	}

	// binary tree inorder
	private void inOrder(BinaryNode<?> node) {
		// left
		if (node.hasLeft()) {
			vertPos++;
			BinaryNode<?> child = node.getLeft();
			inOrder(child);
			vertPos--;
		}

		// visit
		tmp[vertPos][horPos] = node.getElement().toString();
		horPos++;

		// right
		if (node.hasRight()) {
			vertPos++;
			BinaryNode<?> child = node.getRight();
			inOrder(child);
			vertPos--;
		}
	}

	public void prettyPrintSimpleHorizontal(AbstractTree<?> tree) {
		prettyPrintSimple(tree);

		for (int j = 0; j < tmp[0].length; j++) { // 7
			String s = "";
			for (int i = 0; i < tmp.length; i++) { // 4
				if (tmp[i][j] != null) {
					s += tmp[i][j] + padWithSpaces(20 - tmp[i][j].length());
				} else {
					s += padWithSpaces(20);
				}
			}
			if (s.trim().length() > 0) {
				System.out.println(s);
			}
		}
	}

	private void preOrderHorizontal(AbstractNode<?> node) {
		// visit
		tmp[vertPos][horPos] = node.getElement().toString();
		horPos++;

		if (node.getChildren() != null && node.getChildren().size() > 0) {
			vertPos++;
			for (AbstractNode<?> child : node.getChildren()) {
				preOrderHorizontal(child);
			}
			vertPos--;
		}
	}

	private void inOrderHorizontal(BinaryNode<?> node) {
		// left
		if (node.hasLeft()) {
			vertPos++;
			inOrderHorizontal(node.getLeft());
			vertPos--;
		}

		// visit
		tmp[vertPos][horPos] = node.getElement().toString();
		horPos++;

		// right
		if (node.hasRight()) {
			vertPos++;
			inOrderHorizontal(node.getRight());
			vertPos--;
		}
	}

	private Map<AbstractNode<?>, Integer> nodesLengths2;
	private Map<AbstractNode<?>, Integer> nodesPoss2;
	private Map<AbstractNode<?>, Integer> nodesHeight2;

	/**
	 * Looks better, more complicated, but does not work with binary trees!
	 * 
	 * @param <E>
	 * 
	 * @param tree
	 * @return
	 * @throws Exception
	 */
	public <E> char[][] prettyPrint(AbstractTree<?> tree) throws Exception {
		// does not work with binary trees
		if (tree instanceof BinaryTree<?>) {
			throw new Exception(
					"TreePrinter.prettyPrint() does not work with binary trees!");
		}

		final int[] levelPos = new int[10];
		nodesLengths2 = new HashMap<AbstractNode<?>, Integer>();
		nodesPoss2 = new HashMap<AbstractNode<?>, Integer>();
		nodesHeight2 = new HashMap<AbstractNode<?>, Integer>();
		int totalLength = postOrder(tree.root());
		// System.out.println("total length: " + totalLength);

		tree.preOrder(new VisitorInterface<E>() {

			public void visit(AbstractNode<?> p) {
				// System.out.print(p.depth()+":"+p.getElement()+":"+nodesLengths2.get(p)+" ");
				if (p.isRoot()) {
					nodesPoss2.put(p, 0);
					nodesHeight2.put(p, 0);
					levelPos[0] = 0;
					// System.out.println(levelPos[p.depth()]);
				} else {
					if (nodesPoss2.get(p.getParent()) > levelPos[p.depth()]) {
						levelPos[p.depth()] = nodesPoss2.get(p.getParent());
					}
					nodesPoss2.put(p, levelPos[p.depth()]);
					nodesHeight2.put(p, p.depth());
					// System.out.println(levelPos[p.depth()]);
					levelPos[p.depth()] += nodesLengths2.get(p);
				}
			}
		});

		return drawTreeInASCII(tree, totalLength);

	}

	private int postOrder(AbstractNode<?> node) {
		// visit children
		int lenChildren = 0;
		if (node.getChildren() != null && node.getChildren().size() > 0) {
			for (AbstractNode<?> child : node.getChildren()) {
				lenChildren += postOrder(child);
			}
		}

		// visit node
		int lenNode = node.getElement().toString().length() + 1;

		int len = lenChildren;
		if (lenNode > len) {
			len = lenNode;
		}
		// System.out.println(node.getElement()+":"+len + ":"+0
		// +":"+node.depth());
		nodesLengths2.put(node, len);
		return len;
	}

	private char[][] drawTreeInASCII(AbstractTree<?> tree, int totalLength) {
		char[][] graph = new char[tree.height() + 1][totalLength + 1];
		for (int i = 0; i < graph.length; i++) {
			for (int j = 0; j < graph[0].length; j++) {
				graph[i][j] = ' ';
			}
		}
		for (AbstractNode<?> node : nodesPoss2.keySet()) {
			// System.out.println(node.getElement()+":"+nodesPoss2.get(node)+":"+nodesLengths2.get(node)+":"+nodesHeight2.get(node));
			String tmp = node.getElement().toString();
			int x = nodesPoss2.get(node)
					+ (nodesLengths2.get(node) - tmp.length()) / 2;
			int y = nodesHeight2.get(node);
			for (int i = 0; i < tmp.length(); i++) {
				graph[y][x + i] = tmp.charAt(i);
			}
		}
		System.out.println(graph.length + ":" + graph[0].length);
		for (int i = 0; i < graph.length; i++) {
			for (int j = 0; j < graph[0].length; j++) {
				System.out.print(graph[i][j]);
			}
			System.out.println();
		}
		return graph;
	}

	private String padWithSpaces(int n) {
		if (n > 0) {
			return String.format("%" + n + "s", " ");
		}
		return "";
	}

	public static void main(String[] args) {
		OrderedTree<String> tree = new OrderedTreeParser()
				.parseTree("abraham.txt");
		// "adam.txt", "abraham.txt", "pilot.txt", "paper.txt"

		// new TreePrinter().prettyPrintSimpleVertical(tree);
		new TreePrinter().prettyPrintSimpleHorizontal(tree);

		// char[][] treeInAscii = new TreePrinter().prettyPrint(tree);
		// for (int i = 0; i < treeInAscii.length; i++) {
		// for (int j = 0; j < treeInAscii[0].length; j++) {
		// System.out.print(treeInAscii[i][j]);
		// }
		// System.out.println();
		// }
	}
}