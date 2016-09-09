package test;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Test2 implements Serializable {
	String s;
	public Test2(String s){
		this.s = s;
	}
	public void pp(){
		System.out.println(s);
	}
    public static void main(String args[]) {
    	//Object serialization ����ǦC��
        try {
        	Test2 object1 = new Test2("Hello");
            System.out.println("����1: " + object1);
            FileOutputStream fos = new FileOutputStream("./TestObject");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(object1);
            oos.flush();
            oos.close();
        } catch (Exception e) {
        	e.printStackTrace();
        }
        //Object deserialization ����ϧǦC��
        try {
        	Test2 object2;
            FileInputStream fis = new FileInputStream("./TestObject");
            ObjectInputStream ois = new ObjectInputStream(fis);
            object2 = (Test2) ois.readObject();
            ois.close();
            System.out.println("����2: " + object2);
            object2.pp();
        } catch (Exception e) {
        	e.printStackTrace();
        }
    }
}
 
 