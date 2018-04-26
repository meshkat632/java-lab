package thoughtworks.trains;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Test;

import thoughtworks.trains.domain.Graph;

public class TestGraph {
	
	@Test
	public void buildGraphFromEdgeList() {

		Graph graph = Graph
				.buildGraphFromEdgeList(Arrays.asList("AB5", "BC4", "CD8", "DC8", "DE6", "AD5", "CE2", "EB3", "AE7"));				
		assertEquals("should produce the same graph", "AB5 BC4 CD8 DC8 DE6 AD5 CE2 EB3 AE7", graph.toString());

	}

}
