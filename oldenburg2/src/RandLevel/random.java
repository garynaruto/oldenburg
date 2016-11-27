package RandLevel;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import SkyPath.SkyNode;
import SkyPath.SkyPath;
import buildTree.*;
import java.util.Random;


public class random {
	public static final int node = 6105;// OL.cnode100.txt  1000
	public static final int edge = 7035;// OL.cedge100.txt 1152
	public static final int dimasion = 2;
	public static final String nodeFile = "./data/Oldenburg/OL.cnode.txt";
	public static final String edgeFile = "./data/Oldenburg/OL.cedge.txt";
	public static final String writeFile = "./data/Oldenburg/OL.rand_cnode.txt";
	public static void main(String[] args) {
		// read node and edge
		Edge[] edges = readedge();
		Vertex[] vertexs = readVertex();
		System.out.println(edges.length + "\n" + vertexs.length);
		
		for(int i=0; i<vertexs.length; i++){
			System.out.println(vertexs[i].vertexID+" "+vertexs[i].x+" "+vertexs[i].y);
		}
		Random ran = new Random();
		try {
			PrintWriter writer = new PrintWriter(writeFile, "UTF-8");
			for(int i=0; i<vertexs.length; i++){
				writer.println(vertexs[i].vertexID+" "+vertexs[i].x+" "+vertexs[i].y+" "+ran.nextInt(6000));
			}
			writer.flush();
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	public static void writeClusterFile(Vertex[] vertexs) {
		try {
			PrintWriter writer = new PrintWriter(writeFile, "UTF-8");
			for (Vertex v : vertexs) {
				writer.println(String.format("%d %d", v.vertexID, v.level));
			}
			writer.flush();
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Edge[] readedge() {
		System.out.println("readedge");
		Edge[] out = new Edge[edge];
		int edgeID, a, b;
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