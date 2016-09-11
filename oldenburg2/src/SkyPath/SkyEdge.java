package SkyPath;

import java.util.ArrayList;

public class SkyEdge {
	private int startNodeId;
	private int endNodeId;
	private ArrayList<Double> cost = new ArrayList<Double>();

	public SkyEdge(int startNodeId, int endNodeId) {
		this.startNodeId = startNodeId;
		this.endNodeId = endNodeId;
	}

	public int getStartNodeId() {
		return startNodeId;
	}

	public void setStartNodeId(int startNodeId) {
		this.startNodeId = startNodeId;
	}

	public int getEndNodeId() {
		return endNodeId;
	}

	public void setEndNodeId(int endNodeId) {
		this.endNodeId = endNodeId;
	}

	public ArrayList<Double> getCost() {
		return cost;
	}

	public void setCost(ArrayList<Double> cost) {
		this.cost = cost;
	}

	public void addCost(Double cost) {
		this.cost.add(cost);
	}
}
