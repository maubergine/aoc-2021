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
public class ArithmeticLogicUnit {

  private       Map<Character, Long> values;
  private       List<ALUOperation>   toApply;
  private final long                 initValue;


  public ArithmeticLogicUnit(final ArithmeticLogicUnit unit, final long initValue) {
    toApply = unit.toApply.stream().sequential().map(ALUOperation::copy).toList();
    this.initValue = initValue;
  }

  public ArithmeticLogicUnit(final ArithmeticLogicUnit unit) {
    this(unit, 0L);
  }

  public ArithmeticLogicUnit(final List<String> inputs) {
    toApply = toOperations(inputs);
    initValue = 0L;
  }

  public void run(final String input) {

    if (input.length() > 14) {
      throw new IllegalArgumentException("Cannot work with numbers > 14 digits");
    }
    final var asArray = input.chars()
                             .mapToObj(i -> (char) i)
                             .map(String::valueOf)
                             .mapToLong(Long::parseLong)
                             .toArray();

    run(asArray);

  }

  public void run(final long... inputs) {
    final var inputQueue = LongStream.of(inputs)
                                     .boxed()
                                     .collect(Collectors.toCollection(ArrayDeque::new));
    values = reset();
    toApply.stream()
           .sequential()
           .peek(op -> populateValue(inputQueue, op))
           .forEach(op -> op.apply(values));
  }

  private void populateValue(final Deque<Long> inputQueue, final ALUOperation op) {
    if (op instanceof InputOperation in) {
      final var value = inputQueue.poll();
      if (value == null) {
        throw new IllegalArgumentException("No value available to use in " + inputQueue);
      }
      in.setValue(value);
    }
  }

  public Map<Character, Long> reset() {
    return LongStream.of(initValue)
                     .boxed()
                     .collect(Collectors.toMap(l -> 'z',
                                               Function.identity(),
                                               (l1, l2) -> l2,
                                               HashMap::new));
  }

  public long getZValue() {
    return values.get('z');
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
