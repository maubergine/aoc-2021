package uk.co.credera.aoc;

import static java.lang.System.lineSeparator;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import uk.co.credera.aoc.util.FileLoader;

/**
 * @author Marius Rubin
 * @since 0.1.0
 */
public class FirstDecember implements Callable<Integer> {

  private static final int SUCCESS = 0;
  private static final int FAILURE = 1;
  private static final String FIRST_DECEMBER_TXT = "src/main/resources/uk/co/credera/aoc/FirstDecember.txt";

  public static void main(final String... args) {
    try {
      System.exit(new FirstDecember().call());
    } catch (final Exception error) {
      error.printStackTrace();
      System.exit(FAILURE);
    }
  }


  public int countIncreases(final String path, final int window) {
    final var loader = new FileLoader(path);
    return countIncreases(loader.allIntegers(), window);
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

  public int countIncreases(final List<Integer> items) {

    return window(items, 2).map(grouped -> grouped.get(1).compareTo(grouped.get(0)))
                           .filter(i -> i > 0)
                           .reduce(0, Integer::sum);

  }

  private List<Integer> sumWindow(final List<Integer> items, final int window) {
    return window(items, window).map(grouped -> grouped.stream().reduce(0, Integer::sum)).toList();
  }

  private Stream<List<Integer>> window(final List<Integer> items, final int window) {
    return IntStream.range(window - 1, items.size())
                    .mapToObj(i -> items.subList(i - window + 1, i + 1));
  }

  @Override
  public Integer call() {

    final var counter = new FirstDecember();

    final var firstPuzzle = counter.countIncreases(FIRST_DECEMBER_TXT, 1);
    System.out.printf("%d increases%s", firstPuzzle, lineSeparator());

    final var secondPuzzle = counter.countIncreases(FIRST_DECEMBER_TXT, 3);
    System.out.printf("%d increases%s", secondPuzzle, lineSeparator());

    return SUCCESS;
  }
}
