package com.mariusrubin.aoc.eight;

import static com.mariusrubin.aoc.eight.Alphabetiser.alphabetise;
import static java.util.stream.Collectors.toMap;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author Marius Rubin
 * @since 0.1.0
 */
class Display {

  private final Map<Character, Character> wiring;
  private final Map<String, WiredSegment> wiredSegments;

  Display(final String seed) {
    wiring = IntStream.rangeClosed('a', 'g')
                      .mapToObj(i -> (char) i)
                      .collect(toMap(Function.identity(), c -> seed.charAt(Math.abs('a' - c))));
    wiredSegments = generateWiredSegments();
  }

  Map<String, WiredSegment> getWiredSegments() {
    return Collections.unmodifiableMap(wiredSegments);
  }

  int convert(final String input) {
    return wiredSegments.get(alphabetise(input)).number();
  }

  String toWires(final String segments) {
    return segments.chars()
                   .mapToObj(i -> String.valueOf(wiring.get((char) i)))
                   .sorted()
                   .collect(Collectors.joining());
  }

  String segmentPattern() {
    return wiredSegments.values()
                        .stream()
                        .map(WiredSegment::wires)
                        .sorted()
                        .collect(Collectors.joining(" "));
  }

  private Map<String, WiredSegment> generateWiredSegments() {
    return Arrays.stream(SegmentPattern.values())
                 .map(p -> new WiredSegment(p.actual, toWires(p.segments)))
                 .collect(toMap(WiredSegment::wires, Function.identity()));
  }

  record WiredSegment(int number, String wires) {

  }

  private enum SegmentPattern {
    ZERO(0, "abcefg"),
    ONE(1, "cf"),
    TWO(2, "acdeg"),
    THREE(3, "acdfg"),
    FOUR(4, "bcdf"),
    FIVE(5, "abdfg"),
    SIX(6, "abdefg"),
    SEVEN(7, "acf"),
    EIGHT(8, "abcdefg"),
    NINE(9, "abcdfg");
    private final int actual;

    private final String segments;

    SegmentPattern(final int actual, final String segments) {
      this.actual = actual;
      this.segments = segments;
    }

  }

}
