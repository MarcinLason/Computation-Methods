package Lab2;

import org.jgrapht.graph.DefaultWeightedEdge;

public class Edge extends DefaultWeightedEdge {
	String vertex1;
	String vertex2;
	double value;
	int index;
	boolean isSEM;
	
	public Edge(String vertex1, String vertex2, double value, int index, boolean isSEM) {
		this.index = index;
		this.value = value;
		this.vertex1 = vertex1;
		this.vertex2 = vertex2;
		this.isSEM = isSEM;
	}
	
	public String toString() {
		return ("{" + index + ". " + vertex1 + ":" + vertex2 + " - " + value + " " + isSEM + "}");
	}
}
