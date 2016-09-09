package test;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import buildTree.*;
public class Test {
	public static void main(String args[]) {
		//Object deserialization 物件反序列化
        try {
        	Tree<Vertex> graphTree;
            FileInputStream fis = new FileInputStream("./data/SerializationGraphTree");
            ObjectInputStream ois = new ObjectInputStream(fis);
            graphTree = (Tree<Vertex>) ois.readObject();
            ois.close();
            List<Edge> edgeList;
            fis = new FileInputStream("./data/SerializationEdgeList");
            ois = new ObjectInputStream(fis);
            edgeList = (List<Edge>) ois.readObject();
            ois.close();
            Main2.writeTreeFile(edgeList, graphTree,"./data/open.txt");
            
            System.out.println(">remain Edge=====================");
    		for (Edge e : edgeList) {
    			System.out.println(e);
    		}
    		System.out.println(">Tree Vertex===================");
    		for (Tree.Node<Vertex> t: graphTree.root.children) {
    			System.out.println(t.vertex);
    		}
    		System.out.println(">tree============================");
    		graphTree.root.printTree(0);
        } catch (Exception e) {
        	e.printStackTrace();
        }
	}
}
