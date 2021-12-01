package uk.co.credera.aoc.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * A convenience file loader, assuming this will be useful for more than one coding challenge.s
 *
 * @author Marius Rubin
 * @since 0.1.0
 */
public class FileLoader {

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
    return lines().map(Integer::valueOf).mapToInt(Integer::intValue);
  }

  public List<String> allLines() {
    return lines().toList();
  }

  public List<Integer> allIntegers() {
    return boxedIntegers().toList();
  }

  private Stream<Integer> boxedIntegers() {
    return lines().map(Integer::valueOf);
  }

}
