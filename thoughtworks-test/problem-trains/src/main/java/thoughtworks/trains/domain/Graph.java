package thoughtworks.trains.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import thoughtworks.trains.alogs.ShortestPathFinder;


public class Graph {	

	

	private List<Vertex> vertices;
	private List<Edge> edges;	

	private Graph() {
		this.vertices = new ArrayList<>();
		this.edges = new ArrayList<>();		
	}
	
	public List<Vertex> getVertices() {	
		return vertices;		
	}	

	public List<Edge> getEdges() {
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
	
	
	public Optional<Edge> findEdge(String source, String destination) {
		return edges.stream().filter(edge -> {
			return edge.getSource().getName().equals(source) && edge.getDestination().getName().equals(destination);
		}).findFirst();
		
	}
	
	public Vertex getVertexWithName(String name) {
		return vertices.stream().filter(vertex -> vertex.getName().equals(name)).findAny().orElse(null);	
	}
	
	public void addEdge(String source, String destination, int distance) {
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
               .reduce((edge1, edge2) -> edge1+", "+edge2)
               .orElse("");		
	}
	
	public static Graph buildGraphFromString(String graphAsStr) throws IllegalArgumentException {		
		Graph graph = new Graph();		
		Arrays.stream(graphAsStr.split(", ")).forEach( edge-> {						
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
	
	public Graph deepCopy() {
		return Graph.buildGraphFromString(this.toString());		
	}
	
	
	public int getShortestPath(String from, String to) {		
		
		Vertex vertexFrom = getVertexWithName(from);
		Vertex vertexTo = getVertexWithName(to);
		if (vertexFrom != null && vertexTo != null) {			
			ShortestPathFinder shortestPathFinder = new ShortestPathFinder(this, vertexFrom);
			shortestPathFinder.computeShortestPathToAll();
			return shortestPathFinder.getShortestPathTo(this.getVertexWithName(to)).getDistance();				

		} else
			throw new IllegalArgumentException("source and destination vertex no found in graph.");

	}

	



	

}
