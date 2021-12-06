package com.mariusrubin.aoc.six;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

/**
 * @author Marius Rubin
 * @since 0.1.0
 */
public class LanternfishSchool {

  private static final long SPAWN_KEY = 8L;
  private static final long RESET_KEY = 6L;

  private final Map<Long, Long> fishBuckets;

  public LanternfishSchool(final List<Integer> timers) {
    fishBuckets = timers.stream()
                        .collect(Collectors.toMap(i -> (long) i,
                                                  i -> 1L,
                                                  Long::sum,
                                                  ConcurrentHashMap::new));

    LongStream.rangeClosed(0, 8).forEach(l -> fishBuckets.putIfAbsent(l, 0L));
  }

  public void passDays(final int days) {
    IntStream.range(0, days)
             .sequential()
             .mapToObj(i -> Map.copyOf(fishBuckets)).forEach(temp -> {
               final var toSpawn = fishBuckets.get(0L);
               temp.entrySet()
                   .stream()
                   .filter(e -> e.getKey() > 0L)
                   .forEach(e -> fishBuckets.put(e.getKey() - 1, e.getValue()));
               fishBuckets.put(SPAWN_KEY, toSpawn);
               fishBuckets.put(RESET_KEY, fishBuckets.get(RESET_KEY) + toSpawn);
             });
  }


  public long lanternFishCount() {
    return fishBuckets.values().stream().mapToLong(l -> l).sum();
  }

}
