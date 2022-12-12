package algorithms.tree;
import java.util.Collection;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 * 
 * AbstractNode<E>
 * 
 * Abstract class that implements a generic Node.
 * 
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public abstract class AbstractNode<E> {

	protected AbstractNode<E> parent;
	protected Collection<AbstractNode<E>> children;
	protected E element;

	//public abstract void addChild(AbstractNode<E> node) throws Exception;

	public E getElement() {
		return element;
	}

	public void setElement(E element) {
		this.element = element;
	}

	public AbstractNode<E> getParent() {
		return parent;
	}

	public Collection<AbstractNode<E>> getChildren() {
		return children;
	}

	public boolean isRoot() {
		return (parent == null);
	}

	public boolean isInternal() {
		return !isExternal();
	}

	public boolean isExternal() {
		return (children.size() == 0);
	}
	
	public int depth() {
		if (isRoot()){
			return 0;			
		} else {
			return 1+getParent().depth();		
		}
	}
	
	@Override
	public String toString() {
		return "AbstractNode [element=" + element + "]";
	}

}