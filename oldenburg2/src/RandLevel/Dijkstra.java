package RandLevel;

import java.io.*;
import java.util.*;

import buildTree.Vertex;


public class Dijkstra {
	public static final int node = 6105;// OL.cnode100.txt  1000
	public static final int edge = 7035;// OL.cedge100.txt 1152
	public static final int dimasion = 6;
	public static int dc = 0;
	public static final String nodeFile = "./data/Oldenburg/OL.cnode.txt";
	public static final String edgeFile = "./data/Oldenburg/OL_cedge_6Dimension.txt";
	public static final String writeFile = "./data/Oldenburg/OL.cnode_6Dimension_Cluster.txt";
	public static void main(String[] args) {
		//Graph.Edge[] edges = readedge();
		Graph.Edge[] edges;
		Vertex[] vertexs = readVertex();
		Graph.v = vertexs;
		for(dc=0; dc<6; dc++){
			edges = readedge();
			for(int i=0; i<6105; i++){
				for(int j=i;j<6105; j++){
					Graph g = new Graph(edges);
					g.dijkstra(i);
					g.printPath(j);
				}
				System.out.println("."+i);
			}
			System.out.println("i = "+dc);
			writetmpFile(vertexs,dc);
		}
		writeClusterFile(vertexs);
	}
	public static Graph.Edge[] readedge() {
		System.out.println("readedge");
		Graph.Edge[] out = new Graph.Edge[edge];
		int edgeID, a, b;
		try {
			File file = new File(edgeFile);
			Scanner s = new Scanner(file);
			//System.out.println("1");
			for (int i = 0; i < edge; i++) {
				edgeID = s.nextInt();
				a = s.nextInt();
				b = s.nextInt();
				double[] distance = new double[Dijkstra.dimasion];
				
				for(int j=0; j<Dijkstra.dimasion; j++){
					distance[j] = s.nextDouble();
				}
				//System.out.println(edgeID+" "+ a+" "+b+" "+distance);
				out[i] = new Graph.Edge(a, b, (int)(distance[dc]*1000000));
			}
			s.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return out;
	}
	public static Vertex[] readVertex() {
		Vertex[] out = new Vertex[node];
		int nodeNum = 0;
		double nodeX = 0.0;
		double nodeY = 0.0;
		try {
			File file = new File(nodeFile);
			Scanner s = new Scanner(file);
			for (int i = 0; i < node; i++) {
				nodeNum = s.nextInt();
				nodeX = s.nextDouble();
				nodeY = s.nextDouble();
				out[i] = new Vertex(nodeNum, nodeX, nodeY);
			}
			s.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return out;
	}
	public static void writetmpFile(Vertex[] vertexs,int i) {
		String tmpFile = "./data/Oldenburg/tmp_6Dimension_Cluster("+i+").txt";
		try {
			PrintWriter writer = new PrintWriter(tmpFile, "UTF-8");
			for (Vertex v : vertexs) {
				writer.println(String.format("%d %d", v.vertexID, v.level));
			}
			writer.flush();
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void writeClusterFile(Vertex[] vertexs) {
		try {
			PrintWriter writer = new PrintWriter(writeFile, "UTF-8");
			for (Vertex v : vertexs) {
				writer.println(String.format("%d %d", v.vertexID, v.level));
			}
			writer.flush();
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

class Graph {
	private final Map<Integer, Vertex> graph; 
	public static buildTree.Vertex[] v; 
	public static class Edge {
		public final int v1, v2;
		public final int dist;

		public Edge(int v1, int v2, int dist) {
			this.v1 = v1;
			this.v2 = v2;
			this.dist = dist;
		}
	}

	/**
	 * One vertex of the graph, complete with mappings to neighbouring vertices
	 */
	public static class Vertex implements Comparable<Vertex> {
		public final int name;
		public int dist = Integer.MAX_VALUE; // MAX_VALUE assumed to be infinity
		public Vertex previous = null;
		public final Map<Vertex, Integer> neighbours = new HashMap<>();

		public Vertex(int name) {
			this.name = name;
		}

		private void printPath() {
			if (this == this.previous) {
				//System.out.printf("%d", this.name);
				v[this.name].level++;
			} else if (this.previous == null) {
				//System.out.printf("%d(unreached)", this.name);
			} else {
				this.previous.printPath();
				//System.out.printf(" -> %d(%d)", this.name, this.dist);
				v[this.name].level++;
			}
		}

		public int compareTo(Vertex other) {
			return Integer.compare(dist, other.dist);
		}
	}

	/** Builds a graph from a set of edges */
	public Graph(Edge[] edges) {
		graph = new HashMap<>(edges.length);

		// one pass to find all vertices
		for (Edge e : edges) {
			if (!graph.containsKey(e.v1))
				graph.put(e.v1, new Vertex(e.v1));
			if (!graph.containsKey(e.v2))
				graph.put(e.v2, new Vertex(e.v2));
		}

		// another pass to set neighbouring vertices
		for (Edge e : edges) {
			graph.get(e.v1).neighbours.put(graph.get(e.v2), e.dist);
			graph.get(e.v2).neighbours.put(graph.get(e.v1), e.dist); 
		}
	}

	/** Runs dijkstra using a specified source vertex */
	public void dijkstra(int startName) {
		if (!graph.containsKey(startName)) {
			System.err.printf("Graph doesn't contain start vertex \"%s\"\n", startName);
			return;
		}
		final Vertex source = graph.get(startName);
		NavigableSet<Vertex> q = new TreeSet<>();

		// set-up vertices
		for (Vertex v : graph.values()) {
			v.previous = v == source ? source : null;
			v.dist = v == source ? 0 : Integer.MAX_VALUE;
			q.add(v);
		}

		dijkstra(q);
	}

	/** Implementation of dijkstra's algorithm using a binary heap. */
	private void dijkstra(final NavigableSet<Vertex> q) {
		Vertex u, v;
		while (!q.isEmpty()) {

			u = q.pollFirst(); // vertex with shortest distance (first iteration
								// will return source)
			if (u.dist == Integer.MAX_VALUE)
				break; // we can ignore u (and any other remaining vertices)
						// since they are unreachable

			// look at distances to each neighbour
			for (Map.Entry<Vertex, Integer> a : u.neighbours.entrySet()) {
				v = a.getKey(); // the neighbour in this iteration

				final int alternateDist = u.dist + a.getValue();
				if (alternateDist < v.dist) { // shorter path to neighbour found
					q.remove(v);
					v.dist = alternateDist;
					v.previous = u;
					q.add(v);
				}
			}
		}
	}

	/** Prints a path from the source to the specified vertex */
	public void printPath(int endName) {
		if (!graph.containsKey(endName)) {
			System.err.printf("Graph doesn't contain end vertex \"%s\"\n", endName);
			return;
		}

		graph.get(endName).printPath();
		//System.out.println();
	}

	/**
	 * Prints the path from the source to every vertex (output order is not
	 * guaranteed)
	 */
	public void printAllPaths() {
		for (Vertex v : graph.values()) {
			v.printPath();
			System.out.println();
		}
	}
}