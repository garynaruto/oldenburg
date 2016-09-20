package search;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import buildTree.Tree;
import buildTree.Vertex;
import buildTree.Tree.Node;

public class Cluster {
	List<List<Vertex>> l;
	List<Node<Vertex>> t;
	Tree<Vertex> graphTree;

	public Cluster(Tree<Vertex> graphTree) {
		this.graphTree = graphTree;
		l = new ArrayList<List<Vertex>>();
		t = graphTree.root.children;
		for (int i = 0; i < graphTree.root.children.size(); i++) {
			l.add(i, new ArrayList<Vertex>());
			Node<Vertex> n = graphTree.root.children.get(i);
			n.getContainVerte(n, l.get(i));
		}
	}

	public void print() {

		for (int i = 0; i < graphTree.root.children.size(); i++) {
			Node<Vertex> n = graphTree.root.children.get(i);
			System.out.println(">" + n.vertex.vertexID);
			for (Vertex v : l.get(i)) {
				System.out.println("  ." + v.vertexID);
			}
		}
	}

	public int findCluster(int objectID) {
		
		for (int i = 0; i < graphTree.root.children.size(); i++) {
			if(objectID == graphTree.root.children.get(i).vertex.vertexID ){
				return i;
			}
			for (Vertex v : l.get(i)) {
				//System.out.println("  ." + v.vertexID);
				if(v.vertexID == objectID ){
					return i;
				}
			}
		}
		return -1;
	}
	public int findClusterId(int objectID) {
		
		for (int i = 0; i < graphTree.root.children.size(); i++) {
			if(objectID == graphTree.root.children.get(i).vertex.vertexID ){
				return graphTree.root.children.get(i).vertex.vertexID;
			}
			for (Vertex v : l.get(i)) {
				//System.out.println("  ." + v.vertexID);
				if(v.vertexID == objectID ){
					return graphTree.root.children.get(i).vertex.vertexID;
				}
			}
		}
		return -1;
	}
}
