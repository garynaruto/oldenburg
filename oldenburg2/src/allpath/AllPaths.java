package allpath;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

import buildTree.Tree;
import buildTree.Vertex;
import buildTree.Tree.Node;

public class AllPaths {
    private boolean[] onPath;        // vertices in current path
    private Stack<Integer> path;     // the current path
    private int numberOfPaths;       // number of simple path
    public List<List<Integer>> ans;
    public List<List<Node<Vertex>>> treeNodeAns;
    public Tree<Vertex> graphTree;
    public Graph G;
    // show all simple paths from s to t - use DFS
    public AllPaths(Graph g, Tree<Vertex> graphTree, int star, int end) {
    	//System.out.println("Star = " + star);
    	//System.out.println("end = " + end);
		this.ans = new LinkedList<List<Integer>>();
		this.treeNodeAns = new LinkedList<List<Node<Vertex>>>();
    	this.G = g;
    	this.graphTree = graphTree;
        onPath = new boolean[G.V()];
        path = new Stack<Integer>();
        dfs(G, G.mapVertexIDtoIndex(star), G.mapVertexIDtoIndex(end));
        
        for(int i=0; i<ans.size(); i++){
        	List<Integer> path = ans.get(i);
        	LinkedList<Node<Vertex>> tans= new LinkedList<Node<Vertex>>();
        	for(int j=0; j<path.size(); j++){
        		path.set(j, G.mapIndextoVertexID(path.get(j)));
        		tans.add(mapNode(path.get(j)));
        	}
        	treeNodeAns.add(tans);
        }
        
    }
    public Node<Vertex> mapNode(int vid){
    	for(Node<Vertex> n : graphTree.root.children){
    		if(n.vertex.vertexID == vid){
    			return n;
    		}
    	}
    	return null;
    }
    // use DFS
    private void dfs(Graph G, int v, int t) {
    	// add v to current path
        path.push(v);
        onPath[v] = true;
        
        // found path from s to t
        if (v == t) {
        	ans.add(getPath());
        	//processCurrentPath();
        	numberOfPaths++;
        }
        // consider all neighbors that would continue path with repeating a node
        else {
            for (int w : G.adj(v)) {
                if (!onPath[w])
                    dfs(G, w, t);
            }
        }
        // done exploring from v, so remove from path
        path.pop();
        onPath[v] = false;
        
        return;
    }

    // this implementation just prints the path to standard output
    private List<Integer> getPath() {
    	LinkedList<Integer> out = new LinkedList<Integer>();
    	Queue<Integer> reverse = new LinkedList<>();

        for (int v : path)
            reverse.add(v);
        
        if (reverse.size() >= 1)
        	 out.add(reverse.poll());
        while (!reverse.isEmpty())
        	out.add(reverse.poll());
    	
    	return out;
    }
    
    
    private void processCurrentPath() {
        Stack<Integer> reverse = new Stack<Integer>();
        for (int v : path)
            reverse.push(v);
        if (reverse.size() >= 1)
            System.out.print(reverse.pop());
        while (!reverse.isEmpty())
        	System.out.print("-" + reverse.pop());
        System.out.println();
    }

    // return number of simple paths between s and t
    public int numberOfPaths() {
        return numberOfPaths;
    }

}

