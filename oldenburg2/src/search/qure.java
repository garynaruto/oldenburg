package search;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

import SkyPath.SkyNode;
import SkyPath.SkyPath;
import buildTree.Edge;
import buildTree.Tree;
import buildTree.Vertex;
import buildTree.Tree.Node;

public class qure {
	public static Tree<Vertex> graphTree;
	public static List<Edge> edgeList;
	
	public static void main(String args[]) throws Exception {
		// Object deserialization 物件反序列化
		/* graphTree */
		FileInputStream fis = new FileInputStream("./data/SerializationGraphTree");
		ObjectInputStream ois = new ObjectInputStream(fis);
		graphTree = (Tree<Vertex>) ois.readObject();
		ois.close();
		/* edgeList */
		fis = new FileInputStream("./data/SerializationEdgeList");
		ois = new ObjectInputStream(fis);
		edgeList = (List<Edge>) ois.readObject();
		ois.close();
		
		
		List<Vertex> l = new ArrayList<Vertex>();
		
		Node<Vertex> n =graphTree.root.children.get(0);
		n.getContainVerte(n, l);
		
		
		System.out.println(">"+n.vertex.vertexID);
		for(Vertex v :l){
			System.out.println(v.vertexID);
		}
		
		/*
		int[] start ={2938};
		int[] end ={3152};
		SkyPath skyPath = new SkyPath();
		skyPath.inputData(edgeList, graphTree.root.children);
		
		skyPath.multipointSkyline(start, end);
		
		for(int i=0; i<skyPath.multipointSkypathSet.size(); i++){
			SkyNode[] ans =skyPath.multipointSkypathSet.get(i);
			System.out.println("Path "+(i+1)+" ------>");
			for(Object a : ans){
				System.out.println(a);
			}
		}*/
	}
	public static List<Vertex[]> findallpath(){
		List<Vertex[]> out = new ArrayList<>();
		
		
		
		return out;
	}
}
