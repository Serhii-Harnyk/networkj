package utils;

import topology.Topology;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;

public class GraphStreamUtils {

    public static Graph fromTopology(Topology tp, String name) {
        Graph graph = new SingleGraph(name);
        graph.setStrict(false);
        tp.keySet().forEach(
                a -> graph.addNode(a.toString())
        );
        tp.forEach(
                (k,v) -> v.forEach(
                node -> graph.addEdge(k.toString() + node.toString(), k.toString(), node.toString())
        ));

        return graph;
    }

    public static void drawTopology(Topology tp) {
        System.setProperty("org.graphstream.ui", "swing");

        Graph graph = fromTopology(tp, tp.toString());

        for (Node node : graph) {
            node.setAttribute("ui.label", node.getId());
        }

        graph.display();
    }
}
