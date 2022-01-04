package com.mariusrubin.aoc.twentyfour;

import static java.util.function.Predicate.not;
import static java.util.stream.Collectors.joining;

import com.mariusrubin.aoc.twentyfour.MonadRun.RunType;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.ToIntFunction;
import java.util.stream.IntStream;

/**
 * @author Marius Rubin
 * @since 0.1.0
 */
class MonadProcessor {

  private static final int MIN_VALUE = 1;
  private static final int MAX_VALUE = 9;

  private final List<String> inputs;

  MonadProcessor(final List<String> inputs) {
    this.inputs = Collections.unmodifiableList(inputs);
  }

  long findLargestValidNumber() {
    return findValidNumberFromMonads(MonadRun::getInputMax);
  }

  long findSmallestValidNumber() {
    return findValidNumberFromMonads(MonadRun::getInputMin);
  }

  private long findValidNumberFromMonads(final ToIntFunction<MonadRun> intFunc) {
    final List<MonadRun> runs      = getMonadRuns();
    final var            extracted = extract(runs, intFunc);
    final var            validator = new ArithmeticLogicUnit(inputs);
    validator.run(extracted);
    if (validator.getZValue() != 0) {
      throw new IllegalStateException("Failed to find a valid MONAD number");
    }
    return Long.parseLong(extracted);
  }

  private List<MonadRun> getMonadRuns() {

    final var runs = MonadParser.parse(inputs);

    final var it = runs.listIterator();

    while (it.hasNext()) {
      final var pop = it.next();
      if (pop.getType() == RunType.POP) {

        final var push = IntStream.iterate(it.previousIndex() - 1, i -> i >= 0, i -> i - 1)
                                  .mapToObj(runs::get)
                                  .filter(not(MonadRun::isComplete))
                                  .findFirst()
                                  .orElseThrow();

        final var popRange = IntStream.rangeClosed(push.getOffset() + MIN_VALUE,
                                                   push.getOffset() + MAX_VALUE)
                                      .map(i -> i + pop.getOffset())
                                      .filter(MonadProcessor::isWithinAcceptableRange)
                                      .toArray();

        final var pushRange = IntStream.of(popRange)
                                       .map(i -> i - pop.getOffset() - push.getOffset())
                                       .toArray();

        pop.setInputMin(popRange[0]);
        pop.setInputMax(popRange[popRange.length - 1]);

        push.setInputMin(pushRange[0]);
        push.setInputMax(pushRange[pushRange.length - 1]);

        while (it.nextIndex() < runs.indexOf(pop)) {
          it.next();
        }
      }
    }

    return runs;

  }

  private static boolean isWithinAcceptableRange(final int value) {
    return value >= MIN_VALUE && value <= MAX_VALUE;
  }

  private static String extract(final Collection<MonadRun> runs,
                                final ToIntFunction<MonadRun> intFunc) {
    return runs.stream()
               .mapToInt(intFunc)
               .mapToObj(String::valueOf)
               .collect(joining());
  }


}
