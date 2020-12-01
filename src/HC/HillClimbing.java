package HC;

import java.io.*;
import java.util.*;

public class HillClimbing {
	//Define Output Path
	private static String OutputPath;
	private static String OutputTracePath;
	private static PrintWriter Output;
	private static PrintWriter OutputTrace;
	private static ArrayList<Long> sortedInitialVertices;
	private static int index = 0;
	/**
	 * @author chujiechen
	 * @param G: original graph
	 * @param cutoff: time upper limit
	 * @param randomSeed: a seed
	 * @return result vertex cover
	 */
	public static Graph hillClimbing(Graph G, int cutoff, int randomSeed){
		long start_time = System.currentTimeMillis();
		long end_time;
		float run_time = 0;
		// Initial VC
		Graph current = makeNode(G, randomSeed);
		sortedInitialVertices = (ArrayList<Long>) current.getDegreeSortedVertices();
		while(run_time < cutoff) {
			int currentSize = current.getVerticesNum();
			Graph next = findSuccessor(current);
			end_time = System.currentTimeMillis();
			run_time = (end_time-start_time)/1000F;
			
			if(next.getVerticesNum() >= currentSize) {
				return current;
			}
			current = next;
			OutputTrace.printf("%.3f, %d%n", run_time, current.getVerticesNum());
		}
		return current;
	}

	private static Graph findSuccessor(Graph current) {
		// TODO Auto-generated method stub
		Graph next = current;
//		for(long i: next.getDegreeSortedVertices()) {
		while(index < sortedInitialVertices.size()) {
			long i = sortedInitialVertices.get(index);
			Vertex u = next.getVertex(i);
			if(next.isRemovableVertex(u)) {
				// a deepcopied graph is produced after remove
				next = next.removeVertex(u, false);
				index++;
				break;
			}
			index++;
		}
		return next;
	}

	private static Graph makeNode(Graph g, int randomSeed) {
		// TODO Auto-generated method stub
		int n = g.getVerticesNum();
		Random rand = new Random();
		rand.setSeed(randomSeed);
		int id = rand.nextInt(n) + 1;
		Vertex u = g.getVertex(id);
		if(g.isRemovableVertex(u)) {
			g.removeVertex(u, false);
		}
		return g;
	}

	public static void main(String[] args) throws FileNotFoundException {
    	String filename = "star2.graph";
    	int cutoff = 600;
    	int randomSeed = 1;
		OutputPath = "output/"+filename +"_hillClimbing_"+ cutoff +"_"+ randomSeed+".sol";
		OutputTracePath = "output/"+filename +"_hillClimbing_" +cutoff+"_"+randomSeed+".trace";
		Output = new PrintWriter(OutputPath);
		OutputTrace = new PrintWriter(OutputTracePath);
		
    	System.out.println("running...");
    	Graph G = Graph.read("data/" + filename);
    	Graph res = hillClimbing(G, cutoff, randomSeed);
    	
    	System.out.println(res);
    	Output.printf(res.toString());
    	System.out.println("Done");
    	Output.close();
    	OutputTrace.close();
    }
}