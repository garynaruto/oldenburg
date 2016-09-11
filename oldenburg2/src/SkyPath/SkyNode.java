package SkyPath;

import java.util.ArrayList;

public class SkyNode {
	public ArrayList<Integer> relationNodesId = new ArrayList<Integer>();
	public int nodeId;
	public double x, y;
	public int count = 0;
	
	public SkyNode(int nodeId, double x, double y) {
		this.nodeId = nodeId;
		this.x = x;
		this.y = y;
	}
	public String toString(){
		return nodeId+" ";
	}
	public int getNodeId() {
		return nodeId;
	}
	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public ArrayList<Integer> getRelationNodesId() {
		return relationNodesId;
	}

	public void setRelationNodesId(ArrayList<Integer> relationNodesId) {
		this.relationNodesId = relationNodesId;
	}

	public void addRelationNodeId(Integer nodeId) {
		relationNodesId.add(nodeId);
	}

	public boolean equals(SkyNode node) {
		if (this.nodeId == node.nodeId) {
			return true;
		} else {
			return false;
		}
	}
}
