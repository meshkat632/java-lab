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

public class TestGraph {
	
	
	
	@Test
	public void buildGraphFromEdgeList() throws IOException {
		
		List<String> edges = loadData("data/input.txt");
		Graph graph  = Graph.buildGraphFromEdgeList(edges);						
		System.out.println("["+graph.toString()+"]");
		assertEquals("should produce the same graph", "AB5, BC4, CD8, DC8, DE6, AD5, CE2, EB3, AE7", graph.toString());													   

	}
	 
	
	@Test
	public void test7() {
		Graph graph = Graph.buildGraphFromEdgeList(Arrays.asList("AB6", "AD1", "DB2", "DE1", "BE2", "BC5", "EC5"));		
		assertEquals(7, graph.getShortestPath("A", "C"));
	}	
	

	@Test
	public void test8() {
		Graph graph = Graph.buildGraphFromEdgeList(Arrays.asList("AB6", "AD1", "DB2", "DE1", "BE2", "BC5", "EC5"));
		assertEquals(7, graph.getShortestPath("A", "C"));
	}

	@Test
	public void test9() {

		Graph graph = Graph
				.buildGraphFromEdgeList(Arrays.asList("AB5", "BC4", "CD8", "DC8", "DE6", "AD5", "CE2", "EB3", "AE7"));
		assertEquals(9, graph.getShortestPath("A", "C"));

	}

	@Test
	public void test10() {

		Graph graph = Graph
				.buildGraphFromEdgeList(Arrays.asList("AB5", "BC4", "CD8", "DC8", "DE6", "AD5", "CE2", "EB3", "AE7"));
		assertEquals(9, graph.getShortestPath("B", "B"));

	}
	
	
	@Test
	public void test11() {
		Graph graph = Graph.buildGraphFromEdgeList(Arrays.asList("AB6"));		
		assertEquals(6, graph.getShortestPath("A", "B"));
	}
	
	@Test
	public void test0() {

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
	public void test1() {

		Graph graph = Graph
				.buildGraphFromEdgeList(Arrays.asList("AB5", "BC4", "CD8", "DC8", "DE6", "AD5", "CE2", "EB3", "AE7"));
		assertEquals(2, graph.getPathsWithMaximumStops("C", "C", 3).count());

	}

	@Test
	public void test2() {

		Graph graph = Graph
				.buildGraphFromEdgeList(Arrays.asList("AB5", "BC4", "CD8", "DC8", "DE6", "AD5", "CE2", "EB3", "AE7"));
		assertEquals(3, graph.getPathsWithExactStops("A", "C", 4).count());
	}

	@Test
	public void test3() {

		Graph graph = Graph
				.buildGraphFromEdgeList(Arrays.asList("AB5", "BC4", "CD8", "DC8", "DE6", "AD5", "CE2", "EB3", "AE7"));
		assertEquals(7, graph.getPathsWithMaxDistance("C", "C", 30).count());
	}
	
	
	
	public static List<String> loadData(String filePath) throws IOException {
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

