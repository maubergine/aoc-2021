package com.mariusrubin.aoc.fifteen;

import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

/**
 * @author Marius Rubin
 * @since 0.1.0
 */
class CavernNavigator {

  private final Graph<CavernNode, DefaultWeightedEdge> graph = new SimpleDirectedWeightedGraph<>(
      DefaultWeightedEdge.class);

  private final int[][] cavern;

  private final int maxRow;
  private final int maxColumn;

  CavernNavigator(final int[][] cavern) {

    this.cavern = cavern.clone();

    maxRow = cavern.length - 1;
    maxColumn = cavern[0].length - 1;

    initVertices();
    initEdges();

  }

  private void initVertices() {
    IntStream.rangeClosed(0, maxRow)
             .forEach(rowNum -> {
               final var row = cavern[rowNum];
               IntStream.rangeClosed(0, maxColumn)
                        .forEach(columnNum -> graph.addVertex(new CavernNode(row[columnNum],
                                                                             rowNum,
                                                                             columnNum)));
             });
  }

  private void initEdges() {
    graph.vertexSet()
         .forEach(source -> getAdjacent(source, cavern).forEach(target -> {
           graph.addEdge(source, target);
           graph.setEdgeWeight(source, target, target.riskScore());
           graph.addEdge(target, source);
           graph.setEdgeWeight(target, source, source.riskScore());
         }));
  }


  List<CavernNode> lowestRiskPath() {
    return getPath().getVertexList();
  }

  long scoreLowestPath() {
    return Math.round(getPath().getWeight());
  }

  private GraphPath<CavernNode, DefaultWeightedEdge> getPath() {

    final var algo = new DijkstraShortestPath<>(graph);

    final var start = graph.vertexSet()
                           .stream()
                           .filter(node -> node.row() + node.column() == 0)
                           .findAny()
                           .orElseThrow();

    final var end = graph.vertexSet()
                         .stream()
                         .filter(node -> node.row() == maxRow)
                         .filter(node -> node.column() == maxColumn)
                         .findAny()
                         .orElseThrow();

    return algo.getPath(start, end);

  }

  private Stream<CavernNode> getAdjacent(final CavernNode node, final int[][] cavern) {

    return Stream.of(new int[]{node.row() - 1, node.column()},
                     new int[]{node.row() + 1, node.column()},
                     new int[]{node.row(), node.column() - 1},
                     new int[]{node.row(), node.column() + 1})
                 .filter(ints -> ints[0] >= 0 && ints[0] <= maxRow)
                 .filter(ints -> ints[1] >= 0 && ints[1] <= maxColumn)
                 .map(ints -> new CavernNode(cavern[ints[0]][ints[1]], ints[0], ints[1]));

  }

  public record CavernNode(int riskScore, int row, int column) {

  }
}
