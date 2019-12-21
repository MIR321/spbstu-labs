package ru.spbstu.telematics.lab2;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

public class MyArrayList<T> implements IMyArrayList<T>, Iterable<T> {

	private Object[] _items;
	private int _size = 0;
	
	public MyArrayList() {
		_items = new Object[0];
	}
	
	public MyArrayList(Collection<? extends T> col) {
		int count = col.size();
		if (count == 0)
            _items = new Object[0];
		else {
			_items = col.toArray().clone();
			_size = _items.length;
		}
	}
	
	
	public MyArrayList(int capacity) {
		 if (capacity < 0)
             throw new IllegalArgumentException();
         if (capacity == 0)
             _items = new Object[0];
         else {
        	 _items = new Object[capacity];
         }
	}
	
	public void add(int index, T obj) {
		 if (index >= _size || index < 0)
			 throw new ArrayIndexOutOfBoundsException();
		 if (_size == _items.length) 
			 ensureCapacity();
		 
		 for(int i = _size-1; i >= index; i--) {
			 _items[i+1] = _items[i];
		 }
		 _items[index] = obj;
		 _size++;
	}
	
	public void add(T obj) {
		 if (_size == _items.length) 
			 ensureCapacity();
		 _items[_size] = obj;
		 _size++;
	}

	private void ensureCapacity() {
		_items = Arrays.copyOf(_items, (int)(_items.length*1.5+1));
	}

	public int size() {
		return _size;
	}

	public int indexOf(T elem) {
		for(int i = 0; i < _size; i++) {
			if((_items[i] != null  && _items[i].equals(elem))||
					(elem == null && _items[i] == null))
				return i;
		}
		return -1;
	}

	public T remove(int index) {
		
		if (index >= _size || index < 0)
			 throw new ArrayIndexOutOfBoundsException();
		
		T res = (T)_items[index];
		for(int i = index; i < _size-1; i++) {
			_items[i] = _items[i+1];
		}
		_size--;
		
		return res;
	}
	
	public boolean remove(T obj) {
		int index = indexOf(obj);
		
		if(index < 0)
			return false;
		
		remove(index);
		return true;
	}
	
	public boolean contains(T obj) {
		int index = indexOf(obj);
		if(index < 0)
			return false;
		else
			return true;
	}

	public T get(int index) {
		if (index >= _size || index < 0)
			 throw new ArrayIndexOutOfBoundsException();
		return (T)_items[index];
	}
	
	public Iterator<T> iterator() {
		return new Iterator<T>() {
			int cnt = 0;

			public boolean hasNext() {
				return cnt < _size;
			}

			public T next() {
				T result = (T)_items[cnt];
				cnt++;
				return result;
			}
		};
	}

	public T set(int index, T obj) {
		if (index >= _size || index < 0)
			 throw new ArrayIndexOutOfBoundsException();
		T oldElem = (T)_items[index];
		_items[index] = obj;
		return oldElem;
	}
}
