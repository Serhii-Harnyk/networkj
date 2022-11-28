package topology;

import topology.Topology;

import java.util.List;
import java.util.Set;
import java.util.SplittableRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TopologyGenerator {

    final SplittableRandom random = new SplittableRandom();
    private int m0 = 1;
    private int m1 = 2; // excluded

    public TopologyGenerator(int m0, int m1) {
        this.m0 = m0;
        this.m1 = m1;
    }

    public TopologyGenerator() {
    }

    /**
     * random growth for each new node add M=rand(m0,m1) edges to random nodes
     * each node has a same probability to be chosen
     */
    public Topology genErdosRenyl(int nodesCount, int edgesCount) {

        Topology graph = new Topology();
        for (int i = 0; i < nodesCount; i++) {
            graph.addNode(i);
        }
        for (int i = 0; i < edgesCount; i++) {
            int v1 = 0;
            int v2 = 0;
            while (v1 == v2 || graph.get(v1).contains(v2)) { // generate until definitely new edge would be added
                v1 = random.nextInt(0, nodesCount);
                v2 = random.nextInt(0, nodesCount);

            }
            graph.addEdge(v1, v2);
        }

        return graph;
    }

    public Topology genErdosRenyl(int nodesCount, float probability) {
        Topology graph = new Topology();
        for (int i = 0; i < nodesCount; i++) {
            graph.addNode(i);

            for (int j = 0; j < nodesCount; j++) {
                if (i != j && random.nextFloat(0, 1) <= probability) {
                    graph.addEdge(i, j);
                }
            }
        }

        return graph;
    }

    /**
     * @param nodesCount
     * @param k          - size of wired neighbourhood
     * @param prob       - rewiring probability
     * @return
     */
    public Topology genWattsStrogatz(int nodesCount, int k, float prob) {
        if (k < 0)
            throw new RuntimeException("K should be >= 0");

        Topology tp = new Topology();

        // create ring
        for (int i = 1; i < nodesCount; i++) {
            tp.addEdge(i - 1, i);
        }
        tp.addEdge(nodesCount - 1, 0);

        // connect each node with k*2 nearest nodes
        for (int i = 0; i < nodesCount; i++) {
            for (int j = i - k; j <= i + k; j++) {
                if (j == i) continue;
                int neigh = (j >= 0) ? j : (nodesCount - j) % nodesCount;
                if (neigh >= nodesCount)
                    neigh = neigh % nodesCount;
                if (neigh == i) continue;
                tp.addEdge(i, neigh);
            }
        }

        // rewiring
        for (int i = 0; i < nodesCount; i++) {
            List<Integer> neigs = tp.get(i).stream().toList();
            for(Integer neigh : neigs) {
                if (random.nextFloat(1f) < prob) {
                    tp.removeEdge(neigh, i);
                    int next = random.nextInt(0, nodesCount);
                    while (next == i)
                        next = random.nextInt(0, nodesCount);
                    tp.addEdge(i, next);
                }
            }

        }

        return tp;
    }

    /**
     * random growth for each new node add M=mEdges edges to random existing nodes
     * but probability of node to be chosen as neighbour is proportional to each node degree
     */
    public Topology genBarabasiAlbert(int nodesCount, int mEdges, int startGraphSize) {
        Topology graph = genCompleteGraph(Math.min(nodesCount, startGraphSize));
        int initSize = graph.size();
        for (int i = initSize; i < nodesCount; i++) {

            long sum = graph.values().stream().map(Set::size).map(Integer::longValue).reduce(0L, Long::sum);
            int edges = mEdges;
            while (edges > 0) {
                edges--;
                long probe = random.nextLong(0, sum);
                int walkSum = 0;
                for (int j = 0; j < graph.size(); j++) {
                    walkSum += graph.get(j).size();
                    if (walkSum >= probe) {
                        graph.addEdge(i, j);
                        break;
                    }
                }
            }
        }

        return graph;
    }

    /**
     * Generate and return graph where every node connected to every another node
     * @param startGraphSize - number of nodes
     */
    public Topology genCompleteGraph(int startGraphSize) {
        Topology graph = new Topology();
        for (int i = 0; i < startGraphSize; i++) {
            graph.put(i, IntStream.range(0, startGraphSize).boxed().collect(Collectors.toSet()));
            graph.get(i).remove(i);
        }

        return graph;
    }

    //**************** Experimental

    /**
     * Similar to Barabasi-Albert but edges are added randomly without weights
     * @param nodesCount
     * @param startGraphSize
     */
    public Topology genCumulative(int nodesCount, int startGraphSize) {
        Topology graph = genCompleteGraph(Math.min(nodesCount, startGraphSize));
        // for each new node add M=rand(m0,m1) edges to random nodes
        int initSize = graph.size();
        for (int i = initSize; i < nodesCount; i++) {
            int newEdges = random.nextInt(m0, m1);
            while (newEdges > 0) {
                newEdges--;
                graph.addEdge(i, random.nextInt(0, i));
            }
        }

        return graph;
    }

    /**
     * random growth for each new node add M=rand(m0,m1) edges to random nodes
     * each node has a same probability to be chosen
     */
    public Topology genRandomByNode(int nodesCount) {
        Topology graph = new Topology();
        for (int i = 0; i < nodesCount; i++) {
            graph.addNode(i);
            int newEdges = random.nextInt(m0, m1);
            while (newEdges > 0) {
                newEdges--;
                int v1 = random.nextInt(0, nodesCount);
                if (v1 != i)
                    graph.addEdge(i, v1);
            }
        }

        return graph;
    }


    /**
     * random growth for each new node add M=rand(m0,m1) edges to random existing nodes
     * but probability of node to be chosen as neighbour is proportional to square of each node degree
     */
    public Topology generatePowerLawSquareTopology(int nodesCount, int startGraphSize) {
        Topology graph = genCompleteGraph(Math.min(nodesCount, startGraphSize));
        int initSize = graph.size();
        for (int i = initSize; i < nodesCount; i++) {
            int newEdges = random.nextInt(m0, m1);
            long sum = graph.values().stream().map(a -> (long) a.size() * a.size()).reduce(0L, Long::sum);

            while (newEdges >= 0) {
                newEdges--;
                long probe = random.nextLong(0, sum);
                int walkSum = 0;
                for (int j = 0; j < graph.size(); j++) {
                    Set<Integer> node = graph.get(j);
                    walkSum += node.size() * node.size();
                    if (walkSum >= probe) {
                        graph.addEdge(i, j);
                        break;
                    }
                }
            }
        }

        return graph;
    }



    /**
     *
     */
    public Topology generateRandomGraph(int nodesCount) {
        Topology graph = new Topology();
        for (int i = 0; i < nodesCount; i++) {
            int newEdges = random.nextInt(m0, m1);
            while (newEdges >= 0) {
                newEdges--;
                graph.addEdge(i, random.nextInt(0, nodesCount));
            }
        }

        return graph;
    }

    /**
     * graph where degree of nodes are uniformly distributed
     * TODO check it
     */
    public Topology generateUniformGraph(int nodesCount) {
        Topology graph = new Topology();
        for (int i = 0; i < nodesCount; i++) {
            //int newEdges = random.nextInt(m0, m1);
            int newEdges = 5;
            while (newEdges >= 0) {
                newEdges--;
                int a = random.nextInt(0, nodesCount);
                int b = random.nextInt(0, nodesCount);
                while (a == b) {
                    a = random.nextInt(0, nodesCount);
                    b = random.nextInt(0, nodesCount);
                }

                graph.addEdge(a, b);
            }
        }


        return graph;
    } //todo generate graph where degree of nodes are uniformly distributed and each node has minimum N nodes

}
