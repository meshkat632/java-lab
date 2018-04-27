package thoughtworks.trains;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.junit.Test;

import thoughtworks.trains.domain.Graph;
import thoughtworks.trains.domain.NoSuchRouteException;

public class TestGraph {
	
	
	
	@Test
	public void test_graphToString() throws IOException {
		
		List<String> edges = loadData("data/input.txt");
		Graph graph  = Graph.buildGraphFromEdgeList(edges);						
		System.out.println("["+graph.toString()+"]");
		assertEquals("should produce the same graph", "AB5, BC4, CD8, DC8, DE6, AD5, CE2, EB3, AE7", graph.toString());													   

	}
	 
	@Test
	public void test_getShortestPath() {
		Graph graph = Graph.buildGraphFromEdgeList(Arrays.asList("AB6", "AD1", "DB2", "DE1", "BE2", "BC5", "EC5"));		
		assertEquals(7, graph.getShortestPath("A", "C"));
	}	
	
	@Test (expected = IllegalArgumentException.class)
	public void test_getShortestPath_noSuchRoute() {
		Graph graph = Graph.buildGraphFromEdgeList(Arrays.asList("AB6", "AD1", "DB2", "DE1", "BE2", "BC5", "EC5"));		
		graph.getShortestPath("A", "G");
	}	
	
	@Test (expected = NoSuchRouteException.class)
	public void test_no_shortestpath() {
		Graph graph = Graph.buildGraphFromString("AB6, CB6");		
		graph.getShortestPath("A", "C");
	}
 
	@Test
	public void test_pathToSameNode() {
		Graph graph = Graph
				.buildGraphFromEdgeList(Arrays.asList("AB5", "BC4", "CD8", "DC8", "DE6", "AD5", "CE2", "EB3", "AE7"));
		assertEquals(9, graph.getShortestPath("B", "B"));
	}
	
	@Test
	public void test_shortestPathAC() {

		Graph graph = Graph
				.buildGraphFromEdgeList(Arrays.asList("AB5", "BC4", "CD8", "DC8", "DE6", "AD5", "CE2", "EB3", "AE7"));
		assertEquals(9, graph.getShortestPath("A", "C"));
	}
	
	@Test
	public void test_single_edge_graph() {
		Graph graph = Graph.buildGraphFromEdgeList(Arrays.asList("AB6"));		
		assertEquals(6, graph.getShortestPath("A", "B"));
	}
	
	@Test
	public void test_getDistance() {

		Graph graph = Graph
				.buildGraphFromEdgeList(Arrays.asList("AB5", "BC4", "CD8", "DC8", "DE6", "AD5", "CE2", "EB3", "AE7"));
		graph.getPath("A-B-C").ifPresent(path ->{
			assertEquals(9, path.getDistance());
		});
		
		graph.getPath("A-D").ifPresent(path ->{
			assertEquals(5, path.getDistance());
		});
		graph.getPath("A-D-C").ifPresent(path ->{
			assertEquals(13, path.getDistance());
		});
		
		graph.getPath("A-E-B-C-D").ifPresent(path ->{
			assertEquals(22, path.getDistance());
		});
		
		if(!graph.getPath("A-E-D").isPresent()) {
			System.out.println("Output #5: NO SUCH ROUTE");
		}
		assertEquals(false, graph.getPath("A-E-D").isPresent());		 

	}

	@Test
	public void test_getPathsWithMaximumStops() {

		Graph graph = Graph
				.buildGraphFromEdgeList(Arrays.asList("AB5", "BC4", "CD8", "DC8", "DE6", "AD5", "CE2", "EB3", "AE7"));
		assertEquals(2, graph.getPathsWithMaximumStops("C", "C", 3).count());

	}

	@Test
	public void test_getPathsWithExactStops() {

		Graph graph = Graph
				.buildGraphFromEdgeList(Arrays.asList("AB5", "BC4", "CD8", "DC8", "DE6", "AD5", "CE2", "EB3", "AE7"));
		assertEquals(3, graph.getPathsWithExactStops("A", "C", 4).count());
	}

	@Test
	public void test_getPathsWithMaxDistance() {

		Graph graph = Graph
				.buildGraphFromEdgeList(Arrays.asList("AB5", "BC4", "CD8", "DC8", "DE6", "AD5", "CE2", "EB3", "AE7"));
		assertEquals(7, graph.getPathsWithMaxDistance("C", "C", 30).count());
	}
	
	
	
	private static List<String> loadData(String filePath) throws IOException {
		List<String> ret = new ArrayList<>();
		try (Stream<String> stream = Files.lines(Paths.get(filePath))) {
			stream.forEach(line -> {
				Arrays.stream(line.split(",")).forEach(path -> {
					ret.add(path.trim());
				});
			});

		} catch (IOException e) {
			e.printStackTrace();
		}
		return ret;
	}

}

