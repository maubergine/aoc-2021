package com.mariusrubin.aoc.twentythree;

import static com.mariusrubin.aoc.twentythree.AmphipodType.fromLetter;
import static com.mariusrubin.aoc.twentythree.AmphipodType.isRoomPosition;
import static java.lang.System.lineSeparator;
import static java.util.function.Predicate.not;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.summingLong;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * @author Marius Rubin
 * @since 0.1.0
 */
public class AmphipodMover {

  private static final Pattern AMPHIPOD = Pattern.compile("#([A-D])#([A-D])#([A-D])#([A-D])#");

  private final List<Amphipod>          starting = new ArrayList<>();
  private       List<Amphipod>          current;
  private       Set<Integer>            done     = new LinkedHashSet<>();
  private final Map<AmphipodType, Long> costThresholds;
  private final int                     bottomY;

  public AmphipodMover(final List<String> input) {
    final var rows = input.stream().filter(AMPHIPOD.asPredicate()).toList();
    bottomY = rows.size();
    IntStream.range(0, rows.size())
             .forEach(i -> populate(rows, i));
    current = new ArrayList<>(starting);
    costThresholds = buildThresholds();
  }

  public long calculateLowestEnergy() {

    final var availableMoves = new PriorityQueue<List<Move>>(new MoveCostComparator());
    availableMoves.addAll(availableMoves(Collections.emptyList()));

    List<Move> cheapest = null;

    while (!availableMoves.isEmpty() && cheapest == null) {

      //Reset the status then execute the path until the next set of moves becomes available
      current = reset();
      final var next = availableMoves.poll();
      doMoves(next);
      final var afterExec = availableMoves(next);
      if (afterExec.isEmpty()) {
        //Either all Amphipods are now happy, or this is a dead end.
        if (allAmphipodsInCorrectRoom()) {
          cheapest = next;
        }
      } else {
        availableMoves.addAll(afterExec);
      }

    }

    return calculateCost(cheapest);
  }

  private Map<AmphipodType, Long> buildThresholds() {
    return simpleSolve().collect(groupingBy(Move::type,
                                            summingLong(AmphipodMover::calculateCost)));
  }

  private Stream<Move> simpleSolve() {
    return current.stream().map(this::directToTarget);
  }

  private Move directToTarget(final Amphipod amphipod) {

    return new Move(amphipod.getPosition(),
                    amphipod.inFinalPosition()
                    ? amphipod.getPosition()
                    : new Coordinate(amphipod.getTargetRoom(), bottomY),
                    amphipod.getType());

  }

  private static long findCheapest(final Collection<? extends Collection<Move>> moves) {
    return moves.stream()
                .mapToLong(AmphipodMover::calculateCost)
                .min()
                .orElseThrow();
  }

  private static long calculateCost(final Collection<Move> moves) {
    return moves.stream()
                .mapToLong(AmphipodMover::calculateCost)
                .sum();
  }

  private static long calculateCost(final Move move) {
    return calculateStepCount(move) * move.type().getEnergy();
  }

  private static long calculateStepCount(final Move move) {
    return Math.abs(move.to().x() - move.from().x()) + move.from().y() + move.to().y();
  }

  private List<Amphipod> reset() {
    return starting.stream()
                   .map(Amphipod::new)
                   .toList();
  }

  private boolean allAmphipodsInCorrectRoom() {
    return current.stream()
                  .allMatch(Amphipod::inCorrectRoom);
  }

  private void doMoves(final List<Move> moves) {
    moves.forEach(move -> {
      final var amphipod = amphipodAtPosition(move.from.x(), move.from.y()).stream()
                                                                           .findAny()
                                                                           .orElseThrow();
      amphipod.setPosition(move.to());

    });
    done.add(moves.hashCode());
  }

  private List<List<Move>> availableMoves(final List<Move> previouslyExecuted) {
    final var availableMoves = current.stream()
                                      .filter(not(Amphipod::inFinalPosition))
                                      .filter(not(this::isStackedInRoomCorrectly))
                                      .flatMap(a -> {
                                        final var direct = moveDirectToRoom(a);
                                        if (direct.isPresent()) {
                                          return direct.stream();
                                        }
                                        return moveToHallway(a);

                                      })
                                      .toList();

    final var roomMoves = availableMoves.stream()
                                        .filter(move -> move.to().y() > 0)
                                        .collect(Collectors.toCollection(LinkedList::new));

    //Prioritise completing rooms over other moves
    if (roomMoves.isEmpty()) {
      return combineAndFilter(previouslyExecuted, availableMoves);
    } else {
      return combineAndFilter(previouslyExecuted, roomMoves);
    }
  }

