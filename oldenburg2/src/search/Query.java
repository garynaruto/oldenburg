package search;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import buildTree.Edge;
import buildTree.Tree;
import buildTree.Vertex;
import buildTree.Tree.Node;
import search.Skyline;

public class Query {
	public static final int start = 451;// 2945-3152
	public static final int end = 3000;
	public static Tree<Vertex> graphTree;
	public static List<Edge> edgeList;
	public static Edge[] edges;

	public static void main(String args[]) throws Exception {
		// Object deserialization 物件反序列化
		/* graphTree */
		FileInputStream fis = new FileInputStream("./data/SerializationGraphTree");
		ObjectInputStream ois = new ObjectInputStream(fis);
		graphTree = (Tree<Vertex>) ois.readObject();
		ois.close();
		fis.close();

		/* edgeList */
		fis = new FileInputStream("./data/SerializationEdgeList");
		ois = new ObjectInputStream(fis);
		edgeList = (List<Edge>) ois.readObject();
		ois.close();
		fis.close();

		/* edges */
		fis = new FileInputStream("./data/SerializationEdgeArray");
		ois = new ObjectInputStream(fis);
		edges = (Edge[]) ois.readObject();
		ois.close();
		fis.close();
		System.out.println("deserialization.");
		 long startTime = System.currentTimeMillis();
		// heap & skyline path list
		Skyline.edgeArray = edges;
		List<Skyline> list = new LinkedList<Skyline>();
		Heap<Skyline> h = new Heap<Skyline>();

		// find belong level 1 node
		Cluster c = new Cluster(graphTree);
		int starIndex = c.findCluster(start);
		Node<Vertex> starNode = graphTree.root.children.get(starIndex);
		int endIndex = c.findCluster(end);
		Node<Vertex> endNode = graphTree.root.children.get(endIndex);
		
		// find all path
		DFS d = new DFS(graphTree);
		List<Node<Vertex>> allpath = d.dfs(starNode, endNode);
		Skyline allpaths = new Skyline(allpath);
		h.insert(allpaths);
		System.out.println("");
		/*
		 * for (int i = 0; i < allpath.size(); i++) { System.out.println("" +
		 * allpath.get(i).vertex.vertexID); }
		 */
		while ((allpaths = h.deleteMin()) != null) {
			//allpaths = h.deleteMin();
			boolean containSmallGraFlg=false;
			for (int i = 0; i < allpaths.path.size(); i++) {
				Node<Vertex> n = allpaths.path.get(i);
				if (containSmallGraph(n)) {// if node has small graph
					containSmallGraFlg=true;
					if (n.vertex.vertexID == starNode.vertex.vertexID) {
						//System.out.println(">>> " + n.vertex.vertexID);
						List<List<Node<Vertex>>> ans = findsmallpath(true, c, start, n, allpaths.path.get(i + 1));
						// one skyline
						if(ans.size()>1){
							for (int j = 0; j < ans.size(); j++) {
								Skyline otherpaths = new Skyline(allpaths);
								otherpaths.expansion(false, i, ans.get(j));
								h.insert(otherpaths);
								System.out.println("add. new path 1");
							}
						}else if(ans.size() == 1) {
							allpaths.expansion(false, i, ans.get(0));
						}
						
					} else if (n.vertex.vertexID == endNode.vertex.vertexID) {
						//System.out.println("end " + n.vertex.vertexID);
						List<List<Node<Vertex>>> ans = findsmallpath(false, c, end, n, allpaths.path.get(i - 1));
						if(ans.size() > 1){
							for (int j = 0; j < ans.size(); j++) {
								List<Node<Vertex>> l = ans.get(j);
								Skyline otherpaths = new Skyline(allpaths);
								otherpaths.expansion(true, i, l);
								h.insert(otherpaths);
								System.out.println("add. new path 2");
							}
						}else if(ans.size() == 1) {
							allpaths.expansion(true, i, ans.get(0));
						}
						
						break;
					} else {
						//System.out.println("   ." + n.vertex.vertexID);
						List<List<Node<Vertex>>> ans = findsmallpath(n, allpaths.path.get(i - 1), allpaths.path.get(i + 1), c);
						if(ans.size() > 1){
							for (int j = 0; j < ans.size(); j++) {
								List<Node<Vertex>> l = ans.get(j);
								Skyline otherpaths = new Skyline(allpaths);
								otherpaths.expansion(false, i, l);
								h.insert(otherpaths);
								System.out.println("add. new path 3");
							}
						}else if(ans.size() == 1) {
							allpaths.expansion(false, i,  ans.get(0));
						}
						

					}
				}

			}
			for (Node<Vertex> n : allpaths.path) {
				if (n.vertex.vertexID == start && !n.isChildren()) {
					starNode = n;
				} else if (n.vertex.vertexID == end && !n.isChildren()) {
					endNode = n;
				}
			}
			if(containSmallGraFlg){
				h.insert(allpaths);
				System.out.println(String.format(">>%.4f", allpaths.dist));
			}
			else{
				System.out.println(">===================================<");
				System.out.println("allpath.size(): " + allpaths.path.size());
				for (Node<Vertex> n : allpaths.path) {
					System.out.print("->" + n.vertex.vertexID);
				}
				System.out.println();
				//結束時間
				  long endTime = System.currentTimeMillis();
				//執行時間
				  long totTime = startTime - System.currentTimeMillis();
				  totTime = endTime - startTime;
				//印出執行時間
				  System.out.println("Using Time:" + totTime);
			}
			
			
			//Thread.sleep(1000);
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

	public static List<List<Node<Vertex>>> findsmallpath(Node<Vertex> n, Node<Vertex> lastn, Node<Vertex> nextn,
			Cluster c) {
		List<List<Node<Vertex>>> out = new LinkedList<List<Node<Vertex>>>();
		//System.out.println("findsmallpath2 :" + lastn.vertex.vertexID + " " + n.vertex.vertexID + " " + nextn.vertex.vertexID);
		if (!n.isChildren()) {
			LinkedList<Node<Vertex>> tmp = new LinkedList<Node<Vertex>>();
			out.add(tmp);
			//System.out.println("Find no children");
			return out;
		} else if (n.children.size() == 1) {
			//System.out.println("only one children");
			out.add(new LinkedList<Node<Vertex>>(n.children));
			return out;
		} else {
			//System.out.println("has children");
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
			// map node didn't found
			if (findMapFlg1 == false || findMapFlg2 == false) {
				if (findMapFlg1 == false) {
					int last = c.findClusterId(lastn.vertex.vertexID);
					for (Edge e : n.edges) {
						if (last == e.v1) {
							mapLastId = e.v2;
							findMapFlg1 = true;
						} else if (last == e.v2) {
							mapLastId = e.v1;
							findMapFlg1 = true;
						}
					}

				}
				if (findMapFlg2 == false) {
					int Next = c.findClusterId(nextn.vertex.vertexID);
					//System.out.println("Next " + Next);
					for (Edge e : n.edges) {
						if (Next == e.v1) {
							mapNextId = e.v2;
							findMapFlg2 = true;
						} else if (Next == e.v2) {
							mapNextId = e.v1;
							findMapFlg2 = true;
						}
					}
				}
				//System.out.println("find Map node Flg false 2");
				//System.out.println(findMapFlg1 + " " + findMapFlg2);
				//System.out.println(mapLastId + " " + mapNextId);
				// System.exit(1);
			} else {
				//System.out.println("find Map node done 2");
				//System.out.println(mapLastId + " " + mapNextId);
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
				boolean foundflg = false;
				for (int i = 0; i < n.skyLinePath.size(); i++) {
					Node<Vertex>[] skyArray = n.skyLinePath.get(i);
					if (skyArray[0].vertex.vertexID == mapLastId) {
						if (skyArray[skyArray.length - 1].vertex.vertexID == mapNextId) {
							out.add(new LinkedList<Node<Vertex>>(Arrays.asList(skyArray)));
							foundflg = true;
						}
					} else if (skyArray[skyArray.length - 1].vertex.vertexID == mapLastId) {
						if (skyArray[0].vertex.vertexID == mapNextId) {
							out.add(new LinkedList<Node<Vertex>>(Arrays.asList(skyArray)));
							foundflg = true;
						}
					}
				}
				if (!foundflg) {
					//System.out.println("!foundflg");
					// System.exit(1);
				} else {

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

	public static List<List<Node<Vertex>>> findsmallpath(boolean starflg, Cluster c, int objectID, Node<Vertex> n,
			Node<Vertex> linkedn) {
		List<List<Node<Vertex>>> out = new LinkedList<List<Node<Vertex>>>();
		//System.out.println("findsmallpath1 :" + n.vertex.vertexID + " " + linkedn.vertex.vertexID);

		if (!n.isChildren()) {
			LinkedList<Node<Vertex>> tmp = new LinkedList<Node<Vertex>>();
			out.add(tmp);
			//System.out.println("Find no children");
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
				//System.out.println("find Map node Flg false 1");
				int last = c.findClusterId(linkedn.vertex.vertexID);
				for (Edge e : n.edges) {
					if (last == e.v1) {
						mapNodeid = e.v2;
						findMapFlg = true;
					} else if (last == e.v2) {
						mapNodeid = e.v1;
						findMapFlg = true;
					}
				}

				//System.out.println("findMapFlg " + findMapFlg);
				//System.out.println("mapLastId " + mapNodeid);
				// System.exit(1);

			} else {
				//System.out.println("mapNodeid = " + mapNodeid);
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
				}
				out.add(tmp);
			} else {

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
					//System.out.println("!contain object");
					boolean containflg = false;
					for (int i = 0; i < n.skyLinePath.size(); i++) {
						Node<Vertex>[] skyArray = n.skyLinePath.get(i);
						if (skyArray[0].vertex.vertexID == mapNodeid) {
							if (skyArray[skyArray.length - 1].vertex.vertexID == n.vertex.vertexID) {
								out.add(new LinkedList<Node<Vertex>>(Arrays.asList(skyArray)));
								containflg = true;
							}
						} else if (skyArray[skyArray.length - 1].vertex.vertexID == mapNodeid) {
							if (skyArray[0].vertex.vertexID == n.vertex.vertexID) {
								out.add(new LinkedList<Node<Vertex>>(Arrays.asList(skyArray)));
								containflg = true;
							}
						}
					}
					if (!containflg && mapNodeid == n.vertex.vertexID) {
						//System.out.println("!contain sky line");
						LinkedList<Node<Vertex>> level1 = new LinkedList<Node<Vertex>>();
						level1.add(findNodeByID(mapNodeid, n));
						out.add(level1);
					}

				}
				// check and exchange order
				if (starflg) {
					for (int i = 0; i < out.size(); i++) {
						List<Node<Vertex>> list = out.get(i);
						if (list.get(0).vertex.vertexID != objectID
								&& list.get(0).vertex.vertexID != n.vertex.vertexID) {
							//System.out.println("!star");
							for (int j = 0; j < list.size() / 2; j++) {
								Node<Vertex> temp = list.get(j);
								list.set(j, list.get(list.size() - j - 1));
								list.set(list.size() - j - 1, temp);
							}
						} else if (list.get(0).vertex.vertexID != objectID) {
							boolean ContainObject = false;
							for (int j = 0; j < list.size(); j++) {
								Node<Vertex> tmp = list.get(j);
								if (tmp.vertex.vertexID == objectID) {
									ContainObject = true;
								}
							}
							if (ContainObject) {
								//System.out.println("!star ContainObject");
								for (int j = 0; j < list.size() / 2; j++) {
									Node<Vertex> temp = list.get(j);
									list.set(j, list.get(list.size() - j - 1));
									list.set(list.size() - j - 1, temp);
								}
							}
						}

					}
				} else {
					for (int i = 0; i < out.size(); i++) {
						List<Node<Vertex>> list = out.get(i);
						if (list.get(list.size() - 1).vertex.vertexID != objectID
								&& list.get(list.size() - 1).vertex.vertexID != n.vertex.vertexID) {
							//System.out.println("!end");
							for (int j = 0; j < list.size() / 2; j++) {
								Node<Vertex> temp = list.get(j);
								list.set(j, list.get(list.size() - j - 1));
								list.set(list.size() - j - 1, temp);
							}
						} else if (list.get(list.size() - 1).vertex.vertexID != objectID) {
							boolean ContainObject = false;
							for (int j = 0; j < list.size(); j++) {
								Node<Vertex> tmp = list.get(j);
								if (tmp.vertex.vertexID == objectID) {
									ContainObject = true;
								}
							}
							if (ContainObject) {
								//System.out.println("!star ContainObject");
								for (int j = 0; j < list.size() / 2; j++) {
									Node<Vertex> temp = list.get(j);
									list.set(j, list.get(list.size() - j - 1));
									list.set(list.size() - j - 1, temp);
								}
							}
						}
					}
				}
			}
			return out;

		}
	}

	public static Node<Vertex> findNodeByID(int ID, Node<Vertex> n) {
		for (int i = 0; i < n.children.size(); i++) {
			if (n.children.get(i).vertex.vertexID == ID) {
				return n.children.get(i);
			}
		}
		return null;
	}

	public static boolean containSmallGraph(Skyline s) {
		for (Node<Vertex> n : s.path) {
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
