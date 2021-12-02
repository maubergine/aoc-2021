package com.mariusrubin.aoc.util;

import java.util.concurrent.Callable;

/**
 * @author Marius Rubin
 * @since 0.1.0
 */
public class Executor {

  public static final  int SUCCESS = 0;
  private static final int FAILURE = 1;

  public static void doRun(final Callable<Integer> toRun) {
    try {
      System.exit(toRun.call());
    } catch (final Exception error) {
      error.printStackTrace();
      System.exit(FAILURE);
    }
  }

}
