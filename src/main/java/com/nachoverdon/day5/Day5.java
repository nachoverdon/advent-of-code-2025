package com.nachoverdon.day5;

import com.nachoverdon.common.Range;
import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day5 {
  private static final boolean RANGE = true;
  private static final boolean INGREDIENTS = false;
  private static final String RESOURCES = "src/main/resources/" + Day5.class.getSimpleName().toLowerCase() + "/";;
  private long resultPart1 = 0;
  private long resultPart2 = 0;

  static void main() {
    Day5 test = new Day5();

    test.execute(RESOURCES + "test.txt");
    System.out.println("Test result is of part 1 is " + (test.resultPart1 == 3 ? "" : "in") + "correct: " + test.resultPart1);
    System.out.println("Test result is of part 2 is " + (test.resultPart2 == 14 ? "" : "in") + "correct: " + test.resultPart2);

    Day5 exercise = new Day5();
    exercise.execute(RESOURCES + "input.txt");
    System.out.println("Results for part 1 is: " + exercise.resultPart1);
    System.out.println("Results for part 2 is: " + exercise.resultPart2);
  }

  private void execute(String fileName) {
    Path path = Paths.get(fileName);

    try (BufferedReader reader = Files.newBufferedReader(path)) {
      var map = getRangesAndIngredients(reader.lines());
      List<Range> ranges = getReducedRanges(getRanges(map.get(RANGE)));
      List<Long> ingredients = getIngredients(map.get(INGREDIENTS));

      resultPart1 = getFreshIngredientsCount(ingredients, ranges);
      resultPart2 = sumInRange(ranges);
    } catch (Exception e) {
      System.err.println(e.getMessage());
      e.printStackTrace();
    }
  }

  private List<Range> getReducedRanges(List<Range> ranges) {
    List<Range> reduced = new ArrayList<>();
    Range currentRange = ranges.getFirst();

    for (int i = 1; i < ranges.size(); i++) {
      currentRange = reduceOrAddRange(ranges, reduced, i, currentRange);
    }

    reduced.add(currentRange);

    return reduced;
  }

  private Range reduceOrAddRange(List<Range> ranges, List<Range> gapless, int i, Range currentRange) {
    Range nextRange = ranges.get(i);

    if (currentRange.isOverlapping(nextRange)) {
      currentRange = currentRange.getMergedRange(nextRange);
    } else {
      gapless.add(currentRange);
      currentRange = nextRange;
    }

    return currentRange;
  }

  private Map<Boolean, List<String>> getRangesAndIngredients(Stream<String> stream) {
    return stream
        .filter(Predicate.not(String::isEmpty))
        .collect(Collectors.groupingByConcurrent(this::isRange));
  }

  private List<Range> getRanges(List<String> list) {
    return list.parallelStream()
        .map(Range::getRangeFromString)
        .sorted(Comparator.comparingLong(Range::start))
        .toList();
  }

  private List<Long> getIngredients(List<String> list) {
    return list.parallelStream()
        .map(Long::parseLong)
        .toList();
  }

  private boolean isRange(String str) {
    return str.contains("-");
  }

  private boolean isFresh(Long id, List<Range> ranges) {
    for (Range range : ranges) {
      if (range.inRange(id)) return true;
    }

    return false;
  }

  private long getFreshIngredientsCount(List<Long> ingredients, List<Range> ranges) {
    return ingredients.stream()
        .filter(i -> isFresh(i, ranges))
        .count();
  }

  private Long sumInRange(List<Range> ranges) {
    return ranges.stream()
        .map(Range::howManyInRange)
        .mapToLong(Long::longValue)
        .sum();
  }
}
