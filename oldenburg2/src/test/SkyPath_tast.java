package test;

import java.util.Arrays;

import SkyPath.SkyNode;
import SkyPath.SkyPath;

public class SkyPath_tast {
	public static final String nodeFile = "./data/small/sky1.txt";
	public static final String edgeFile = "./data/small/sky2.txt";
	public static void main(String[] args) {
		int[] start = new int[1];
		start[0] = 1;
		int[] a = {0,1,2,3,4,5,6,7,8}; 
		int[] end = Arrays.copyOfRange(a, 4, 5);
		SkyPath skyPath = new SkyPath();
		skyPath.inputData(nodeFile, edgeFile);
		skyPath.multipointSkyline(start, end);
		//Object[] ans =skyPath.multipointSkypathSet.get(0);//裡面就是答案，每個Object[]就是一條路
		int count = skyPath.multipointSkypathSet.size();
		for(int i=0; i<count; i++){
			SkyNode[] ans =skyPath.multipointSkypathSet.get(i);
			System.out.println("Path "+(i+1)+" ------>");
			for(SkyNode b : ans){
				System.out.println(b);
			}
		}
		
	}

}
