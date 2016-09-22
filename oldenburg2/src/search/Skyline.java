package search;
import java.util.LinkedList;
import java.util.List;
import buildTree.*;
import buildTree.Tree.Node;

public class Skyline implements Comparable<Skyline> {
	public static Edge[] edgeArray;
	public List<Node<Vertex>> path;
	public List<Edge> edges;
	public double dist = 0.0;
	public double[] dimasion;

	public Skyline(List<Node<Vertex>> sk) {
		this.path = new LinkedList<Node<Vertex>>(sk);
		this.edges = new LinkedList<Edge>();
		this.dimasion = new double[Main2.dimasion];
		this.mapEdge();
		this.calculateDimasion();
		this.calculateDist();
	}
	public Skyline(Skyline s) {
		this.path = new LinkedList<Node<Vertex>>(s.path);
		this.edges = new LinkedList<Edge>(s.edges);
		this.dimasion = new double[Main2.dimasion];
		this.dist = s.dist;
		for(int i=0; i<dimasion.length; i++){
			dimasion[i] = s.dimasion[i];
		}
	}
	
	
	public void mapEdge() {
		for(int i=0; i<path.size()-1; i++){
			Node<Vertex> n = path.get(i);
			Node<Vertex> n2 = path.get(i+1);
			for(Edge e :edgeArray){
				if(e.v1 == n.vertex.vertexID && e.v2 == n2.vertex.vertexID && !edges.contains(e)){
					edges.add(e);
				}else if(e.v1 == n2.vertex.vertexID && e.v2 == n.vertex.vertexID && !edges.contains(e)){
					edges.add(e);
				}
			}
		}
		
	}
	@Override
	public int compareTo(Skyline o) {
		return (int) (this.dist - o.dist);
	}
	public void expansion(boolean end, int index, List<Node<Vertex>> l) {
		if(end){
			this.path.remove(this.path.size() - 1);
			this.path.addAll(this.path.size(), l);
		}else{
			this.path.remove(index);
			this.path.addAll(index, l);
		}
		this.mapEdge();
		this.calculateDimasion();
		this.calculateDist();
		return;
	}
	//new Sky line
	public double calculateDist() {
		double out = 0.0;
		for (Edge e : edges) {
			out += e.dist;
		}
		dist = out;
		return dist;
	}
	//new Sky line
	public void calculateDimasion() {
		this.dimasion = new double[Main2.dimasion];
		for (Edge e : edges) {
			for (int i = 0; i < Main2.dimasion; i++) {
				this.dimasion[i] += e.dimasion[i];
			}
		}
		return;
	}

	public boolean isDominateByOther(Skyline o) {

		if (this.dist <= o.dist) {
			return false;
		}
		for (int i = 0; i < Main2.dimasion; i++) {
			if (this.dimasion[i] < o.dimasion[i]) {
				return false;
			}
		}
		return true;
	}

}
