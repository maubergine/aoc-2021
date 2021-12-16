package com.mariusrubin.aoc.sixteen;

import static com.mariusrubin.aoc.sixteen.BitsDecoder.PacketType.LESS_THAN;
import static com.mariusrubin.aoc.sixteen.BitsDecoder.PacketType.LITERAL;
import static com.mariusrubin.aoc.sixteen.BitsDecoder.PacketType.MAXIMUM;
import static org.assertj.core.api.Assertions.assertThat;

import com.mariusrubin.aoc.sixteen.BitsDecoder.Packet;
import com.mariusrubin.aoc.sixteen.BitsDecoder.PacketHeader;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

/**
 * @author Marius Rubin
 * @since 0.1.0
 */
public class ABitsDecoder {

  private BitsDecoder underTest;

  @Test
  public void shouldParseLiteralPackets() {
    underTest = new BitsDecoder("D2FE28");
    final var packets = underTest.decodePackets();
    assertThat(packets.get(0)).isEqualTo(new Packet(new PacketHeader(6, LITERAL, 0), 2021L));
  }

  @Test
  public void shouldParseLengthBasedSubPackets() {
    underTest = new BitsDecoder("38006F45291200");
    final var packets = underTest.decodePackets();
    assertThat(packets).containsExactly(
        new Packet(new PacketHeader(6, LITERAL, 1), 10L),
        new Packet(new PacketHeader(2, LITERAL, 1), 20L),
        new Packet(new PacketHeader(1, LESS_THAN, 0), 1L)
    );
  }

  @Test
  public void shouldParseCountBasedSubpackets() {
    underTest = new BitsDecoder("EE00D40C823060");
    final var packets = underTest.decodePackets();
    assertThat(packets).containsExactly(
        new Packet(new PacketHeader(2, LITERAL, 1), 1L),
        new Packet(new PacketHeader(4, LITERAL, 1), 2L),
        new Packet(new PacketHeader(1, LITERAL, 1), 3L),
        new Packet(new PacketHeader(7, MAXIMUM, 0), 3L)
    );
  }

  @Test
  public void shouldSumVersions() {

    final var output = Stream.of("8A004A801A8002F478",
                                 "620080001611562C8802118E34",
                                 "C0015000016115A2E0802F182340",
                                 "A0016C880162017C3686B18A3D4780")
                             .map(BitsDecoder::new)
                             .map(BitsDecoder::sumVersions);

    assertThat(output).containsExactly(16, 12, 23, 31);
  }

  @Test
  public void shouldSumValues() {
    underTest = new BitsDecoder("C200B40A82");
    assertThat(underTest.outermostValue()).isEqualTo(3L);
  }

  @Test
  public void shouldFindProductOfValues() {
    underTest = new BitsDecoder("04005AC33890");
    assertThat(underTest.outermostValue()).isEqualTo(54L);
  }

  @Test
  public void shouldFindMinimumValue() {
    underTest = new BitsDecoder("880086C3E88112");
    assertThat(underTest.outermostValue()).isEqualTo(7L);
  }

  @Test
  public void shouldFindMaximumValue() {
    underTest = new BitsDecoder("CE00C43D881120");
    assertThat(underTest.outermostValue()).isEqualTo(9L);
  }

  @Test
  public void shouldIndicateLess() {
    underTest = new BitsDecoder("D8005AC2A8F0");
    assertThat(underTest.outermostValue()).isEqualTo(1L);
  }

  @Test
  public void shouldIndicateNotMore() {
    underTest = new BitsDecoder("F600BC2D8F");
    assertThat(underTest.outermostValue()).isEqualTo(0L);
  }

  @Test
  public void shouldIndicateEquality() {
    underTest = new BitsDecoder("9C005AC2F8F0");
    assertThat(underTest.outermostValue()).isEqualTo(0L);
  }

  @Test
  public void shouldIndicateEqualityInChildPackets() {
    underTest = new BitsDecoder("9C0141080250320F1802104A08");
    assertThat(underTest.outermostValue()).isEqualTo(1L);
  }

}