package com.mariusrubin.aoc.ten;

import static java.util.Spliterator.IMMUTABLE;
import static java.util.Spliterator.NONNULL;
import static java.util.Spliterator.ORDERED;
import static java.util.Spliterators.spliterator;
import static java.util.stream.Collectors.toMap;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * @author Marius Rubin
 * @since 0.1.0
 */
class NavigationParser {

  private static final int SCORE_MULTIPLIER = 5;

  private static final Map<Character, Character> CHAR_MAP =
      Stream.of("()", "[]", "{}", "<>")
            .map(String::toCharArray)
            .collect(toMap(chars -> chars[0], chars -> chars[1]));

  private static final Map<Character, Integer> ILLEGAL_SCORE_MAP =
      Stream.of(new SimpleEntry<>(')', 3),
                new SimpleEntry<>(']', 57),
                new SimpleEntry<>('}', 1197),
                new SimpleEntry<>('>', 25137))
            .collect(toMap(Entry::getKey, Entry::getValue));

  private static final Map<Character, Integer> COMPLETION_SCORE_MAP =
      Stream.of(new SimpleEntry<>(')', 1),
                new SimpleEntry<>(']', 2),
                new SimpleEntry<>('}', 3),
                new SimpleEntry<>('>', 4))
            .collect(toMap(Entry::getKey, Entry::getValue));

  private final List<String> inputs;

  NavigationParser(final List<String> inputs) {
    this.inputs = Collections.unmodifiableList(inputs);
  }

  List<IllegalCharacter> findIllegalCharacters() {
    return IntStream.range(0, inputs.size())
                    .mapToObj(this::illegalOrEmpty)
                    .flatMap(Function.identity())
                    .toList();
  }

  int calculateIllegalityScore() {
    return findIllegalCharacters().stream()
                                  .map(IllegalCharacter::actual)
                                  .mapToInt(ILLEGAL_SCORE_MAP::get)
                                  .sum();
  }

  List<String> getCompletedLines() {
    return getCompletions().map(Completion::result).toList();
  }

  long calculateCompletionScore() {
    final var results = getCompletions().map(Completion::completion)
                                        .mapToLong(NavigationParser::calculateScore)
                                        .sorted()
                                        .toArray();

    return results[(results.length - 1) / 2];
  }

  private Stream<Completion> getCompletions() {
    return IntStream.range(0, inputs.size())
                    .mapToObj(this::completeOrEmpty)
                    .flatMap(Function.identity());
  }

  private Stream<Completion> completeOrEmpty(final int lineNo) {

    final var isLegal = illegalOrEmpty(lineNo).findAny().isEmpty();

    if (isLegal) {
      return complete(lineNo);
    }

    return Stream.empty();

  }

  private Stream<Completion> complete(final int lineNo) {

    final var terminators = new ArrayDeque<Character>();
    final var input       = inputs.get(lineNo);

    input.chars()
         .sequential()
         .mapToObj(i -> (char) i)
         .forEach(c -> {
           if (isOpeningCharacter(c)) {
             terminators.add(CHAR_MAP.get(c));
           } else {
             terminators.removeLast();
           }
         });

    final var completion = toCompletionString(terminators);

    return Stream.of(new Completion(input.concat(completion), completion));

  }

  private Stream<IllegalCharacter> illegalOrEmpty(final int lineNo) {

    final var terminators = new ArrayDeque<Character>();
    final var line        = inputs.get(lineNo);
    return IntStream.range(0, line.length())
                    .sequential()
                    .mapToObj(i -> determineIllegality(lineNo, terminators, line, i))
                    .flatMap(Function.identity());

  }

  private static long calculateScore(final String completion) {
    return completion.chars()
                     .sequential()
                     .mapToObj(i -> (char) i)
                     .mapToLong(COMPLETION_SCORE_MAP::get)
                     .reduce(0, (i1, i2) -> i1 * SCORE_MULTIPLIER + i2);
  }

  private static String toCompletionString(final Deque<Character> terminators) {
    return StreamSupport.stream(spliterator(terminators.descendingIterator(),
                                            terminators.size(),
                                            NONNULL | IMMUTABLE | ORDERED),
                                false)
                        .map(String::valueOf)
                        .collect(Collectors.joining());
  }

  private static Stream<IllegalCharacter> determineIllegality(final int lineNo,
                                                              final Deque<Character> terminators,
                                                              final String line,
                                                              final int position) {

    final var actual = line.charAt(position);
    if (isOpeningCharacter(actual)) {
      terminators.add(CHAR_MAP.get(actual));
      return Stream.empty();
    }

    final var expected = terminators.removeLast();

    if (!expected.equals(actual)) {
      return Stream.of(new IllegalCharacter(expected, actual, position, lineNo));
    }

    return Stream.empty();

  }


  private static boolean isOpeningCharacter(final char character) {
    return CHAR_MAP.containsKey(character);
  }

  record Completion(String result, String completion) {

  }

  record IllegalCharacter(char expected, char actual, int position, int line) {

  }

}
