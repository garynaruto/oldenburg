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

import SkyPath.SkyNode;
import SkyPath.SkyPath;
import buildTree.*;

public class Test {
	@SuppressWarnings("unchecked")
	public static void main(String args[]) throws Exception {
		// Object deserialization 物件反序列化
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
		//System.out.println(">tree============================");
		//graphTree.root.printTree(0);
		
		int[] start ={2938};
		int[] end ={3152};
		SkyPath skyPath = new SkyPath();
		skyPath.inputData(edgeList, graphTree.root.children);
		
		skyPath.multipointSkyline(start, end);
		SkyNode[][] ansNodes = skyPath.multipointSkypathSet.toArray(new SkyNode[skyPath.multipointSkypathSet.size()][]);
		/*
		for(int i=0; i<skyPath.multipointSkypathSet.size(); i++){
			//Object[] ans =skyPath.multipointSkypathSet.get(i);
			
			SkyNode[][] ansNodes = ans.toArray(new SkyNode[ans.length]);
			System.out.println("Path "+(i+1)+" ------>");
			for(Object a : ans){
				System.out.println((SkyNode)a);
			}
		}
		*/
		
		
		
	}
}
