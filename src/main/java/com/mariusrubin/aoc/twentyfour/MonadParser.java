package com.mariusrubin.aoc.twentyfour;

import com.mariusrubin.aoc.twentyfour.MonadRun.RunType;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author Marius Rubin
 * @since 0.1.0
 */
final class MonadParser {

  private static final Pattern X_OP = Pattern.compile("^add x ([-0-9]+)$");
  private static final Pattern Y_OP = Pattern.compile("^add y ([-0-9]+)$");

  private MonadParser() {
  }

  static List<MonadRun> parse(final List<String> inputs) {

    final var runs    = new ArrayList<MonadRun>();
    final var toParse = new ArrayList<String>();

    for (final String input : inputs) {

      if (OperationType.fromString(input) == OperationType.INP) {
        if (!toParse.isEmpty()) {
          runs.add(parseIndividual(toParse));
        }
        toParse.clear();
      }

      toParse.add(input);

    }

    runs.add(parseIndividual(toParse));

    return runs;

  }

  private static MonadRun parseIndividual(final List<String> inputBlock) {
    final var match = X_OP.matcher(inputBlock.get(5));
    if (!match.find()) {
      throw new IllegalArgumentException("Couldn't parse " + inputBlock);
    }
    final var     xOp = Integer.parseInt(match.group(1));
    final RunType runType;
    final int     offset;

    if (xOp > 10) {
      runType = RunType.PUSH;
      offset = findYOffset(inputBlock);
    } else {
      runType = RunType.POP;
      offset = xOp;
    }

    return new MonadRun(runType, offset);

  }


  private static int findYOffset(final List<String> inputBlock) {
    final var match = Y_OP.matcher(inputBlock.get(15));
    if (!match.find()) {
      throw new IllegalArgumentException("Couldn't parse " + inputBlock);
    }

    return Integer.parseInt(match.group(1));

  }


}
