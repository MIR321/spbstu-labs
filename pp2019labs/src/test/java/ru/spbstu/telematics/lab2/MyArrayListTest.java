package ru.spbstu.telematics.lab2;
import org.junit.Test;
import org.junit.Assert;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class MyArrayListTest {

	public static <T> boolean colEquals(ArrayList<T> arr, MyArrayList<T> marr) {
		if(arr == null || marr == null)
			return false;
		if(arr.size() != marr.size())
			return false;
		for(int i = 0; i < arr.size(); i++) {
			if(!arr.get(i).equals(marr.get(i)))
				return false;
		}
		return true;
	}
	
	MyArrayList<String> marr;
	ArrayList<String> arr;
	
	
	public void createLists() {
		arr = new ArrayList<String>();
		arr.add("123");
		arr.add("234");
		marr = new MyArrayList<String>(arr);
	}
	
	@Test
	public void testGetAndSet() {
		createLists();
		Assert.assertTrue(arr.get(1).equals(marr.get(1)));
		arr.set(1, "rgr");
		Assert.assertFalse(arr.get(1).equals(marr.get(1)));
		marr.set(1, "rgr");
		Assert.assertTrue(arr.get(1).equals(marr.get(1)));
	}
	
	@Test
	public void testAdd() {
		createLists();
		marr.add("123");
		marr.add("rgr");
		arr.add("123");
		arr.add("rgr");
		Assert.assertTrue(MyArrayListTest.<String>colEquals(arr, marr));
		arr.add("rgr");
		Assert.assertFalse(MyArrayListTest.<String>colEquals(arr, marr));
		marr.add(marr.size()-1,"rgr");
		Assert.assertTrue(MyArrayListTest.<String>colEquals(arr, marr));
		marr.add(1,"some");
		arr.add(1,"some");
		Assert.assertTrue(MyArrayListTest.<String>colEquals(arr, marr));
	}
	
	@Test
	public void testRemove() {
		createLists();
		Assert.assertEquals(arr.remove(1), marr.remove(1));
		createLists();
		Assert.assertNotEquals(arr.remove(1), marr.remove(0));
		createLists();
		marr.add("123");
		marr.add("rgr");
		arr.add("123");
		arr.add("rgr");
		marr.remove("123");
		arr.remove("123");
		Assert.assertTrue(MyArrayListTest.<String>colEquals(arr, marr));
		marr.remove("rgr3");
		arr.remove("rgr3");
		Assert.assertTrue(MyArrayListTest.<String>colEquals(arr, marr));
	}
	
	@Test
	public void testContains() {
		createLists();
		marr.add("123");
		marr.add("rgr");
		arr.add("123");
		arr.add("rgr");
		arr.add(null);
		marr.add(null);
		Assert.assertTrue(marr.contains("rgr") == arr.contains("rgr")
				== marr.contains("123") == arr.contains("123")
				== marr.contains(null) == arr.contains(null)
				== (marr.contains("bvc") == arr.contains("bvc")));
	}
	
	@Test
	public void testSize() {
		createLists();
		Assert.assertTrue(arr.size() == marr.size());
	}

}
