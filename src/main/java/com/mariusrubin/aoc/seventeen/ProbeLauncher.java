package com.mariusrubin.aoc.seventeen;

import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * @author Marius Rubin
 * @since 0.1.0
 */
public class ProbeLauncher {


  private static final Pattern PATTERN = Pattern.compile(
      "^target area: x=([0-9]+)\\.\\.([0-9]+), y=(-[0-9]+)\\.\\.(-[0-9]+)$");

  private final int xMin;
  private final int xMax;
  private final int yMin;
  private final int yMax;

  public ProbeLauncher(final String input) {
    final var match = PATTERN.matcher(input);
    if (match.find()) {
      xMin = Integer.parseInt(match.group(1));
      xMax = Integer.parseInt(match.group(2));
      yMin = Integer.parseInt(match.group(3));
      yMax = Integer.parseInt(match.group(4));
    } else {
      throw new IllegalArgumentException("Input " + " didn't match expected pattern");
    }
  }

  public List<Step> calculateStepsForTrajectory(final int forwardVelocity,
                                                final int verticalVelocity,
                                                final int howManySteps) {

    return IntStream.rangeClosed(1, howManySteps)
                    .mapToObj(i -> calculateStep(forwardVelocity, verticalVelocity, i))
                    .toList();

  }

  public InitialVelocity findHighestViableVelocity() {
    return findViableVelocities().stream()
                                 .max(Comparator.comparing(InitialVelocity::verticalVelocity))
                                 .orElseThrow();
  }

  public int findHighestYPosition() {
    return findViableFiringSolutions().stream()
                                      .mapToInt(ProbeLauncher::getMaxHeight)
                                      .max()
                                      .orElseThrow();
  }

  private static int getMaxHeight(final FiringSolution solution) {
    return solution.steps()
                   .stream()
                   .mapToInt(Step::y)
                   .max()
                   .orElseThrow();
  }


  public List<FiringSolution> findViableFiringSolutions() {

    final var viableXVelocities = findViableXVelocities();
    final var viableYVelocities = findViableYVelocities();

    final var initialVelocities = combineVelocities(viableXVelocities, viableYVelocities);

    return initialVelocities.flatMap(this::toViableFiringSolutions).toList();

  }

  public List<InitialVelocity> findViableVelocities() {
    return findViableFiringSolutions().stream()
                                      .map(FiringSolution::initialVelocity)
                                      .distinct()
                                      .toList();
  }

  public int countViableVelocities() {
    return findViableVelocities().size();
  }

  private Stream<FiringSolution> toViableFiringSolutions(final InitialVelocity initial) {

    final var steps = calculateStepsForTrajectory(initial.forwardVelocity(),
                                                  initial.verticalVelocity(),
                                                  Math.abs(yMin) - yMin);

    if (steps.stream().anyMatch(this::isViableStep)) {
      return Stream.of(new FiringSolution(initial, steps));
    }

    return Stream.empty();

  }

  private List<Integer> findViableXVelocities() {
    return IntStream.rangeClosed(0, xMax)
                    .flatMap(this::viableXSteps)
                    .boxed()
                    .toList();

  }

  private IntStream viableXSteps(final int initialVelocity) {
    return IntStream.rangeClosed(0, xMax)
                    .map(j -> calculateXForStep(initialVelocity, j))
                    .filter(this::isViableX)
                    .count() > 0 ? IntStream.of(initialVelocity) : IntStream.empty();
  }

  private List<Integer> findViableYVelocities() {
    return IntStream.range(yMin, Math.abs(yMin))
                    .flatMap(this::viableYSteps)
                    .boxed()
                    .toList();
  }

  private IntStream viableYSteps(final int initialVelocity) {
    return IntStream.rangeClosed(0, Math.abs(yMin) * 2)
                    .map(j -> calculateYForStep(initialVelocity, j))
                    .filter(this::isViableY)
                    .count() > 0 ? IntStream.of(initialVelocity) : IntStream.empty();
  }

  private boolean isViableStep(final Step step) {
    return isViableX(step.x()) && isViableY(step.y());
  }

  private boolean isViableX(final int x) {
    return x >= xMin && x <= xMax;
  }

  private boolean isViableY(final int y) {
    return y >= yMin && y <= yMax;
  }

  private static Step calculateStep(final int initialForward,
                                    final int initialVertical,
                                    final int step) {

    final var x = calculateXForStep(initialForward, step);

    final var y = calculateYForStep(initialVertical, step);

    return new Step(x, y);

  }

  private static int calculateXForStep(final int initialForward, final int step) {
    return IntStream.range(0, step)
                    .map(i -> Math.max(initialForward - i, 0))
                    .sum();
  }

  private static int calculateYForStep(final int initialVertical, final int step) {
    return IntStream.range(0, step)
                    .map(i -> initialVertical - i)
                    .sum();
  }

  private static Stream<InitialVelocity> combineVelocities(final List<Integer> xVelocities,
                                                           final List<Integer> yVelocities) {

    return xVelocities.stream()
                      .flatMap(x -> yVelocities.stream().map(y -> new InitialVelocity(x, y)));

  }

  record Step(int x, int y) {

  }

  record InitialVelocity(int forwardVelocity, int verticalVelocity) {

  }

  record FiringSolution(InitialVelocity initialVelocity, List<Step> steps) {

  }

}
