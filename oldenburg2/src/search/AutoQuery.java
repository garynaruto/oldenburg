package search;
import java.io.PrintWriter;
import buildTree.Vertex;

public class AutoQuery {
	public static final int dimI = 4;
	public static final int[] dim = {2,3,4,5,6};
	public static final int[] start = {300,800,1300,3000,500};
	public static final int[][] end = {{294,804,1329,3008,519},
										{234,832,1421,3098,563},
										{231,847,1442,3208,591},
										{121,847,1519,3223,610},
										{119,976,1539,3241,631}};
	public static final String[] name = {"0.5Km1-1_", "1.0Km1-1_","1.5Km1-1_","2.0Km1-1_","2.5Km1-1_"};
	public static final String folder ="./data/ans/";
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		/**/
			Query.d = dim[dimI];
			for(int i=0; i<name.length; i++){
				int totles=0;
				int totletime=0;
				for(int e =0; e<5; e++){
					System.out.println(start[e]+" "+end[i][e]);
					Query.start = start[e];
					Query.end = end[i][e];
					int[] ans = Query.main();
					String file= folder+dim[dimI]+"Dimension"+name[i]+(e+1)+".txt";
					writeFile(file, start[e], end[i][e], ans);
					totles += ans[0];
					totletime += ans[1];
				}
				writeFile(folder+dim[dimI]+"Dimension"+name[i]+"Average.txt", totles/5.0, totletime/5.0 );
			}
		
	}
	public static void writeFile(String s,int start, int end, int[] ans) {
		try {
			PrintWriter writer = new PrintWriter(s, "UTF-8");
			writer.println("startPoint: " + start);
			writer.println("endPoint: " + end);
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
