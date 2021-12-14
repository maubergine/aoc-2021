package com.mariusrubin.aoc.fourteen;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toMap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author Marius Rubin
 * @since 0.1.0
 */
public class Polymeriser {

  private static final Pattern PAIR_RULE = Pattern.compile("^([A-Z]{2}) -> ([A-Z])$");

  private final String                 template;
  private final Map<String, Character> rules;

  public Polymeriser(final List<String> inputs) {
    template = inputs.get(0);
    rules = inputs.stream()
                  .filter(PAIR_RULE.asMatchPredicate())
                  .map(PAIR_RULE::matcher)
                  .peek(Matcher::find)
                  .collect(toMap(match -> match.group(1), match -> match.group(2).charAt(0)));
  }

  public long calculateOutput(final String input, final int steps) {
    final var pairs = initPairs(input);
    final var counts = input.chars()
                            .mapToObj(i -> (char) i)
                            .collect(groupingBy(identity(), HashMap::new, counting()));

    IntStream.range(0, steps)
             .sequential()
             .mapToObj(i -> new HashMap<>(pairs))
             .forEach(scroll -> doStep(pairs, counts, scroll));

    return getMaxLessMin(counts);

  }

  private void doStep(final Map<String, Long> pairs,
                      final Map<Character, Long> counts,
                      final Map<String, Long> scroll) {

    scroll.entrySet()
          .stream()
          .filter(e -> e.getValue() > 0L)
          .forEach(e -> {
            final var insert    = rules.get(e.getKey());
            final var inserted  = new StringBuilder(e.getKey()).insert(1, insert).toString();
            final var leftPair  = inserted.substring(0, 2);
            final var rightPair = inserted.substring(1, 3);
            pairs.put(e.getKey(), pairs.getOrDefault(e.getKey(), 0L) - e.getValue());
            pairs.put(leftPair, pairs.getOrDefault(leftPair, 0L) + e.getValue());
            pairs.put(rightPair, pairs.getOrDefault(rightPair, 0L) + e.getValue());
            counts.put(insert, counts.getOrDefault(insert, 0L) + e.getValue());
          });
  }

  public long calculateOutput(final int steps) {
    return calculateOutput(template, steps);
  }

  private Map<String, Long> initPairs(final String input) {
    return IntStream.range(0, input.length() - 1)
                    .mapToObj(i -> new char[]{template.charAt(i), template.charAt(i + 1)})
                    .map(String::new)
                    .collect(Collectors.toMap(identity(), s -> 1L, Long::sum));
  }


  private long getMaxLessMin(final Map<Character, Long> counts) {
    final var max = counts.values().stream().mapToLong(Long::longValue).max().orElseThrow();
    final var min = counts.values().stream().mapToLong(Long::longValue).min().orElseThrow();
    return max - min;
  }

}
