import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;
import org.graphstream.algorithm.Dijkstra;

import java.util.*;

public class CarnivalRouteSystem {
    public static void main(String[] args) {
        System.setProperty("org.graphstream.ui", "swing");

        // Create graph
        Graph cityGraph = new SingleGraph("Carnival Routes");

        // Tourist locations
        String[] locations = {
                "Carnival Plaza",
                "Nariño Square",
                "Panamerican Avenue",
                "Carnival Street",
                "Cultural Museum",
                "Hummingbird Monument",
                "Sun Square",
                "Children Park"
        };

        // Add nodes
        for (String loc : locations) {
            cityGraph.addNode(loc).setAttribute("ui.label", loc);
        }

        // Connections (start, end, weight)
        Object[][] connections = {
                {"Carnival Plaza", "Nariño Square", 3},
                {"Nariño Square", "Panamerican Avenue", 5},
                {"Panamerican Avenue", "Carnival Street", 2},
                {"Carnival Street", "Cultural Museum", 4},
                {"Cultural Museum", "Hummingbird Monument", 7},
                {"Hummingbird Monument", "Sun Square", 3},
                {"Sun Square", "Children Park", 4},
                {"Children Park", "Carnival Plaza", 6},
                {"Nariño Square", "Carnival Street", 4},
                {"Carnival Plaza", "Children Park", 9}
        };

        // Add weighted edges
        int id = 0;
        for (Object[] c : connections) {
            cityGraph.addEdge("E" + id, (String) c[0], (String) c[1])
                    .setAttribute("weight", (int) c[2]);
            id++;
        }

        // Show available locations
        System.out.println("Welcome to the Carnival Route System (Pasto, Colombia)\n");
        System.out.println("Available locations:");
        for (int i = 0; i < locations.length; i++) {
            System.out.println((i + 1) + ". " + locations[i]);
        }

        Scanner sc = new Scanner(System.in);

        // User inputs
        System.out.print("\nEnter start location number: ");
        int startId = sc.nextInt() - 1;

        System.out.print("Enter destination location number: ");
        int endId = sc.nextInt() - 1;

        String startPoint = locations[startId];
        String endPoint = locations[endId];

        // Run Dijkstra
        Dijkstra dijkstra = new Dijkstra(
                Dijkstra.Element.EDGE,
                "distance",
                "weight"
        );

        dijkstra.init(cityGraph);
        dijkstra.setSource(cityGraph.getNode(startPoint));
        dijkstra.compute();

        double distance = dijkstra.getPathLength(cityGraph.getNode(endPoint));
        List<Node> path = dijkstra.getPath(cityGraph.getNode(endPoint)).getNodePath();

        // Print shortest path
        System.out.println("\nShortest route from " + startPoint + " to " + endPoint + ":");
        for (int i2 = 0; i2 < path.size(); i2++) {
            System.out.print(path.get(i2).getId());
            if (i2 < path.size() - 1) System.out.print(" -> ");
        }
        System.out.println("\nEstimated distance: " + distance + " units");

        // Show graph
        cityGraph.setAttribute("ui.stylesheet", "node { fill-color: lightgreen; size: 25px; text-size: 15;} edge { text-size: 14; }");

        for (Edge edge : cityGraph.getEdgeSet()) {
            edge.setAttribute("ui.label", edge.getAttribute("weight"));
        }

        cityGraph.display();
    }
}
