package com.mariusrubin.aoc.fifteen;

import com.mariusrubin.aoc.util.FileLoader;
import java.util.stream.IntStream;

/**
 * @author Marius Rubin
 * @since 0.1.0
 */
class CavernScanner {

  private static final int RISK_THRESHOLD = 10;

  private final int range;

  CavernScanner() {
    this(1);
  }

  CavernScanner(final int range) {
    this.range = range;
  }

  int[][] scanCavern(final String filePath) {
    return scanCavern(new FileLoader(filePath).integerArray());
  }

  int[][] scanCavern(final int[][] source) {

    if (range < 2) {
      return source;
    }

    final var maxRow    = source.length;
    final var maxColumn = source[0].length;

    final var outputMaxRow    = maxRow * range;
    final var outputMaxColumn = maxColumn * range;
    final var output          = new int[outputMaxRow][outputMaxColumn];

    IntStream.range(0, maxRow)
             .forEach(rowNum -> projectAcross(source, maxColumn, output, rowNum));

    IntStream.range(0, maxRow)
             .forEach(rowNum -> projectDown(maxRow, outputMaxColumn, output, rowNum));

    return output;
  }

  private void projectDown(final int maxRow,
                           final int outputMaxColumn,
                           final int[][] output,
                           final int rowNum) {

    IntStream.range(0, outputMaxColumn)
             .forEach(columnNum -> projectDown(maxRow, output, rowNum, columnNum));

  }

  private void projectDown(final int maxRow,
                           final int[][] output,
                           final int rowNum,
                           final int columnNum) {

    IntStream.range(0, range)
             .forEach(step -> {
               final var targetRow = rowNum + step * maxRow;
               output[targetRow][columnNum] = calculateRisk(output[rowNum][columnNum], step);
             });

  }

  private void projectAcross(final int[][] source,
                             final int maxColumn,
                             final int[][] output,
                             final int rowNum) {

    IntStream.range(0, maxColumn)
             .forEach(columnNum -> projectAcross(source, maxColumn, output, rowNum, columnNum));

  }

  private void projectAcross(final int[][] source,
                             final int maxColumn,
                             final int[][] output,
                             final int rowNum,
                             final int columnNum) {

    IntStream.range(0, range)
             .forEach(step -> {
               final var targetColumn = columnNum + step * maxColumn;
               output[rowNum][targetColumn] = calculateRisk(source[rowNum][columnNum], step);
             });
  }

  private static int calculateRisk(final int sourceValue, final int step) {
    final var risk = sourceValue + step;
    return risk < RISK_THRESHOLD ? risk : Math.abs(RISK_THRESHOLD - 1 - risk);
  }

}
