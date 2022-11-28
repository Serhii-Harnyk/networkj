package utils;

import topology.Topology;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.stream.Collectors;

public class GraphUtils {

  //prints graph in format nodeId: node degree(count of edges)
  public void printGraph(Topology graph) {
    graph.forEach((k,v) -> System.out.println(k + ": " + v.size()));
  }

  //prints graph in format nodeId: list of neighbors
  public void printGraphFull(Topology graph) {
    graph.forEach((k,v) -> {

      System.out.println(k + ": {"
          + v.stream().map(Object::toString).collect(Collectors.joining(", ")) + "}");
    });
  }

  // print stat in format degree: count of nodes with such degree
  public void printGraphDegreeStat(Topology graph) {
    int[] degrees = getGraphDegreeStat(graph);

    for(int i=0; i<degrees.length; i++) {
      if(degrees[i]>0)
        System.out.println(i + ": " + degrees[i]);
    }
  }

  public int[] getGraphDegreeStat(Topology graph) {
    int[] degrees = new int[graph.size()];
    for(int i=0; i< graph.size(); i++) {
      degrees[graph.get(i).size()]++;
    }

    return degrees;
  }
  
  public void graphDegreeStatToCsv(Topology graph, String filename) {
    int[] powerLawStat = getGraphDegreeStat(graph);

    FileWriter out = null;
    try {
      out = new FileWriter(filename);
    } catch (IOException e) {
      e.printStackTrace();
    }
    try (CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT
        .withHeader("degrees", "count"))) {
      for (int i = 0; i < powerLawStat.length; i++) {
        if (powerLawStat[i] > 0) {
          printer.printRecord(i, powerLawStat[i]);
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }


}
