package com.mariusrubin.aoc.four;

import static com.mariusrubin.aoc.four.BingoBoard.INITIAL_NUMBER;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author Marius Rubin
 * @since 0.1.0
 */
class BingoGame {

  private final List<Integer>    calls;
  private final List<BingoBoard> boards;

  BingoGame(final int[] calls, final int[][][] boards) {
    this(Arrays.stream(calls).boxed().toList(), Stream.of(boards).map(BingoBoard::new).toList());
  }

  BingoGame(final List<Integer> calls, final List<BingoBoard> boards) {
    this.calls = Collections.unmodifiableList(calls);
    this.boards = Collections.unmodifiableList(boards);
  }

  BingoBoard findWinningBoard() {

    return calls.stream()
                .flatMap(this::doCalls)
                .filter(cbp -> cbp.call() > INITIAL_NUMBER)
                .map(CallBoardPair::board)
                .findFirst()
                .orElseThrow();

  }

  BingoBoard findLastWinningBoard() {
    return calls.stream()
                .sequential()
                .flatMap(this::doCalls)
                .filter(cbp -> cbp.call() > INITIAL_NUMBER)
                .map(CallBoardPair::board)
                .reduce((b1, b2) -> b2)
                .orElseThrow();
  }

  private Stream<CallBoardPair> doCalls(final int call) {
    return boards.stream().flatMap(board -> {
      //Do not return boards that have already won into the results
      if (board.getScore() == INITIAL_NUMBER) {
        return Stream.of(new CallBoardPair(board.call(call), board));
      }
      return Stream.empty();
    });
  }

  private record CallBoardPair(int call, BingoBoard board) {

  }

}
