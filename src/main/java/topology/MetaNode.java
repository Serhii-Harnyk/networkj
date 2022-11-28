package topology;

import java.util.Set;

/**
 * Set of nodes inside graph that is treated as one
 * Has no sense without Topology
 * @todo may be inner class?
 */
public class MetaNode {
    Set<Integer> innerNodes; // ids of inner nodes
    int innerEdgesCount;

    public MetaNode(Set<Integer> innerNodes, int innerEdgesCount) {
        this.innerNodes = innerNodes;
        this.innerEdgesCount = innerEdgesCount;
    }
}
