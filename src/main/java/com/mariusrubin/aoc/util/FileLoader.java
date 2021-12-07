package com.mariusrubin.aoc.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * A convenience file loader, assuming this will be useful for more than one coding challenge.
 *
 * @author Marius Rubin
 * @since 0.1.0
 */
public class FileLoader {

  private static final Pattern COMMA = Pattern.compile(",");

  private final Path path;

  public FileLoader(final String path) {
    this.path = Paths.get(path);
    if (Files.notExists(this.path)) {
      throw new IllegalArgumentException(String.format("File %s does not exist", path));
    }
  }

  public Stream<String> lines() {
    try {
      return Files.lines(path);
    } catch (final IOException ignored) {
      return Stream.empty();
    }
  }

  public IntStream integers() {
    return boxedIntegers().mapToInt(Integer::intValue);
  }

  public IntStream binaryIntegers() {
    return boxedIntegers(2).mapToInt(Integer::intValue);
  }

  public IntStream commaSeparatedIntegers() {
    return lines().flatMap(COMMA::splitAsStream).mapToInt(Integer::parseInt);
  }

  public List<Integer> allCommaSeparatedIntegers() {
    return commaSeparatedIntegers().boxed().toList();
  }

  public List<String> allLines() {
    return lines().toList();
  }

  public List<Integer> allIntegers() {
    return boxedIntegers().toList();
  }

  public List<Integer> allBinaryIntegers() {
    return boxedIntegers(2).toList();
  }

  private Stream<Integer> boxedIntegers() {
    return boxedIntegers(10);
  }

  private Stream<Integer> boxedIntegers(final int radix) {
    return lines().map(s -> Integer.valueOf(s, radix));
  }

}
