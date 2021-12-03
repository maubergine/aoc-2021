package com.mariusrubin.aoc.two;

import static java.lang.System.lineSeparator;

import com.mariusrubin.aoc.util.Executor;
import com.mariusrubin.aoc.util.FileLoader;
import java.util.concurrent.Callable;

/**
 * @author Marius Rubin
 * @since 0.1.0
 */
public class SecondDecember implements Callable<Integer> {

  private static final String INSTRUCTIONS = "src/main/resources/com/mariusrubin/aoc/SecondDecember.txt";

  public static void main(final String... args) {
    Executor.doRun(new SecondDecember());
  }

  @Override
  public Integer call() {

    final var instructions = new FileLoader(INSTRUCTIONS).allLines();

    final Submarine simple = new SimpleSubmarine();
    instructions.forEach(simple::followInstruction);

    System.out.printf("Simple submarine final position: %d%s",
                      simple.getFinalPosition(),
                      lineSeparator());

    final Submarine complex = new ComplexSubmarine();
    instructions.forEach(complex::followInstruction);

    System.out.printf("Complex submarine final position: %d%s",
                      complex.getFinalPosition(),
                      lineSeparator());

    return Executor.SUCCESS;
  }
}
