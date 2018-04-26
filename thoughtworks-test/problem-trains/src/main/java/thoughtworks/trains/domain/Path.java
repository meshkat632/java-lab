package thoughtworks.trains.domain;

import java.util.ArrayList;
import java.util.List;

public class Path {

	private List<Edge> edges = new ArrayList<>();
	private int distance = 0;

	public void addedge(Edge edge) {
		edges.add(edge);
		distance = distance + edge.getWeight();
	}

	public int getDistance() {
		return distance;
	}

	public List<Edge> getEdges() {
		return edges;
	}

	@Override
	public String toString() {
		return "Path [edges=" + edges + ", distance=" + distance + "]";
	}

}
