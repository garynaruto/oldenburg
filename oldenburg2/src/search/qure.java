package search;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import SkyPath.SkyNode;
import SkyPath.SkyPath;
import buildTree.Edge;
import buildTree.Tree;
import buildTree.Vertex;
import buildTree.Tree.Node;

public class qure {
	public static final int start = 3152;
	public static final int end = 2945;
	public static Tree<Vertex> graphTree;
	public static List<Edge> edgeList;

	public static void main(String args[]) throws Exception {
		// Object deserialization ����ϧǦC��
		/* graphTree */
		FileInputStream fis = new FileInputStream("./data/SerializationGraphTree");
		ObjectInputStream ois = new ObjectInputStream(fis);
		graphTree = (Tree<Vertex>) ois.readObject();
		ois.close();
		/* edgeList */
		fis = new FileInputStream("./data/SerializationEdgeList");
		ois = new ObjectInputStream(fis);
		edgeList = (List<Edge>) ois.readObject();
		ois.close();
		System.out.println("deserialization.");

		// find belong level 1 node
		Cluster c = new Cluster(graphTree);
		int starIndex = c.findCluster(start);
		Node<Vertex> starNode = graphTree.root.children.get(starIndex);
		int endIndex = c.findCluster(end);
		Node<Vertex> endNode = graphTree.root.children.get(endIndex);

		// find all path
		DFS d = new DFS(graphTree);
		List<Node<Vertex>> allpath = d.dfs(starNode, endNode);
		System.out.println(">>>>");
		for (int i = 0; i < allpath.size(); i++) {
			System.out.println("" + allpath.get(i).vertex.vertexID);
		}
		while (containSmallGraph(allpath)) {
			System.out.println(">===================================<");
			for (int i = 0; i < allpath.size(); i++) {
				Node<Vertex> n = allpath.get(i);
				if (containSmallGraph(n)) {// if node has small graph
					if (n.vertex.vertexID == starNode.vertex.vertexID) {
						System.out.println(">>> " + n.vertex.vertexID);
						List<List<Node<Vertex>>> ans = findsmallpath(true, start, n, allpath.get(i + 1));
						allpath.remove(0);
						// one skyline
						List<Node<Vertex>> l = ans.get(0);
						allpath.addAll(0, l);
						for (Node<Vertex> a2 : ans.get(0)) {
							System.out.println("s=" + a2.vertex.vertexID);
						}
						/*
						 * all skyline for(int j=0;j<ans.size(); j++){
						 * List<Node<Vertex>> l = ans.get(j); }
						 * 
						 * for(List<Node<Vertex>> a1 :ans){ for(Node<Vertex>
						 * a2:a1){ System.out.println("s="+a2.vertex.vertexID);
						 * } }
						 */

					} else if (n.vertex.vertexID == endNode.vertex.vertexID) {
						System.out.println("end " + n.vertex.vertexID);
						List<List<Node<Vertex>>> ans = findsmallpath(false, end, n, allpath.get(i - 1));

						allpath.remove(allpath.size() - 1);
						// one skyline
						List<Node<Vertex>> l = ans.get(0);
						allpath.addAll(allpath.size(), l);
						for (Node<Vertex> a2 : ans.get(0)) {
							System.out.println("s=" + a2.vertex.vertexID);
						}
						/*
						 * all skyline for(int j=0;j<ans.size(); j++){
						 * List<Node<Vertex>> l = ans.get(j);
						 * 
						 * }
						 * 
						 * for(List<Node<Vertex>> a1 :ans){ for(Node<Vertex>
						 * a2:a1){ System.out.println("e="+a2.vertex.vertexID);
						 * } }
						 */
						break;
					} else {
						System.out.println("   ." + n.vertex.vertexID);
						// findsmallpath(n, allpath.get(i-1), allpath.get(i+1));

					}
				}

			}
			System.out.println("allpath.size(): " + allpath.size());
			for (Node<Vertex> n : allpath) {
				System.out.print("->" + n.vertex.vertexID);
			}
			System.out.println();
			Thread.sleep(1000);
		}

		/*
		 * int[] start ={2938}; int[] end ={3152}; SkyPath skyPath = new
		 * SkyPath(); skyPath.inputData(edgeList, graphTree.root.children);
		 * 
		 * skyPath.multipointSkyline(start, end);
		 * 
		 * for(int i=0; i<skyPath.multipointSkypathSet.size(); i++){ SkyNode[]
		 * ans =skyPath.multipointSkypathSet.get(i);
		 * System.out.println("Path "+(i+1)+" ------>"); for(Object a : ans){
		 * System.out.println(a); } }
		 */
	}

