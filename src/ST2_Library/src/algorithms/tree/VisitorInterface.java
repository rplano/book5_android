package algorithms.tree;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 * 
 * VisitorInterface<E>
 * 
 * If you have heard of the visitor design pattern (gof) then you will like this.
 * 
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public interface VisitorInterface<E> {

	public abstract void visit(AbstractNode<?> node);
	
}
