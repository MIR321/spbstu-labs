package ru.spbstu.telematics.lab2;

public interface IMyArrayList<T> {
	int size(); 
	boolean contains(T elem);
	void add(int index, T obj);
	void add(T obj);
	T remove(int index); 
	boolean remove(T obj); 
	T get(int index); 
	T set(int index, T obj);
	int indexOf(T elem);
}
