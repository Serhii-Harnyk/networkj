import topology.Topology;
import org.junit.Test;
import topology.TopologyGenerator;

import static org.junit.Assert.assertEquals;

public class WattsStrogatzGenTest {

    @Test
    public void testRing() {
        TopologyGenerator tg = new TopologyGenerator();

        Topology tp = tg.genWattsStrogatz(10, 1, 0);

        assertEquals(tp.sizeLLC(), 10);

        tp.values().forEach(v -> {
            assertEquals(2, v.size());
        });
    }

    @Test
    public void testWiring() {
        TopologyGenerator tg = new TopologyGenerator();

        Topology tp = tg.genWattsStrogatz(100, 2, 0);

        assertEquals(tp.sizeLLC(), 100);

        tp.values().forEach(v -> {
            assertEquals(4, v.size());
        });
    }

    @Test
    public void testLargeK() {
        TopologyGenerator tg = new TopologyGenerator();

        Topology tp = tg.genWattsStrogatz(3, 6, 0);

        assertEquals(tp.sizeLLC(), 3);

        tp.values().forEach(v -> {
            assertEquals(2, v.size());
        });
    }

}
