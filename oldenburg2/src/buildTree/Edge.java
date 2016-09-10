package buildTree;
import java.io.Serializable;

public class Edge implements Serializable{
	public int edgeID;
	public int v1;
	public int v2;
	public double dist;
	public double[] dimasion;
	public boolean tmp=false;
	
	public Edge(int edgeID, int nodeID, int nodeID2,double distance) {
		this.edgeID = edgeID;
		this.v1 = nodeID;
		this.v2 = nodeID2;
		this.dist = distance;
	}
	public Edge(int edgeID, int nodeID, int nodeID2,double distance, double[] dom) {
		this.edgeID = edgeID;
		this.v1 = nodeID;
		this.v2 = nodeID2;
		this.dist = distance;
		dimasion = new double[Main2.dimasion];
		for(int i=0; i<Main2.dimasion; i++){
			dimasion[i] = dom[i];
		}
	}
	public Edge(boolean tmp,int edgeID,Vertex v1,Vertex v2,double distance) {
		this.edgeID = edgeID;
		this.v1 = v1.vertexID;
		this.v2 = v2.vertexID;
		this.dist = distance;
		tmp = true;
	}
	public Edge(int v1, int v2, int dist) {
		this.v1 = v1;
		this.v2 = v2;
		this.dist = dist;
	}

	public String toString() {
		String s = edgeID + " " + v1 + " " + v2 + " " + dist;
		return s;
	}
}
