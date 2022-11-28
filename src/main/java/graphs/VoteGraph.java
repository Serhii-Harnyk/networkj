package graphs;

import utils.RandomUtils;
import topology.Topology;

import java.util.HashSet;
import java.util.Set;

public class VoteGraph {

  RandomUtils ru = new RandomUtils();

  Topology topology;
  int[] values;

  public VoteGraph(Topology topology) {
    this.topology = topology;
    values = new int[topology.size()];
    generateValues();
  }

  public VoteGraph(Topology topology, int[] values) {
    this.topology = topology;
    this.values = values;
  }

  public int[] generateValues() {
    topology.keySet().forEach(
        v -> values[v] = (Math.random() < 0.5 ? 0 : 1)
    );

    return values;
  }

  /**
   * 1. Choose listener node randomly from whole graph
   * 2. Choose publisher node randomly from listener's neighbours
   * 3. Set value of listener equal to value of publisher
   * @return new value of listener
   */
  public Integer votePull() {

    Integer listenerKey = ru.getRandomKeyOfMap(topology);

    Integer publisherKey = ru.getRandomValueOfSet(topology.get(listenerKey));

    values[listenerKey] = values[publisherKey];

    return values[listenerKey];
  }

  /**
   * 1. Choose publisher node randomly from whole graph
   * 2. Choose listener node randomly from listener's neighbours
   * 3. Set value of listener equal to value of publisher
   * @return new value of listener
   */
  public Integer votePush() {

    Integer publisherKey = ru.getRandomKeyOfMap(topology);

    Integer listenerKey = ru.getRandomValueOfSet(topology.get(publisherKey));

    values[listenerKey] = values[publisherKey];

    return values[listenerKey];
  }

  /**
   * Check if graph values are the same
   */
  public boolean checkHomogenization() {
    Set<Integer> uniqueValues = new HashSet<>();

    for(int val: values) {
      uniqueValues.add(val);
      if(uniqueValues.size() > 1)
        return false;
    }

    return true;
  }

  public void setValues(int[] values) {
    this.values = values;
  }


}
