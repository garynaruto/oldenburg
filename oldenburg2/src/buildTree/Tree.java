package buildTree;

import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Tree<T> implements Serializable {
	public Node<T> root;

	public Tree(T rootData) {
		root = new Node<T>();
		root.vertex = rootData;
	}

	public Tree() {
		root = new Node<T>();
		root.children = new ArrayList<Node<T>>();
	}

	public static class Node<T> implements Serializable {
		public T vertex;
		public Node<T> parent;
		public List<Node<T>> children;
		public List<Edge> edges;
		public List<Node<T>[]> skyLinePath;

		public Node() {
			children = new ArrayList<Node<T>>();
			edges = new ArrayList<Edge>();
			skyLinePath = new ArrayList<Node<T>[]>();
		}

		public void printTree(int count) {
			for (int i = 0; i < count; i++) {
				System.out.print("  ");
			}
			System.out.print("." + vertex + "\n");
			count++;
			for (Node<T> t : children) {
				t.printTree(count);
			}

			for (Edge e : edges) {
				for (int i = 0; i < count; i++) {
					System.out.print("  ");
				}
				System.out.print("> " + e + "\n");
			}
			for (Node<T>[] e : skyLinePath) {
				for (int i = 0; i < count; i++) {
					System.out.print("  ");
				}
				System.out.print("-");
				for (Node<T> t : e) {
					System.out.print(t.vertex + ">");
				}
				System.out.println();
			}
			count--;
			return;
		}

		public void writeTree(PrintWriter writer, int count) {
			for (int i = 0; i < count; i++) {
				writer.print("  ");
			}
			writer.println("." + vertex);
			count++;
			for (Node<T> t : children) {
				t.writeTree(writer, count);
			}

			for (Edge e : edges) {
				for (int i = 0; i < count; i++) {
					writer.print("  ");
				}
				writer.println("> " + e);
			}
			for (Node<T>[] e : skyLinePath) {
				for (int i = 0; i < count; i++) {
					writer.print("  ");
				}
				writer.print("-");
				for (Node<T> t : e) {
					writer.print(t.vertex + ">");
				}
				writer.println();
			}
			count--;
			return;
		}

		public void getContainVerte(Node<Vertex> n, List<Vertex> v){
    		
    		for(Node<T> t : children){
    			if(!t.vertex.equals(n.vertex)){
        			v.add((Vertex)t.vertex);
    			}
    			t.getContainVerte(n,v);
    		}
    		
    		return;
    	}
		
		public Edge findEdgeByVertex(int v1,int v2) {
			for (int i = 0; i <  edges.size(); i++) {
				Edge out =  edges.get(i);
				if (out.v1 == v1) {
					if (out.v2 == v2) {
						return out;
					}
				} else if (out.v1 == v2) {
					if (out.v2 == v1) {
						return out;
					}
				}
			}
			return null;
		}

		public boolean isChildren() {
			return !this.children.isEmpty();
		}
	}

}