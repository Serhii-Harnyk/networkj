package graphs;

import utils.RandomUtils;
import topology.Topology;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class VoteBreakGraph extends VoteGraph {

    RandomUtils ru = new RandomUtils();

    Set<Set<Integer>> diffEdges = new HashSet<>();

    public VoteBreakGraph(Topology topology) {
        super(topology);
        calculateDiffEdges();
    }

    public VoteBreakGraph(Topology topology, int[] values) {
        super(topology, values);
        calculateDiffEdges();
    }

    private void calculateDiffEdges() {
        topology.forEach((node, neigs) -> {
            neigs.forEach(neigh -> {
                if(values[node] != values[neigh]) {
                    addDiffEdge(node, neigh);
                }
            });
        });
    }

    private void addDiffEdge(Integer v1, Integer v2) {
        Set<Integer> edge = new HashSet<>();
        edge.add(v1);
        edge.add(v2);
        diffEdges.add(edge);
    }

    public Set<Integer> getRandomEdge() {
        return ru.getRandomValueOfSet(diffEdges);
    }

    public void removeEdge(Set<Integer> edge) {
        List<Integer> tmp = edge.stream().toList(); // todo optimize this
        Integer v1 = tmp.get(0);
        Integer v2 = tmp.get(1);

        topology.get(v1).remove(v2);
        topology.get(v2).remove(v1);
        diffEdges.remove(edge);
    }

    public void vote(Set<Integer> edge) {
        List<Integer> tmp = edge.stream().toList(); // todo optimize this

        Integer listener = tmp.get(0);
        Integer speaker = tmp.get(1);
        if(ru.percent(50)) {
            listener = tmp.get(1);
            speaker = tmp.get(0);
        }

        values[listener] = values[speaker];

        diffEdges.remove(edge);

        Set<Integer> neigs = topology.get(listener);

        Integer finalListener = listener;
        neigs.forEach(neigh -> {
            if(values[neigh] != values[finalListener]) {
                addDiffEdge(finalListener, neigh);
            } else {
                Set<Integer> obsoleteEdge = new HashSet<>();
                obsoleteEdge.add(finalListener);
                obsoleteEdge.add(neigh);
                diffEdges.remove(obsoleteEdge);
            }
        });
    }

    public Set<Set<Integer>> getDiffEdges() {
        return diffEdges;
    }
}
