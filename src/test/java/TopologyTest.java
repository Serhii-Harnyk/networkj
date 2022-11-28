import topology.Topology;
import org.junit.Test;
import topology.TopologyGenerator;

import static org.junit.Assert.assertEquals;

public class TopologyTest {

    TopologyGenerator tg = new TopologyGenerator();

    @Test
    public void removeNodeTest() {
        Topology tp = tg.genCompleteGraph(5);
        tp.values().forEach(a -> assertEquals(4, a.size()));
        tp.removeNode(4);

        assertEquals(4, tp.size());
        tp.values().forEach(a -> assertEquals(3, a.size()));
    }

    @Test
    public void shortestPathTest() {
        Topology tp = new Topology();
        tp.addEdge(0,1);
        tp.addEdge(1,2);

        assertEquals(2, tp.shortestPathLength(0,2));

        tp.addEdge(0,2);

        assertEquals(1, tp.shortestPathLength(0,2));
    }

    @Test
    public void shortestPathCycleTest() {
        Topology tp = new Topology();
        tp.addEdge(0, 1);
        tp.addEdge(1, 2);
        tp.addEdge(3, 2);
        tp.addEdge(3, 4);
        tp.addEdge(0, 4);

        assertEquals(1, tp.shortestPathLength(0,4));
        assertEquals(2, tp.shortestPathLength(0,3));
    }

    @Test
    public void eccentricityTest() {
        Topology tp = new Topology();
        tp.addEdge(0,1);
        tp.addEdge(1,2);

        assertEquals(2, tp.eccentricity(0));
        assertEquals(1, tp.eccentricity(1));

        tp.addEdge(0,2);

        assertEquals(1, tp.eccentricity(0));
    }

    @Test
    public void eccentricityCycleTest() {
        Topology tp = new Topology();
        tp.addEdge(0, 1);
        tp.addEdge(1, 2);
        tp.addEdge(3, 2);
        tp.addEdge(3, 4);
        tp.addEdge(0, 4);

        assertEquals(2, tp.eccentricity(0));
    }

    @Test
    public void diameterTest() {
        Topology tp = new Topology();
        tp.addEdge(0, 1);
        tp.addEdge(1, 2);

        assertEquals(2, tp.diameter());

        tp.addEdge(0,2);

        assertEquals(1, tp.diameter());

        tp.addEdge(3,4);
        tp.addEdge(5,4);
        tp.addEdge(6,4);
        tp.addEdge(7,4);
        tp.addEdge(8,4);

        assertEquals(2, tp.diameter());

        tp.addEdge(9,10);
        tp.addEdge(11,10);
        tp.addEdge(11,12);
        tp.addEdge(12,13);

        assertEquals(4, tp.diameter());
    }

    @Test
    public void diameterCycleTest() {
        Topology tp = new Topology();
        tp.addEdge(0, 1);
        tp.addEdge(1, 2);
        tp.addEdge(3, 2);
        tp.addEdge(3, 4);
        tp.addEdge(0, 4);

        assertEquals(2, tp.diameter());
    }

    @Test
    public void sizeLLCTest() {
        Topology tp = new Topology();
        tp.addEdge(0,1);
        tp.addEdge(2,1);
        tp.addEdge(2,3);
        tp.addEdge(0,3);

        tp.addEdge(4,5);
        tp.addEdge(6,5);
        tp.addEdge(6,7);

        assertEquals(4, tp.sizeLLC());

        tp.addEdge(7,8);
        tp.addEdge(6,8);
        tp.addEdge(5,8);
        tp.addEdge(4,8);

        assertEquals(5, tp.sizeLLC());
    }

    @Test
    public void averageDegreeTest() {
        Topology tp = new Topology();

        tp.addEdge(0,1);
        assertEquals(1f, tp.averageDegree(), 0);

        tp.addEdge(1,2);
        assertEquals(4f/3f, tp.averageDegree(), 0);

        tp.addEdge(0,2);
        assertEquals(2f, tp.averageDegree(), 0);

        tp.addNode(3);
        tp.addNode(4);
        tp.addNode(5);

        assertEquals(1f, tp.averageDegree(), 0);

    }

    @Test
    public void nodesWithDegreeTest() {
        Topology tp = new Topology();
        tp.addNode(0);

        assertEquals(1, tp.nodesWithDegree(0));
    }

    @Test
    public void avgShortPathTest() {
        Topology tp = new Topology();
        tp.addEdge(0,1);
        tp.addEdge(1,2);

        assertEquals(8f/6, tp.avgShortestPathLength(), 0.001f);

        tp.addEdge(0, 2);

        assertEquals(1f, tp.avgShortestPathLength(), 0.001f);

        tp = tg.genCompleteGraph(10);
        assertEquals(1f, tp.avgShortestPathLength(), 0.001f);


    }

    @Test
    public void avgShortPathDiamondTest() {
        Topology tp = new Topology();
        tp.addEdge(0,1);
        tp.addEdge(0,2);


        tp.addEdge(7,1);
        tp.addEdge(7,2);


        assertEquals(4/3f, tp.avgShortestPathLength(), 0.001f);
    }

    @Test
    public void largestConnectedComponentTest() {
        Topology tp = new Topology();

        tp.addEdge(0,1);
        tp.addEdge(2,1);
        tp.addEdge(3,1);

        tp.addEdge(4,6);
        tp.addEdge(5,6);

        assertEquals(4, tp.largestConnectedComponent().size());
    }

