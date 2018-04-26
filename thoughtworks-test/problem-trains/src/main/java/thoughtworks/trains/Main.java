package thoughtworks.trains;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import thoughtworks.trains.domain.Graph;

public class Main {
	

	public static void main(String[] args) throws IOException {
		
		List<String> edges = loadData("data/input.txt");		
		Graph graph = Graph.buildGraphFromEdgeList(edges);		
		
		/*
		 * The distance of the route A-B-C.
		 */
		//System.out.println("Output #1: "+graph.computeDistanceOfpath("A->B->C"));
		
		/*
		 * The distance of the route A-D.
		 */
		//System.out.println("Output #2: "+computeDistance(graph, "A->D"));
		
		/*
		 * The distance of the route A-D-C.
		 */
		//System.out.println("Output #3: "+computeDistance(graph, "A->D->C"));
		
		/*
		 * The distance of the route A-E-B-C-D.
		 */
		//System.out.println("Output #4: "+computeDistance(graph, "A->E->B->C->D"));
		
		/*
		 * The distance of the route A-E-D.
		 */
		//System.out.println("Output #5: "+computeDistance(graph, "A->E->D"));
				
		/*
		 * The number of trips starting at C and ending at C with a maximum of 3 stops.  
		 * In the sample data below, there are two such trips: C-D-C (2 stops). and C-E-B-C (3 stops).
		 */
		//System.out.println("Output #6: "+graph.getPathsWithMaximumStops("C", "C", 3).count());
		
		/*
		 * The number of trips starting at A and ending at C with exactly 4 stops.  
		 * In the sample data below, there are three such trips: A to C (via B,C,D); A to C (via D,C,D); and A to C (via D,E,B).
		 */
		//System.out.println("Output #7: "+graph.getPathsWithExactStops("A", "C", 4).count());		
		
		/*
		 * The length of the shortest route (in terms of distance to travel) from A to C.
		 */
		System.out.println("Output #8: "+graph.getShortestPath("A", "C"));
		
		/*
		 * The length of the shortest route (in terms of distance to travel) from B to B.
		 */
		System.out.println("Output #9: "+graph.getShortestPath("B", "B"));
		
		/*
		 * The number of different routes from C to C with a distance of less than 30.  
		 * In the sample data, the trips are: CDC, CEBC, CEBCDC, CDCEBC, CDEBC, CEBCEBC, CEBCEBCEBC. 
		 */
		//System.out.println("Output #10: "+graph.getPathsWithMaxDistance("C", "C", 30).count());		
		
		
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
