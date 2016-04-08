package Lab2;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import org.jgrapht.alg.cycle.PatonCycleBase;
import org.jgrapht.graph.ListenableUndirectedGraph;
import org.jgrapht.graph.ListenableUndirectedWeightedGraph;

public class ElectricalCircuit {
	static int versePointer = 0; //variable which contains verse of our Matrix where we should add new equation to
	static int edgesCounter = 0; //
	static ListenableUndirectedWeightedGraph<String, Edge> graph = new ListenableUndirectedWeightedGraph<String, Edge>(Edge.class); 
	
	public static void fillGraph(String path) { // method which reads file with all edges and sem and write them to our graph
		try {
			Scanner dataInput = new Scanner(new File(path));
			String line;
			boolean inEdges = false;
			boolean inForce = false;
			
			while(dataInput.hasNextLine()) {
				line = dataInput.nextLine();
				
				if(line.equals("edges:")) {
					inEdges = true;
					inForce = false;
					line = dataInput.nextLine();
				}
				
				if(line.equals("force:")) {
					inEdges = false;
					inForce = true;
					line = dataInput.nextLine();
				}
				
				
				if(inForce == true) {
					String splittedForce[] = line.split(" ");
					graph.addVertex(splittedForce[0]);
					graph.addVertex(splittedForce[1]);
					Edge currentEdge = new Edge(splittedForce[0], splittedForce[1], Double.parseDouble(splittedForce[2]), 0, true);
					if(graph.containsEdge(splittedForce[0], splittedForce[1]) == false) {
						currentEdge.index = edgesCounter;
						edgesCounter++;
						graph.addEdge(currentEdge.vertex1, currentEdge.vertex2, currentEdge);
					}
				}
				
				if(inEdges == true) {
					String splittedEdge[] = line.split(" ");
					graph.addVertex(splittedEdge[0]);
					graph.addVertex(splittedEdge[1]);
					Edge currentEdge = new Edge(splittedEdge[0], splittedEdge[1], Double.parseDouble(splittedEdge[2]), 0, false);
					if(graph.containsEdge(splittedEdge[0], splittedEdge[1]) == false) {
						currentEdge.index = edgesCounter;
						edgesCounter++;
						graph.addEdge(currentEdge.vertex1, currentEdge.vertex2, currentEdge);
					}
				}
			}
			
			dataInput.close();
		}catch(Exception e) {}
		indexEdges(); //where we've got only correct edges in our graph (for example edges (2,3) and (3,2) will be only one edge) we can ascripe them indexes.
	} 
	
	public static void indexEdges() { // helping method to fill indexes of existing edges
		Set<Edge> allEdgesInGraph = graph.edgeSet();
		int i = 0;
		for(Edge e : allEdgesInGraph) {
			e.index = i;
			i++;
		}
	}
	
	public static void drawArrays(double[][] matrix, double[] freeTerms, int size) { //helping method to draw array of equations and vector of free terms
		for(int i = 0; i < size; i++) {
			for(int j = 0; j < size; j++) {
				System.out.print(matrix[i][j] + " ");
			}
			System.out.println("|||" + freeTerms[i]);
		}	
	}
	
	public static void kirchoff1 (double[][] matrix, int SIZE) { 
		Set<String> vertexes = graph.vertexSet(); 
		for(String v : vertexes) { 
			Set<Edge> touchingEdges = graph.edgesOf(v); //touching edges is a set of all edges outgoing vector v
			
			for(Edge e : touchingEdges) { //this loop create one equation 
				matrix[versePointer][e.index] = 1.0;
			}
			versePointer++;
		}
		versePointer--;
		for(int i = 0; i < SIZE; i++) {
			matrix[versePointer][i] = 0.0; //the last equation is useless so I can delete it in order to have one more equation from Kirchoff2
		}
	}
	
	public static void kirchoff2 (double[][] matrix, double[] freeTerms, int SIZE) {
		PatonCycleBase<String,Edge> pcb = new PatonCycleBase<String,Edge>(); //creating object which is able to find cycles in our graph
		pcb.setGraph(graph); //it will be looking for cycles in our graph
		List<List<String>> list = pcb.findCycleBase(); //list is a list of all cycles (cycle is a list of vertexes)
		Comparator c = new ListsSizeComparator();
		list.sort(c); //sorting list in the way that we will operate on the smallest cycles at the beginning

		for(List<String> cycle : list) {
			if(versePointer < SIZE){	
			for(String v : cycle) {
				if(versePointer < SIZE){
				Set<Edge> touchingEdges = graph.edgesOf(v);
				for(Edge e : touchingEdges) {
					if(versePointer < SIZE && cycle.contains(e.vertex1) && cycle.contains(e.vertex2)){
						if(e.isSEM == false) {
							matrix[versePointer][e.index] = e.value;
						}
						if(e.isSEM == true) {
							freeTerms[versePointer] = e.value;
						}
					}	
				}
			}
		}
			versePointer++;
		}
		}	
	}		

	
	public static void drawGraph( String pythonScriptPath, String resultsFile) {
		String [] systemLine = {"python", pythonScriptPath, resultsFile}; 
		ProcessBuilder pb = new ProcessBuilder(systemLine); 
		Process p;
		
		try {
			p = pb.start();
		} catch (IOException e){
			System.out.println("Python have not started.");
		}
    }

	public static void main(String[] args) {
		String pathToTheData = "data.txt";
		String pathToTheDrawingDataFile = "drawingData.txt";
		fillGraph(pathToTheData); //creating a graph with vertexes and edges from the file
		
		int SIZE = graph.edgeSet().size(); //array of equations will have SIZE = number of edges
		double[][] matrix = new double[SIZE][SIZE]; //creating an array for all equations 
		double[] freeTerms = new double[SIZE]; //creating an array for free terms of the equations
		double[] results = new double[SIZE]; //creating an array for values of the intensities
		
		kirchoff1(matrix, SIZE);
		kirchoff2(matrix, freeTerms, SIZE);
		
		GaussJordan.SIZE = SIZE;
		GaussJordan.Matrix = matrix;
		GaussJordan.FreeTerms = freeTerms;
		GaussJordan.fillUnknows();
		GaussJordan.gauss();
		GaussJordan.clear();
		System.out.println(SIZE);
		results = GaussJordan.passResults();
		for(Edge e : graph.edgeSet()){
			e.result= results[e.index];	
		}
		
		
		try {
			PrintWriter writer = new PrintWriter(new File(pathToTheDrawingDataFile));
			for(Edge e : graph.edgeSet()){
				writer.println(e.vertex1 + " "+ e.vertex2 + " "+ e.result);
			}
			writer.close();
		} catch (Exception e) {System.out.println(e.getMessage());}	
		
		drawGraph("drawingScript.py", "drawingData.txt");
	}
}
