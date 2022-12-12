package algorithms.tree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 * 
 * BinaryTree<E>
 * 
 * A binary tree implementation using AbstractTree. 
 * 
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class BinaryTree<E> extends AbstractTree<E> {

	public BinaryTree(BinaryNode<E> root) {
		this.root = root;
	}

	public Collection<AbstractNode<E>> elements() {
		List<AbstractNode<E>> elements = getDescendants(root);
		elements.add(root);
		return elements;
	}

	private List<AbstractNode<E>> getDescendants(AbstractNode<E> ancestor) {
		if (ancestor != null) {
			Collection<AbstractNode<E>> children = ancestor.getChildren();
			if ((children != null) && (children.size() > 0)) {
				List<AbstractNode<E>> decendants = new ArrayList<AbstractNode<E>>();
				for (AbstractNode<E> child : children) {
					decendants.add((BinaryNode<E>) child);
					List<AbstractNode<E>> temp = getDescendants((BinaryNode<E>) child);
					if (temp != null) {
						decendants.addAll(temp);
					}
				}
				return decendants;
			}
		}
		return new ArrayList<AbstractNode<E>>();
	}

	public void inOrder() {
		inOrder((BinaryNode<E>) root);
	}

	private void inOrder(BinaryNode<E> node) {
		if (node.hasLeft()) {
			inOrder(node.getLeft());
		}
		visit(node);
		if (node.hasRight()) {
			inOrder(node.getRight());
		}
	}

	public void inOrder(VisitorInterface<E> visitor) {
		inOrder((BinaryNode<E>) root, visitor);
	}

	private void inOrder(BinaryNode<E> node, VisitorInterface<E> visitor) {
		if (node.hasLeft()) {
			inOrder(node.getLeft());
		}
		visitor.visit(node);
		if (node.hasRight()) {
			inOrder(node.getRight());
		}
	}

	public String toString() {
		return "BinaryTree [root=" + root + "]";
	}

	/**
	 * Shows how to use BinaryTree class
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		// init
		BinaryNode<String> adam = new BinaryNode<String>("Adam");
		BinaryNode<String> cain = new BinaryNode<String>("Cain");
		cain.setLeft(new BinaryNode<String>("Enoch"));
		adam.setLeft(cain);
		BinaryNode<String> abel = new BinaryNode<String>("Abel");
		adam.setRight(abel);

		// create a tree
		BinaryTree<String> humans = new BinaryTree<String>(adam);

		// test
		System.out.println(humans);
		System.out.println(humans.size());
		System.out.println(humans.height());
		System.out.println(humans.elements());
		System.out.println("pre-order:");
		humans.preOrder();
		System.out.println("post-order:");
		humans.postOrder();
		System.out.println("level-order:");
		humans.levelOrder();
		System.out.println("in-order:");
		humans.inOrder();

		System.out.println();
		System.out.println("depth of Adam: " + adam.depth());
		System.out.println("depth of Abel: " + abel.depth());
		// System.out.println(humans.prettyPrint());
		
		new algorithms.tree.TreePrinter().prettyPrintSimpleVertical(humans);
//		char[][] treeInAscii = new TreePrinter().prettyPrint(humans);
//		for (int i = 0; i < treeInAscii.length; i++) {
//			for (int j = 0; j < treeInAscii[0].length; j++) {
//				System.out.print(treeInAscii[i][j]);
//			}
//			System.out.println();
//		}
		
//		System.out.println();
//		humans.inOrder(new VisitorInterface<String>() {
//			public void visit(AbstractNode<String> p) {
//				System.out.println(p);
//			}
//		});
	}
}