  private List<List<Move>> combineAndFilter(final List<Move> previous,
                                            final List<Move> additional) {
    return additional.stream()
                     .filter(this::withinCostThreshold)
                     .map(move -> combine(previous, move))
                     .filter(updated -> !done.contains(updated.hashCode()))
                     .collect(Collectors.toCollection(LinkedList::new));
  }

  private boolean withinCostThreshold(final Move move) {
    return calculateCost(move) <= costThresholds.get(move.type());
  }

  private static List<Move> combine(final List<Move> previous, final Move toAdd) {
    final var withAdditional = new ArrayList<>(previous);
    withAdditional.add(toAdd);
    return withAdditional;
  }

  private boolean isStackedInRoomCorrectly(final Amphipod toMove) {
    if (!toMove.inCorrectRoom() || toMove.isBottom()) {
      return false;
    }

    final var below = IntStream.rangeClosed(toMove.getY() + 1, bottomY)
                               .mapToObj(i -> amphipodAtPosition(toMove.getTargetRoom(),
                                                                 i).stream())
                               .flatMap(Function.identity())
                               .toList();

    return !below.isEmpty() && below.stream().allMatch(Amphipod::inCorrectRoom);
  }

  private Stream<Move> moveToHallway(final Amphipod toMove) {
    //Moves within the hallway are forbidden
    if (toMove.inHallway()) {
      return Stream.empty();
    }
    //We have already handled scenarios where the amphipods are where they should be
    //so we only need to deal with an amphipod where it is blocking another
    //or is itself in the wrong room.

    //If it is blocked in do nothing
    if (!toMove.isTop()) {
      final var blockedBy = IntStream.range(1, toMove.getY())
                                     .mapToObj(i -> amphipodAtPosition(toMove.getX(),
                                                                       i))
                                     .flatMap(Optional::stream)
                                     .findAny();
      if (blockedBy.isPresent()) {
        return Stream.empty();
      }
    }

    //Otherwise expose the list of hallway positions that are viable.
    return IntStream.rangeClosed(0, 10)
                    .filter(i -> !isRoomPosition(i))
                    .filter(i -> canReachHallwayPosition(i, toMove))
                    .mapToObj(i -> new Move(toMove.getPosition(),
                                            new Coordinate(i, 0),
                                            toMove.getType()));


  }

  private Optional<Move> moveDirectToRoom(final Amphipod toMove) {
    //Amphipods will never move from the hallway into a room unless that room is their destination
    // room and that room contains no amphipods which do not also have that room as their own
    // destination.

    //First check if the room is available
    final var inRoom = amphipodsInDestinationRoom(toMove.getType());

    //The room is full - can't move there.
    if (inRoom.size() == bottomY) {
      return Optional.empty();
    }

    final int destY;

    if (inRoom.isEmpty()) {
      destY = bottomY;
    } else {
      //If any of them are in the wrong room we can't go there
      if (inRoom.stream().anyMatch(not(Amphipod::inCorrectRoom))) {
        return Optional.empty();
      }
      final var highestRoomPos = inRoom.stream()
                                       .map(Amphipod::getPosition)
                                       .mapToInt(Coordinate::y)
                                       .min()
                                       .orElseThrow();
      destY = highestRoomPos - 1;
    }

    //Check whether the route to the hallway is free
    if (!toMove.isTop()) {
      final var blockedBy = IntStream.range(1, toMove.getY())
                                     .mapToObj(i -> amphipodAtPosition(toMove.getX(),
                                                                       i))
                                     .flatMap(Optional::stream)
                                     .findAny();
      if (blockedBy.isPresent()) {
        return Optional.empty();
      }
    }

    final var destX = toMove.getTargetRoom();

    //Check whether the hallway is unobstructed
    final var startX = Math.min(toMove.getX(), destX);
    final var endX   = Math.max(toMove.getX(), destX);

    return inHallwayBetween(startX, endX).filter(not(toMove::equals))
                                         .toList()
                                         .isEmpty()
           ? Optional.of(new Move(toMove.getPosition(),
                                  new Coordinate(destX, destY),
                                  toMove.getType()))
           : Optional.empty();

  }


