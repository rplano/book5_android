package algorithms.graph;

import java.util.HashMap;
import java.util.Map;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 * 
 * DisjointSet<E>
 * 
 * Disjoint-set data structure needed for Kruskal's algorithm.
 * 
 * @see Disjoint-set data structure, https://en.wikipedia.org/wiki/Disjoint-set_data_structure
 * 
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class DisjointSet<E> {
	static class DT<E> {
		
		private DT<E> parent;
		private E element;
		
		public DT(E element) {
			this.element = element;
		}

		public E getElement() {
			return element;
		}
	}

	private Map<E,DT<E>> elements;
	
	public DisjointSet() {
		elements = new HashMap<E, DT<E>>();
	}

	// function MakeSet(x)
	public void makeSet(DT<E> x) {
		elements.put(x.getElement(), x);
		x.parent = x;
	}
	
	public DT<E> getDT(E element) {
		return elements.get(element);
	}

	// function Find(x)
	public DT<E> find(DT<E> x) {
		if (x.parent == x) {
			return x;
		} else {
			return find(x.parent);
		}
	}

	// function Union(x, y)
	public void union(DT<E> x, DT<E> y) {
		DT<E> xRoot = find(x);
		DT<E> yRoot = find(y);
		xRoot.parent = (DT<E>) yRoot;
	}
	
	public static void main(String[] args) {
		//testWithFriends();
		//testWithVertices();
	}

	private static void testWithVertices() {
		DisjointSet<Vertex<String>> dus = new DisjointSet<Vertex<String>>();
		Vertex<String> vA = new Vertex<String>("A");
		Vertex<String> vB = new Vertex<String>("B");
		Vertex<String> vC = new Vertex<String>("C");
		Vertex<String> vD = new Vertex<String>("D");
		Vertex<String> vE = new Vertex<String>("E");
		DT<Vertex<String>> dtsA = new DT<Vertex<String>>(vA);
		DT<Vertex<String>> dtsB = new DT<Vertex<String>>(vB);
		DT<Vertex<String>> dtsC = new DT<Vertex<String>>(vC);
		DT<Vertex<String>> dtsD = new DT<Vertex<String>>(vD);
		DT<Vertex<String>> dtsE = new DT<Vertex<String>>(vE);
		dus.makeSet(dtsA);	
		dus.makeSet(dtsB);	
		dus.makeSet(dtsC);	
		dus.makeSet(dtsD);	
		dus.makeSet(dtsE);	
		
		// 0 is a friend of 2
		dus.union(dtsA, dtsC);

		// 4 is a friend of 2
		dus.union(dtsE, dtsC);

		// 3 is a friend of 1
		dus.union(dtsD, dtsB);

		// Check if 4 is a friend of 0
		if (dus.find(dtsE) == dus.find(dtsA))
			System.out.println("Yes");
		else
			System.out.println("No");

		// Check if 1 is a friend of 0
		if (dus.find(dtsB) == dus.find(dtsA))
			System.out.println("Yes");
		else
			System.out.println("No");
	}

	private static void testWithFriends() {
		// Let there be 5 persons with ids as
		// 0, 1, 2, 3 and 4
		int n = 5;
		DisjointSet<Integer> dus = new DisjointSet<Integer>();

		DT<Integer>[] dts = new DT[n];
		for (int i = 0; i < dts.length; i++) {
			dts[i] = new DT<Integer>(i);
			dus.makeSet(dts[i]);	
		}
		
		// 0 is a friend of 2
		dus.union(dts[0], dts[2]);

		// 4 is a friend of 2
		dus.union(dts[4], dts[2]);

		// 3 is a friend of 1
		dus.union(dts[3], dts[1]);

		// Check if 4 is a friend of 0
		if (dus.find(dts[4]) == dus.find(dts[0]))
			System.out.println("Yes");
		else
			System.out.println("No");

		// Check if 1 is a friend of 0
		if (dus.find(dts[1]) == dus.find(dts[0]))
			System.out.println("Yes");
		else
			System.out.println("No");
	}
}
