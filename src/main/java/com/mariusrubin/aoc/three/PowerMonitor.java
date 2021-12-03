package com.mariusrubin.aoc.three;

import java.util.Collections;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.function.Function;
import java.util.stream.IntStream;

/**
 * @author Marius Rubin
 * @since 0.1.0
 */
public class PowerMonitor {

  private final List<Integer> input;
  private final int           maxBits;

  private int gammaRate      = -1;
  private int epsilonRate    = -1;
  private int oxygenRating   = -1;
  private int scrubberRating = -1;

  public PowerMonitor(final List<Integer> input, final int maxBits) {
    this.input = Collections.unmodifiableList(input);
    this.maxBits = maxBits;
  }

  private static int majorityMask(final MaskingSummaryStatistics stats) {
    final double midPoint = (double) stats.getCount() / 2;
    return IntStream.range(0, stats.getCollect().length)
                    .map(i -> (stats.getCollect()[i] >= midPoint ? 1 : 0) * (1 << i))
                    .sum();
  }

  private static int minorityMask(final MaskingSummaryStatistics stats) {
    final double midPoint = (double) stats.getCount() / 2;
    return IntStream.range(0, stats.getCollect().length)
                    .map(i -> (stats.getCollect()[i] < midPoint ? 1 : 0) * (1 << i))
                    .sum();
  }

  private void calculateRates() {
    if (gammaRate > -1) {
      return;
    }

    final MaskingSummaryStatistics stats = getStats(input);

    gammaRate = majorityMask(stats);
    epsilonRate = minorityMask(stats);

    oxygenRating = calculateOxygenRating();
    scrubberRating = calculateScrubberRating();

  }

  private MaskingSummaryStatistics getStats(final List<Integer> input) {
    return input.stream()
                .collect(() -> new MaskingSummaryStatistics(maxBits),
                         MaskingSummaryStatistics::accept,
                         MaskingSummaryStatistics::combine);
  }

  private int calculateOxygenRating() {
    return calculateRating(PowerMonitor::majorityMask);
  }

  private int calculateScrubberRating() {
    return calculateRating(PowerMonitor::minorityMask);
  }

  private int calculateRating(final Function<MaskingSummaryStatistics, Integer> ratingOperation) {

    var readings = input;

    for (int i = 1 << maxBits - 1; i > 0 && readings.size() > 1; i >>= 1) {

      final var stats    = getStats(readings);
      final var majority = ratingOperation.apply(stats);
      final var mask     = i & majority;
      final int filter   = i;

      readings = readings.stream()
                         .filter(r -> mask == 0 ? (r & filter) == 0 : (r & mask) == mask)
                         .toList();
    }

    return readings.get(0);

  }

  public int getGammaRate() {
    calculateRates();
    return gammaRate;
  }

  public int getEpsilonRate() {
    calculateRates();
    return epsilonRate;
  }

  public int getPowerConsumption() {
    calculateRates();
    return gammaRate * epsilonRate;
  }

  public int getOxygenRating() {
    calculateRates();
    return oxygenRating;
  }

  public int getScrubberRating() {
    calculateRates();
    return scrubberRating;
  }

  public int getLifeSupportRating() {
    calculateRates();
    return oxygenRating * scrubberRating;
  }

  private static class MaskingSummaryStatistics extends IntSummaryStatistics {

    private final int[] masks;
    private final int[] collect;

    MaskingSummaryStatistics(final int maxBits) {
      collect = new int[maxBits];
      masks = IntStream.range(0, maxBits)
                       .map(i -> 1 << i)
                       .toArray();
    }

    @Override
    public void accept(final int value) {
      super.accept(value);
      IntStream.range(0, masks.length)
               .forEach(i -> collect[i] += (masks[i] & value) / masks[i]);
    }

    @Override
    public void combine(final IntSummaryStatistics other) {
      super.combine(other);
      if (other instanceof MaskingSummaryStatistics masking) {
        IntStream.range(0, collect.length)
                 .forEach(i -> collect[i] += masking.getCollect()[i]);
      }
    }

    public int[] getCollect() {
      return collect.clone();
    }

  }


}
