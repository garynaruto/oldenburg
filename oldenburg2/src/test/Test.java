package test;

public class Test {
	
	public static int rangeNum = 8;// level range
	public static int[] range;
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		range = new int[rangeNum];
		range[0] = 6999/rangeNum;
		for(int i=1; i<range.length; i++){
			range[i] = range[i-1] + range[0];
		}
		for(int i=0; i<range.length; i++){
			System.out.println(range[i]);;
		}
	}

}
