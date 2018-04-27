# Design
To find the shortest path we have implemented the 'Dijkstra's algorithm'. 
The detail of the algorithm can be found 'https://en.wikipedia.org/wiki/Dijkstra%27s_algorithm'. 
We have not introduced any kind of optimization. The basic implementation can be found in 'ShortestPathFinder.class'.
For other graph traversal cases, we just use few recursive functions with appropriate termination logic. We have not introduced any kind of optimization(e.g caching). 

# Assumptions
The name of a vertex can be any character from A-Z. For example, an edge must be confirmed with the [A-Z]{2}[0-9]{1,9} regx. 
Each vertex is identified uniquely by its name. And Each edge is uniquely by its source and destination name. So our graph can have only one edge from one source to its direct destination. And edge, where the source and destination are same, is not permitted ( were not tested).  

Regarding finding the shortest path to the same node:
Dijkstra's algorithm assumes the distance to the same source is zero. When we ask to find the path which starts from source and end with the same vertex we need to rewrite the graph. 
We introduce a new node with (a different name other than the source) and add to the graph. Add an out-going edge to 'vertexTo' with weight 0.
Add same incoming edges to as 'vertexTo'. Please note that computed path distance is correct. But not the path itself, as we have introduced an artificial node to the original graph. 


# How to run the application
The input.text file is placed on 'data' folder (same directory where src folder is). 

checkout the code from github with command
'git clone https://github.com/meshkat632/java-lab.git'

cd into thoughtworks-test/problem-trains folder and run the ./gradlew test command to run the test 
'./gradlew test'

To run the Main class to see the output printed for the example input.  

If we want to open the project in eclipse, we can run the following command
'./gradlew eclipse'

To open the project in eclipse and run all the unit tests.