	public static List<List<Node<Vertex>>> findsmallpath(Node<Vertex> n, Node<Vertex> lastn, Node<Vertex> nextn) {
		List<List<Node<Vertex>>> out = new LinkedList<List<Node<Vertex>>>();

		if (!n.isChildren()) {
			LinkedList<Node<Vertex>> tmp = new LinkedList<Node<Vertex>>();
			out.add(tmp);
			System.out.println("Find no children");
			return out;
		} else if (n.children.size() == 1) {
			out.add(new LinkedList<Node<Vertex>>(n.children));
			return out;
		} else {
			// find map edge node
			int mapLastId = 0;
			int mapNextId = 0;
			boolean findMapFlg1 = false;
			boolean findMapFlg2 = false;
			for (Edge e : n.edges) {
				if (lastn.vertex.vertexID == e.v1) {
					mapLastId = e.v2;
					findMapFlg1 = true;
				} else if (lastn.vertex.vertexID == e.v2) {
					mapLastId = e.v1;
					findMapFlg1 = true;
				}
				if (nextn.vertex.vertexID == e.v1) {
					mapNextId = e.v2;
					findMapFlg2 = true;
				} else if (nextn.vertex.vertexID == e.v2) {
					mapNextId = e.v1;
					findMapFlg2 = true;
				}

			}
			if (findMapFlg1 == false || findMapFlg2 == false) {
				System.out.println("find Map node Flg false");
				System.exit(1);
			}
			// find skyline
			if (mapNextId == mapLastId) {
				LinkedList<Node<Vertex>> tmp = new LinkedList<Node<Vertex>>();
				boolean foundflg = false;
				for (int i = 0; i < n.children.size(); i++) {
					Node<Vertex> t = n.children.get(i);
					if (t.vertex.vertexID == mapNextId) {
						foundflg = true;
						tmp.add(t);
						break;
					}
				}
				if (!foundflg) {
					System.out.println("objectID == mapNodeid  !foundflg");
					System.exit(1);
				}
				out.add(tmp);
			} else {
				for (int i = 0; i < n.skyLinePath.size(); i++) {
					Node<Vertex>[] skyArray = n.skyLinePath.get(i);
					if (skyArray[0].vertex.vertexID == mapLastId) {
						if (skyArray[skyArray.length - 1].vertex.vertexID == mapNextId) {
							out.add(new LinkedList<Node<Vertex>>(Arrays.asList(skyArray)));
						}
					} else if (skyArray[skyArray.length - 1].vertex.vertexID == mapLastId) {
						if (skyArray[0].vertex.vertexID == mapNextId) {
							out.add(new LinkedList<Node<Vertex>>(Arrays.asList(skyArray)));
						}
					}
				}
			}
			// check and exchange order

			for (int i = 0; i < out.size(); i++) {
				List<Node<Vertex>> list = out.get(i);
				if (list.get(0).vertex.vertexID != mapLastId) {
					for (int j = 0; j < list.size() / 2; j++) {
						Node<Vertex> temp = list.get(j);
						list.set(j, list.get(list.size() - j - 1));
						list.set(list.size() - j - 1, temp);
					}
				}
			}

		}

		return out;
	}

