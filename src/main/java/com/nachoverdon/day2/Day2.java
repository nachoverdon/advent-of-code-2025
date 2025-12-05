package com.nachoverdon.day2;

import static com.nachoverdon.common.Range.getRangeFromString;

import com.nachoverdon.common.Range;
import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Day2 {
  private static final int PART = 2;
  private static final Pattern pattern = Pattern.compile("^(\\d+)(?:\\1" + (PART == 2 ? "+" : "") + ")$");;
  private static final String RESOURCES = "src/main/resources/" + Day2.class.getSimpleName().toLowerCase() + "/";;
  private final List<String> invalidIds = new ArrayList<>();
  private long result = 0;

  static void main() {
    Day2 test = new Day2();
    Day2 exercise = new Day2();

    test.execute(RESOURCES + "test.txt");
    System.out.println(String.join(", ", test.invalidIds));
    System.out.println("Test result is: " + test.result);

    exercise.execute(RESOURCES + "input.txt");
    System.out.println(String.join(", ", exercise.invalidIds));
    System.out.println("Result is: " + exercise.result);
  }

  private void execute(String fileName) {
    Path path = Paths.get(fileName);

    try (BufferedReader reader = Files.newBufferedReader(path)) {
      String input = reader.lines().findFirst().orElseThrow();
      String[] ranges = input.split(",");

      Arrays.stream(ranges).parallel().forEach(this::processRange);

      result = sumInvalidIds(invalidIds);
    } catch (Exception e) {
      System.err.println(e.getMessage());
      e.printStackTrace();
    }
  }

  private void processRange(String rangeString) {
    invalidIds.addAll(getInvalidIds(getRangeFromString(rangeString)));
  }

  private List<String> getInvalidIds(Range range) {
    List<String> ids = new ArrayList<>();

    for (long id = range.start(); id < range.end()+1; id++) {
      // if (isInvalidSplit(id)) ids.add(Long.toString(id));
      if (isInvalidRegex(id)) ids.add(Long.toString(id));
    }

    return ids;
  }

  private boolean isInvalidSplit(long id) {
    String stringId = Long.toString(id);
    int length = stringId.length();

    if (length % 2 != 0) return false;

    int half = length / 2;

    return stringId.substring(0, half).equals(stringId.substring(half));
  }

  private boolean isInvalidRegex(long id) {
    String stringId = Long.toString(id);
    Matcher matcher = pattern.matcher(stringId);

    int matches = 0;
    while (matcher.find()) {
      matches++;
    }

    return matches > 0;
  }

  private long sumInvalidIds(List<String> invalidIds) {
    return invalidIds.parallelStream()
        .mapToLong(Long::parseLong)
        .sum();
  }
}
