package topology;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Topology of undirected, unweighted graph
 */
public class Topology extends HashMap<Integer, Set<Integer>> {


    public void addEdge(int node1, int node2) {
        this.computeIfAbsent(node1, a -> new HashSet<>());
        this.computeIfAbsent(node2, a -> new HashSet<>());
        this.get(node1).add(node2);
        this.get(node2).add(node1);
    }

    public void addNode(int node) {
        this.computeIfAbsent(node, a -> new HashSet<>());
    }

    public void removeEdge(int node1, int node2) {
        this.get(node1).remove(node2);
        this.get(node2).remove(node1);
    }

    public void removeNode(Integer node) {
        Set<Integer> neighs = this.get(node);
        neighs.forEach(neigh -> this.get(neigh).remove(node));
        this.remove(node);
    }

    /**
     * Merge two topologies into one
     * Node Ids of second topology would be changes
     * Resulting graph is not connected
     */
    public void merge(Topology tp) {
        int shift = this.size();
        tp.forEach((k,v) -> {
            this.put(k+shift, v.stream().map(a -> a+shift).collect(Collectors.toSet()));
        });
    }

    /**
     * The density of a network is the fraction between 0 and 1 that tells us what portion of
     * all possible edges are actually realized in the network.  For a network G made of n
     * nodes and m edges, the density ρ(G) is given by
     * @return ρ(G) = m/n(n − 1)
     *
     */
    public float networkDensity() {
        float nodes = this.size();
        int edges = edgesCount();

        return edges/(nodes*(nodes-1));
    }

    public float averageDegree() {
        float nodes = (float) this.size();
        int degreesSum = degreeSum();

        return degreesSum/nodes;
    }

    public int degreeSum() {
        return this.values().stream().map(Set::size).reduce(0, Integer::sum);
    }

    public int edgesCount() {
        return this.values().stream().map(Set::size).reduce(0, Integer::sum)/2;
    }

    public long nodesWithDegree(int degree) {
        return this.values().stream().map(Set::size)
                .filter(a -> a == degree)
                .count();
    }

    /**
     * Key - arbitrary node of connected component
     * Value - size
     * Complexity - O(N) - marked BFS
     * @return {root node, size}
     */
    public Map<Integer, Integer> connectedComponents(boolean skipSingles) {
        Map<Integer, Integer> cc = new HashMap<>();
        Set<Integer> marked = new HashSet<>();

        for(Integer startNode : this.keySet()) { // we should check each node cause graph may be not connected
            if(marked.contains(startNode))
                continue;

            Queue<Integer> path = new LinkedList<>();
            path.add(startNode);

            int size = 0;
            Integer last = null;
            while (!path.isEmpty()) {

                Integer node = path.poll();
                if(marked.contains(node))
                    continue;
                marked.add(node);
                size++;
                Set<Integer> next = new HashSet<>(this.get(node));
                next.removeAll(marked);
                path.addAll(next);
                last = node;
            }

            if(!skipSingles || size > 1)
                cc.put(last, size);
        }

        return cc;
    }

    /**
     * Return subgraph wich is largest connected component
     * If there are multiple LCCs of same size arbitrary is returned
     * Complexity - O(N) - complexity of connectedComponents() + BFS
     */
    public Topology largestConnectedComponent() {
        Map<Integer, Integer> cc = connectedComponents(false);
        if(cc.size() == 0)
            return this;
        Integer root = cc.entrySet().stream().max(Entry.comparingByValue()).get().getKey();

        Topology tp = new Topology();

        Set<Integer> marked = new HashSet<>();
        Queue<Integer> path = new LinkedList<>();
        path.add(root);

        while (!path.isEmpty()) {
            Integer node = path.poll();
            marked.add(node);

            this.get(node).forEach(next -> {
                if(!marked.contains(next)) {
                    tp.addEdge(node, next);
                    path.add(next);
                }
            });
        }

        return tp;
    }

