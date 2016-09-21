package search;

import java.util.LinkedList;
import java.util.List;
import buildTree.*;
import buildTree.Tree.Node;

public class SkyList {
	List<Skyline> list =new LinkedList<Skyline>();
	
	
	
	public class Skyline implements Comparable<Skyline>{
		List<Node<Vertex>> path;
		List<Edge> edges;
		private double dist =0.0;
		public double[] dimasion;
		public Skyline(List<Node<Vertex>> sk){
			path = new LinkedList<Node<Vertex>>(sk);
			edges =new LinkedList<Edge>();
			dimasion = new double[Main2.dimasion];
		}
		@Override
		public int compareTo(Skyline o) {
			return (int)(this.dist-o.dist);
		}
		public double calculateDist() {
			double out = 0.0;
			for(Edge e: edges){
				out+=e.dist;
			}
			dist = out;
			return dist;
		}
		public void calculateDimasion() {
			this.dimasion = new double[Main2.dimasion];
			for(Edge e: edges){
				for(int i=0; i<Main2.dimasion; i++){
					this.dimasion[i] += e.dimasion[i];
				}
			}
			return;
		}
		public boolean isDominateByOther(Skyline o){
			
			if(this.dist <= o.dist){
				return false;
			}
			for(int i=0; i<Main2.dimasion; i++){
				if(this.dimasion[i] < o.dimasion[i]){
					return false;
				}
			}
			return true;
		}
		
		
		
	}
}
