package buildTree;
import java.io.Serializable;
import java.util.ArrayList;

/** One vertex of the graph, complete with mappings to neighbouring vertices */
public class Vertex implements Comparable<Vertex> , Serializable{

	public int vertexID;
	public double x;
	public double y;
	public int count = 0;
	public int level = 0;
	
	// use in dijkstra
	public int dist = Integer.MAX_VALUE; // MAX_VALUE assumed to be infinity
	public Vertex previous = null;// ¤W­Ó Vertex
	public ArrayList<Vertex> neighbours = new ArrayList<Vertex>();

	public Vertex(int name) {
		this.vertexID = name;
	}

	public Vertex(int nodeID, double nodeX, double nodeY) {
		this.vertexID = nodeID;
		this.x = nodeX;
		this.y = nodeY;

	}

	public Vertex(int nodeID, double nodeX, double nodeY, int count) {
		this.vertexID = nodeID;
		this.x = nodeX;
		this.y = nodeY;
		this.count = count;
	}
	
	public String toString() {
		String s = vertexID+"";
		return s;
	}

	public void printPath() {
		if (this == this.previous) {
			//System.out.printf("%s", this.vertexID);
			count++;
		} else if (this.previous == null) {
			//System.out.printf("%s(unreached)", this.vertexID);
		} else {
			this.previous.printPath();
			//System.out.printf(" -> %s(%d)", this.vertexID, this.dist);
			count++;
		}
	}

	public int compareTo(Vertex other) {
		return Integer.compare(dist, other.dist);
	}
}
