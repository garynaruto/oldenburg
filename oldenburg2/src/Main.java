import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {
	public static final int node = 100;// OL.cnode100.txt
	public static final int edge = 107;// OL.cedge100.txt
	public static final String nodeFile = "D:/Oldenburg/Oldenburg_cnode100.txt";
	public static final String edgeFile = "D:/Oldenburg/OL.cedge100.txt";
	public static final String writeClusterFile = "D:/Oldenburg/OL.cnode100 Cluster.txt";
	public static int rangeNum = 8;// level range
	public static int[] range;

	public static void main(String[] args) throws Exception {
		// read node and edge
		Main m = new Main();
		Edge[] edges = m.readedge();
		Vertex[] vertexs = m.readSkyVertex();
		System.out.println(edges.length + "\n" + vertexs.length);
		range = new int[rangeNum];
		range[0] = node / rangeNum;
		for (int i = 1; i < range.length; i++) {
			range[i] = range[i - 1] + range[0];
		}
		Vertex tmp;
		// set neighbours
		for (Edge e : edges) {
			tmp = findNodeByID(e.v1, vertexs);
			tmp.neighbours.put(findNodeByID(e.v2, vertexs), e.dist);
			tmp = findNodeByID(e.v2, vertexs);
			tmp.neighbours.put(findNodeByID(e.v1, vertexs), e.dist);
		}

		/* sort Vertex array */
		for (int i = 0; i < vertexs.length; i++) {
			for (int j = i; j < vertexs.length; j++) {
				if (vertexs[i].count < vertexs[j].count) {
					tmp = vertexs[i];
					vertexs[i] = vertexs[j];
					vertexs[j] = tmp;
				}
			}
		}
		/* set level to v array : 8 range */
		int level = 1;
		int index =0;
		for (int j = 0; j < vertexs.length; j++) {
			vertexs[j].level = level;
			if (j > range[index] && (index+1 < range.length)) {
				level++;
				index++;
			}
		}

		/* write Cluster file */
		PrintWriter writer = new PrintWriter(writeClusterFile, "UTF-8");
		for (int j = 0; j < vertexs.length; j++) {
			writer.println(String.format("%d %3d", vertexs[j].vertexID,
					vertexs[j].level));
		}
		writer.flush();
		writer.close();

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
							Edge e = findEdgeByVertex(vertexs[i], a, edgeList);
							treeNode.edges.add(e);
							removeEdgeByID(e.edgeID, edgeList);
							// add neighboursEdage
							for (Vertex b : a.neighbours.keySet()) {
								if (b.vertexID != vertexs[i].vertexID && !isNeighbours(b.vertexID, vertexs[i])) {
									Edge eDelete = null;
									eDelete = findEdgeByVertex(a, b, edgeList);
									if (eDelete != null) {
										treeNode.edges.add(eDelete);
										removeEdgeByID(eDelete.edgeID, edgeList);
										edgeList.add(new Edge(true, 0, vertexs[i], b, (e.dist + eDelete.dist)));
									}
									
								} else if (isNeighbours(b.vertexID, vertexs[i])) {
									Edge eDelete = null;
									eDelete = findEdgeByVertex(a, b, edgeList);
									if (eDelete != null) {
										treeNode.edges.add(eDelete);
										removeEdgeByID(eDelete.edgeID, edgeList);
									}

								}
							}

							removeVertexByID(a.vertexID, vertexList);
						} else {
							treeNode.edges.add(findEdgeByVertex(vertexs[i], a,
									edgeList));
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
		System.out.println(">remain Vertex===================");
		for (Vertex v : vertexList) {
			System.out.println(v);
		}
		System.out.println(">tree============================");
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

	public static boolean isNeighbours(int vertexID, Vertex center) {

		for (Vertex v : center.neighbours.keySet()) {
			if (v.vertexID == vertexID) {
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

	public static Edge findEdgeByVertex(Vertex v1, Vertex v2,
			List<Edge> edgeList) {
		for (int i = 0; i < edgeList.size(); i++) {
			Edge out = edgeList.get(i);
			if (out.v1 == v1.vertexID) {
				if (out.v2 == v2.vertexID) {
					return out;
				}
			} else if (out.v1 == v2.vertexID) {
				if (out.v2 == v1.vertexID) {
					return out;
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

	public Vertex[] readSkyVertex() {
		Vertex[] out = new Vertex[node];
		int nodeNum = 0;
		double nodeX = 0.0;
		double nodeY = 0.0;
		int skyLineCount = 0;
		try {
			File file = new File(nodeFile);
			Scanner s = new Scanner(file);

			for (int i = 0; i < node; i++) {
				nodeNum = s.nextInt();
				nodeX = s.nextDouble();
				nodeY = s.nextDouble();
				skyLineCount = s.nextInt();
				out[i] = new Vertex(nodeNum, nodeX, nodeY, skyLineCount);
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

	public void printEdges(Edge[] edges) {
		for (Edge n : edges) {
			System.out.println(n);
		}
	}

	public void printNodes(Vertex[] vs) {
		for (Vertex v : vs) {
			System.out.println(v);
		}
	}

}
