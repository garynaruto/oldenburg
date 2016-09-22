package search;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import buildTree.Tree;
import buildTree.Vertex;
import buildTree.Tree.Node;

public class DFS {
	Tree<Vertex> graphTree;

	public DFS(Tree<Vertex> graphTree){
		this.graphTree = graphTree;
	}
	public List<Node<Vertex>> dfs(Tree.Node<Vertex> rootNode, Tree.Node<Vertex> endNode) {
		// DFS uses Stack data structure
		List<List<Node<Vertex>>> out2 = new LinkedList<List<Node<Vertex>>>();
		List<Node<Vertex>> out = null;
		
		Stack<Node<Vertex>> s = new Stack<Node<Vertex>>();
		s.push(rootNode);
		rootNode.vertex.visited=true;
		
		while(!s.isEmpty()) {
			Tree.Node<Vertex> node = (Tree.Node<Vertex>) s.peek();
			Tree.Node<Vertex> child = getUnvisitedChildNode(node);
			if(child != null) {
				child.vertex.visited = true;
				s.push(child);
				if(child.vertex.vertexID == endNode.vertex.vertexID){
					out =  new LinkedList<Node<Vertex>>(s);
					
					System.out.print(">");
					for(Node<Vertex> a :out){
						System.out.print(" "+a.vertex.vertexID);
					}
					System.out.println();
				}
			}
			else {
				s.pop();
			}
		}
		return out;
	}
	public Tree.Node<Vertex> getUnvisitedChildNode(Tree.Node<Vertex> node){
		Tree.Node<Vertex> t = null;
		
		for(Vertex v : node.vertex.neighbours){
			if(v.visited == false){
				for(int i=0; i<graphTree.root.children.size(); i++){
					Tree.Node<Vertex> n = graphTree.root.children.get(i);
					if(v.vertexID == n.vertex.vertexID ){
						return n;
					}
				}
			}
		}
		
		return t;
	}
	
	
	public DFS(String[] args) {
		// TODO Auto-generated method stub

	}

}
