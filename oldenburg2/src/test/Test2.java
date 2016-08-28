package test;

import java.io.PrintWriter;

public class Test2 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try{
			PrintWriter writer = new PrintWriter("./test.txt", "UTF-8");
			for (int j = 0; j < 3; j++) {
				writer.println("0.0");
			}
			writer.flush();
			writer.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
