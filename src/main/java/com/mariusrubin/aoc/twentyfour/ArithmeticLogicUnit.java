package com.mariusrubin.aoc.twentyfour;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

/**
 * @author Marius Rubin
 * @since 0.1.0
 */
class ArithmeticLogicUnit {

  private static final int  MAX_NUM_LENGTH = 14;
  private static final char Z_REGISTER     = 'z';

  private       Map<Character, Long> values;
  private final List<ALUOperation>   toApply;
  private final long                 initValue;


  ArithmeticLogicUnit(final List<String> inputs) {
    toApply = toOperations(inputs);
    initValue = 0L;
  }

  void run(final String input) {

    if (input.length() > MAX_NUM_LENGTH) {
      throw new IllegalArgumentException("Cannot work with numbers > 14 digits");
    }
    final var asArray = input.chars()
                             .mapToObj(i -> (char) i)
                             .map(String::valueOf)
                             .mapToLong(Long::parseLong)
                             .toArray();

    run(asArray);

  }

  void run(final long... inputs) {
    final var inputQueue = LongStream.of(inputs)
                                     .boxed()
                                     .collect(Collectors.toCollection(ArrayDeque::new));
    values = reset();
    toApply.stream()
           .sequential()
           .peek(op -> populateValue(inputQueue, op))
           .forEach(op -> op.apply(values));
  }

  Map<Character, Long> reset() {
    return LongStream.of(initValue)
                     .boxed()
                     .collect(Collectors.toMap(l -> Z_REGISTER,
                                               Function.identity(),
                                               (l1, l2) -> l2,
                                               HashMap::new));
  }

  long getZValue() {
    return values.get(Z_REGISTER);
  }

  private static void populateValue(final Deque<Long> inputQueue, final ALUOperation op) {
    if (op instanceof InputOperation in) {
      final var value = inputQueue.poll();
      if (value == null) {
        throw new IllegalArgumentException("No value available to use in " + inputQueue);
      }
      in.setValue(value);
    }
  }

  private static List<ALUOperation> toOperations(final List<String> inputs) {
    return inputs.stream()
                 .sequential()
                 .map(in -> {
                   switch (OperationType.fromString(in)) {
                     case INP -> {
                       return new InputOperation(in);
                     }
                     case ADD -> {
                       return new DefaultALUOperation(Math::addExact, in);
                     }
                     case MUL -> {
                       return new DefaultALUOperation(Math::multiplyExact, in);
                     }
                     case DIV -> {
                       return new DefaultALUOperation((l1, l2) -> l1 / l2, in);
                     }
                     case MOD -> {
                       return new DefaultALUOperation((l1, l2) -> l1 % l2, in);
                     }
                     case EQL -> {
                       return new DefaultALUOperation((l1, l2) -> l1.equals(l2) ? 1L : 0L, in);
                     }
                   }
                   throw new IllegalStateException("Unreachable");
                 })
                 .toList();
  }

}
