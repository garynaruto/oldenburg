import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main1 {
	public static final int node = 100;// OL.cnode100.txt
	public static final int edge = 107;// OL.cedge100.txt
	// public static final String nodeFile = "./data/small/1.txt"; 8
	// public static final String edgeFile = "./data/small/2.txt"; 8
	public static final String nodeFile = "./data/Oldenburg/Oldenburg_cnode100.txt";
	public static final String edgeFile = "./data/Oldenburg/OL.cedge100.txt";
	public static final String writeClusterFile = "./OL.cnode100 Cluster.txt";
	public static int rangeNum = 8;// level range
	public static int[] range;

	public static void main(String[] args) {
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
			tmp.neighbours.add(findNodeByID(e.v2, vertexs));
			tmp = findNodeByID(e.v2, vertexs);
			tmp.neighbours.add(findNodeByID(e.v1, vertexs));
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
		int index = 0;
		for (int j = 0; j < vertexs.length; j++) {
			vertexs[j].level = level;
			if (j > range[index] && (index + 1 < range.length)) {
				level++;
				index++;
			}
		}
		/*
		 * small level vertexs[0].level =1; for (int j = 1; j < vertexs.length;
		 * j++){ vertexs[j].level =2; }
		 */

		Tree<Vertex> graphTree = new Tree<Vertex>();
		graphTree.root.vertex = new Vertex(0, 0, 0);
		// level 1 cluster
		List<Vertex> vertexList = new ArrayList<Vertex>(Arrays.asList(vertexs));
		List<Edge> edgeList = new ArrayList<Edge>(Arrays.asList(edges));

		// System.out.println("");
		// System.out.println("");
		for (int i = 0; i < vertexs.length; i++) {
			System.out.println("I: " + vertexs[i].vertexID);
			if (vertexs[i].level == 1) {
				Tree.Node<Vertex> treeNode = new Tree.Node<Vertex>();
				treeNode.vertex = vertexs[i];
				treeNode.parent = graphTree.root;
				// add level 1 vertex's neighboursNode & neighboursEdage
				for (int j = 0; j < vertexs[i].neighbours.size(); j++) {
					Vertex a = vertexs[i].neighbours.get(j);
					if (isContainsVertexByID(a.vertexID, vertexList)) {
						if (a.level > 1) {
							// System.out.println(a.vertexID+" level > 1");
							Tree.Node<Vertex> neighboursNode = new Tree.Node<Vertex>();
							neighboursNode.vertex = findNodeByID(a.vertexID, vertexs);
							neighboursNode.parent = treeNode;
							treeNode.children.add(neighboursNode);
							Edge e = findEdgeByVertex(vertexs[i], a, edgeList);
							treeNode.edges.add(e);
							removeEdgeByID(e.edgeID, edgeList);
							// add neighboursEdage
							for (int t = 0; t < a.neighbours.size(); t++) {
								Vertex b = a.neighbours.get(t);
								if (b.vertexID != vertexs[i].vertexID && !isNeighbours(b.vertexID, vertexs[i])) {
									Edge eDelete = null;
									eDelete = findEdgeByVertex(a, b, edgeList);
									if (eDelete != null) {
										treeNode.edges.add(eDelete);
										removeEdgeByID(eDelete.edgeID, edgeList);
										edgeList.add(new Edge(true, 0, vertexs[i], b, (e.dist + eDelete.dist)));
										b.neighbours.remove(a);
									} else {
										System.out.println("eDelete == null");
									}

								} else if (b.vertexID != vertexs[i].vertexID && isNeighbours(b.vertexID, vertexs[i])) {
									Edge eDelete = null;
									eDelete = findEdgeByVertex(a, b, edgeList);
									if (eDelete != null) {
										treeNode.edges.add(eDelete);
										removeEdgeByID(eDelete.edgeID, edgeList);
										b.neighbours.remove(a);
									} else {
										System.out.println("eDelete2 == null");
									}

								}
							}
							vertexs[i].neighbours.remove(a);
							j--;// when we remove neighbours the size() will -1
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
		for (int i = 0; i < vertexList.size(); i++) {
			Vertex v = vertexList.get(i);
			if (v.level != 1) {
				Tree.Node<Vertex> treeNode = new Tree.Node<Vertex>();
				treeNode.vertex = v;
				treeNode.parent = graphTree.root;
				graphTree.root.children.add(treeNode);
			}
		}
		System.out.println("22222");
		/*-----------------2----------------------------------------------*/
		for (int i = 0; i < graphTree.root.children.size(); i++) {
			// System.out.println("I >> "+graphTree.root.children.size());
			Tree.Node<Vertex> v = graphTree.root.children.get(i);
			System.out.println("level >> " + v.vertex.vertexID);
			if (v.vertex.level == 1) {
				Tree.Node<Vertex> treeNode = new Tree.Node<Vertex>();
				treeNode.vertex = v.vertex;
				v.parent = treeNode;
				treeNode.parent = graphTree.root;
				treeNode.children.add(v);
				// add level 1 vertex's neighboursNode & neighboursEdage
				for (int j = 0; j < v.vertex.neighbours.size(); j++) {
					Vertex a = v.vertex.neighbours.get(j);
					if (isContainsTreeNodeByID(a.vertexID, graphTree.root.children)) {
						if (a.level > 1) {
							Tree.Node<Vertex> neighboursNode = findTreeNodeByID(a.vertexID, graphTree.root.children);
							neighboursNode.parent = treeNode;
							treeNode.children.add(neighboursNode);
							removeTreeNodeByID(a.vertexID, graphTree.root.children);
							i--;
							Edge e = findEdgeByVertex(v.vertex, a, edgeList);
							treeNode.edges.add(e);
							removeEdgeByID(e.edgeID, edgeList);
							// add neighboursEdage
							for (int t = 0; t < a.neighbours.size(); t++) {
								Vertex b = a.neighbours.get(t);
								if (b.vertexID != v.vertex.vertexID
										&& !isNeighbours(b.vertexID, v.vertex)) {
									Edge eDelete = null;
									eDelete = findEdgeByVertex(a, b, edgeList);
									if (eDelete != null) {
										treeNode.edges.add(eDelete);
										removeEdgeByID(eDelete.edgeID, edgeList);
										edgeList.add(new Edge(true, 0, v.vertex, b, (e.dist + eDelete.dist)));
										b.neighbours.remove(a);
									} else {
										System.out.println("eDelete == null");
									}

								} else if (b.vertexID != v.vertex.vertexID && isNeighbours(b.vertexID, v.vertex)) {
									Edge eDelete = null;
									eDelete = findEdgeByVertex(a, b, edgeList);
									if (eDelete != null) {
										treeNode.edges.add(eDelete);
										removeEdgeByID(eDelete.edgeID, edgeList);
										b.neighbours.remove(a);
									} else {
										System.out.println("eDelete2 == null");
									}

								}
							}
							v.vertex.neighbours.remove(a);
							j--;// when we remove neighbours the size() will -1
							int indexi = graphTree.root.children.indexOf(a);
							removeTreeNodeByID(a.vertexID, graphTree.root.children);
							if (indexi < i) {
								i--;
							}
						} else {
							treeNode.edges.add(findEdgeByVertex(v.vertex, a, edgeList));
						}
					}
				}
				graphTree.root.children.add(0,treeNode);
			}

			graphTree.root.children.remove(v);
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

	public static boolean isContainsTreeNodeByID(int vertexID,
			List<Tree.Node<Vertex>> l) {
		for (Tree.Node<Vertex> v : l) {
			if (v.vertex.vertexID == vertexID) {
				return true;
			}
		}
		return false;
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

		for (Vertex v : center.neighbours) {
			if (v.vertexID == vertexID) {
				return true;
			}
		}
		return false;
	}

	public static boolean removeTreeNodeByID(int vertexID,
			List<Tree.Node<Vertex>> l) {
		Tree.Node<Vertex> tmp;
		for (int i = 0; i < l.size(); i++) {
			tmp = l.get(i);
			if (tmp.vertex.vertexID == vertexID) {
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

	public static Tree.Node<Vertex> findTreeNodeByID(int ID,
			List<Tree.Node<Vertex>> l) {
		for (int i = 0; i < l.size(); i++) {
			Tree.Node<Vertex> v = l.get(i);
			if (v.vertex.vertexID == ID) {
				return v;
			}
		}
		return null;
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
