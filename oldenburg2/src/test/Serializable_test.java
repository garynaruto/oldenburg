package test;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Serializable_test implements Serializable {
	String s;
	public Serializable_test(String s){
		this.s = s;
	}
	public void pp(){
		System.out.println(s);
	}
    public static void main(String args[]) {
    	//Object serialization 物件序列化
        try {
        	Serializable_test object1 = new Serializable_test("Hello");
            System.out.println("物件1: " + object1);
            FileOutputStream fos = new FileOutputStream("./TestObject");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(object1);
            oos.flush();
            oos.close();
        } catch (Exception e) {
        	e.printStackTrace();
        }
        //Object deserialization 物件反序列化
        try {
        	Serializable_test object2;
            FileInputStream fis = new FileInputStream("./TestObject");
            ObjectInputStream ois = new ObjectInputStream(fis);
            object2 = (Serializable_test) ois.readObject();
            ois.close();
            System.out.println("物件2: " + object2);
            object2.pp();
        } catch (Exception e) {
        	e.printStackTrace();
        }
    }
}
 
 