package search;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import buildTree.Edge;
import buildTree.Tree;
import buildTree.Vertex;
import buildTree.Tree.Node;

//new Q
public class Query{
	//public static final int start = 2945;// 2945-3152
	//public static final int end = 425;
	public static int d = 3;
	public static int start = 1300;// 2945-3152
	public static int end = 1329;
	public static Tree<Vertex> graphTree;
	public static List<Edge> edgeList;
	public static Edge[] edges;

	public static void main(String args[]) throws Exception {
		main();
	}
	@SuppressWarnings("unchecked")
	public static int[] main() throws Exception {
		// Object deserialization 物件反序列化
		try{
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
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("deserialization Exception");
			System.exit(1);
		}
		System.out.println("deserialization.");
		long startTime = System.currentTimeMillis();
		
		// heap & skyline path list
		Edge.setDimasion(d);
		Skyline.setMap(edges);
		List<Skyline> list = new LinkedList<Skyline>();
		Heap<Skyline> h = new Heap<Skyline>();
		
		// find belong level1 node
		Cluster c = new Cluster(graphTree);
		int starIndex = c.findClusterId(start);
		Node<Vertex> starNode = graphTree.root.children.get(c.findCluster(starIndex));
		int endIndex = c.findClusterId(end);
		Node<Vertex> endNode  = graphTree.root.children.get(c.findCluster(endIndex));
		//System.out.println("starIndex "+starIndex);
		//System.out.println("endIndex " + endIndex);
		
		//all path answer
		List<List<Node<Vertex>>> AllPathans = new LinkedList<List<Node<Vertex>>>();
		int from = 1;
		if(starIndex == endIndex){
			/*expansion a node*/
			System.out.println("expansion Exception");
			Node<Vertex> n =null;
			for(int i=0; i<graphTree.root.skyLinePath.size(); i++){
				n = graphTree.root.children.get(i);
				if(n.vertex.vertexID == starIndex){
					System.out.println("n.vertex.vertexID : "+n.vertex.vertexID);
					break;
				}
			}
			boolean moreVertexFlg = false;
			
			while(!moreVertexFlg){
				for(int i=0; i<n.skyLinePath.size(); i++){
					Node<Vertex>[] skyArray = n.skyLinePath.get(i);
					if (skyArray[0].vertex.vertexID == start) {
						if (skyArray[skyArray.length - 1].vertex.vertexID == endIndex || skyArray[skyArray.length - 1].vertex.vertexID == end) {
							if(skyArray[skyArray.length - 1].vertex.vertexID == end){
								AllPathans.clear();
							}
							LinkedList<Node<Vertex>> outArray = new LinkedList<Node<Vertex>>(Arrays.asList(skyArray));
							AllPathans.add(outArray);
							moreVertexFlg = true;
							if(skyArray[skyArray.length - 1].vertex.vertexID == end){
								break;
							}
						}
					} else if (skyArray[skyArray.length - 1].vertex.vertexID == start) {
						if (skyArray[0].vertex.vertexID == endIndex || skyArray[0].vertex.vertexID == end) {
							if(skyArray[0].vertex.vertexID == end){
								AllPathans.clear();
							}
							LinkedList<Node<Vertex>> outArray = new LinkedList<Node<Vertex>>(Arrays.asList(skyArray));
							for(int j=0; j<outArray.size() / 2; j++){
								Node<Vertex> temp = outArray.get(j);
								outArray.set(j, outArray.get(outArray.size() - j - 1));
								outArray.set(outArray.size() - j - 1, temp);
							}
							AllPathans.add(outArray);
							moreVertexFlg = true;
							if(skyArray[0].vertex.vertexID == end){
								break;
							}
						}
					}else if (skyArray[skyArray.length - 1].vertex.vertexID == end) {
						if (skyArray[0].vertex.vertexID == starIndex ) {
							LinkedList<Node<Vertex>> outArray = new LinkedList<Node<Vertex>>(Arrays.asList(skyArray));
							AllPathans.add(outArray);
							moreVertexFlg = true;
						}
					}else if (skyArray[0].vertex.vertexID == end) {
						if (skyArray[skyArray.length - 1].vertex.vertexID == starIndex) {
							LinkedList<Node<Vertex>> outArray = new LinkedList<Node<Vertex>>(Arrays.asList(skyArray));
							for(int j=0; j<outArray.size() / 2; j++){
								Node<Vertex> temp = outArray.get(j);
								outArray.set(j, outArray.get(outArray.size() - j - 1));
								outArray.set(outArray.size() - j - 1, temp);
							}
							AllPathans.add(outArray);
							moreVertexFlg = true;
						}
					}
				}
				if(!moreVertexFlg){
					n = findNodeByID(n.vertex.vertexID, n);
				}
			}
			
			/* add all paths*/
			from = 1;
			for(List<Node<Vertex>> l : AllPathans){
				Skyline tmp = new Skyline(l,l.get(0),l.get(l.size()-1),start,end);
				tmp.from = from++;
				h.insert(tmp);
			} 
			
			
			
			//System.exit(1);
		}else{
			/*find all paths*/
			for(int i=0; i<graphTree.root.skyLinePath.size(); i++){
				Node<Vertex>[] skyArray = graphTree.root.skyLinePath.get(i);
				if (skyArray[0].vertex.vertexID == starIndex) {
					if (skyArray[skyArray.length - 1].vertex.vertexID == endIndex) {
						LinkedList<Node<Vertex>> outArray = new LinkedList<Node<Vertex>>(Arrays.asList(skyArray));
						AllPathans.add(outArray);
					}
				} else if (skyArray[skyArray.length - 1].vertex.vertexID == starIndex) {
					if (skyArray[0].vertex.vertexID == endIndex) {
						LinkedList<Node<Vertex>> outArray = new LinkedList<Node<Vertex>>(Arrays.asList(skyArray));
						for(int j=0; j<outArray.size() / 2; j++){
							Node<Vertex> temp = outArray.get(j);
							outArray.set(j, outArray.get(outArray.size() - j - 1));
							outArray.set(outArray.size() - j - 1, temp);
						}
						
						AllPathans.add(outArray);
					}
				}
			}
			System.out.println("# AllPathans.size = " + AllPathans.size());
			/* add all paths*/
			from = 1;
			for(List<Node<Vertex>> l : AllPathans){
				Skyline tmp = new Skyline(l,starNode,endNode,start,end);
				tmp.from = from++;
				h.insert(tmp);
			} 
		}
		
		/*add a paths
		List<Node<Vertex>> tmmp = AllPathans.get(11);
		for(Node<Vertex> n: tmmp){
			System.out.print(">" + n.vertex.vertexID);
		}
		System.out.println();
		Skyline tmp = new Skyline( tmmp,starNode,endNode,start,end);
		h.insert(tmp); */
		
		Skyline allpaths = null;
		// expansion loop 
		while ((allpaths = h.deleteMin()) != null) {
			System.out.println();
			System.out.println("From : "+allpaths.from);
			for(Node<Vertex> n:allpaths.path){
				System.out.print(">" + n.vertex.vertexID);
			}
			System.out.println();
			boolean Dominate = false;
			for( Skyline s : list ){
				if(allpaths.isDominateByOther(s)){
					Dominate = true;
				}
			}
			if(Dominate == true){
				System.out.println("delete ...");
				continue;
			}
			boolean containSmallGraFlg=false;
			for (int i = 0; i < allpaths.path.size(); i++) {
				Node<Vertex> n = allpaths.path.get(i);
				if (containSmallGraph(n)) {// if node has small graph
					containSmallGraFlg=true;
					//case 1 : star Node
					if (n.vertex.vertexID == allpaths.starNode.vertex.vertexID) {
						System.out.println("star " + n.vertex.vertexID);
						List<List<Node<Vertex>>> ans = findsmallpath(true, c,  allpaths.start, n, allpaths.path.get(i + 1));
						if(ans.size()>1){
							for (int j = 1; j < ans.size(); j++) {
								Skyline otherpaths = new Skyline(allpaths);
								System.out.println("+from " + from);
								otherpaths.from = from++;
								otherpaths.expansion(false, i, ans.get(j));
								//otherpaths.changeEndPoint();
								h.insert(otherpaths);
							}
							allpaths.expansion(false, i, ans.get(0));
							i+=ans.get(0).size()-1;
						}else if(ans.size() == 1) {
							allpaths.expansion(false, i, ans.get(0));
							i+=ans.get(0).size()-1;
						}
						//System.out.println("add. new path 1");
					//case 2 : end Node
					} else if (n.vertex.vertexID == allpaths.endNode.vertex.vertexID) {
						System.out.println("end " + n.vertex.vertexID);
						List<List<Node<Vertex>>> ans = findsmallpath(false, c,  allpaths.end, n, allpaths.path.get(i - 1));
						if(ans.size() > 1){
							for (int j = 1; j < ans.size(); j++) {
								List<Node<Vertex>> l = ans.get(j);
								Skyline otherpaths = new Skyline(allpaths);
								System.out.println("+from " + from);
								otherpaths.from = from++;
								otherpaths.expansion(true, i, l);
								//otherpaths.changeEndPoint();
								h.insert(otherpaths);
							}
							allpaths.expansion(true, i, ans.get(0));
							i+=ans.get(0).size()-1;
						}else if(ans.size() == 1) {
							allpaths.expansion(true, i, ans.get(0));
							i+=ans.get(0).size()-1;
						}
						//System.out.println("add. new path 2");
						break;
					//case 3 : intermediate Node
					} else {
						System.out.println(".in " + n.vertex.vertexID);
						List<List<Node<Vertex>>> ans = findsmallpath(n, allpaths.path.get(i - 1), allpaths.path.get(i + 1), c);
						if(ans.size() > 1){
							for (int j = 1; j < ans.size(); j++) {
								List<Node<Vertex>> l = ans.get(j);
								Skyline otherpaths = new Skyline(allpaths);
								System.out.println("+from " + from);								
								otherpaths.from = from++;
								otherpaths.expansion(false, i, l);
								//otherpaths.changeEndPoint();
								h.insert(otherpaths);
							}
							allpaths.expansion(false, i,  ans.get(0));
							i+=ans.get(0).size()-1;
						}else if(ans.size() == 1) {
							allpaths.expansion(false, i,  ans.get(0));
							i+=ans.get(0).size()-1;
						}
						//System.out.println("add. new path 3");

					}
				}

			}
			for (Node<Vertex> n : allpaths.path) {
				if (n.vertex.vertexID ==  start && !n.isChildren()) {
					allpaths.starNode = n;
				} else if (n.vertex.vertexID ==  end && !n.isChildren()) {
					allpaths.endNode = n;
				}
			}
			if(containSmallGraFlg){
				h.insert(allpaths);
			}
			else{
				/**/
				System.out.println("Sky line : ");
				System.out.println("allpath.size(): " + allpaths.path.size());
				for (Node<Vertex> n : allpaths.path) {
					System.out.print("->" + n.vertex.vertexID);
				}
				System.out.println();
				list.add(allpaths);
			}
			
			//Thread.sleep(100);
		}
		//System.out.println();
		//結束時間
		long endTime = System.currentTimeMillis();
		//執行時間
		long totTime = startTime - System.currentTimeMillis();
		totTime = endTime - startTime;
		
		for(Skyline s :list){
			System.out.println("Sky line : "+ s.path.size());
			for (Node<Vertex> n : s.path) {
				System.out.print(" " + n.vertex.vertexID);
			}
			System.out.println();
			System.out.println("from : "+s.from);
			//System.out.println(String.format("dist :%.2f  [%.2f] [%.2f]", s.dist, s.dimasion[0], s.dimasion[1]));
		}
		//印出執行時間
		System.out.println("Using Time:" + totTime);
		checkRoad(edges, list);
		System.out.println("===========");
		System.out.println("startPoint: " + start);
		System.out.println("endPoint: " + end);
		System.out.println("inputNodes: " + 6105);
		System.out.println("inputEdges: " + 7035);
		System.out.println("skyPathNum: " + list.size());
		System.out.println("executionTime: " + totTime);
		
		int[] out = new int[2];
		out[0] = list.size();
		out[1] = (int)totTime;
		return  out;
	}
	
