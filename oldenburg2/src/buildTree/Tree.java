package buildTree;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Tree<T> implements Serializable{
	public Node<T> root;
    public Tree(T rootData){
        root = new Node<T>();
        root.vertex = rootData;
    }
    public Tree(){
        root = new Node<T>();
        root.children = new ArrayList<Node<T>>();
    }
    public static class Node<T> implements Serializable{
    	public T vertex;
    	public Node<T> parent;
    	public List<Node<T>> children;
    	public List<Edge> edges;
    	
    	public Node(){
    		children = new ArrayList<Node<T>>();
    		edges = new ArrayList<Edge>();
    	}
    	public void printTree(int count){
    		for(int i=0; i<count; i++){
    			System.out.print("  ");
    		}
    		System.out.print("."+vertex+"\n");
    		count++;
    		for(Node<T> t : children){
    			t.printTree(count);
    		}
    		
    		for(Edge e : edges){
    			for(int i=0; i<count; i++){
        			System.out.print("  ");
        		}
        		System.out.print("> "+e+"\n");
    		}
    		count--;
    		return ;
    	}
    	public void writeTree(PrintWriter writer, int count){
    		for(int i=0; i<count; i++){
    			 writer.print("  ");
    		}
    		writer.println("."+vertex);
    		count++;
    		for(Node<T> t : children){
    			t.writeTree(writer, count);
    		}
    		
    		for(Edge e : edges){
    			for(int i=0; i<count; i++){
    				writer.print("  ");
        		}
    			writer.println("> "+e);
    		}
    		count--;
    		return ;
    	}
    	
    	public boolean isChildren(){
        	return !this.children.isEmpty();
        }
    }
    
}