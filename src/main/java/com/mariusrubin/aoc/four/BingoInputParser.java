package com.mariusrubin.aoc.four;

import static java.lang.Math.floor;
import static java.lang.Math.round;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author Marius Rubin
 * @since 0.1.0
 */
public class BingoInputParser {

  private static final Pattern CALL_SPLIT = Pattern.compile(",");

  private final List<String> lines;

  public BingoInputParser(final List<String> lines) {
    this.lines = Collections.unmodifiableList(lines);
  }

  public List<Integer> calls() {
    return CALL_SPLIT.splitAsStream(lines.get(0))
                     .map(Integer::valueOf)
                     .toList();
  }

  public List<BingoBoard> boards() {

    final var counter = new AtomicInteger();

    return lines.stream()
                .sequential()
                .skip(2)
                .filter(s -> !s.isEmpty())
                .map(s -> {
                  final var group = (double) counter.getAndIncrement() / 5;
                  return new IntegerLine((int) round(floor(group)), toIntegers(s));
                })
                .collect(Collectors.groupingBy(IntegerLine::group))
                .values()
                .stream()
                .map(BingoInputParser::toBingoBoard)
                .toList();

  }

  private static BingoBoard toBingoBoard(final List<IntegerLine> intLines) {
    return new BingoBoard(intLines.stream().map(IntegerLine::values).toList());
  }

  private static List<Integer> toIntegers(final String line) {
    return IntStream.range(0, 5)
                    .map(i -> i * 3)
                    .mapToObj(i -> line.substring(i, i + 2))
                    .map(String::trim)
                    .map(Integer::valueOf)
                    .toList();
  }

  private record IntegerLine(int group, List<Integer> values) {

  }

}
