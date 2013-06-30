package org.wiki.walker.collections;

public interface Ordered<T> {

	public boolean contains(T t);
	
	public boolean add(T t);
	
	public boolean remove(T t);
	
	public T poll();
	
	public void clear();
	
	public boolean isEmpty();
}
