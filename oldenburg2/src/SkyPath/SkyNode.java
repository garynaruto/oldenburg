package SkyPath;

import java.util.ArrayList;

public class SkyNode {
	private ArrayList<String> relationNodesId = new ArrayList<String>();
	private String nodeId = null;
	private double x, y;
	private int count = 0;

	public SkyNode(String nodeId, double x, double y) {
		this.nodeId = nodeId;
		this.x = x;
		this.y = y;
	}
	public String toString(){
		return nodeId+" ";
	}
	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public ArrayList<String> getRelationNodesId() {
		return relationNodesId;
	}

	public void setRelationNodesId(ArrayList<String> relationNodesId) {
		this.relationNodesId = relationNodesId;
	}

	public void addRelationNodeId(String nodeId) {
		relationNodesId.add(nodeId);
	}

	public boolean equals(SkyNode node) {
		if (this.getNodeId().equals(node.getNodeId())) {
			return true;
		} else {
			return false;
		}
	}
}
