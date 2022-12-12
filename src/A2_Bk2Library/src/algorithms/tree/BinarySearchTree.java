package algorithms.tree;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 * 
 * BinarySearchTree<E>
 * 
 * A binary search tree implementation using BinaryTree. 
 * 
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class BinarySearchTree<E extends Comparable> extends BinaryTree<E> {

	public BinarySearchTree(BinaryNode<E> root) {
		super(root);
	}
	
	public void add(E word) {
		if (root == null) {
			root = new BinaryNode<E>(word);
		} else {
			BinaryNode<E> current = (BinaryNode<E>) root;

			// first find if word is already in tree,
			// and navigate to approp position
			int comparison = 0;
			while (true) { // loop and a half
				E x = current.getElement();
				comparison = x.compareTo(word);
				if (comparison == 0) {
					// do nothing already in tree
					break;
				} else if (comparison > 0) {
					// go to right
					if (current.hasRight()) {
						current = current.getRight();
					} else {
						break;
					}
				} else {
					// go to left
					if (current.hasLeft()) {
						current = current.getLeft();
					} else {
						break;
					}
				}
			}

			// insert element if not already in tree
			//String x = current.getElement();
			if (comparison > 0) {
				// add to right
				current.setRight(new BinaryNode<E>(word));
			} else if (comparison < 0) {
				// add to left
				current.setLeft(new BinaryNode<E>(word));
			}
		}
	}
	
	public boolean contains(E word) {
		BinaryNode<E> current = (BinaryNode<E>) root;
		while (true) { // loop and a half
			E x = current.getElement();
			int comparison = x.compareTo(word);
			if (comparison == 0) {
				// we found it
				return true;
			} else if (comparison > 0) {
				// go to right
				if (current.hasRight()) {
					current = current.getRight();
				} else {
					break;
				}
			} else {
				// go to left
				if (current.hasLeft()) {
					current = current.getLeft();
				} else {
					break;
				}
			}
		}
		return false;
	}

	public String toString() {
		return "BinarySearchTree [root=" + root + "]";
	}

	public static void main(String[] args) {

		int[] nrs = { 44, 88, 17, 32, 97, 65, 28, 82, 29, 76, 54, 80 };

		// create tree and insert
		BinaryNode<Integer> root = new BinaryNode<Integer>(nrs[0]);
		BinarySearchTree<Integer> tree = new BinarySearchTree<Integer>(root);
		for (int i = 1; i < nrs.length; i++) {
			tree.add(nrs[i]);
		}

		// search:
		System.out.println("Contains 76: " + tree.contains(76));
		System.out.println("Contains 25: " + tree.contains(25));
		
		// list in sorted order:
		tree.inOrder();
	}
}