    /**
     * Size of Largest Connected Component
     * Complexity - O(N) - marked BFS
     */
    public int sizeLLC() {
        Set<Integer> marked = new HashSet<>();
        int maxSize = 0;
        for(Integer startNode : this.keySet()) { // we should check each node cause graph may be not connected
            if(marked.contains(startNode))
                continue;

            Queue<Integer> path = new LinkedList<>();
            path.add(startNode);

            int size = 0;
            while (!path.isEmpty()) {

                Integer node = path.poll();
                if(marked.contains(node))
                    continue;
                marked.add(node);
                size++;
                Set<Integer> next = new HashSet<>(this.get(node));
                next.removeAll(marked);
                path.addAll(next);
            }

            maxSize = Math.max(maxSize, size);
        }

        return maxSize;
    }



    /**
     * @return -1 if no path, 0 if nodes are the same
     */
    public int shortestPathLength(int node1, int node2) {
        if(!this.containsKey(node1) || !this.containsKey(node2))
            throw new RuntimeException("No such node in graph");

        Set<Integer> marked = new HashSet<>();
        Queue<Integer> path = new LinkedList<>();

        path.add(node1);

        int count = 0;
        while (!path.isEmpty()) {
            Set<Integer> next = new HashSet<>();
            while (!path.isEmpty()) {
                Integer node = path.poll();
                marked.add(node);
                if (node == node2)
                    return count;
                next.addAll(this.get(node));
            }
            next.removeAll(marked);
            path.addAll(next);
            count++;
        }

        return -1;
    }

    /**
     * maximal shortest path length
     * a node can have with any other node in the connected component
     */
    public int eccentricity(int node0) {
        if(!this.containsKey(node0))
            throw new RuntimeException("No such node in graph");

        Set<Integer> marked = new HashSet<>();
        Queue<Integer> path = new LinkedList<>();

        path.add(node0);

        int count = 0;
        while (!path.isEmpty()) {
            Set<Integer> next = new HashSet<>();
            while (!path.isEmpty()) {
                Integer node = path.poll();
                marked.add(node);
                next.addAll(this.get(node));
            }
            next.removeAll(marked);
            path.addAll(next);
            count++;
        }

        return count-1;
    }

    /**
     * For each connected component:
     * 1. Take an arbitrary node
     * 2. BFS for finding farthest node
     * 3. Eccentricity of second node would be the diameter of connected component
     * Complexity - O(N) - BFS
     * @return max of diameters of connected components
     */
    public int diameter() {
        Set<Integer> marked = new HashSet<>();
        int diameter = 0;
        for(Integer startNode : this.keySet()) { // we should check each node cause graph may be not connected
            if(marked.contains(startNode))
                continue;

            Queue<Integer> path = new LinkedList<>();
            path.add(startNode);

            Integer last = -1;
            while (!path.isEmpty()) {
                Set<Integer> next = new HashSet<>();
                while (!path.isEmpty()) {
                    Integer node = path.poll();
                    last = node;
                    marked.add(node);
                    next.addAll(this.get(node));
                }
                next.removeAll(marked);
                path.addAll(next);
            }

            if(last != -1)
                diameter = Math.max(diameter, eccentricity(last));

        }

        return diameter;
    }

    /**
     * The average shortest path length is the sum of path lengths d(u,v)
     * between all pairs of nodes
     * normalized by n*(n-1) where n is the number of nodes.
     * Returns -1 if graph is not connected
     * Complexity - O(N^2) - BFS for each node
     */
    public float avgShortestPathLength() {

        if(this.size() > this.sizeLLC())
            return -1f;

        if(this.size() == 0)
            return 0;


        int sum = 0;

        for(Integer startNode : this.keySet()) {
            Set<Integer> marked = new HashSet<>();
            Queue<Integer> path = new LinkedList<>();
            path.add(startNode);

            int rank = 0;

            while (!path.isEmpty()) {
                Set<Integer> next = new HashSet<>();
                while (!path.isEmpty()) {
                    Integer node = path.poll();
                    marked.add(node);
                    next.addAll(this.get(node));
                }
                next.removeAll(marked);
                path.addAll(next);
                rank++;
                sum += rank*next.size();
            }
        }

        return (float) sum/(this.size()*(this.size()-1));
    }

    public Map<Integer, Float> betweennessCentrality() {
        return null; //todo
    }

