import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {
	public static final int node = 8;// OL.cnode100.txt
	public static final int edge = 7;// OL.cedge100.txt
	public static final String nodeFile = "D:/Oldenburg/1.txt";
	public static final String edgeFile = "D:/Oldenburg/2.txt";
	public static final int range = 1;// level range

	public static void main(String[] args) throws Exception {
		// read node and edge
		Main m = new Main();
		Edge[] edges = m.readedge();
		Vertex[] vertexs = m.readVertex();
		System.out.println(edges.length + "\n" + vertexs.length);
		/* run dijkstra's all node to all node*/
		Graph g = new Graph(edges);
		for (int i = 0; i < vertexs.length; i++) {
			for (int j = 0; j < vertexs.length; j++) {
				if (i != j) {
					g.dijkstra(vertexs[i].vertexID);
					g.printPath(vertexs[j].vertexID);
				}
			}
		}
		/* get Vertex array */
		Object[] oarray = g.graph.values().toArray();
		Vertex[] varray = new Vertex[oarray.length];
		for (int i = 0; i < oarray.length; i++) {
			varray[i] = (Vertex) oarray[i];
		}
		// sort Vertex array
		Vertex tmp;
		for (int i = 0; i < varray.length; i++) {
			for (int j = i; j < varray.length; j++) {
				if (varray[i].count < varray[j].count) {
					tmp = varray[i];
					varray[i] = varray[j];
					varray[j] = tmp;
				}
			}
		}
		
		/* set level to varray : 10 range*/
		for (int j = 0; j < varray.length; j++) {
			varray[j].level = j + 1;
		}
		
		/* write Cluster file*/
		PrintWriter writer = new PrintWriter(
				"D:/Oldenburg/OL.cnode Cluster.txt", "UTF-8");
		for (int j = 0; j < varray.length; j++) {
			writer.println(String.format("%d %3d", varray[j].vertexID,
					varray[j].level));
		}
		writer.flush();
		writer.close();
		
		
		// set level back to vertexs
		for (int i = 0; i < varray.length; i++) {
			for (int j = 0; j < varray.length; j++) {
				if (vertexs[i].vertexID == varray[j].vertexID) {
					vertexs[i].level = varray[j].level;
					vertexs[i].count = varray[j].count;
					vertexs[i].neighbours = varray[j].neighbours;
					break;
				}
			}
		}

		Tree<Vertex> graphTree = new Tree<Vertex>();
		graphTree.root.vertex = new Vertex(0, 0, 0);
		// level 1 cluster
		List<Vertex> vertexList = new ArrayList<Vertex>(Arrays.asList(vertexs));
		List<Edge> edgeList = new ArrayList<Edge>(Arrays.asList(edges));

		for (int i = 0; i < vertexs.length; i++) {
			if (vertexs[i].level == 1) {
				Tree.Node<Vertex> treeNode = new Tree.Node<Vertex>();
				treeNode.vertex = vertexs[i];
				treeNode.parent = graphTree.root;

				// add level 1 vertex's neighboursNode & neighboursEdage
				for (Vertex a : vertexs[i].neighbours.keySet()) {
					if (isContainsVertexByID(a.vertexID, vertexList)) {
						// System.out.println(">>>>>>>>>>>>");
						if (a.level != 1) {
							Tree.Node<Vertex> neighboursNode = new Tree.Node<Vertex>();
							neighboursNode.vertex = findNodeByID(a.vertexID,
									vertexs);

							neighboursNode.parent = treeNode;
							treeNode.children.add(neighboursNode);
							Edge e = findEdgeByVertex(vertexs[i], a, edges);
							treeNode.edges.add(e);
							removeEdgeByID(e.edgeID, edgeList);
							// add neighboursEdage
							for (Vertex b : a.neighbours.keySet()) {
								if (b.vertexID != vertexs[i].vertexID) {
									Edge eDelete = findEdgeByVertex(a, b, edges);
									treeNode.edges.add(eDelete);
									removeEdgeByID(eDelete.edgeID, edgeList);
									edgeList.add(new Edge(true, 0, vertexs[i], b,
											(e.dist + eDelete.dist)));
								}
							}

							removeVertexByID(a.vertexID, vertexList);
						} else {
							treeNode.edges.add(findEdgeByVertex(vertexs[i], a,
									edges));
						}
					}
				}
				graphTree.root.children.add(treeNode);
			}
		}
		System.out.println(">remain Edge=====================");
		for (Edge e : edgeList) {
			System.out.println(e);
		}
		System.out.println(">remain Vertex=====================");
		for (Vertex v : vertexList) {
			System.out.println(v);
		}
		System.out.println(">tree=============================");
		graphTree.root.printTree(0);
	}

	public static boolean isContainsVertexByID(int vertexID, List<Vertex> l) {
		for (Vertex v : l) {
			if (v.vertexID == vertexID) {
				return true;
			}
		}
		return false;
	}

	public static boolean removeEdgeByID(int edgeID, List<Edge> l) {
		Edge tmp;
		for (int i = 0; i < l.size(); i++) {
			tmp = l.get(i);
			if (tmp.edgeID == edgeID) {
				l.remove(i);
				return true;
			}
		}
		return false;
	}

	public static boolean removeVertexByID(int vertexID, List<Vertex> l) {
		Vertex tmp;
		for (int i = 0; i < l.size(); i++) {
			tmp = l.get(i);
			if (tmp.vertexID == vertexID) {
				l.remove(i);
				return true;
			}
		}
		return false;
	}

	public static Vertex findNodeByID(int ID, Vertex[] nodes) {
		for (int i = 0; i < nodes.length; i++) {
			if (nodes[i].vertexID == ID) {
				return nodes[i];
			}
		}
		return null;
	}

	public static Edge findEdgeByID(int ID, Edge[] edges) {
		for (int i = 0; i < edges.length; i++) {
			if (edges[i].edgeID == ID) {
				return edges[i];
			}
		}
		return null;
	}

	public static Edge findEdgeByVertex(Vertex v1, Vertex v2, Edge[] edges) {
		for (int i = 0; i < edges.length; i++) {
			if (edges[i].v1 == v1.vertexID) {
				if (edges[i].v2 == v2.vertexID) {
					return edges[i];
				}
			} else if (edges[i].v1 == v2.vertexID) {
				if (edges[i].v2 == v1.vertexID) {
					return edges[i];
				}
			}
		}
		return null;
	}
	public Edge[] readedge() {
		Edge[] out = new Edge[edge];
		int edgeID;
		int a;
		int b;
		double distance;
		try {
			File file = new File(edgeFile);
			Scanner s = new Scanner(file);

			for (int i = 0; i < edge; i++) {
				edgeID = s.nextInt();
				a = s.nextInt();
				b = s.nextInt();
				distance = s.nextDouble();
				out[i] = new Edge(edgeID, a, b, distance);
			}
			s.close();
		} catch (Exception e) {
			e.toString();
		}
		return out;
	}

	public Vertex[] readVertex() {
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
			e.toString();
		}
		return out;
	}

	public void printEdge(Edge edge) {
		System.out.println(edge);
	}

	public void printEdges(Edge[] edges) {
		for (Edge n : edges) {
			System.out.println(n);
		}
	}

	public void printNode(Vertex v) {
		System.out.println(v);
	}

	public void printNodes(Vertex[] vs) {
		for (Vertex v : vs) {
			System.out.println(v);
		}
	}

}
