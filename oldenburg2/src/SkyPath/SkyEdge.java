package SkyPath;
import java.util.ArrayList;

public class SkyEdge 
{
	private String startNodeId;
	private String endNodeId;
	private ArrayList<Double> cost=new ArrayList<Double>();
	public SkyEdge(String startNodeId,String endNodeId)
	{
		this.startNodeId=startNodeId;
		this.endNodeId=endNodeId;
	}
	public String getStartNodeId()
	{
		return startNodeId;
	}
	public void setStartNodeId(String startNodeId)
	{
		this.startNodeId=startNodeId;
	}
	public String getEndNodeId()
	{
		return endNodeId;
	}
	public void setEndNodeId(String endNodeId)
	{
		this.endNodeId=endNodeId;
	}
	public ArrayList<Double> getCost()
	{
		return cost;
	}
	public void setCost(ArrayList<Double> cost)
	{
		this.cost=cost;
	}
	public void addCost(Double cost)
	{
		this.cost.add(cost);
	}
}
