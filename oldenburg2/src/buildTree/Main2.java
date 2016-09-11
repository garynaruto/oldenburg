package buildTree;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import test.Test2;

public class Main2 {
	public static final int node = 1000;// OL.cnode100.txt
	public static final int edge = 1152;// OL.cedge100.txt
	public static final int dimasion = 2;
	//public static final String nodeFile = "./data/small/3.txt";
	//public static final String edgeFile = "./data/small/4.txt";
	public static final String nodeFile = "./data/Oldenburg/Oldenburg_cnode1000.txt";
	public static final String edgeFile = "./data/Oldenburg/Oldenburg_cedge1000.txt";
	public static final String writeClusterFile = "./data/OL.cnode Cluster level.txt";
	public static final String writeTreeFile =  "./data/OL."+node+"tree.txt";
	public static int rangeNum = 8;// level range
	public static int[] range;
	public static void main(String[] args) {
		// read node and edge
		Edge[] edges = readSkyEdge();
		Vertex[] vertexs = readSkyVertex();
		System.out.println(edges.length + "\n" + vertexs.length);
		
		for(Edge e : edges){
			if(e ==null)System.out.println("e==null");
		}
		/*calculate range */
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
		int levelone = 0;
		int level = 1;
		int index = 0;
		for (int j = 0; j < vertexs.length; j++) {
			vertexs[j].level = level;
			if(level == 1)levelone++;
			if (j > range[index] && (index + 1 < range.length)) {
				level++;
				index++;
			}
		}
		/*small level
		int levelone = 2;
		vertexs[0].level = 1;
		vertexs[1].level = 1;
		for (int j = 1; j < vertexs.length;j++){
		vertexs[j].level = 2; 
		}*/
		writeClusterFile(vertexs);
		Tree<Vertex> graphTree = new Tree<Vertex>();
		graphTree.root.vertex = new Vertex(0, 0, 0);
		// level 1 cluster
		List<Vertex> vertexList = new ArrayList<Vertex>(Arrays.asList(vertexs));
		List<Edge> edgeList = new ArrayList<Edge>(Arrays.asList(edges));
		// System.out.println("");
		for (int i = 0; i < vertexs.length; i++) {
			System.out.println("i : "+i+" "+vertexs[i]);
			List<Vertex> addNeighbours =new ArrayList<>();
			if (vertexs[i].level == 1) {
				System.out.println("level >> " + vertexs[i].vertexID+" n : "+vertexs[i].neighbours.size());
				Tree.Node<Vertex> treeNode = new Tree.Node<Vertex>();
				treeNode.vertex = vertexs[i];
				treeNode.parent = graphTree.root;
				Tree.Node<Vertex> vNode = new Tree.Node<Vertex>();
				vNode.parent = treeNode;
				vNode.vertex = vertexs[i];
				treeNode.children.add(vNode);
				// add level 1 vertex's neighboursNode & neighboursEdage
				for (int j = 0; j < vertexs[i].neighbours.size(); j++) {
					Vertex a = vertexs[i].neighbours.get(j);
					if (isContainsVertexByID(a.vertexID, vertexList)) {
						System.out.println("1 ");
						if (a.level > 1) {
							System.out.println("2 ");
							Tree.Node<Vertex> neighboursNode = new Tree.Node<Vertex>();
							neighboursNode.vertex = findNodeByID(a.vertexID, vertexs);
							neighboursNode.parent = treeNode;
							treeNode.children.add(neighboursNode);
							Edge e = findEdgeByVertex(vertexs[i], a, edgeList);
							treeNode.edges.add(e);
							removeEdgeByVertex(vertexs[i], a, edgeList);
							
							// add neighboursEdage
							for (int t = 0; t < a.neighbours.size(); t++) {
								Vertex b = a.neighbours.get(t);
								if (b.vertexID != vertexs[i].vertexID && !isNeighbours(b.vertexID, vertexs[i])) {
									Edge eDelete = null;
									eDelete = findEdgeByVertex(a, b, edgeList);
									if (eDelete != null) {
										treeNode.edges.add(eDelete);
										removeEdgeByVertex(a, b, edgeList);
										edgeList.add(new Edge(true, 0, vertexs[i], b, (e.dist + eDelete.dist)));
										b.neighbours.remove(a);
										b.neighbours.add(vertexs[i]);
										addNeighbours.add(b);
									} else {
										System.out.println("eDelete == null");
									}
								} else if (b.vertexID != vertexs[i].vertexID && isNeighbours(b.vertexID, vertexs[i])) {
									Edge eDelete = null;
									eDelete = findEdgeByVertex(a, b, edgeList);
									if (eDelete != null) {
										treeNode.edges.add(eDelete);
										removeEdgeByVertex(a, b, edgeList);
										b.neighbours.remove(a);
									} else {
										System.out.println("eDelete2 == null");
									}
								}
							}
							
							vertexs[i].neighbours.remove(a);
							j--;// when we remove neighbours the size() will -1
							removeVertexByID(a.vertexID, vertexList);
							a.neighbours.remove(vertexs[i]);// new
						} else {
							treeNode.edges.add(findEdgeByVertex(vertexs[i], a, edgeList));
						}
					}
				}
				for(Vertex v1 :addNeighbours){
					vertexs[i].neighbours.add(v1);
					System.out.println(vertexs[i].vertexID+" add "+v1.vertexID);
				}
				//skyline path
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
		
		System.out.println(">remain Edge=====================");
		for (Edge e : edgeList) {
			System.out.println(e);
		}
		System.out.println(">Tree Vertex===================");
		for (Tree.Node<Vertex> t: graphTree.root.children) {
			System.out.println(t.vertex);
		}
		System.out.println(">tree============================");
		graphTree.root.printTree(0);
		
		
		System.out.println("22222");
		int con = 1;
		System.out.println("level one : "+levelone);

		/*-----------------2----------------------------------------------*/
		while(graphTree.root.children.size() > levelone){
			//System.out.println("c : "+con++);
			System.out.println("graphTree.root.children.size() : "+graphTree.root.children.size());
			if(graphTree.root.children.size() == 49){
				if(con++ > 2){
					System.exit(1);
				}
			}
			for (int i = 0; i < graphTree.root.children.size(); i++) {
				List<Vertex> removeNeighbours =new ArrayList<>();
				Tree.Node<Vertex> v = graphTree.root.children.get(i);
				System.out.println("i : "+i+" "+v.vertex);
				if (v.vertex.level == 1) {
					//System.out.println(v.vertex.vertexID+" level == 1 ");
					System.out.println("level >> " + v.vertex.vertexID+" n : "+v.vertex.neighbours.size());
					
					Tree.Node<Vertex> treeNode = new Tree.Node<Vertex>();
					treeNode.vertex = v.vertex;
					v.parent = treeNode;
					treeNode.parent = graphTree.root;
					treeNode.children.add(v);
					// add level 1 vertex's neighboursNode & neighboursEdage
					for (int j = 0; j < v.vertex.neighbours.size(); j++) {
						Vertex a = v.vertex.neighbours.get(j);
						System.out.println("a neighbours : "+a.vertexID);
						if (isContainsTreeNodeByID(a.vertexID, graphTree.root.children)) {
							System.out.println("1 ");
							if (a.level > 1) {
								System.out.println("2 ");
								Tree.Node<Vertex> neighboursNode = findTreeNodeByID(a.vertexID, graphTree.root.children);
								neighboursNode.parent = treeNode;
								treeNode.children.add(neighboursNode);
								Edge e = findEdgeByVertex(v.vertex, a, edgeList);
								if(e!=null && removeEdgeByVertex(v.vertex, a, edgeList)){
									treeNode.edges.add(e);
								}
								if(e==null){
									System.out.println("continue");
									continue;
								}
								// add neighboursEdage
								System.out.println("a n : "+a.neighbours.size());
								for (int t = 0; t < a.neighbours.size(); t++) {
									Vertex b = a.neighbours.get(t);
									System.out.println("b neighbours : "+b.vertexID);
									if (b.vertexID != v.vertex.vertexID && !isNeighbours(b.vertexID, v.vertex)) {
										System.out.println("b 1");
										Edge eDelete = null;
										eDelete = findEdgeByVertex(a, b, edgeList);
										if (eDelete != null) {
											System.out.println("b 2");
											//System.out.println("eDelete : "+eDelete.edgeID);
											treeNode.edges.add(eDelete);
											removeEdgeByVertex(a, b, edgeList);
											edgeList.add(new Edge(true, 0, v.vertex, b, (e.dist + eDelete.dist)));
											b.neighbours.remove(a);
											b.neighbours.add(v.vertex);
											removeNeighbours.add(b);
										} else {
											System.out.println("eDelete == null");
										}
									} else if (b.vertexID != v.vertex.vertexID && isNeighbours(b.vertexID,v.vertex)){
										Edge eDelete = null;
										eDelete = findEdgeByVertex(a, b, edgeList);
										System.out.println("b 1.");
										if (eDelete != null) {
											System.out.println("b 2.");
											treeNode.edges.add(eDelete);
											removeEdgeByVertex(a, b, edgeList);
											b.neighbours.remove(a);
											
										} else {
											System.out.println("eDelete2 == null");
										}
									}
								}
								v.vertex.neighbours.remove(a);
								j--;// when we remove neighbours the size() will -1
								//int indexi = graphTree.root.children.indexOf(a);
								removeTreeNodeByID(a.vertexID, graphTree.root.children);
								a.neighbours.remove(v);
							} else {
								
								Edge e = findEdgeByVertex(v.vertex, a, edgeList);
								if(e != null){
									treeNode.edges.add(e);
								}
							}
						}
					}
					//skyline path
					graphTree.root.children.add(0,treeNode);
					graphTree.root.children.remove(v);
					for(Vertex v1 :removeNeighbours){
						v.vertex.neighbours.add(v1);
						//System.out.println(vertexs[i].vertexID+" add "+v1.vertexID);
					}
				}	
			}
			System.out.println(">remain Edge=====================");
			for (Edge e : edgeList) {
				System.out.println(e);
			}
			System.out.println(">Tree Vertex===================");
			for (Tree.Node<Vertex> t: graphTree.root.children) {
				System.out.println(t.vertex);
			}
			System.out.println(">tree============================");
			graphTree.root.printTree(0);
		}
		
		System.out.println(">remain Edge=====================");
		for (Edge e : edgeList) {
			System.out.println(e);
		}
		System.out.println(">Tree Vertex===================");
		for (Tree.Node<Vertex> t: graphTree.root.children) {
			System.out.println(t.vertex);
		}
		System.out.println(">tree============================");
		graphTree.root.printTree(0);
		writeTreeFile(edgeList, graphTree);
		
		//Object serialization 物件序列化
        try {
        	System.out.println("物件1: graphTree");
            FileOutputStream fos = new FileOutputStream("./data/SerializationGraphTree");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(graphTree);
            oos.flush();
            oos.close();
            System.out.println("物件2: edgeList");
            fos = new FileOutputStream("./data/SerializationEdgeList");
            oos = new ObjectOutputStream(fos);
            oos.writeObject(edgeList);
            oos.flush();
            oos.close();
        } catch (Exception e) {
        	e.printStackTrace();
        }
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
	public static boolean removeEdgeByVertex(Vertex v1, Vertex v2, List<Edge> edgeList) {
		Edge out;
		for (int i = 0; i < edgeList.size(); i++) {
			out = edgeList.get(i);
			if (out.v1 == v1.vertexID) {
				if (out.v2 == v2.vertexID) {
					edgeList.remove(out);
					System.out.println("remove : "+out);
					System.out.println("v1 : ");
					for ( Vertex t: v1.neighbours ) {
						System.out.println(t.vertexID);
					}
					System.out.println("v2 : ");
					for ( Vertex t: v2.neighbours ) {
						System.out.println(t.vertexID);
					}
					return true;
				}
			} else if (out.v1 == v2.vertexID) {
				if (out.v2 == v1.vertexID) {
					edgeList.remove(out);
					System.out.println("remove : "+out);
					System.out.println("v1 : ");
					for ( Vertex t: v1.neighbours ) {
						System.out.println(t.vertexID);
					}
					System.out.println("v2 : ");
					for ( Vertex t: v2.neighbours ) {
						System.out.println(t.vertexID);
					}
					return true;
				}
			}
		}
		System.out.println("remove fall : "+v1.vertexID+" "+v2.vertexID);
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
	public static Edge findEdgeByVertex(Vertex v1, Vertex v2, List<Edge> edgeList) {
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
	public static Edge[] readedge() {
		System.out.println("readedge");
		Edge[] out = new Edge[edge];
		int edgeID , a, b;
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
			e.printStackTrace();
		}
		return out;
	}
	public static Edge[] readSkyEdge() {
		System.out.println("readSkyedge");
		Edge[] out = new Edge[edge];
		int edgeID;
		int a;
		int b;
		double distance = 0.0;
		double[] dimasion = new double[Main2.dimasion];
		try {
			File file = new File(edgeFile);
			Scanner s = new Scanner(file);
			for (int i = 0; i < edge; i++) {
				edgeID = s.nextInt();
				a = s.nextInt();
				b = s.nextInt();
				for(int j=0; j<Main2.dimasion; j++){
					dimasion[j] = s.nextDouble();
					distance+=dimasion[j];
				}
				out[i] = new Edge(edgeID, a, b, distance, dimasion);
				//if(out[i] == null)System.out.println("edge "+i);
			}
			s.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return out;
	}
	public static Vertex[] readSkyVertex() {
		System.out.println("readSkyVertex");
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
			e.printStackTrace();
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
			e.printStackTrace();
		}
		return out;
	}
	public static void writeClusterFile(Vertex[] vertexs){
		try {
			PrintWriter writer = new PrintWriter(writeClusterFile, "UTF-8");
			for(Vertex v:vertexs){
				writer.println(String.format("%d %d",v.vertexID, v.level ));
			}
			writer.flush();
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void writeTreeFile(List<Edge> el, Tree<Vertex> graphTree){
		try {
			PrintWriter writer = new PrintWriter(writeTreeFile, "UTF-8");
			writer.println(">remain Edge=====================");
			for (Edge e : el) {
				writer.println(e);
			}
			writer.println(">Tree Vertex===================");
			for (Tree.Node<Vertex> t: graphTree.root.children) {
				writer.println(t.vertex);
			}
			writer.println(">tree============================");
			graphTree.root.writeTree(writer, 0);
			writer.flush();
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void writeTreeFile(List<Edge> el, Tree<Vertex> graphTree, String file){
		try {
			PrintWriter writer = new PrintWriter(file, "UTF-8");
			writer.println(">remain Edge=====================");
			for (Edge e : el) {
				writer.println(e);
			}
			writer.println(">Tree Vertex===================");
			for (Tree.Node<Vertex> t: graphTree.root.children) {
				writer.println(t.vertex);
			}
			writer.println(">tree============================");
			graphTree.root.writeTree(writer, 0);
			writer.flush();
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}