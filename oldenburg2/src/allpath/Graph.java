package allpath;
import java.util.Stack;
import buildTree.Edge;
import buildTree.Tree;
import buildTree.Vertex;

public class Graph {
    private static final String NEWLINE = System.getProperty("line.separator");
    private final int V;
    private int E;
    private Bag<Integer>[] adj;
    public int[] mapIndex;
    
    public Graph(int V) {
        if (V < 0) throw new IllegalArgumentException("Number of vertices must be nonnegative");
        this.V = V;
        System.out.println(V);
        this.E = 0;
        adj = (Bag<Integer>[]) new Bag[V];
        for (int v = 0; v < V; v++) {
            adj[v] = new Bag<Integer>();
        }
    }

    public Graph(Edge[] edges,Tree<Vertex> graphTree) {
    	this( graphTree.root.children.size());
    	mapIndex = new int[graphTree.root.children.size()];
    	for(int i=0; i<graphTree.root.children.size();  i++){
    		mapIndex[i] = graphTree.root.children.get(i).vertex.vertexID;
    	}
        int E = edges.length;
        
        if (E < 0) throw new IllegalArgumentException("Number of edges must be nonnegative");
        for (int i = 0; i < edges.length; i++) {
            addEdge(mapVertexIDtoIndex(edges[i].v1), mapVertexIDtoIndex(edges[i].v2));
        }
    }
    public int mapVertexIDtoIndex(int VertexID){
    	for(int i=0; i<mapIndex.length;  i++){
    		if(mapIndex[i] == VertexID){
    			return i;
    		}
    	}
    	System.out.println(VertexID+" to -1");
    	return -1;
    }
    public int mapIndextoVertexID(int i){
    	return mapIndex[i];
    }
    
    public int V() {
        return V;
    }

    
    public int E() {
        return E;
    }

    // throw an IndexOutOfBoundsException unless {@code 0 <= v < V}
    private boolean validateVertex(int v) {
        if (v < 0 || v >= V){
        	//System.out.println("cut Edge"+v);
    		return true;
        }
        return false;
    }

    /**
     * Adds the undirected edge v-w to this graph.
     *
     * @param  v one vertex in the edge
     * @param  w the other vertex in the edge
     * @throws IndexOutOfBoundsException unless both {@code 0 <= v < V} and {@code 0 <= w < V}
     */
    public void addEdge(int v, int w) {
    	if( validateVertex(v) || validateVertex(w) ){
    		//System.out.println("cut Edge"+v+"-"+w);
    		return;
    	}
    	E++;
        adj[v].add(w);
        adj[w].add(v);
    }


    /**
     * Returns the vertices adjacent to vertex {@code v}.
     *
     * @param  v the vertex
     * @return the vertices adjacent to vertex {@code v}, as an iterable
     * @throws IndexOutOfBoundsException unless {@code 0 <= v < V}
     */
    public Iterable<Integer> adj(int v) {
        validateVertex(v);
        return adj[v];
    }

    /**
     * Returns the degree of vertex {@code v}.
     *
     * @param  v the vertex
     * @return the degree of vertex {@code v}
     * @throws IndexOutOfBoundsException unless {@code 0 <= v < V}
     */
    public int degree(int v) {
        validateVertex(v);
        return adj[v].size();
    }


    /**
     * Returns a string representation of this graph.
     *
     * @return the number of vertices <em>V</em>, followed by the number of edges <em>E</em>,
     *         followed by the <em>V</em> adjacency lists
     */
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(V + " vertices, " + E + " edges " + NEWLINE);
        for (int v = 0; v < V; v++) {
            s.append(v + ": ");
            for (int w : adj[v]) {
                s.append(w + " ");
            }
            s.append(NEWLINE);
        }
        return s.toString();
    }

}
