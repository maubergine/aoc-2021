package com.mariusrubin.aoc.eight;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author Marius Rubin
 * @since 0.1.0
 */
public class ADisplayAnalyser {

  private static final int[] LOOK_FOR = {1, 4, 7, 8};

  private static final String INPUT = """
      be cfbegad cbdgef fgaecd cgeb fdcge agebfd fecdb fabcd edb | fdgacbe cefdb cefbgd gcbe
      edbfga begcd cbg gc gcadebf fbgde acbgfd abcde gfcbed gfec | fcgedb cgb dgebacf gc
      fgaebd cg bdaec gdafb agbcfd gdcbef bgcad gfac gcb cdgabef | cg cg fdcagb cbg
      fbegcd cbd adcefb dageb afcb bc aefdc ecdab fgdeca fcdbega | efabcd cedba gadfec cb
      aecbfdg fbg gf bafeg dbefa fcge gcbea fcaegb dgceab fcbdga | gecf egdcabf bgf bfgea
      fgeab ca afcebg bdacfeg cfaedg gcfdb baec bfadeg bafgc acf | gebdcfa ecba ca fadegcb
      dbcfg fgd bdegcaf fgec aegbdf ecdfab fbedc dacgb gdcebf gf | cefg dcbef fcge gbcadfe
      bdfegc cbegaf gecbf dfcage bdacg ed bedf ced adcbefg gebcd | ed bcgafe cdgba cbgef
      egadfb cdbfeg cegd fecab cgb gbdefca cg fgcdab egfdb bfceg | gbdfcae bgc cg cgb
      gcafb gcf dcaebfg ecagb gf abcdeg gaef cafbge fdbac fegbdc | fgae cfgab fg bagce""";

  private DisplayAnalyser underTest;

  @BeforeEach
  public void init() {
    underTest = new DisplayAnalyser();
  }

  @Test
  public void shouldCountDigitsInInput() {
    final var interestingDigits = underTest.countInstancesOf(LOOK_FOR, INPUT.lines().toList());
    Assertions.assertThat(interestingDigits).isEqualTo(26);
  }

  @Test
  public void shouldSumOutputs() {
    final long outputTotal = underTest.sumOutput(INPUT.lines().toList());
    Assertions.assertThat(outputTotal).isEqualTo(61229);
  }

}
