package thoughtworks.trains.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;


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
 
	
	
	public Optional<Path> getPath(String pathStr) {
		if(pathStr == null ) return Optional.empty();
		Path path = new Path();
		String[] splits = pathStr.split("-");
		for (int i = 0; i < splits.length - 1; i++) {
			final String source = splits[i];
			final String destination = splits[i + 1];			
			Optional<Edge> edge = findEdge(source, destination);
			if (edge.isPresent())
				path.addedge(edge.get());	
			else {				
				return Optional.empty();
			}				
		}		
		return Optional.of(path);
	}

	public interface PathEndEventListener {		
		public void onPathEnd(String path);
	}	

	private void findPathsWithMaximumStops(Vertex source, String pathStr, int maximumStops, PathEndEventListener ls) {
		Optional<Path> path = getPath(pathStr);		
		if(path.isPresent() && path.get().getEdges().size() == maximumStops) {
			return;
		}
		
		if (source.getOutgoingEdges().isEmpty()) {
			pathStr = pathStr + "-" + source.getName();
			ls.onPathEnd(pathStr);
		} else {
			String newPath;
			if (pathStr == null || pathStr.isEmpty()) {
				newPath = source.getName();
			} else {
				newPath = pathStr + "-" + source.getName();
			}
			source.getOutgoingEdges().stream().sorted((edge1, edge2) -> edge1.getWeight() - edge2.getWeight())
					.forEach(edge -> {
						ls.onPathEnd(newPath);
						findPathsWithMaximumStops(edge.getDestination(), newPath, maximumStops, ls);
					});
		}
	}
	
	private void findPathsFromSourceWithMaximumDistance(Vertex source, String pathStr, int maximumDistance,
			PathEndEventListener ls) {
		Optional<Path> path = getPath(pathStr);	
		if(path.isPresent() && path.get().getDistance() >= maximumDistance) {
			ls.onPathEnd(pathStr);			
			return;
		}
		
		if (source.getOutgoingEdges().isEmpty()) {
			pathStr = pathStr + "-" + source.getName();
			ls.onPathEnd(pathStr);
		} else {
			String newPath;
			if (pathStr == null || pathStr.isEmpty()) {
				newPath = source.getName();
			} else {
				newPath = pathStr + "-" + source.getName();
			}
			source.getOutgoingEdges().stream().sorted((o1, o2) -> o1.getWeight() - o2.getWeight()).forEach(edge -> {
				ls.onPathEnd(newPath);
				findPathsFromSourceWithMaximumDistance(edge.getDestination(), newPath, maximumDistance, ls);
			});
		}
	}
	

	public Stream<Path> getPathsWithMaximumStops(String from, String to, int maximumStops) {
		Vertex vertexFrom = getVertexWithName(from);
		Vertex vertexTo = getVertexWithName(to);
		if (vertexFrom != null && vertexTo != null) {
			Map<String, Path> paths = new HashMap<>();
			findPathsWithMaximumStops(vertexFrom, null, maximumStops, new PathEndEventListener() {

				@Override
				public void onPathEnd(String pathStr) {
					if (pathStr.endsWith(to)) {
						getPath(pathStr).ifPresent(path -> {
							if (path.getDistance() != 0)
								paths.put(pathStr, path);	
						});					
					}

				}
			});
			return paths.values().stream();
		} else
			throw new IllegalArgumentException("source and destination vertex no found in graph.");
	}
	
	public Stream<Path> getPathsWithExactStops(String from, String to, int stops) {
		Vertex vertexFrom = getVertexWithName(from);
		Vertex vertexTo = getVertexWithName(to);
		if (vertexFrom != null && vertexTo != null) {
			Map<String, Path> paths = new HashMap<>();
			findPathsWithMaximumStops(vertexFrom, null, stops, new PathEndEventListener() {
				@Override
				public void onPathEnd(String pathStr) {
					if (pathStr.endsWith(to)) {						
						getPath(pathStr).ifPresent(path -> {
							if (path.getDistance() != 0 && path.getEdges().size() == stops)
								paths.put(pathStr, path);	
						});					
						
					}
				}
			});
			return paths.values().stream();
		} else
			throw new IllegalArgumentException("source and destination vertex no found in graph.");
	}
	
	public Stream<Path> getPathsWithMaxDistance(String from, String to, int maxDistance) {
		Vertex vertexFrom = getVertexWithName(from);
		Vertex vertexTo = getVertexWithName(to);
		if (vertexFrom != null && vertexTo != null) {
			Map<String, Path> paths = new HashMap<>();
			findPathsFromSourceWithMaximumDistance(vertexFrom, null, maxDistance, new PathEndEventListener() {
				@Override
				public void onPathEnd(String pathStr) {
					if (pathStr.endsWith(to)) {
						getPath(pathStr).ifPresent(path -> {
							if (path.getDistance() != 0 && path.getDistance() < maxDistance)
								paths.put(pathStr, path);
						});					
					}
				}
			});
			return paths.values().stream();
		} else
			throw new IllegalArgumentException("source and destination vertex no found in graph.");

	}
}
