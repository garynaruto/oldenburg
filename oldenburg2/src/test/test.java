package test;

import java.util.Arrays;

public class test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int[] a = {0,1,2,3,4,5,6,7,8}; 
		int[] b = Arrays.copyOfRange(a, 0, 1);
		System.out.println(b.length);
		for(int i : b){
			System.out.println(i);
		}
	}

}
