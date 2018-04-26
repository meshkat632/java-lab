package thoughtworks.trains.domain;

import java.util.ArrayList;
import java.util.List;

public class Graph {	

	

	private List<Vertex> vertices;
	private List<Edge> edges;	

	private Graph() {
		this.vertices = new ArrayList<>();
		this.edges = new ArrayList<>();		
	}
	
	private List<Vertex> getVertices() {	
		return vertices;		
	}	

	private List<Edge> getEdges() {
		return edges;
	}
	
	public boolean hasEdge(Edge edge) {
		return edges.contains(edge);
	}
	
	public void addVertex(Vertex vertex) {
		if(vertices.contains(vertex)) {
			System.out.println("vertex:"+vertex+" already exits.");
		}else vertices.add(vertex);				
	}
	
	public void addEdge(Edge edge) {
		if(hasEdge(edge)) {
			System.out.println("edge:"+edge+" already exits.");
		}else {
			Vertex source = edge.getSource();
			Vertex destination = edge.getDestination();
			if(!vertices.contains(source)) {
				addVertex(source);
				source.addOutGoingEdge(edge);
			}
			if(!vertices.contains(destination)) {
				addVertex(destination);		
				destination.addIncomingEdge(edge);
			}
			
			edges.add(edge);			
		}
	}
	
	private Vertex getVertexWithName(String name) {
		return vertices.stream().filter(vertex -> vertex.getName().equals(name)).findAny().orElse(null);	
	}
	
	private void addEdge(String source, String destination, int distance) {
		Vertex sourceVertext = getVertexWithName(source);
		Vertex destinationVertext = getVertexWithName(destination);
		if(sourceVertext == null) sourceVertext = new Vertex(source); 
		if(destinationVertext == null) destinationVertext = new Vertex(destination);
		
		addEdge(new Edge(sourceVertext, destinationVertext, distance));
		 
		
	}
	
	
	@Override
	public String toString() {
		return edges.stream()
		       .map(edge -> edge.getSource().getName()+""+edge.getDestination().getName()+""+edge.getWeight())
               .reduce((edge1, edge2) -> edge1+" "+edge2)
               .orElse("");	
		
	}

	public static Graph buildGraphFromEdgeList(List<String> edgesAsStr) throws IllegalArgumentException {
		
		Graph graph = new Graph();		
		edgesAsStr.forEach( edge-> {						
			if(!edge.matches("[A-Z]{2}[0-9]{1,9}")) {
				throw new IllegalArgumentException("invalid format for edge input. e.g AB5 or CD2000. Please confirm to the regex [A-Z]{2}[0-9]{1,9}");
			}			
			String source = edge.charAt(0)+"";
			String destination = edge.charAt(1)+"";			
			int distance = Integer.parseInt(edge.substring(2, edge.length()));			
			graph.addEdge(source, destination, distance);			
		});
		return graph;	        
	}

	

}
