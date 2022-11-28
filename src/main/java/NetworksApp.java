import experiments.BarabasiAlbertExp;
import graphs.VoteGraph;
import topology.Topology;
import topology.TopologyGenerator;
import utils.GraphStreamUtils;

public class NetworksApp {

    public static void main(String[] args) {
        TopologyGenerator tg = new TopologyGenerator();
        Topology tp = tg.genCompleteGraph(5);
        tp.merge(tg.genCompleteGraph(5));
        tp.addEdge(4,5);

        GraphStreamUtils.drawTopology(tp);

    }

    private static int count(int n) {

        return n*(n-1)/2;
    }

    private static int runPullVoting(VoteGraph vg) {
        vg.generateValues();

        int i = 0;

        while (!vg.checkHomogenization()) {
            i++;
            vg.votePull();
        }

        return i;
    }

    private static int runPushVoting(VoteGraph vg) {
        vg.generateValues();

        int i = 0;

        while (!vg.checkHomogenization()) {
            i++;
            vg.votePush();
        }

        return i;
    }


}


