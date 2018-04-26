package thoughtworks.trains.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Vertex {


	private String id;
	private String name;

	private List<Edge> outgoingEdges = new ArrayList<>();
	private List<Edge> incomingEdges = new ArrayList<>();

	public Vertex(String name) {
		this(UUID.randomUUID().toString(), name);
	}

	public Vertex(String id, String name) {
		this.id = id;
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public List<Edge> getOutgoingEdges() {
		return outgoingEdges;
	}

	public List<Edge> getIncomingEdges() {
		return incomingEdges;
	}

	public void addOutGoingEdge(Edge edge) {
		if(!outgoingEdges.contains(edge))
			outgoingEdges.add(edge);		
	}

	public void addIncomingEdge(Edge edge) {
		if(!incomingEdges.contains(edge))
			incomingEdges.add(edge);
	}

	 
	@Override
	public String toString() {
		return name;

	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Vertex other = (Vertex) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}


}