package algorithms.tree;
import java.util.HashSet;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 * 
 * UnOrderedNode<E>
 * 
 * UnOrderedNode is basically the same as Node, just shows how to use
 * inheritance.
 * 
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class UnOrderedNode<E> extends AbstractNode<E> {

	public UnOrderedNode(E element) {
		this.parent = null;
		this.element = element;
		this.children = new HashSet<AbstractNode<E>>();
	}

	// this must be private!
	private void setParent(UnOrderedNode<E> parent) {
		this.parent = parent;
	}
	
	public void addChild(UnOrderedNode<E> node){
		node.setParent(this);
		children.add(node);		
	}

	public void removeChild(UnOrderedNode<E> node) {
		node.setParent(null);
		children.remove(node);
	}

	@Override
	public String toString() {
		return "UnOrderedNode [element=" + element + "]";
	}
	
	/**
	 * Shows how to use UnOrderedNode class
	 * 
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		// init
		UnOrderedNode<String> adam = new UnOrderedNode<String>("Adam");
		UnOrderedNode<String> cain = new UnOrderedNode<String>("Cain");
		cain.addChild(new UnOrderedNode<String>("Enoch"));
		adam.addChild(cain);
		UnOrderedNode<String> abel = new UnOrderedNode<String>("Abel");
		adam.addChild(abel);
		adam.addChild(new UnOrderedNode<String>("Seth"));
		adam.addChild(new UnOrderedNode<String>("Awan"));
		adam.addChild(new UnOrderedNode<String>("Azura"));

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
