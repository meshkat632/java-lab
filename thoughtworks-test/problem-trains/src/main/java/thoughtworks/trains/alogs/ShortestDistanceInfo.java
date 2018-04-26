package thoughtworks.trains.alogs;

import thoughtworks.trains.domain.Vertex;

public class ShortestDistanceInfo {
	
	private Integer shortestPathFromStar;
	private Vertex previousVertex;
	
	public ShortestDistanceInfo(Integer shortestPathFromStar, Vertex previousVertex) {
		super();
		this.shortestPathFromStar = shortestPathFromStar;
		this.previousVertex = previousVertex;
	}
	
	
	
	public Integer getShortestPathFromStar() {
		return shortestPathFromStar;
	}
	public void setShortestPathFromStar(Integer shortestPathFromStar) {
		this.shortestPathFromStar = shortestPathFromStar;
	}
	public Vertex getPreviousVertex() {
		return previousVertex;
	}
	public void setPreviousVertex(Vertex previousVertex) {
		this.previousVertex = previousVertex;
	}
	
	@Override
	public String toString() {
		return shortestPathFromStar + ":" + previousVertex;
	}

    

}
