package experiments;

import topology.Topology;
import topology.TopologyGenerator;

public class BarabasiAlbertExp {

    TopologyGenerator tg = new TopologyGenerator();

    public void run() {
        int nodes = 200;
        int coreSize = 5;
        int edges = 1;

        Topology tp = tg.genBarabasiAlbert(nodes, edges, coreSize);

        int min = Integer.MAX_VALUE;
        for(int i=0; i<coreSize; i++) {
            System.out.println("Node: " + i + " degree: " + tp.get(i).size());
            min = Math.min(min, tp.get(i).size());
        }
        System.out.println("Average degree: " + tp.averageDegree());

        int count = 0;
        for(int i=coreSize-1; i<tp.size(); i++) {
            if(tp.get(i).size() >= min) {
                count++;
                System.out.println("Non core: " + i + " degree: " + tp.get(i).size());
            }
        }

        System.out.println("Non core hubs: " + count);
    }
}
