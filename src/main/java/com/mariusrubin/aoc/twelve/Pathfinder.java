package com.mariusrubin.aoc.twelve;

import static java.util.stream.Collectors.joining;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Marius Rubin
 * @since 0.1.0
 */
public class Pathfinder {

  private static final Pattern SPLIT = Pattern.compile("-");


  private final Map<String, Cave> vertices;
  private final List<Edge>        edges;
  private final int               smallCaveMaxVisit;

  public Pathfinder(final List<String> inputData) {
    this(inputData, 1);
  }

  public Pathfinder(final List<String> inputData, final int smallCaveMaxVisit) {

    this.smallCaveMaxVisit = smallCaveMaxVisit;

    vertices = inputData.stream()
                        .flatMap(SPLIT::splitAsStream)
                        .distinct()
                        .collect(Collectors.toMap(Function.identity(), Cave::new));

    edges = inputData.stream()
                     .map(SPLIT::split)
                     .flatMap(split -> Stream.of(new Edge(vertices.get(split[0]),
                                                          vertices.get(split[1])),
                                                 new Edge(vertices.get(split[1]),
                                                          vertices.get(split[0]))))
                     .toList();
  }

  public int countPaths() {
    return findPaths().size();
  }

  public List<String> findPaths() {

    final var openPaths     = new ArrayList<List<Cave>>();
    final var completePaths = new ArrayList<List<Cave>>();

    openPaths.add(List.of(vertices.get("start")));

    while (!openPaths.isEmpty()) {
      final List<List<Cave>> openTemp = new ArrayList<>(openPaths);
      for (final List<Cave> path : openTemp) {

        if (path.get(path.size() - 1).isEnd()) {
          completePaths.add(path);
        } else {
          final var next = getNextSteps(path);
          next.stream()
              .map(cave -> {
                final var withNext = new ArrayList<>(path);
                withNext.add(cave);
                return withNext;
              })
              .forEach(openPaths::add);
        }

        openPaths.remove(path);

      }
    }

    return completePaths.stream()
                        .map(path -> path.stream().map(Cave::name).collect(joining(",")))
                        .toList();

  }

  private List<Cave> getNextSteps(final List<Cave> path) {

    final var lastStop = path.get(path.size() - 1);

    if (lastStop.isEnd()) {
      //Route is complete
      return Collections.emptyList();
    }

    return edges.stream()
                .filter(e -> e.from().equals(lastStop))
                .map(Edge::to)
                .filter(cave -> isViableStep(path, cave))
                .toList();
  }

  private boolean isViableStep(final List<Cave> followed, final Cave possible) {
    //Ensure that the next step is either a big cave that can be visited multiple times
    //or a small one that has not been visited too many times
    return possible.isBig() || !possible.isStart() && isBelowVisitThreshold(followed, possible);
  }

  private boolean isBelowVisitThreshold(final List<Cave> visited, final Cave possible) {
    //Find the existing max number of visits to any small cave
    final var previousMaxVisits = visited.stream()
                                         .filter(Cave::isSmall)
                                         .collect(Collectors.groupingBy(Cave::name))
                                         .values()
                                         .stream()
                                         .mapToInt(List::size)
                                         .max()
                                         .orElse(0);

    final var maxAllowableVisits = previousMaxVisits == smallCaveMaxVisit ? 1 : smallCaveMaxVisit;

    return visited.stream().filter(possible::equals).count() < maxAllowableVisits;
  }

  private record Cave(String name) {

    private static final String START = "start";
    private static final String END   = "end";

    private boolean isBig() {
      return name.toUpperCase().equals(name);
    }

    private boolean isSmall() {
      return name.toLowerCase().equals(name);
    }


    private boolean isStart() {
      return START.equals(name);
    }

    private boolean isEnd() {
      return END.equals(name);
    }

    @Override
    public boolean equals(final Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }
      final Cave cave = (Cave) o;
      return name.equals(cave.name);
    }

    @Override
    public int hashCode() {
      return Objects.hash(name);
    }

    @Override
    public String toString() {
      return name;
    }
  }

  private record Edge(Cave from, Cave to) {

  }


}
