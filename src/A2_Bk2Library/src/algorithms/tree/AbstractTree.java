package algorithms.tree;

import java.util.Collection;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 * 
 * AbstractTree<E>
 * 
 * Abstract class that implements the simple interface of a generic Tree. Note:
 * nothing in here checks if this is really a tree, it might actually be a
 * graph!
 * 
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public abstract class AbstractTree<E> implements AbstractTreeInterface<E> {

	protected AbstractNode<E> root;

	public abstract Collection<AbstractNode<E>> elements();

	public AbstractNode<E> root() {
		return root;
	}

	public int size() {
		return elements().size();
	}

	public int height() {
		return height(root());
	}

	private int height(AbstractNode<E> node) {
		if (node == null) {
			return 0;
		} else {
			int maxHeight = 0;
			for (AbstractNode<E> child : node.getChildren()) {
				int h = height(child);
				if (h > maxHeight) {
					maxHeight = h;
				}
			}
			return maxHeight + 1;
		}
	}

	public void preOrder(VisitorInterface<?> visitor) {
		preOrder(root, (VisitorInterface<E>) visitor);
	}

	private void preOrder(AbstractNode<?> node, VisitorInterface<E> visitor) {
		visitor.visit(node);
		if (node.getChildren() != null) {
			for (AbstractNode<?> child : node.getChildren()) {
				preOrder(child, visitor);
			}
		}
	}

	public void postOrder(VisitorInterface<?> visitor) {
		postOrder(root, (VisitorInterface<E>) visitor);
	}

	private void postOrder(AbstractNode<?> node, VisitorInterface<E> visitor) {
		if (node.getChildren() != null) {
			for (AbstractNode<?> child : node.getChildren()) {
				postOrder(child, visitor);
			}
		}
		visitor.visit(node);
	}

	public void levelOrder(VisitorInterface<?> visitor) {
		int h = height(root);
		for (int i = 1; i <= h; i++) {
			levelOrder(root, (VisitorInterface<E>) visitor, i);
		}
		levelOrder(root, (VisitorInterface<E>) visitor, 0);
	}

	private void levelOrder(AbstractNode<?> node, VisitorInterface<E> visitor,
			int level) {
		if (node == null) {
			return;
		} else if (level == 1) {
			visitor.visit(node);
		} else if (level > 1) {
			for (AbstractNode<?> child : node.getChildren()) {
				levelOrder(child, visitor, level - 1);
			}
		}
	}

	public void preOrder() {
		preOrder(root);
	}

	private void preOrder(AbstractNode<E> node) {
		visit(node);
		if (node.getChildren() != null) {
			for (AbstractNode<E> child : node.getChildren()) {
				preOrder(child);
			}
		}
	}

	public void postOrder() {
		postOrder(root);
	}

	private void postOrder(AbstractNode<E> node) {
		if (node.getChildren() != null) {
			for (AbstractNode<E> child : node.getChildren()) {
				postOrder(child);
			}
		}
		visit(node);
	}

	public void levelOrder() {
		int h = height(root);
		for (int i = 1; i <= h; i++) {
			levelOrder(root, i);
		}
		levelOrder(root, 0);
	}

	private void levelOrder(AbstractNode<E> node, int level) {
		if (node == null) {
			return;
		} else if (level == 1) {
			visit(node);
		} else if (level > 1) {
			for (AbstractNode<E> child : node.getChildren()) {
				levelOrder(child, level - 1);
			}
		}
	}

	protected void visit(AbstractNode<E> node) {
		System.out.println(node);
	}

	public String toString() {
		return "AbstractTree [root=" + root + "]";
	}
}