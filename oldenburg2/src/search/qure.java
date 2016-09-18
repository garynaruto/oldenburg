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
	public static final int start =2938;
	public static final int end =3152;
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
		System.out.println("deserialization.");
		
		//find belong level 1 node
		Cluster c = new Cluster(graphTree);
		int starIndex = c.findCluster(start);
		Node<Vertex> starNode = graphTree.root.children.get(starIndex);
		int endIndex = c.findCluster(end);
		Node<Vertex> endNode = graphTree.root.children.get(endIndex);
		
		//find all path
		DFS d = new DFS( graphTree);
		List<Node<Vertex>> allpath = d.dfs(starNode, endNode);
		System.out.println(">>>>");
		for(int i=0; i<allpath.size(); i++){
			System.out.println(""+allpath.get(i).vertex.vertexID);
		}
		
		while(containSmallGraph(allpath)){
			for(int i=0; i<allpath.size(); i++){
				Node<Vertex> n = allpath.get(i);
				if(n.vertex.vertexID == starNode.vertex.vertexID || n.vertex.vertexID == endNode.vertex.vertexID){
					//System.out.println(">>"+n.vertex.vertexID);
					
					
					
				}
				else{
					//System.out.println("   ."+n.vertex.vertexID);
					
					
					
					
				}
				
			}
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
	public static boolean containSmallGraph(List<Node<Vertex>> path){
		for(Node<Vertex> n : path){
			if(n.skyLinePath.size()>0 || n.children.size()>0 || n.edges.size()>0){
				return true;
			}
		}
		return false;
	}
	public static boolean isLevel1(int vid){
		for(int i=0; i<graphTree.root.children.size(); i++){
			Tree.Node<Vertex> n = graphTree.root.children.get(i);
			if(vid == n.vertex.vertexID ){
				return true;
			}
		}
		return false;
	}
	public static List<Node<Vertex>> findsmallpath(Node<Vertex> n){
		List<Node<Vertex>> out = new ArrayList<>();
		
		
		
		return out;
	}
	public static List<Node<Vertex>> findsmallpath(Node<Vertex> n, Node<Vertex> lastn, Node<Vertex> nextn){
		List<Node<Vertex>> out = new ArrayList<>();
		
		
		
		return out;
	}
}