	//find the small path in intermediate Node
	public static List<List<Node<Vertex>>> findsmallpath(Node<Vertex> n, Node<Vertex> lastn, Node<Vertex> nextn,Cluster c) {
		List<List<Node<Vertex>>> out = new LinkedList<List<Node<Vertex>>>();
		System.out.println("L="+lastn.vertex.vertexID);
		System.out.println("N="+nextn.vertex.vertexID);
		//System.out.println("findsmallpath2 :" + lastn.vertex.vertexID + " " + n.vertex.vertexID + " " + nextn.vertex.vertexID);
		if (!n.isChildren()) {
			LinkedList<Node<Vertex>> tmp = new LinkedList<Node<Vertex>>();
			out.add(tmp);
			System.out.println("Find no child");
			return out;
		} else if (n.children.size() == 1) {
			System.out.println("only one child");
			out.add(new LinkedList<Node<Vertex>>(n.children));
			return out;
		} else {
			System.out.println("has children");
			/* find map edge node*/
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
			System.out.println("mapLastId="+mapLastId);
			System.out.println("mapNextId="+mapNextId);
			// map node didn't found
			if (findMapFlg1 == false || findMapFlg2 == false) {
				if (findMapFlg1 == false) {
					int last = c.findClusterId(lastn.vertex.vertexID);
					System.out.println("last="+last);
					for (Edge e : n.edges) {
						if (last == e.v1 ) {
							mapLastId = e.v2;
							findMapFlg1 = true;
							break;
						} else if (last == e.v2) {
							mapLastId = e.v1;
							findMapFlg1 = true;
							break;
						}
					}
					if(!findMapFlg1){
						System.out.println("!mapLastId="+mapLastId);
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
			
			System.out.println("mapLastId "+mapLastId);
			System.out.println("mapNextId "+mapNextId);
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
							LinkedList<Node<Vertex>> outArray = new LinkedList<Node<Vertex>>(Arrays.asList(skyArray));
							out.add(outArray);
							foundflg = true;
							/*check more linked
							boolean  containLevel1flg = false;
							for(Node<Vertex> node : outArray){
								if(node.vertex.vertexID == n.vertex.vertexID){
									 containLevel1flg = true;
								}
							}
							if( containLevel1flg ){
								
							}*/
						}
					} else if (skyArray[skyArray.length - 1].vertex.vertexID == mapLastId) {
						if (skyArray[0].vertex.vertexID == mapNextId) {
							LinkedList<Node<Vertex>> outArray = new LinkedList<Node<Vertex>>(Arrays.asList(skyArray));
							out.add(outArray);
							foundflg = true;
							//check more linked
							
							
						}
					}
				}
				if (!foundflg) {
					//System.out.println("!foundflg");
					//System.exit(1);
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
		System.out.println("out1=");
		for(List<Node<Vertex>> s : out){
			for(Node<Vertex> tmpN : s ){
				System.out.print(tmpN.vertex.vertexID+" > ");
			}
			System.out.println();
		}
		return out;
	}
	
	//find the small path in endNode
	public static List<List<Node<Vertex>>> findsmallpath(boolean starflg, Cluster c, int objectID, Node<Vertex> n,
			Node<Vertex> linkedn) {
		List<List<Node<Vertex>>> out = new LinkedList<List<Node<Vertex>>>();
		//System.out.println("findsmallpath1 :" + n.vertex.vertexID + " " + linkedn.vertex.vertexID);

		if (!n.isChildren()) {
			LinkedList<Node<Vertex>> tmp = new LinkedList<Node<Vertex>>();
			out.add(tmp);
			System.out.println("Find no child");
			return out;
		} else if (n.children.size() == 1) {
			System.out.println("1 child");
			out.add(new LinkedList<Node<Vertex>>(n.children));
			return out;
		} else {
			System.out.println("has children");
			/* find map edge node*/
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
			System.out.println("out2=");
			for(List<Node<Vertex>> s : out){
				for(Node<Vertex> tmpN : s ){
					System.out.print(tmpN.vertex.vertexID+" > ");
				}
				System.out.println();
			}
			
			
			return out;
		}
	}
	public static boolean isContainLinked(int ID, Node<Vertex> n) {
		
		for(int i = 0; i < n.edges.size(); i++){
			Edge edge = n.edges.get(i);
			
			if(edge.v1 == ID){
				return true;
			}else if(edge.v2 == ID){
				return true;
			}
		}
		for (int i = 0; i < n.children.size(); i++) {
			if(isContainLinked(ID, n.children.get(i))){
				return true;
			}
		}
		return false;
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
	public static boolean checkRoad(Edge[] edges, List<Skyline> list) {
		for (Skyline s : list) {
			for(int i=0; i < s.path.size()-1; i++){
				Node<Vertex> n = s.path.get(i);
				Node<Vertex> n2 = s.path.get(i+1);
				boolean foundFlg = false;
				for(Edge e : edges){
					if(e.v1 == n.vertex.vertexID && e.v2 == n2.vertex.vertexID){
						foundFlg = true;
						break;
					}else if(e.v1 == n2.vertex.vertexID && e.v2 == n.vertex.vertexID){
						foundFlg = true;
						break;
					}
				}
				if(!foundFlg){
					System.out.println("CHECK ERROR");
					System.out.println(n.vertex.vertexID);
					System.out.println(n2.vertex.vertexID);
				}
			}
		}
		System.out.println("CHECK OK");
		return true;
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
	public static void writeTreeFile(PrintWriter writer, int startPoint,int endPoint,int inputNodes,int inputEdges,int skyPathNum,int executionTime) {
		try {
			writer.printf("%d,%d,%d,%d,%d,%d",startPoint, endPoint, inputNodes, inputEdges, skyPathNum, executionTime);
			writer.println();
			writer.flush();
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
