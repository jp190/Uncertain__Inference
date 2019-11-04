package csp.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Set;

/**
 * Set implementation backed by an ArrayList.
 * Useful for small unchanging sets.
 * Optional membership test for add().
 */
public class ArrayListSet<E> extends ArrayList<E> implements Set<E> {
	
	private static final long serialVersionUID = 1L;
	
	protected boolean addcheck = true;
	
	public ArrayListSet() {
		super();
	}

	public ArrayListSet(boolean addcheck) {
		super();
		this.addcheck = addcheck;
	}
	
	public ArrayListSet(Collection<E> elements) {
		super(elements);
	}

	public ArrayListSet(E[] elements) {
		super(Arrays.asList(elements));
	}

	@Override
	public boolean add(E element) {
		if (this.addcheck && this.contains(element)) {
			return false;
		} else {
			super.add(element);
			return true;
		}
	}
}
