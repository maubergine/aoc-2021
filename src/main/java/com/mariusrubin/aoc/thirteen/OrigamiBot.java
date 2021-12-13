package com.mariusrubin.aoc.thirteen;

import static java.lang.Integer.parseInt;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * @author Marius Rubin
 * @since 0.1.0
 */
public class OrigamiBot {

  private static final Pattern COORD_SPLIT = Pattern.compile("^([0-9]+),([0-9]+)$");
  private static final Pattern FOLD_SPLIT  = Pattern.compile("^fold along ([x|y])=([0-9]+)$");

  private       String[][] dots;
  private final List<Fold> folds;

  public OrigamiBot(final List<String> instructions) {
    dots = createDots(instructions);
    folds = createFolds(instructions);
  }

  private List<Fold> createFolds(final List<String> instructions) {

    return instructions.stream()
                       .filter(FOLD_SPLIT.asMatchPredicate())
                       .map(FOLD_SPLIT::matcher)
                       .peek(Matcher::find)
                       .map(this::toFold)
                       .toList();


  }

  private Fold toFold(final Matcher match) {
    final var value = Integer.parseInt(match.group(2));
    return switch (match.group(1)) {
      case "x" -> new Fold(value, -1);
      case "y" -> new Fold(-1, value);
      default -> throw new IllegalArgumentException("Could not find dimension matching "
                                                    + match.group(1));
    };
  }

  private String[][] createDots(final List<String> instructions) {
    final var coords = instructions.stream()
                                   .takeWhile(COORD_SPLIT.asMatchPredicate())
                                   .map(COORD_SPLIT::matcher)
                                   .peek(Matcher::find)
                                   .map(match -> new int[]{parseInt(match.group(1)),
                                                           parseInt(match.group(2))})
                                   .toList();

    final var maxX = coords.stream()
                           .mapToInt(array -> array[0])
                           .max()
                           .orElseThrow();

    final var maxY = coords.stream()
                           .mapToInt(array -> array[1])
                           .max()
                           .orElseThrow();

    final var output = new String[maxY + 1][maxX + 1];

    coords.forEach(array -> output[array[1]][array[0]] = "#");

    return output;

  }

  public int countDotsAfterFolding(final int howMany) {
    final var counts = folds.stream()
                            .limit(howMany)
                            .map(this::doFold)
                            .mapToInt(this::countVisible)
                            .toArray();

    return counts.length > 0 ? counts[counts.length - 1] : 0;
  }

  public Stream<String> fold() {
    return Stream.concat(Stream.of(new String[][][]{dots}), folds.stream().map(this::doFold))
                 .map(this::format);
  }

  private int countVisible(final String[][] paper) {
    return Arrays.stream(paper)
                 .flatMapToInt(array -> Arrays.stream(array).mapToInt(s -> s == null ? 0 : 1))
                 .sum();
  }

  private String format(final String[][] result) {
    return Arrays.stream(result)
                 .map(line -> Arrays.stream(line)
                                    .map(s -> s == null ? "." : s)
                                    .collect(Collectors.joining()))
                 .collect(Collectors.joining(System.lineSeparator()));
  }

  private String[][] doFold(final Fold fold) {
    return fold.x() > -1
           ? doVerticalFold(fold)
           : doHorizontalFold(fold);
  }

  private String[][] doHorizontalFold(final Fold fold) {

    final var output = new String[dots.length - Math.abs(fold.y() - dots.length)][dots[0].length];

    IntStream.range(0, fold.y())
             .forEach(rowNum -> {
               final var row = dots[rowNum];
               IntStream.range(0, row.length)
                        .forEach(colNum -> output[rowNum][colNum] = row[colNum]);
             });

    IntStream.range(fold.y() + 1, dots.length)
             .forEach(rowNum -> {
               final var row = dots[rowNum];
               IntStream.range(0, row.length)
                        .forEach(colNum -> {
                          if (row[colNum] == null) {
                            return;
                          }
                          output[fold.y() - (rowNum - fold.y())][colNum] = row[colNum];
                        });
             });

    dots = output;

    return output;
  }

  private String[][] doVerticalFold(final Fold fold) {
    final var output = new String[dots.length][dots[0].length - Math.abs(fold.x()
                                                                         - dots[0].length)];

    IntStream.range(0, dots.length)
             .forEach(rowNum -> {
               final var row = dots[rowNum];
               IntStream.range(0, fold.x())
                        .forEach(colNum -> output[rowNum][colNum] = row[colNum]);
               IntStream.range(fold.x() + 1, row.length)
                        .forEach(colNum -> {
                          if (row[colNum] == null) {
                            return;
                          }
                          output[rowNum][fold.x() - (colNum - fold.x())] = row[colNum];
                        });

             });

    dots = output;

    return output;
  }

  private record Fold(int x, int y) {

  }

}
