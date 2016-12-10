package search;
import java.io.PrintWriter;

import buildTree.Edge;
import buildTree.Vertex;

public class AutoQuery2 {
	public static  int dimI = 4;
	public static final int[] dim = {2,3,4,5,6};
	public static final int[] start = {300,800,1300,3000,500};
	public static final int[][][] end2 = {{{294,291},{804,806},{1329, 1330},{3008, 3009},{519, 520}},          
											{{234, 233},{832, 834},{1421, 1422},{3098, 3100},{563, 564}},
											{{231, 230},{847, 848},{1442, 1443},{3208, 3209},{591, 586}},
											{{121, 123},{948, 949},{1519, 1515},{3223, 3224},{610, 611}},
											{{119, 120},{976, 977},{1539, 1540},{3241, 3246},{631, 628}}};
	public static final String[] name = {"0.5Km1-2_", "1.0Km1-2_","1.5Km1-2_","2.0Km1-2_","2.5Km1-2_"};
	public static final String folder ="./data/ans1-2/";
	public static void main(String[] args) throws Exception {
		
		for(dimI = 0; dimI<5; dimI++){
			System.out.println("dimI : "+dimI);
			RandLevel.Main2.dimasion = dim[dimI];
			buildTree.Main2.dimasion = dim[dimI];
			Edge.setDimasion(dim[dimI]);
			RandLevel.Main2.main(new String[1]);
			Query.d = dim[dimI];
			for(int i=0; i<name.length; i++){
				int totles=0;
				int totletime=0;
				for(int e =0; e<5; e++){
					//System.out.println(start[e]+" "+end2[i][e][0]+" "+end2[i][e][1]);
					int[] startArray = new int[1];
					startArray[0] = start[e];
					//1-2 
					int[] endArray = end2[i][e];
					int[] ans = Query.main(startArray,endArray);
					
					String file= folder+dim[dimI]+"DimensionCorr"+name[i]+(e+1)+".txt";
					writeFile(file, start[e], end2[i][e], ans);
					totles += ans[0];
					totletime += ans[1];
				}
				writeFile(folder+dim[dimI]+"DimensionCorr"+name[i]+"Average.txt", totles/5.0, totletime/5.0 );
			}
		}
	}
	public static void writeFile(String s,int start, int[] endArray, int[] ans) {
		try {
			PrintWriter writer = new PrintWriter(s, "UTF-8");
			writer.println("startPoint: " + start);
			writer.print("endPoint:");
			for(int tmp : endArray){
				writer.print(" "+tmp);
			}
			System.out.println();
			writer.println("inputNodes: " + 6105);
			writer.println("inputEdges: " + 7035);
			writer.println("skyPathNum: " +ans[0]);
			writer.println("executionTime: " + ans[1]);
			
			writer.flush();
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void writeFile(String s,double avgs, double avgt) {
		try {
			PrintWriter writer = new PrintWriter(s, "UTF-8");
			writer.println("skyPathNum: " +avgs);
			writer.println("executionTime: " + avgt);
			
			writer.flush();
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
