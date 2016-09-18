package search;

import java.awt.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;
import SkyPath.SkyNode;
import buildTree.*;
import buildTree.Tree.Node;

class BFS {
	Queue queue;
	Tree<Vertex> graphTree;

	public BFS(Tree<Vertex> graphTree){
		this.graphTree = graphTree;
	}
	
	public void bfs(Tree.Node<Vertex> rootNode, Tree.Node<Vertex> endNode)
	{
		ArrayList<Node<Vertex>[]> out =  new ArrayList<Node<Vertex>[]>();
		ArrayList<Node<Vertex>> tmp = new ArrayList<Node<Vertex>>();
		
		// BFS uses Queue data structure
		queue = new LinkedList();
		tmp.add(rootNode);
		rootNode.vertex.visited = true;
		while(!queue.isEmpty()) {
			Tree.Node<Vertex> node = (Tree.Node<Vertex>)queue.remove();
			tmp.add(rootNode);
			Tree.Node<Vertex> child=null;
			while((child=getUnvisitedChildNode(node))!=null){
				child.vertex.visited=true;
				queue.add(child);
				if(child.vertex.vertexID == endNode.vertex.vertexID){
					
				}
			}
		}
		
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
	public void clearNodes(){
		
	}
}


