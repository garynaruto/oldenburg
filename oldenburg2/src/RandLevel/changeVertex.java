package RandLevel;

import java.io.File;
import java.io.PrintWriter;
import java.util.Scanner;

import buildTree.Vertex;

public class changeVertex {
	public static final int node = 6105;
	public static final int s = 5;
	public static final String clFile =  "./data/Oldenburg/tmp_6Dimension_Cluster("+s+").txt";
	public static final String nodeFile =  "./data/Oldenburg/OL.cnode.txt";
	public static final String writeFile = "./data/6Dimension_VertexCluster"+s+".txt";
	
	public static void main(String[] args) {
		Vertex[] vertexs = readVertex();
		int nodeNum = 0;
		int skyLineCount = 0;
		try {
			File file = new File(clFile);
			Scanner s = new Scanner(file);
			for (int i = 0; i < node; i++) {
				nodeNum = s.nextInt();
				skyLineCount = s.nextInt();
				if(vertexs[i].vertexID == nodeNum){
					vertexs[i].count = skyLineCount;
				}
				else{
					System.out.println("Erroe");
					System.exit(1);;
				}
			}
			s.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			PrintWriter writer = new PrintWriter(writeFile, "UTF-8");
			for (Vertex v : vertexs) {
				writer.println(String.format("%d %f %f %d", v.vertexID, v.x, v.y, v.count));
			}
			writer.flush();
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("done");
	}
	
	public static Vertex[] readVertex() {
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

}
