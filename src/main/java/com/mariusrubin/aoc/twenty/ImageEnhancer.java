package com.mariusrubin.aoc.twenty;

import static java.lang.Math.abs;
import static java.lang.System.lineSeparator;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * @author Marius Rubin
 * @since 0.1.0
 */
public class ImageEnhancer {

  private static final String                     PAD = "...";
  private final        int[]                      algorithm;
  private final        Function<Integer, Integer> blinkHandler;

  public ImageEnhancer(final String algorithm) {

    this.algorithm = algorithm.chars()
                              .mapToObj(i -> (char) i)
                              .mapToInt(c -> c == '#' ? 1 : 0)
                              .toArray();

    if (this.algorithm[0] == 1 && this.algorithm[algorithm.length() - 1] == 0) {
      blinkHandler = turn -> turn % 2 == 0 ? 0 : 1;
    } else {
      blinkHandler = turn -> 0;

    }
  }

  public long countLitCells(final List<String> input, final int times) {

    final var asCharArray = toCharArray(input);
    return countLitCells(asCharArray, times);

  }

  public long countLitCells(final char[][] input, final int times) {
    final var result = enhance(input, times);
    return countLit(result);
  }

  public int[][] enhance(final char[][] input, final int times) {

    var result = toIntArray(input);

    for (int i = 0; i < times; i++) {
      result = enhance(result, i).clone();
    }

    return result;

  }

  public int[][] enhance(final int[][] input, final int turn) {
    final var start = new Coordinate(-1, -1, -1);
    final var end   = detectEnd(input);

    final var output = new int[abs(end.y() - start.y()) + 1][abs(end.x() - start.x()) + 1];

    getRenderTargets(start, end, input).parallel()
                                       .map(coord -> withValue(coord, input, turn))
                                       .forEach(coord -> render(coord, output));

    return output;

  }

  public Coordinate withValue(final Coordinate renderTarget, final int[][] input, final int turn) {

    final var stringNum = IntStream.rangeClosed(renderTarget.y() - 1, renderTarget.y() + 1)
                                   .sequential()
                                   .flatMap(y -> processRow(renderTarget, input, y, turn))
                                   .mapToObj(String::valueOf)
                                   .collect(Collectors.joining());

    final var charRef = Integer.parseInt(stringNum, 2);

    return new Coordinate(renderTarget.x(), renderTarget.y(), algorithm[charRef]);

  }

  public String enhanceAsString(final char[][] input, final int times) {
    final var result = enhance(input, times);

    final var topBottomPad = buildPad(result);

    return topBottomPad
           + lineSeparator()
           + stringify(result)
           + lineSeparator()
           + topBottomPad;
  }


  private static int[][] toIntArray(final char[][] input) {
    return Arrays.stream(input)
                 .map(arr -> IntStream.range(0, arr.length)
                                      .mapToObj(i -> arr[i])
                                      .mapToInt(ImageEnhancer::toInt)
                                      .toArray())
                 .toArray(int[][]::new);
  }

  private static int toInt(final char c) {
    return c == '#' ? 1 : 0;
  }

  private static int countLit(final int[][] input) {

    return Arrays.stream(input)
                 .flatMapToInt(Arrays::stream)
                 .sum();

  }

  private static char[][] toCharArray(final List<String> strings) {

    return strings.stream()
                  .sequential()
                  .map(String::trim)
                  .map(String::toCharArray)
                  .toArray(char[][]::new);

  }

  private IntStream processRow(final Coordinate renderTarget,
                               final int[][] input,
                               final int y,
                               final int turn) {
    return IntStream.rangeClosed(renderTarget.x() - 1,
                                 renderTarget.x() + 1)
                    .sequential()
                    .map(x -> isOutOfBounds(input, y, x)
                              ? blinkHandler.apply(turn)
                              : input[y][x]);
  }


  public static void render(final Coordinate value, final int[][] into) {
    into[value.y() + 1][value.x() + 1] = value.value();
  }

  private static boolean isOutOfBounds(final int[][] input, final int y, final int x) {
    return y < 0 || x < 0 || y >= input.length || x >= input[0].length;
  }

  private static String buildPad(final int[][] result) {
    return IntStream.range(0, 3)
                    .mapToObj(
                        i -> IntStream.range(0, result[0].length + 6)
                                      .mapToObj(j -> ".")
                                      .collect(Collectors.joining())
                    )
                    .collect(Collectors.joining(lineSeparator()));
  }

  private static String stringify(final int[][] result) {
    return Arrays.stream(result)
                 .map(arr -> Arrays.stream(arr)
                                   .mapToObj(i -> i > 0 ? "#" : ".")
                                   .collect(Collectors.joining()))
                 .map(StringBuilder::new)
                 .map(sb -> sb.insert(0, PAD))
                 .map(sb -> sb.append(PAD))
                 .collect(Collectors.joining(lineSeparator()));
  }

  private static Stream<Coordinate> getRenderTargets(final Coordinate start,
                                                     final Coordinate end,
                                                     final int[][] inputs) {
    return IntStream.rangeClosed(start.y(), end.y())
                    .sequential()
                    .mapToObj(y -> IntStream.rangeClosed(start.x(), end.x())
                                            .sequential()
                                            .mapToObj(x -> new Coordinate(x, y, '!')))
                    .flatMap(Function.identity());
  }


  private static Coordinate detectEnd(final int[][] input) {
    return new Coordinate(input[0].length, input.length, -1);
  }

  record Coordinate(int x, int y, int value) {

  }

}