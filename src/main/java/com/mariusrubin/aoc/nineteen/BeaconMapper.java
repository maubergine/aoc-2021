package com.mariusrubin.aoc.nineteen;

import static java.lang.Integer.parseInt;
import static java.util.stream.Collectors.toMap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author Marius Rubin
 * @since 0.1.0
 */
public class BeaconMapper {

  private static final Pattern SCANNER_NAME = Pattern.compile("^--- scanner ([0-9]+) ---$");

  private static final Pattern READING = Pattern.compile("^([\\-0-9]+),([\\-0-9]+),([\\-0-9]+)$");

  private final Map<Integer, BeaconScanner> beacons = new TreeMap<>();

  public BeaconMapper(final List<String> inputs) {

    var id       = -1;
    var readings = new ArrayList<BeaconReading>();

    for (final String input : inputs) {
      final var nameMatch = SCANNER_NAME.matcher(input);
      if (nameMatch.matches()) {
        id = parseInt(nameMatch.group(1));
      } else {
        final var readingMatch = READING.matcher(input);
        if (readingMatch.matches()) {
          readings.add(toBeaconReading(input));
        } else {
          beacons.put(id, new BeaconScanner(id, readings));
          readings = new ArrayList<>();
        }
      }
    }

    beacons.put(id, new BeaconScanner(id, readings));

  }

  public int getLargestManhattanDistance() {
    processBeacons();
    return beacons.values()
                  .stream()
                  .map(BeaconScanner::getActualPosition)
                  .flatMapToInt(bc -> beacons.values()
                                             .stream()
                                             .map(BeaconScanner::getActualPosition)
                                             .mapToInt(bc::distanceTo))
                  .max()
                  .orElseThrow();
  }

  public int countBeacons() {
    processBeacons();
    return allReadings().size();
  }

  public Set<BeaconReading> allReadings() {
    processBeacons();
    return beacons.values()
                  .stream()
                  .map(BeaconScanner::getReadings)
                  .flatMap(Collection::stream)
                  .collect(Collectors.toSet());
  }


  public Map<Integer, BeaconReading> getRelativePositions() {

    processBeacons();
    return beacons.values()
                  .stream()
                  .collect(toMap(BeaconScanner::getId, BeaconScanner::getActualPosition));

  }

  public void processBeacons() {

    final var absolute = new ArrayList<BeaconScanner>();
    absolute.add(beacons.get(0));

    while (absolute.size() < beacons.size()) {

      final var toProcess = new ArrayList<>(beacons.values());
      toProcess.removeAll(absolute);

      toProcess.forEach(bc -> {

        absolute.stream()
                .sequential()
                .takeWhile(abs -> !bc.isAbsolute())
                .forEach(bc::matchAndReorient);

        if (bc.isAbsolute()) {
          absolute.add(bc);
        }

      });

    }

  }


  private static BeaconReading toBeaconReading(final String input) {
    final var match = READING.matcher(input);
    if (!match.matches()) {
      throw new IllegalArgumentException("Could not parse reading line " + input);
    }

    return new BeaconReading(parseInt(match.group(1)),
                             parseInt(match.group(2)),
                             parseInt(match.group(3)));

  }


}