    /**
     * Init formula 2*sum/(deg)(deg-1), where sum = sum of real triangles
     * in the node neighbourhood.
     */
    public float clusteringCoeff(int node) {
        Set<Integer> neighs = this.get(node);
        int deg = neighs.size(); // node degree
        if(deg < 2)
            return 0;

        long sum = neighs.stream()
                .mapToLong(neig -> this.get(neig).stream().filter(neighs::contains).count())
                .sum(); // each triangle is calculated twice, so there is no *2 in final formula


        return (float) sum / deg /(deg-1);
    }

    public float avgClusteringCoeff() {
        float sum = 0f;
        for(Integer node : this.keySet()) {
            sum += clusteringCoeff(node);
        }

        return sum/this.size();
    }

    /**
     * The assortativity coefficient is a Pearson correlation coefficient of some node
     * property f between pairs of connected nodes
     * @return - degree assortativity coefficient
     */
    public double degreeAssort() {
        float avgDegree = averageDegree();

        // for each edge
        //sum1 =(deg(node1)-avg)*(deg(node2)-avg)
        //sum2 = (deg(node1)-avg)^2
        //sum3 = (deg(node2)-avg)^2

        float sum1 = 0;
        float sum2 = 0;
        float sum3 = 0;
        for(int node1 : this.keySet()) {
            for(int node2 : this.get(node1)) {
                sum1 += (this.get(node1).size() - avgDegree)*(this.get(node2).size() - avgDegree);
                sum2 += Math.pow(this.get(node1).size() - avgDegree, 2);
                sum3 += Math.pow(this.get(node2).size() - avgDegree, 2);
            }
        }

        if(sum1 == 0 || sum2 == 0 || sum3 == 0)
            return 0;
        return sum1/Math.sqrt(sum2)/Math.sqrt(sum3);
    }

    public float modularCoeff(Set<Integer> comm) {
        return modularCoeff(comm, -1);
    }
    public float modularCoeff(Set<Integer> comm, float p) {
        int totalEdges = this.edgesCount();
        int innerEdges = commInnerEdgesCount(comm);
        int commNodes = comm.size();
        p = (p > 0) ? p : (float) totalEdges/(this.size()*(this.size()-1)/2);
        float expectedEdges = commNodes*(commNodes-1)/2*p;

        return (innerEdges - expectedEdges)/totalEdges;
    }

    // todo incorrect?
    public Map<Integer, MetaNode> findCommunities() {
        Topology commTopology = new Topology();
        Map<Integer, MetaNode> metaNodes = new HashMap<>();
        for(Integer node: this.keySet()) {
            commTopology.put(node, this.get(node));
            MetaNode metaNode = new MetaNode(new HashSet<>(), 0);
            metaNode.innerNodes.add(node);
            metaNodes.put(node, metaNode);
        }

        boolean repeat = true;
        while (repeat) {
            repeat = false;
            for (Entry<Integer, MetaNode> entry : metaNodes.entrySet()) {
                int node = entry.getKey();
                MetaNode metaNode = entry.getValue();
                if (metaNode.innerEdgesCount < 0)
                    continue;
                float Q = modularCoeff(metaNode.innerNodes);
                int mergeNode = -1;
                for (int neigh : commTopology.get(node)) {
                    Set<Integer> virtualCommunity = new HashSet<>(metaNode.innerNodes);
                    virtualCommunity.addAll(metaNodes.get(neigh).innerNodes);
                    float newQ = modularCoeff(virtualCommunity);
                    if (newQ > Q) {
                        mergeNode = neigh;
                        Q = newQ;
                    }
                }
                if (mergeNode >= 0) { //merge first MetaNode into neighbour
                    metaNodes.get(mergeNode).innerNodes.addAll(metaNode.innerNodes);
                    metaNode.innerEdgesCount = -1;

                    Set<Integer> newNeighs = commTopology.get(mergeNode);
                    newNeighs.addAll(commTopology.get(node));
                    newNeighs.remove(node);
                    newNeighs.remove(mergeNode);

                    commTopology.removeNode(node);

                    repeat = true;
                }
            }
        }


        return metaNodes.entrySet().stream().filter(a -> a.getValue().innerEdgesCount >=0 )
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (x, y) -> y, HashMap::new));
    }

    public int commInnerEdgesCount(Set<Integer> community) {
        int count = 0;
        for(Integer member: community) {
            count += this.get(member).stream().filter(community::contains).count();
        }

        return count/2;
    }
}
