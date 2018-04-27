package thoughtworks.trains.alogs;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import thoughtworks.trains.domain.Edge;
import thoughtworks.trains.domain.Graph;
import thoughtworks.trains.domain.NoSuchRouteException;
import thoughtworks.trains.domain.Path;
import thoughtworks.trains.domain.Vertex;

public class ShortestPathFinder {

	private Set<Vertex> unvisitedNodes;
	private Map<Vertex, ShortestDistanceInfo> distanceFromStart;
	private Vertex startVertex;
	private Graph graph;

	public ShortestPathFinder(Graph graph, Vertex sourceVertexWithName) {
		this.graph = graph;
		this.distanceFromStart = new HashMap<>();
		this.unvisitedNodes = new HashSet<>();
		this.startVertex = sourceVertexWithName;
	}

	/*
	 * Dijkstra's algorithm
	 * https://en.wikipedia.org/wiki/Dijkstra%27s_algorithm	 * 
	 */
	public void computeShortestPathToAll() {		
		this.distanceFromStart = new HashMap<>();
		this.unvisitedNodes = new HashSet<>();
		this.graph.getVertices().stream().forEach(vertex -> {
			unvisitedNodes.add(vertex);
			distanceFromStart.put(vertex, new ShortestDistanceInfo(Integer.MAX_VALUE, null));
		});

		distanceFromStart.put(startVertex, new ShortestDistanceInfo(0, null));

		while (!unvisitedNodes.isEmpty()) {
			debug("distanceFromStart:" + distanceFromStart);
			debug("unvisitedNodes:" + unvisitedNodes);
			Optional<Vertex> minimumNode = getMinimumUnvisitedNode();
			if (minimumNode.isPresent()) {
				Vertex node = minimumNode.get();
				unvisitedNodes.remove(node);
				debug("getOutgoingEdges of " + node + " : " + node.getOutgoingEdges());
				node.getOutgoingEdges().stream().map(edge -> edge.getDestination())
						.filter(neighbour -> unvisitedNodes.contains(neighbour)).forEach(unvisitedNeighbour -> {
							debug("unvisitedNeighbour:" + unvisitedNeighbour + " for node:" + node);
							int oldValue = distanceFromStart.get(unvisitedNeighbour).getShortestPathFromStar();
							int alt = length(node, unvisitedNeighbour)
									+ distanceFromStart.get(node).getShortestPathFromStar();
							debug("oldValue:" + oldValue + " alt:" + alt);
							if (alt < oldValue) {
								distanceFromStart.put(unvisitedNeighbour, new ShortestDistanceInfo(alt, node));
								debug("distanceFromStart:" + distanceFromStart);
							}

						});
			}

		}	

	}

	private int length(Vertex from, Vertex to) {
		return graph.getEdges().stream()
				.filter(edge -> edge.getSource().equals(from) && edge.getDestination().equals(to))
				.map(edge -> edge.getWeight()).findFirst().orElse(Integer.MAX_VALUE);
	}

	private Optional<Vertex> getMinimumUnvisitedNode() {

		return distanceFromStart.keySet().stream().filter(vertex -> unvisitedNodes.contains(vertex))
				.sorted((node1, node2) -> distanceFromStart.get(node1).getShortestPathFromStar()
						- distanceFromStart.get(node2).getShortestPathFromStar())
				.findFirst();
	}
	
	private void buildPath(Vertex to, Path path) {		
		Vertex previousVertex = distanceFromStart.get(to).getPreviousVertex();
		if (previousVertex == null) {
			if(path.getEdges().isEmpty()) {
				throw new NoSuchRouteException("NO SUCH ROUTE "+to);				
			}else 
				Collections.reverse(path.getEdges());
		} else {
			Optional<Edge> edge = graph.findEdge(previousVertex.getName(), to.getName());			
			if (edge.isPresent()) 
				path.addedge(edge.get());
			buildPath(previousVertex, path);
		}
	}

	private void debug(String message) {

		// System.out.println("DEBUG:"+message);

	}

	public Path getShortestPathTo(Vertex vertexTo) {		
		/*
		 * Dijkstra's algorithm assumes distance to the same source is zero. 
		 * When we ask to find path which starts from source and end to the source we need rewrite the graph. 
		 * introduce a new node with (different name other than the source) and add to graph. Add an out-going edge to 'vertexTo' with weight 0.
		 * Add same incoming edges to as 'vertexTo'. Please note that computed path distance is correct. But not the path it-self, as we have introduced an
		 * artificial node to the original graph. 
		 */
		if (startVertex.equals(vertexTo)) {
			debug("rewrite the graph"+ graph.toString());
			Graph rewrittenGraph = graph.deepCopy();
			final String newVertexName = UUID.randomUUID().toString();			
			vertexTo.getIncomingEdges().forEach(edge -> {
				rewrittenGraph.addEdge(edge.getSource().getName(), newVertexName, edge.getWeight());
			});
			rewrittenGraph.addEdge(newVertexName, vertexTo.getName(), 0);
			ShortestPathFinder shortestPathFinder = new ShortestPathFinder(rewrittenGraph, rewrittenGraph.getVertexWithName(startVertex.getName()));
			shortestPathFinder.computeShortestPathToAll();			
			return shortestPathFinder.getShortestPathTo(rewrittenGraph.getVertexWithName(newVertexName));			
		}else {
			Path path = new Path();
			buildPath(vertexTo, path);
			return path;	
		}	
			 
	}	 

	 


}
