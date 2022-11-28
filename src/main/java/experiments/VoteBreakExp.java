package experiments;

import graphs.VoteBreakGraph;
import utils.RandomUtils;
import topology.Topology;
import topology.TopologyGenerator;

import java.util.Set;

public class VoteBreakExp {

    private int nodes;
    private int pv;
    private int pbr;

    TopologyGenerator tg = new TopologyGenerator();
    RandomUtils ru = new RandomUtils();

    /**
     *
     * @param nodes - size of the graph
     * @param pv 0-100
     * @param pbr 0-100
     */
    public VoteBreakExp(int nodes, int pv, int pbr) {
        this.nodes = nodes;
        this.pv = pv;
        this.pbr = pbr;
    }

    public void run() {
        Topology ERTopology = tg.genBarabasiAlbert(nodes, 5, 5);

        VoteBreakGraph vbg = new VoteBreakGraph(ERTopology);

        String state;
        int count = 0;
        while (vbg.getDiffEdges().size() > 0) {
            Set<Integer> edge = vbg.getRandomEdge();
            if(ru.percent(pv)) {
                vbg.vote(edge);
            }

            if(vbg.getDiffEdges().size() == 0)
                break;

            edge = vbg.getRandomEdge();
            if(ru.percent(pbr)) {
                vbg.removeEdge(edge);
            }

            count++;
        }

        if(vbg.checkHomogenization()) {
            state = "vote";
        } else
            state = "break";

        System.out.println("Vote-break. State " + state +" Count:" + count );
    }
}
