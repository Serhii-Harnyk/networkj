import graphs.VoteBreakGraph;
import org.junit.Test;
import utils.GraphUtils;
import topology.TopologyGenerator;

import static org.junit.Assert.assertEquals;

public class VoteBreakGraphTest {

    GraphUtils gu = new GraphUtils();

    @Test
    public void edgesCountTest(){

        TopologyGenerator tg = new TopologyGenerator();
        var topology = tg.genCompleteGraph(5);
        int[] values = {1,0,1,0,1};

        VoteBreakGraph vbg = new VoteBreakGraph(topology, values);

        assertEquals(6, vbg.getDiffEdges().size());
    }
}