    @Test
    public void clusteringCoeffTest() {
        Topology tp = new Topology();
        tp.addEdge(0, 1);
        tp.addEdge(0, 2);
        tp.addEdge(0, 3);
        tp.addEdge(0, 4);
        assertEquals(0f, tp.clusteringCoeff(0), 0.001f);
        assertEquals(0f, tp.avgClusteringCoeff(), 0.001f);

        tp.addEdge(1, 2);
        assertEquals(1/6f, tp.clusteringCoeff(0), 0.001f);
        assertEquals(1f, tp.clusteringCoeff(1), 0.001f);
        assertEquals(1f, tp.clusteringCoeff(2), 0.001f);
        assertEquals(0f, tp.clusteringCoeff(3), 0.001f);
        assertEquals(0f, tp.clusteringCoeff(4), 0.001f);
        assertEquals(13/30f, tp.avgClusteringCoeff(), 0.001f);
        
        tp.addEdge(1, 3);
        assertEquals(1/3f, tp.clusteringCoeff(0), 0.001f);
        assertEquals(2/3f, tp.clusteringCoeff(1), 0.001f);
        assertEquals(1f, tp.clusteringCoeff(2), 0.001f);
        assertEquals(1f, tp.clusteringCoeff(3), 0.001f);
        assertEquals(0f, tp.clusteringCoeff(4), 0.001f);
        assertEquals(0.6f, tp.avgClusteringCoeff(), 0.001f);
        
        tp.addEdge(1, 4);
        assertEquals(0.5f, tp.clusteringCoeff(0), 0.001f);
        assertEquals(0.5f, tp.clusteringCoeff(1), 0.001f);
        assertEquals(1f, tp.clusteringCoeff(2), 0.001f);
        assertEquals(1f, tp.clusteringCoeff(3), 0.001f);
        assertEquals(1f, tp.clusteringCoeff(4), 0.001f);
        assertEquals(0.8f, tp.avgClusteringCoeff(), 0.001f);

        tp.addEdge(2, 3);
        assertEquals(2/3f, tp.clusteringCoeff(0), 0.001f);
        assertEquals(2/3f, tp.clusteringCoeff(1), 0.001f);
        assertEquals(1f, tp.clusteringCoeff(2), 0.001f);
        assertEquals(1f, tp.clusteringCoeff(3), 0.001f);
        assertEquals(1f, tp.clusteringCoeff(4), 0.001f);
        assertEquals(26/30f, tp.avgClusteringCoeff(), 0.001f);

        tp.addEdge(2, 4);
        assertEquals(5/6f, tp.clusteringCoeff(0), 0.001f);
        assertEquals(5/6f, tp.clusteringCoeff(1), 0.001f);
        assertEquals(5/6f, tp.clusteringCoeff(2), 0.001f);
        assertEquals(1f, tp.clusteringCoeff(3), 0.001f);
        assertEquals(1f, tp.clusteringCoeff(4), 0.001f);
        assertEquals(0.9, tp.avgClusteringCoeff(), 0.001f);

        tp.addEdge(4, 3);
        assertEquals(1f, tp.clusteringCoeff(0), 0.001f);
        assertEquals(1f, tp.clusteringCoeff(1), 0.001f);
        assertEquals(1f, tp.clusteringCoeff(2), 0.001f);
        assertEquals(1f, tp.clusteringCoeff(3), 0.001f);
        assertEquals(1f, tp.clusteringCoeff(4), 0.001f);
        assertEquals(1f, tp.avgClusteringCoeff(), 0.001f);
        
    }

    @Test
    public void degreeAssortTest() {
        Topology tp = new Topology();
        tp = tg.genCompleteGraph(100);
        assertEquals(0, tp.degreeAssort(), 0.01d);

        for(int i=0; i<100; i++) {
            tp.addEdge(100+i, 200+i);
        }
        assertEquals(1, tp.degreeAssort(), 0.00000001d);
    }

    @Test
    public void mergeTest() {
        Topology tp1 = tg.genCompleteGraph(5);
        Topology tp2 = tg.genCompleteGraph(3);

        tp1.merge(tp2);

        assertEquals(8, tp1.size());
        assertEquals(2, tp1.get(7).size());
        assertEquals(5, tp1.sizeLLC());

        tp1.addEdge(4,5);
        assertEquals(8, tp1.sizeLLC());
    }

    @Test
    public void findCommunitiesTest() {
//        Topology tp = new Topology();
//        tp.addEdge(0, 1);
//        tp.addEdge(0, 2);
//        tp.addEdge(0, 3);
//        tp.addEdge(1, 4);
//        tp.addEdge(1, 5);
//
//        var result = tp.findCommunities(); // {0,1,2,3} {4} {5}
//
//        tp = new Topology();
//        tp.addEdge(0,1);
//        tp.addEdge(0,2);
//        tp.addEdge(3,4);
//        tp.addEdge(3,5);
//        tp.addEdge(3,0);
//
//        result = tp.findCommunities(); // {0,1,2,3} {4} {5}

        Topology tp = tg.genCompleteGraph(5);
        tp.merge(tg.genCompleteGraph(5));
        tp.addEdge(4,5);

        var communities = tp.findCommunities();
        assertEquals(2, communities.size());
    }
}
