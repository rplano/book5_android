package algorithms.tree;
import java.util.ArrayList;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 * 
 * OrderedNode<E>
 * 
 * OrderedNode is like Node, but uses an ArrayList instead of Set for children,
 * hence the children have an order.
 * 
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class OrderedNode<E> extends AbstractNode<E> {

	public OrderedNode(E element) {
		this.parent = null;
		this.element = element;
		this.children = new ArrayList<AbstractNode<E>>();
	}

	// this must be private!
	private void setParent(OrderedNode<E> parent) {
		this.parent = parent;
	}

	public void addChild(OrderedNode<E> node) {
		node.setParent(this);
		children.add(node);
	}

	public void removeChild(OrderedNode<E> node) {
		node.setParent(null);
		children.remove(node);
	}

	@Override
	public String toString() {
		return "OrderedNode [element=" + element + "]";
	}
	
	/**
	 * Shows how to use OrderedNode class
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		// init
		OrderedNode<String> adam = new OrderedNode<String>("Adam");
		OrderedNode<String> cain = new OrderedNode<String>("Cain");
		cain.addChild(new OrderedNode<String>("Enoch"));
		adam.addChild(cain);
		OrderedNode<String> abel = new OrderedNode<String>("Abel");
		adam.addChild(abel);
		adam.addChild(new OrderedNode<String>("Seth"));
		adam.addChild(new OrderedNode<String>("Awan"));
		adam.addChild(new OrderedNode<String>("Azura"));

		// test
		System.out.println(adam);
		System.out.println(adam.isRoot());
		System.out.println(adam.isInternal());
		System.out.println(adam.isExternal());
		System.out.println(adam.getChildren());
		System.out.println(abel.getParent());

		// Abel got killed
		adam.removeChild(abel);
		System.out.println(adam.getChildren());
		System.out.println(abel.getParent());
	}
}
