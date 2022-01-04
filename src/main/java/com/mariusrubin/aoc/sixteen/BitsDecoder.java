package com.mariusrubin.aoc.sixteen;

import static com.mariusrubin.aoc.sixteen.BitsDecoder.PacketType.LITERAL;
import static java.lang.Math.abs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * @author Marius Rubin
 * @since 0.1.0
 */
class BitsDecoder {

  private static final Map<Character, String> LOOKUP            = buildLookup();
  private static final String                 LAST_GROUP_MARKER = "0";

  private final List<String> unpacked;

  BitsDecoder(final String input) {
    unpacked = unpack(input);
  }

  int sumVersions() {
    return decodePackets().stream()
                          .map(Packet::header)
                          .mapToInt(PacketHeader::version)
                          .sum();
  }

  long outermostValue() {
    final var packets = decodePackets();
    return packets.get(packets.size() - 1).value();
  }

  List<Packet> decodePackets() {
    return decodePackets(unpacked.listIterator(), Integer.MAX_VALUE, Integer.MAX_VALUE, 0);
  }

  private static List<Packet> decodePackets(final ListIterator<String> it,
                                            final int maxBits,
                                            final int maxPackets,
                                            final int currentDepth) {

    final var packets = new ArrayList<Packet>();
    final var start   = it.nextIndex();

    int packetsFound = 0;

    final var header = new AtomicReference<PacketHeader>();

    while (it.hasNext() && packetsFound < maxPackets && it.nextIndex() < abs(start + maxBits)) {

      try {

        if (header.get() == null) {
          header.set(buildHeader(it, currentDepth));
        }

        if (header.get().type() == LITERAL) {

          packets.add(decodeLiteral(it, header.get()));

        } else {

          decodeComplex(it, currentDepth, packets, header.get());

        }
        packetsFound++;

        //Reset header
        header.set(null);


      } catch (final NoSuchElementException ignored) {
        //Do nothing, just means we've reached the end of one of the padded lengths
      }

    }

    return packets;

  }

  private static void decodeComplex(final ListIterator<String> it,
                                    final int currentDepth,
                                    final List<Packet> packets,
                                    final PacketHeader header) {

    final var lengthType = LengthType.fromId(it.next());
    final var howMany    = readToInt(it, lengthType.getToRead());

    final var childPackets = switch (lengthType) {
      case BIT_BASED -> decodePackets(it, howMany, Integer.MAX_VALUE, currentDepth + 1);
      case SUB_PACKET -> decodePackets(it, Integer.MAX_VALUE, howMany, currentDepth + 1);
    };

    packets.addAll(childPackets);

    if (header.type().isReducing()) {
      packets.add(reduce(childPackets, header));
    } else {
      packets.add(compare(childPackets, header));
    }

  }

  private static PacketHeader buildHeader(final ListIterator<String> it, final int currentDepth) {

    final var version = readToInt(it, 3);
    final var id      = readToInt(it, 3);
    return new PacketHeader(version, PacketType.fromId(id), currentDepth);

  }

  private static Packet compare(final List<Packet> packets, final PacketHeader header) {

    final var directChildren = getDirectChildren(packets, header).toList();

    final var l1 = directChildren.get(0).value();
    final var l2 = directChildren.get(1).value();

    return new Packet(header, header.type().getOperation().apply(l1, l2));

  }

  private static Packet reduce(final List<Packet> packets, final PacketHeader header) {

    final var value = getDirectChildren(packets, header).map(Packet::value)
                                                        .reduce(header.type().getOperation())
                                                        .orElseThrow();

    return new Packet(header, value);

  }

  private static Stream<Packet> getDirectChildren(final List<Packet> packets,
                                                  final PacketHeader header) {

    return packets.stream().filter(p -> p.header().depth() == header.depth() + 1);

  }

  private static Packet decodeLiteral(final Iterator<String> it, final PacketHeader header) {

    final var valueBuffer = new StringBuilder();

    boolean lastGroup;

    do {
      lastGroup = it.next().equals(LAST_GROUP_MARKER);
      IntStream.range(0, 4)
               .mapToObj(i -> it.next())
               .forEach(valueBuffer::append);
    } while (!lastGroup);

    return new Packet(header, longToBinary(valueBuffer.toString()));

  }

  private static int readToInt(final Iterator<String> it, final int howMany) {

    final var value = IntStream.range(0, howMany)
                               .mapToObj(i -> it.next())
                               .collect(Collectors.joining());

    return intToBinary(value);

  }

  private static List<String> unpack(final String input) {

    return input.chars()
                .mapToObj(i -> (char) i)
                .map(LOOKUP::get)
                .flatMapToInt(String::chars)
                .mapToObj(i -> (char) i)
                .map(String::valueOf)
                .toList();
  }

  private static long longToBinary(final String value) {
    return Long.parseLong(value, 2);
  }

  private static int intToBinary(final String value) {
    return Integer.parseInt(value, 2);
  }

  private static Map<Character, String> buildLookup() {
    return Stream.of(new String[][]{
                     {"0", "0000"},
                     {"1", "0001"},
                     {"2", "0010"},
                     {"3", "0011"},
                     {"4", "0100"},
                     {"5", "0101"},
                     {"6", "0110"},
                     {"7", "0111"},
                     {"8", "1000"},
                     {"9", "1001"},
                     {"A", "1010"},
                     {"B", "1011"},
                     {"C", "1100"},
                     {"D", "1101"},
                     {"E", "1110"},
                     {"F", "1111"}
                 })
                 .collect(Collectors.toMap(data -> data[0].charAt(0), data -> data[1]));
  }

  record Packet(PacketHeader header, long value) {

  }

  record PacketHeader(int version, PacketType type, int depth) {

  }

  private enum LengthType {

    BIT_BASED(0, 15),
    SUB_PACKET(1, 11);

    private final int id;
    private final int toRead;

    LengthType(final int id, final int toRead) {
      this.id = id;
      this.toRead = toRead;
    }

    private int getToRead() {
      return toRead;
    }

    private static LengthType fromId(final String id) {
      return fromId(Integer.parseInt(id));
    }

    private static LengthType fromId(final int id) {
      return Arrays.stream(LengthType.values())
                   .filter(p -> p.id == id)
                   .findAny()
                   .orElseThrow();
    }

  }

  enum PacketType {

    SUM(0, Long::sum, true),
    PRODUCT(1, Math::multiplyExact, true),
    MINIMUM(2, Math::min, true),
    MAXIMUM(3, Math::max, true),
    LITERAL(4, null, false),
    GREATER_THAN(5, (l1, l2) -> l1 > l2 ? 1L : 0L, false),
    LESS_THAN(6, (l1, l2) -> l2 > l1 ? 1L : 0L, false),
    EQUAL(7, (l1, l2) -> l1.equals(l2) ? 1L : 0L, false);

    private final int                  id;
    private final BinaryOperator<Long> operation;

    private final boolean reducing;

    PacketType(final int id, final BinaryOperator<Long> operation, final boolean reducing) {
      this.id = id;
      this.operation = operation;
      this.reducing = reducing;
    }

    private static PacketType fromId(final int id) {
      return Arrays.stream(PacketType.values())
                   .filter(p -> p.id == id)
                   .findAny()
                   .orElseThrow();
    }

    private boolean isReducing() {
      return reducing;
    }

    private BinaryOperator<Long> getOperation() {
      return operation;
    }

  }

}
