package com.mariusrubin.aoc.twentythree;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledOnOs;
import org.junit.jupiter.api.condition.OS;

/**
 * @author Marius Rubin
 * @since 0.1.0
 */
@DisabledOnOs(OS.MAC)
public class AnAmphipodMover {

  private static final String INPUT = """
      #############
      #...........#
      ###B#C#B#D###
        #A#D#C#A#
        #########
            """;

  private AmphipodMover underTest;

  @Test
  public void calculateLowestEnergy() {
    final var input = """
        #############
        #...........#
        ###B#C#B#D###
          #A#D#C#A#
          #########
              """;
    underTest = new AmphipodMover(input.lines().toList());
    assertThat(underTest.calculateLowestEnergy()).isEqualTo(12521L);
  }

  @Test
  public void calculateLowestEnergyForBigRooms() {
    final var input = """
        #############
        #...........#
        ###B#C#B#D###
          #D#C#B#A#
          #D#B#A#C#
          #A#D#C#A#
          #########
        """;

    underTest = new AmphipodMover(input.lines().toList());
    assertThat(underTest.calculateLowestEnergy()).isEqualTo(44169L);
  }

}
