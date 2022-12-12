package algorithms.tree;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 * 
 * BinaryNode<E>
 * 
 * BinaryNode is like OrderedNode, but since it can have maximally two children,
 * it needs to do a little trick when removing elements.<br/>
 * 
 * Bug: it uses ArrayList for children, hence when printing it, can not
 * distinguish between left and right child.
 * 
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class BinaryNode<E> extends AbstractNode<E> {
	private BinaryNode<E> leftNode;
	private BinaryNode<E> rightNode;

	public BinaryNode(E element) {
		this.parent = null;
		this.element = element;
		this.children = new ArrayList<AbstractNode<E>>();
	}

	// this must be private!
	private void setParent(BinaryNode<E> parent) {
		this.parent = parent;
	}

	public void setLeft(BinaryNode<E> node) {
		node.setParent(this);
		leftNode = node;
		children.add(node);
	}

	public void setRight(BinaryNode<E> node) {
		node.setParent(this);
		rightNode = node;
		children.add(node);
	}

	public void removeLeft() {
		BinaryNode<E> node = getLeft();
		node.setParent(null);
		children.remove(node);
		node = null;
	}

	public void removeRight() {
		BinaryNode<E> node = getRight();
		node.setParent(null);
		children.remove(node);
		node = null;
	}

	public BinaryNode<E> getLeft() {
		return leftNode;
	}

	public BinaryNode<E> getRight() {
		return rightNode;
	}

	public boolean hasLeft() {
		return (leftNode != null);
	}

	public boolean hasRight() {
		return (rightNode != null);
	}

	@Override
	public String toString() {
		return "BinaryNode [element=" + element + "]";
	}

	/**
	 * Shows how to use BinaryNode class
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

		// test
		System.out.println(adam);
		System.out.println(adam.isRoot());
		System.out.println(adam.isInternal());
		System.out.println(adam.isExternal());
		System.out.println(adam.getChildren());
		System.out.println(abel.getParent());

		// Abel got killed
		adam.removeRight();
		System.out.println(adam.getChildren());
		System.out.println(abel.getParent());
	}
}
