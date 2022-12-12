package algorithms.tree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 * 
 * OrderedTree<E>
 * 
 * A simple implementation of an ordered, generic AbstractTree class, basically
 * a wrapper for the Node class.
 * 
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class OrderedTree<E> extends AbstractTree<E> {

	public OrderedTree(OrderedNode<E> root) {
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
					decendants.add((OrderedNode<E>) child);
					List<AbstractNode<E>> temp = getDescendants((OrderedNode<E>) child);
					if (temp != null) {
						decendants.addAll(temp);
					}
				}
				return decendants;
			}
		}
		return new ArrayList<AbstractNode<E>>();
	}

	public String toString() {
		return "OrderedTree [root=" + root + "]";
	}

	/**
	 * Shows how to use OrderedTree class
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

		// create a tree
		OrderedTree<String> humans = new OrderedTree<String>(adam);

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

		System.out.println();
		System.out.println("depth of Adam: "+adam.depth());
		System.out.println("depth of Abel: "+abel.depth());
		//System.out.println(humans.prettyPrint());
		
//		System.out.println();
//		humans.preOrder(new VisitorInterface<String>() {
//			public void visit(AbstractNode<String> p) {
//				System.out.println(p);
//			}
//		});
//		System.out.println();
//		humans.postOrder(new VisitorInterface<String>() {
//			public void visit(AbstractNode<String> p) {
//				System.out.println(p);
//			}
//		});		
//		System.out.println();
//		humans.levelOrder(new VisitorInterface<String>() {
//			public void visit(AbstractNode<String> p) {
//				System.out.println(p.depth()+": "+p);
//			}
//		});		
	}

}
