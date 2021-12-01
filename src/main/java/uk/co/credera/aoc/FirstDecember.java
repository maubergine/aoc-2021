package uk.co.credera.aoc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * @author Marius Rubin
 * @since 0.1.0
 */
public class FirstDecember {

  public static void main(String[] args) {

    final FirstDecember counter = new FirstDecember();

    final int firstPuzzle = counter.countIncreases("src/main/resources/FirstDecember.txt", 1);
    System.out.printf("%d increases\n", firstPuzzle);

    final int secondPuzzle = counter.countIncreases("src/main/resources/FirstDecember.txt", 3);
    System.out.printf("%d increases\n", secondPuzzle);

  }


  public int countIncreases(final String path, final int window) {
    final List<Integer> items = loadItems(path);
    return countIncreases(items, window);
  }


  public int countIncreases(final List<Integer> items) {

    return window(items, 2).map(grouped -> grouped.get(1).compareTo(grouped.get(0)))
                           .filter(i -> i > 0)
                           .reduce(0, Integer::sum);

  }

  public int countIncreases(final List<Integer> items, final int window) {

    if (window < 1) {
      throw new IllegalArgumentException(String.format("Window cannot be smaller than 1, but was %d",
                                                       window));
    }

    if (window > items.size()) {
      throw new IllegalArgumentException(String.format(
          "Window cannot be larger than the number of items (%d), but was %d",
          items.size(), window));
    }

    if (window >= items.size()) {
      return 0;
    }

    return window == 1
           ? countIncreases(items)
           : countIncreases(sumWindow(items, window));

  }

  private List<Integer> loadItems(final String path) {

    try (final Stream<String> lines = Files.lines(Path.of(path))) {

      return lines.map(Integer::valueOf).toList();

    } catch (final IOException ignored) {
      return Collections.emptyList();
    }

  }

  private List<Integer> sumWindow(final List<Integer> items, final int window) {
    return window(items, window).map(grouped -> grouped.stream().reduce(0, Integer::sum)).toList();
  }

  private Stream<List<Integer>> window(final List<Integer> items, final int window) {
    return IntStream.range(window - 1, items.size())
                    .mapToObj(i -> items.subList(i - window + 1, i + 1));
  }

}
