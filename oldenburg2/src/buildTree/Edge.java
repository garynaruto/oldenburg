package buildTree;
import java.io.Serializable;

public class Edge implements Serializable{
	public static int d = Main2.dimasion;
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
		dimasion = new double[d];
	}
	public Edge(int edgeID, int nodeID, int nodeID2,double distance, double[] dom) {
		this.edgeID = edgeID;
		this.v1 = nodeID;
		this.v2 = nodeID2;
		this.dist = distance;
		dimasion = new double[d];
		for(int i=0; i<d; i++){
			dimasion[i] = dom[i];
		}
	}
	public Edge(int edgeID, int nodeID, int nodeID2,double distance, double[] dom,int dimasions) {
		this.edgeID = edgeID;
		this.v1 = nodeID;
		this.v2 = nodeID2;
		this.dist = distance;
		dimasion = new double[dimasions];
		for(int i=0; i<dimasions; i++){
			dimasion[i] = dom[i];
		}
	}
	public Edge(boolean tmp,int edgeID,Vertex v1,Vertex v2,double distance) {
		this.edgeID = edgeID;
		this.v1 = v1.vertexID;
		this.v2 = v2.vertexID;
		this.dist = distance;
		dimasion = new double[d];
		tmp = true;
	}
	public Edge(int v1, int v2, int dist) {
		this.v1 = v1;
		this.v2 = v2;
		this.dist = dist;
		dimasion = new double[d];
	}

	public String toString() {
		String s = edgeID + " " + v1 + " " + v2 + " " + dist;
		return s;
	}
	public static void setDimasion(int dimasion) {
		d = dimasion;
		return;
	}
}