	public static List<List<Node<Vertex>>> findsmallpath(boolean starflg, int objectID, Node<Vertex> n,
			Node<Vertex> linkedn) {
		List<List<Node<Vertex>>> out = new LinkedList<List<Node<Vertex>>>();

		if (!n.isChildren()) {
			LinkedList<Node<Vertex>> tmp = new LinkedList<Node<Vertex>>();
			out.add(tmp);
			System.out.println("Find no children");
			return out;
		} else if (n.children.size() == 1) {
			out.add(new LinkedList<Node<Vertex>>(n.children));
			return out;
		} else {
			// find map edge node
			int mapNodeid = 0;
			boolean findMapFlg = false;
			for (Edge e : n.edges) {
				if (linkedn.vertex.vertexID == e.v1) {
					mapNodeid = e.v2;
					findMapFlg = true;
				} else if (linkedn.vertex.vertexID == e.v2) {
					mapNodeid = e.v1;
					findMapFlg = true;
				}
			}
			if (findMapFlg == false) {
				System.out.println("find Map node Flg false");
				System.exit(1);
			}
			// find skyline

			if (objectID == mapNodeid) {
				LinkedList<Node<Vertex>> tmp = new LinkedList<Node<Vertex>>();
				boolean foundflg = false;
				for (int i = 0; i < n.children.size(); i++) {
					Node<Vertex> t = n.children.get(i);
					if (t.vertex.vertexID == objectID) {
						foundflg = true;
						tmp.add(t);
					}
				}
				if (!foundflg) {
					System.out.println("objectID == mapNodeid  !foundflg");
					System.exit(1);
					;
				}
				out.add(tmp);
			}
			boolean nidflg = false;
			for (int i = 0; i < n.skyLinePath.size(); i++) {
				Node<Vertex>[] skyArray = n.skyLinePath.get(i);
				if (skyArray[0].vertex.vertexID == mapNodeid) {
					if (skyArray[skyArray.length - 1].vertex.vertexID == objectID) {
						out.add(new LinkedList<Node<Vertex>>(Arrays.asList(skyArray)));
						nidflg = true;
					}
				} else if (skyArray[skyArray.length - 1].vertex.vertexID == mapNodeid) {
					if (skyArray[0].vertex.vertexID == objectID) {
						out.add(new LinkedList<Node<Vertex>>(Arrays.asList(skyArray)));
						nidflg = true;
					}
				}
			}
			if (!nidflg) {
				for (int i = 0; i < n.skyLinePath.size(); i++) {
					Node<Vertex>[] skyArray = n.skyLinePath.get(i);
					if (skyArray[0].vertex.vertexID == mapNodeid) {
						if (skyArray[skyArray.length - 1].vertex.vertexID == n.vertex.vertexID) {
							out.add(new LinkedList<Node<Vertex>>(Arrays.asList(skyArray)));
						}
					} else if (skyArray[skyArray.length - 1].vertex.vertexID == mapNodeid) {
						if (skyArray[0].vertex.vertexID == n.vertex.vertexID) {
							out.add(new LinkedList<Node<Vertex>>(Arrays.asList(skyArray)));
						}
					}
				}
			}
			// check and exchange order
			if (starflg) {
				for (int i = 0; i < out.size(); i++) {
					List<Node<Vertex>> list = out.get(i);
					if (list.get(0).vertex.vertexID == mapNodeid) {
						for (int j = 0; j < list.size() / 2; j++) {
							Node<Vertex> temp = list.get(j);
							list.set(j, list.get(list.size() - j - 1));
							list.set(list.size() - j - 1, temp);
						}
					}
				}
			} else {
				for (int i = 0; i < out.size(); i++) {
					List<Node<Vertex>> list = out.get(i);
					if (list.get(list.size() - 1).vertex.vertexID == mapNodeid) {
						for (int j = 0; j < list.size() / 2; j++) {
							Node<Vertex> temp = list.get(j);
							list.set(j, list.get(list.size() - j - 1));
							list.set(list.size() - j - 1, temp);
						}
					}
				}
			}
			return out;
		}

	}

	public static boolean containSmallGraph(List<Node<Vertex>> path) {
		for (Node<Vertex> n : path) {
			if (n.skyLinePath.size() > 0 || n.children.size() > 0 || n.edges.size() > 0) {
				return true;
			}
		}
		return false;
	}

	public static boolean containSmallGraph(Node<Vertex> n) {
		if (n.skyLinePath.size() > 0 || n.children.size() > 0 || n.edges.size() > 0) {
			return true;
		}
		return false;
	}

	public static boolean isLevel1(int vid) {
		for (int i = 0; i < graphTree.root.children.size(); i++) {
			Tree.Node<Vertex> n = graphTree.root.children.get(i);
			if (vid == n.vertex.vertexID) {
				return true;
			}
		}
		return false;
	}

}
