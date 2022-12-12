package algorithms.tree;
import java.util.Collection;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 * 
 * AbstractTreeInterface<E>
 * 
 * A simple interface of a generic Tree.
 * 
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public interface AbstractTreeInterface<E> {

	public abstract AbstractNode<E> root();

	public abstract int size();
	
	public abstract int height();

	public abstract Collection<AbstractNode<E>> elements();

	public abstract void preOrder();

	public abstract void postOrder();

	public abstract void levelOrder();

	public abstract void preOrder(VisitorInterface<?> visitor);

	public abstract void postOrder(VisitorInterface<?> visitor);

	public abstract void levelOrder(VisitorInterface<?> visitor);
	
	public abstract String toString();

}