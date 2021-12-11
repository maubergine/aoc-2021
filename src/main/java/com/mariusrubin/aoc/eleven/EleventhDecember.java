package com.mariusrubin.aoc.eleven;

import static java.lang.System.lineSeparator;

import com.mariusrubin.aoc.util.Executor;
import com.mariusrubin.aoc.util.FileLoader;
import java.util.concurrent.Callable;

/**
 * @author Marius Rubin
 * @since 0.1.0
 */
public class EleventhDecember implements Callable<Integer> {

  private static final String SWARM = "src/main/resources/com/mariusrubin/aoc/EleventhDecember.txt";

  public static void main(final String... args) {
    Executor.doRun(new EleventhDecember());
  }

  @Override
  public Integer call() {

    final var data = new FileLoader(SWARM).integerArray();

    final var swarm = new OctopusSwarm(data);

    System.out.printf("Flash count: %d%s", swarm.countFlashes(100), lineSeparator());

    final var flashCountSwarm = new OctopusSwarm(data);

    System.out.printf("First full flash step: %d%s",
                      flashCountSwarm.findFirstFlash(),
                      lineSeparator());

    return Executor.SUCCESS;

  }

}