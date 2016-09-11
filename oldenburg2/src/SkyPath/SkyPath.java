package SkyPath;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Stack;
import buildTree.*;

public class SkyPath {
	//public static final String nodeFile = "./data/small/sky1.txt";
	//public static final String edgeFile = "./data/small/sky2.txt";
	public static final String nodeFile = Main2.nodeFile;
	public static final String edgeFile = Main2.edgeFile;
	public ArrayList<SkyNode> nodes = new ArrayList<SkyNode>();
	public ArrayList<SkyEdge> edges = new ArrayList<SkyEdge>();
	public Stack<SkyNode> stack = new Stack<SkyNode>();
	public ArrayList<Object[]> pathSet = new ArrayList<Object[]>();
	public ArrayList<Object[]> multipointSkypathSet = new ArrayList<Object[]>();
	public double[][] min;
	public PriorityQueue<String> queue = new PriorityQueue<String>();
	public int[] predecessor;
	
	public static void main(String[] args) {
		String[] start ={"2983"};
		String[] end ={"3030"};
		SkyPath skyPath = new SkyPath();
		skyPath.inputData();
		skyPath.multipointSkyline(start, end);
		//Object[] ans =skyPath.multipointSkypathSet.get(0);//裡面就是答案，每個Object[]就是一條路
		int count = skyPath.multipointSkypathSet.size();
		for(int i=0; i<count; i++){
			Object[] ans =skyPath.multipointSkypathSet.get(i);
			System.out.println("Path "+(i+1)+" ------>");
			for(Object a : ans){
				System.out.println((SkyNode)a);
			}
		}
	}
	public void inputData(Edge[] elist, Tree.Node<Vertex>[] vlist) {
		try {
			for(Tree.Node<Vertex> v : vlist){
				nodes.add(new SkyNode(String.format("%d",v.vertex.vertexID), v.vertex.x, v.vertex.y));
			}
			for(Edge e : elist){
				SkyEdge temp = new SkyEdge(String.format("%d",e.v1),String.format("%d",e.v2));
				for (int i = 0; i <Main2.dimasion; i++) {
					temp.addCost(e.dimasion[i]);
				}
				edges.add(temp);
				for (int i = 0; i < nodes.size(); i++) {
					if (nodes.get(i).getNodeId().equals(String.format("%d",e.v1))) {
						nodes.get(i).addRelationNodeId(String.format("%d",e.v2));
					}
					if (nodes.get(i).getNodeId().equals(String.format("%d",e.v2))) {
						nodes.get(i).addRelationNodeId(String.format("%d",e.v1));
					}
				}
			}
			min = new double[6200][edges.get(0).getCost().size()];
			predecessor = new int[6200];
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	public void inputData() {
		try {
			FileReader nodeFileReader = new FileReader(nodeFile);
			BufferedReader nodeBufferReader = new BufferedReader(nodeFileReader);
			String node = null;
			FileReader edgeFileReader = new FileReader(edgeFile);
			BufferedReader edgeBufferReader = new BufferedReader(edgeFileReader);
			String edge = null;
			while ((node = nodeBufferReader.readLine()) != null) {
				String[] fields = node.split(" ");
				nodes.add(new SkyNode(fields[0], Double.parseDouble(fields[1]), Double.parseDouble(fields[2])));
			}
			while ((edge = edgeBufferReader.readLine()) != null) {
				String[] fields = edge.split(" ");
				SkyEdge temp = new SkyEdge(fields[1], fields[2]);
				for (int i = 3; i < fields.length; i++) {
					temp.addCost(Double.parseDouble(fields[i]));
				}
				edges.add(temp);
				for (int i = 0; i < nodes.size(); i++) {
					if (nodes.get(i).getNodeId().equals(fields[1])) {
						nodes.get(i).addRelationNodeId(fields[2]);
					}
					if (nodes.get(i).getNodeId().equals(fields[2])) {
						nodes.get(i).addRelationNodeId(fields[1]);
					}
				}
			}
			min = new double[6200][edges.get(0).getCost().size()];
			predecessor = new int[6200];
		} catch (FileNotFoundException e){
			System.out.println("FileNot Found Exception");
			e.printStackTrace();
		} catch (IOException e){
			System.out.println("IO Exception");
			e.printStackTrace();
		}
	}
	
	public SkyPath() {
		nodes = new ArrayList<SkyNode>();
		edges = new ArrayList<SkyEdge>();
		stack = new Stack<SkyNode>();
		pathSet = new ArrayList<Object[]>();
		multipointSkypathSet = new ArrayList<Object[]>();
		queue = new PriorityQueue<String>();
	}

	public boolean isNodeInStack(SkyNode node) {
		Iterator<SkyNode> it = stack.iterator();
		while (it.hasNext()) {
			SkyNode node1 = (SkyNode) it.next();
			if (node.equals(node1))
				return true;
		}
		return false;
	}

	public void showAndSavePath() {
		Object[] o = stack.toArray();
		/*
		 * for (int i = 0; i < o.length; i++) { Node nNode = (Node) o[i];
		 * 
		 * if(i < (o.length - 1)) System.out.print(nNode.getNodeId() + "->");
		 * else System.out.print(nNode.getNodeId()); }
		 */
		for (int i = 0; i < pathSet.size(); i++) {
			if (pathEquals(pathSet.get(i), o)) {
				return;
			}
		}
		if (pathSet.size() > 0) {
			for (int i = 0; i < pathSet.size(); i++) {
				if (compare(o, pathSet.get(i)) == 1) {
					pathSet.remove(i);
					i--;
				}
			}
		}
		pathSet.add(o);
		// System.out.println("\n");
	}

	public boolean getPaths(SkyNode cNode, SkyNode pNode, SkyNode sNode, SkyNode eNode) {
		SkyNode nNode = null;
		if (cNode != null && pNode != null && cNode.equals(pNode)) {
			return false;
		}
		if (cNode != null) {
			int i = 0;

			stack.push(cNode);
			if (pathSet.size() > 0) {
				for (int j = 0; j < pathSet.size(); j++) {
					if (compare(pathSet.get(j), stack.toArray()) == 1) {
						return true;
					}
				}
			}
			if (min[Integer.parseInt(cNode.getNodeId())][0] == 0) {
				Object[] o1 = stack.toArray();
				for (int j = 0; j < o1.length - 1; j++) {
					for (int k = 0; k < edges.get(0).getCost().size(); k++) {
						min[Integer.parseInt(cNode.getNodeId())][k] = min[Integer.parseInt(cNode.getNodeId())][k]
								+ searchEdge(((SkyNode) (o1[j])).getNodeId(), ((SkyNode) (o1[j + 1])).getNodeId()).getCost()
										.get(k);
					}
				}
			} else {
				double[] cost = new double[edges.get(0).getCost().size()];
				Object[] o1 = stack.toArray();
				boolean o1DominateO2 = true;
				boolean o2DominateO1 = true;
				for (int j = 0; j < o1.length - 1; j++) {
					for (int k = 0; k < edges.get(0).getCost().size(); k++) {
						cost[k] = cost[k] + searchEdge(((SkyNode) (o1[j])).getNodeId(), ((SkyNode) (o1[j + 1])).getNodeId())
								.getCost().get(k);
					}
				}
				for (int j = 0; j < edges.get(0).getCost().size(); j++) {
					if (min[Integer.parseInt(cNode.getNodeId())][j] > cost[j]) {
						o1DominateO2 = false;
					}
					if (cost[j] > min[Integer.parseInt(cNode.getNodeId())][j]) {
						o2DominateO1 = false;
					}
				}
				if (o1DominateO2 == true && o2DominateO1 == false) {
					return true;
				} else if (o1DominateO2 == false && o2DominateO1 == true) {
					for (int j = 0; j < edges.get(0).getCost().size(); j++) {
						min[Integer.parseInt(cNode.getNodeId())][j] = cost[j];
					}
				}
			}
			if (cNode.equals(eNode)) {
				showAndSavePath();
				return true;
			} else {
				if (cNode.getRelationNodesId().size() == 0) {
					nNode = null;
				} else {
					nNode = searchNode(cNode.getRelationNodesId().get(i));
				}
				while (nNode != null) {
					if (pNode != null && (nNode.equals(sNode) || nNode.equals(pNode) || isNodeInStack(nNode))) {
						i++;
						if (i >= cNode.getRelationNodesId().size())
							nNode = null;
						else
							nNode = searchNode(cNode.getRelationNodesId().get(i));
						continue;
					}
					if (getPaths(nNode, cNode, sNode, eNode)) {
						stack.pop();
					}
					i++;
					if (i >= cNode.getRelationNodesId().size())
						nNode = null;
					else
						nNode = searchNode(cNode.getRelationNodesId().get(i));
				}

				stack.pop();
				return false;
			}
		} else
			return false;
	}


	public SkyNode searchNode(String nodeId) {
		for (int i = 0; i < nodes.size(); i++) {
			if (nodes.get(i).getNodeId().equals(nodeId)) {
				return nodes.get(i);
			}
		}
		return null;
	}

	public SkyEdge searchEdge(String startNodeId, String endNodeId) {
		for (int i = 0; i < edges.size(); i++) {
			if (edges.get(i).getStartNodeId().equals(startNodeId) && edges.get(i).getEndNodeId().equals(endNodeId)
					|| edges.get(i).getStartNodeId().equals(endNodeId)
							&& edges.get(i).getEndNodeId().equals(startNodeId)) {
				return edges.get(i);
			}
		}
		return null;
	}

	public int compare(Object[] o1, Object[] o2) {
		double[] cost1 = new double[edges.get(0).getCost().size()];
		double[] cost2 = new double[edges.get(0).getCost().size()];
		boolean o1DominateO2 = true;
		boolean o2DominateO1 = true;
		for (int i = 0; i < o1.length - 1; i++) {
			for (int j = 0; j < edges.get(0).getCost().size(); j++) {
				cost1[j] = cost1[j]
						+ searchEdge(((SkyNode) (o1[i])).getNodeId(), ((SkyNode) (o1[i + 1])).getNodeId()).getCost().get(j);
			}
		}
		for (int i = 0; i < o2.length - 1; i++) {
			for (int j = 0; j < edges.get(0).getCost().size(); j++) {
				cost2[j] = cost2[j]
						+ searchEdge(((SkyNode) (o2[i])).getNodeId(), ((SkyNode) (o2[i + 1])).getNodeId()).getCost().get(j);
			}
		}
		for (int i = 0; i < edges.get(0).getCost().size(); i++) {
			if (cost1[i] > cost2[i]) {
				o1DominateO2 = false;
			}
			if (cost2[i] > cost1[i]) {
				o2DominateO1 = false;
			}
		}
		if (o1DominateO2 == true && o2DominateO1 == false) {
			return 1;
		} else if (o1DominateO2 == false && o2DominateO1 == true) {
			return -1;
		} else {
			return 0;
		}
	}

	public void multipointSkyline(String[] startPoint, String[] endPoint) {
		for (int i = 0; i < startPoint.length; i++) {
			for (int j = 0; j < endPoint.length; j++) {
				preprocess(searchNode(startPoint[i]), searchNode(endPoint[j]));
				getPaths(searchNode(startPoint[i]), null, searchNode(startPoint[i]), searchNode(endPoint[j]));
				for (int k = 0; k < pathSet.size(); k++) {
					multipointSkypathSet.add(pathSet.get(k));
				}
				stack = new Stack<SkyNode>();
				pathSet = new ArrayList<Object[]>();
				min = new double[6200][edges.get(0).getCost().size()];
				queue = new PriorityQueue<String>();
				predecessor = new int[6200];
			}
		}
		pathSet = multipointSkypathSet;
		multipointSkypathSet = new ArrayList<Object[]>();
		for (int i = 0; i < pathSet.size(); i++) {
			boolean isDominated = false;
			for (int j = 0; j < pathSet.size(); j++) {
				if (compare(pathSet.get(j), pathSet.get(i)) == 1) {
					isDominated = true;
				}
			}
			if (isDominated == false) {
				multipointSkypathSet.add(pathSet.get(i));
			}
		}
	}

	public void preprocess(SkyNode startNode, SkyNode endNode) {
		ArrayList<SkyNode> tempList = new ArrayList<SkyNode>();
		ArrayList<SkyNode> ansList = new ArrayList<SkyNode>();
		for (int i = 0; i < predecessor.length; i++) {
			predecessor[i] = -2;
		}
		predecessor[Integer.parseInt(startNode.getNodeId())] = -1;
		queue.add(startNode.getNodeId());
		while (true) {
			String temp;
			ArrayList<String> relationNodesId = new ArrayList<String>();
			temp = queue.poll();
			if (temp.equals(endNode.getNodeId())) {
				int i = Integer.parseInt(endNode.getNodeId());
				while (i != -1) {
					tempList.add(searchNode(String.valueOf(i)));
					i = predecessor[i];
				}
				for (int j = tempList.size() - 1; j >= 0; j--) {
					ansList.add(tempList.get(j));
				}
				pathSet.add(ansList.toArray());
				break;
			}
			relationNodesId = searchNode(temp).getRelationNodesId();
			for (int i = 0; i < relationNodesId.size(); i++) {
				if (predecessor[Integer.parseInt(relationNodesId.get(i))] != -2) {
					continue;
				}
				queue.add(relationNodesId.get(i));
				predecessor[Integer.parseInt(relationNodesId.get(i))] = Integer.parseInt(temp);
			}
		}
	}

	public boolean pathEquals(Object[] o1, Object[] o2) {
		boolean equals = true;
		if (o1.length != o2.length) {
			return false;
		}
		for (int i = 0; i < o1.length; i++) {
			if (!((SkyNode) (o1[i])).equals((SkyNode) (o2[i]))) {
				equals = false;
			}
		}
		return equals;

	}
}
