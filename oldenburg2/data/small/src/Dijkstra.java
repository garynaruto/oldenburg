import java.util.*;

public class Dijkstra {
	
}
class Graph {
	public final Map<Integer, Vertex> graph; // mapping of vertex names to Vertex objects,
												// built from a set of Edges
	
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
		    graph.get(e.v2).neighbours.put(graph.get(e.v1), e.dist); // also do this for an undirected graph
		}
	}
	/** Runs dijkstra using a specified source vertex */
	public void dijkstra(int startName) {
		if (!graph.containsKey(startName)) {
			System.err.printf("Graph doesn't contain start vertex \"%s\"\n", startName );
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
			System.err.printf("Graph doesn't contain end vertex \"%s\"\n",
					endName);
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
	public String printCount(int vertexName){
		Vertex v = graph.get(vertexName);
		return String.format("%d %3d",v.vertexID, v.count);
		
		
		
	}
	
}