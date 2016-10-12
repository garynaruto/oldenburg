package test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import buildTree.Edge;

public class test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int[] a = {0,1,2,3,4,5,6,7,8}; 
		int[] b = Arrays.copyOfRange(a, 0, 1);
		System.out.println(b.length);
		for(int i : b){
			//System.out.println(i);
		}
		Map<String, String> s = new HashMap<>(4);
		
		s.put("0", "A");
		s.put("1", "B");
		s.put("2", "C");
		s.put("3", "A");
		System.out.println(s.get("0"));
		System.out.println(s.get("1"));
		System.out.println(s.get("2"));
		System.out.println(s.get("7"));
	}

}
