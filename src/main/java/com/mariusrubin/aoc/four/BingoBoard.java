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

  private final List<BingoLine> lines;

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
    this.lines = lines.map(line -> new BingoLine(line, Orientation.HORIZONTAL))
                      .collect(Collectors.toList());
    processVerticals();
  }

  private void processVerticals() {
    IntStream.range(0, LINE_LENGTH)
             .mapToObj(i -> lines.stream().limit(LINE_LENGTH).map(line -> line.getNumberAtPosition(i)).toList())
             .map(line -> new BingoLine(line, Orientation.VERTICAL))
             .forEach(lines::add);
  }

  public int getNumberAtPosition(final int x, final int y) {
    return lines.get(LINE_LENGTH - y).getNumberAtPosition(x - 1);
  }

  public boolean mark(final int number) {
    return lines.stream()
                .anyMatch(bingoLine -> bingoLine.mark(number));
  }

  private final class BingoLine {

    private final List<Integer> numbers;
    private final List<Integer> matches = new ArrayList<>(LINE_LENGTH);

    private final Orientation orientation;

    private BingoLine(final List<Integer> numbers, final Orientation orientation) {
      this.numbers = Collections.unmodifiableList(numbers);
      this.orientation = orientation;
    }

    private boolean mark(final int number) {
      if (numbers.contains(number)) {
        matches.add(number);
      }
      return isComplete();
    }

    private boolean isComplete() {
      return numbers.size() == matches.size();
    }

    private int getNumberAtPosition(final int position) {
      return numbers.get(position);
    }

  }

  private enum Orientation {
    HORIZONTAL,
    VERTICAL
  }

}