  private boolean canReachHallwayPosition(final int x, final Amphipod toMove) {
    return !amphipodsAreInHallwayBetween(Math.min(x, toMove.getX()),
                                         Math.max(x, toMove.getX()));
  }

  private boolean amphipodsAreInHallwayBetween(final int startX, final int endX) {
    return inHallwayBetween(startX, endX).findAny().isPresent();
  }

  private Stream<Amphipod> inHallwayBetween(final int startX, final int endX) {
    return amphipodsInHallway().stream()
                               .filter(a -> isBetween(startX, endX, a));
  }

  private static boolean isBetween(final int startX, final int endX, final Amphipod amphipod) {
    return amphipod.getX() >= startX && amphipod.getX() <= endX;
  }

  private Optional<Amphipod> amphipodAtPosition(final int x, final int y) {
    return current.stream()
                  .filter(a -> a.getX() == x)
                  .filter(a -> a.getY() == y)
                  .findAny();
  }

  private List<Amphipod> amphipodsInHallway() {
    return current.stream()
                  .filter(Amphipod::inHallway)
                  .toList();
  }

  private List<Amphipod> amphipodsInDestinationRoom(final AmphipodType type) {
    return current.stream()
                  .filter(Amphipod::inRoom)
                  .filter(a -> a.getX() == type.getRoomPosition())
                  .toList();
  }

  private void populate(final List<String> rows,
                        final int rowNum) {

    final var matcher = AMPHIPOD.matcher(rows.get(rowNum));

    if (!matcher.find()) {
      throw new IllegalArgumentException("Couldn't parse rows of amphipods from input " + rows.get(
          rowNum));
    }

    starting.add(new Amphipod(fromLetter(matcher.group(1)), 2, rowNum + 1, bottomY));
    starting.add(new Amphipod(fromLetter(matcher.group(2)), 4, rowNum + 1, bottomY));
    starting.add(new Amphipod(fromLetter(matcher.group(3)), 6, rowNum + 1, bottomY));
    starting.add(new Amphipod(fromLetter(matcher.group(4)), 8, rowNum + 1, bottomY));
  }

  @Override
  public String toString() {
    final var builder = new StringBuilder("#############").append(lineSeparator())
                                                          .append("#")
                                                          .append(hallway())
                                                          .append("#")
                                                          .append(lineSeparator())
                                                          .append("###")
                                                          .append(roomsToString(1))
                                                          .append("###")
                                                          .append(lineSeparator());

    IntStream.rangeClosed(2, bottomY)
             .mapToObj(this::roomsToString)
             .forEach(room -> {
               builder.append("  #");
               builder.append(room);
               builder.append("#");
               builder.append(lineSeparator());
             });

    builder.append("  #########");

    return builder.toString();
  }

  private String hallway() {
    return IntStream.range(0, 11)
                    .mapToObj(i -> {
                      final var inHallway = current.stream()
                                                   .filter(a -> a.getY() == 0)
                                                   .filter(a -> a.getX() == i)
                                                   .findAny();
                      return inHallway.isPresent() ? inHallway.get().toString() : ".";
                    })
                    .collect(Collectors.joining());
  }

  private String roomsToString(final int row) {

    return IntStream.of(2, 4, 6, 8)
                    .mapToObj(i -> {
                      final var inRoom = current.stream()
                                                .filter(a -> a.getY() == row)
                                                .filter(a -> a.getX() == i)
                                                .findAny();
                      return inRoom.isPresent() ? inRoom.get().toString() : ".";
                    })
                    .collect(Collectors.joining("#"));

  }

  private record Move(Coordinate from, Coordinate to, AmphipodType type) {

  }

  private class MoveCostComparator implements Comparator<Collection<Move>> {

    @Override
    public int compare(final Collection<Move> o1, final Collection<Move> o2) {
      return Long.compare(calculateCost(o1), calculateCost(o2));
    }
  }
}
