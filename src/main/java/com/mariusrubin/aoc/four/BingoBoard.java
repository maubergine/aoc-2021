package com.mariusrubin.aoc.four;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * @author Marius Rubin
 * @since 0.1.0
 */
public class BingoBoard {

  private static final int LINE_LENGTH = 5;

  private final List<BingoLine> horizontals;
  private final List<BingoLine> verticals;
  private       int             winningNumber = -1;

  public BingoBoard(final int[][] lines) {
    this(Arrays.stream(lines)
               .map(IntStream::of)
               .map(IntStream::boxed)
               .map(Stream::toList));
  }

  public BingoBoard(final List<List<Integer>> lines) {
    this(lines.stream());
  }

  private BingoBoard(final Stream<List<Integer>> lines) {
    horizontals = lines.map(BingoLine::new).collect(Collectors.toList());
    verticals = processVerticals();
  }

  private List<BingoLine> processVerticals() {
    return IntStream.range(0, LINE_LENGTH)
                    .mapToObj(i -> horizontals.stream()
                                              .map(line -> line.getNumberAtPosition(i))
                                              .toList())
                    .map(BingoLine::new)
                    .collect(Collectors.toList());
  }

  public int getNumberAtPosition(final int x, final int y) {
    return horizontals.get(LINE_LENGTH - y).getNumberAtPosition(x - 1);
  }

  public int call(final int number) {
    return Stream.concat(horizontals.stream(), verticals.stream())
                 .map(bingo -> bingo.call(number))
                 .filter(i -> i >= 0)
                 .peek(i -> winningNumber = i)
                 .findAny()
                 .orElse(-1);
  }

  public int getScore() {
    if (winningNumber < 0) {
      return winningNumber;
    }

    return horizontals.stream()
                      .flatMap(line -> line.getUnmarked().stream())
                      .mapToInt(Integer::intValue)
                      .sum() * winningNumber;
  }

  private static final class BingoLine {

    private final List<Integer> numbers;
    private final List<Integer> matches = new ArrayList<>(LINE_LENGTH);

    private BingoLine(final List<Integer> numbers) {
      this.numbers = Collections.unmodifiableList(numbers);
    }

    private int call(final int number) {
      if (numbers.contains(number)) {
        matches.add(number);
      }
      return isComplete() ? number : -1;
    }

    private boolean isComplete() {
      return numbers.size() == matches.size();
    }

    private List<Integer> getUnmarked() {
      final var toFilter = new ArrayList<>(numbers);
      toFilter.removeAll(matches);
      return toFilter;
    }

    private int getNumberAtPosition(final int position) {
      return numbers.get(position);
    }

  }

}
