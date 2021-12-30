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
public class MonadProcessor {

  private final List<String> inputs;


  public MonadProcessor(final List<String> inputs) {
    this.inputs = Collections.unmodifiableList(inputs);
  }


  public long findLargestValidNumber() {
    return findValidNumberFromMonads(MonadRun::getInputMax);
  }

  public long findSmallestValidNumber() {
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

    final var runs = new MonadParser().parse(inputs);

    final var it = runs.listIterator();

    while (it.hasNext()) {
      final var pop = it.next();
      if (pop.getType() == RunType.POP) {

        final var push = IntStream.iterate(it.previousIndex() - 1, i -> i >= 0, i -> i - 1)
                                  .mapToObj(runs::get)
                                  .filter(not(MonadRun::isComplete))
                                  .findFirst()
                                  .orElseThrow();

        final var popRange = IntStream.rangeClosed(push.getOffset() + 1, push.getOffset() + 9)
                                      .map(i -> i + pop.getOffset())
                                      .filter(i -> i > 0 && i <= 9)
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


  private static String extract(final Collection<MonadRun> runs,
                                final ToIntFunction<MonadRun> intFunc) {
    return runs.stream()
               .mapToInt(intFunc)
               .mapToObj(String::valueOf)
               .collect(joining());
  }


}
