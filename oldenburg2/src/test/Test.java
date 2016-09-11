package test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import buildTree.*;

public class Test {
	@SuppressWarnings("unchecked")
	public static void main(String args[]) throws Exception {
		// Object deserialization ����ϧǦC��
		/* graphTree */
		Tree<Vertex> graphTree;
		FileInputStream fis = new FileInputStream("./data/SerializationGraphTree");
		ObjectInputStream ois = new ObjectInputStream(fis);
		graphTree = (Tree<Vertex>) ois.readObject();
		ois.close();
		/* edgeList */
		List<Edge> edgeList;
		fis = new FileInputStream("./data/SerializationEdgeList");
		ois = new ObjectInputStream(fis);
		edgeList = (List<Edge>) ois.readObject();
		ois.close();

		System.out.println(">remain Edge=====================");
		for (Edge e : edgeList) {
			System.out.println(e);
		}
		System.out.println(">Tree Vertex===================");
		for (Tree.Node<Vertex> t : graphTree.root.children) {
			System.out.println(t.vertex);
		}
		System.out.println(">tree============================");
		graphTree.root.printTree(0);

	}
}
