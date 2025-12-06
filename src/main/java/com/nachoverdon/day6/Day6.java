package com.nachoverdon.day6;

import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

public class Day6 {
  private static final boolean NUMBERS = false;
  private static final boolean OPERATORS = true;
  private static final String ADD = "+";
  private static final String MULT = "*";
  private static final String RESOURCES = "src/main/resources/" + Day6.class.getSimpleName().toLowerCase() + "/";;
  private long resultPart1 = 0;
  private long resultPart2 = 0;

  static void main() {
    Day6 test = new Day6();

    test.execute(RESOURCES + "test.txt");
    System.out.println("Test result is of part 1 is " + (test.resultPart1 == 4277556 ? "" : "in") + "correct: " + test.resultPart1);
    System.out.println("Test result is of part 2 is " + (test.resultPart2 == 3263827 ? "" : "in") + "correct: " + test.resultPart2);

    Day6 exercise = new Day6();
    exercise.execute(RESOURCES + "input.txt");
    System.out.println("Results for part 1 is: " + exercise.resultPart1);
    System.out.println("Results for part 2 is: " + exercise.resultPart2);
  }

  private void execute(String fileName) {
    Path path = Paths.get(fileName);

    try (BufferedReader reader = Files.newBufferedReader(path)) {
      List<String> lines = reader.readAllLines();
      resultPart1 = solve(lines);
      resultPart2 = solve2(lines);
    } catch (Exception e) {
      System.err.println(e.getMessage());
      e.printStackTrace();
    }
  }

  private String[] trimAndSplit(String row) {
    return row.trim().split("\\s+");
  }

  private boolean isOperator(String row) {
    return row.contains(ADD) || row.contains(MULT);
  }

  private List<Long> rowToNumberList(String row) {
    return Arrays.stream(trimAndSplit(row))
        .map(Long::parseLong)
        .toList();
  }

  private List<List<Long>> getNumbersLists(List<String> rows) {
    return rows.stream()
        .map(this::rowToNumberList)
        .toList();
  }

  private List<String> rowToOperator(String row) {
    return Arrays.stream(trimAndSplit(row))
        // .map(op -> op.equals(ADD) ? Long::sum : this::sum)
        .toList();
  }

  private List<List<String>> rowsToOperator(List<String> rows) {
    return rows.stream().map(this::rowToOperator).toList();
  }

  private LongStream getLongStream(List<Long> list) {
    return list.stream().mapToLong(Long::longValue);
  }

  public static long mult(long a, long b) {
    return a * b;
  }

  private long solve(List<String> lines) {
    var map = lines.stream().collect(Collectors.groupingBy(this::isOperator));
    var operators = rowsToOperator(map.get(OPERATORS)).getFirst();
    var numbersList = getNumbersLists(map.get(NUMBERS));

    long total = 0;

    int size = operators.size();

    for (int i = 0; i < operators.size(); i++) {
      List<Long> columnNumbers = new ArrayList<>(numbersList.size());

      for (List<Long> numbers : numbersList) {
        columnNumbers.add(numbers.get(i));
      }

      total += operators.get(i).equals(ADD)
          ? getLongStream(columnNumbers).sum()
          : getLongStream(columnNumbers).reduce(Day6::mult).orElse(0L);
    }

    return total;
  }

  private long solve2(List<String> lines) {
    var map = lines.stream().collect(Collectors.groupingBy(this::isOperator));
    var operators = rowsToOperator(map.get(OPERATORS)).getFirst();
    // TODO: Don't parse long yet
    var numbersList = getNumbersLists(map.get(NUMBERS));

    long total = 0;

    int size = operators.size();
    int start = size - 1;
    int end   = -1;

    for (int i = start; i != end; i--) {
      List<Long> columnNumbers = new ArrayList<>(numbersList.size());

      for (List<Long> numbers : numbersList) {
        columnNumbers.add(numbers.get(i));
      }

      total += operators.get(i).equals(ADD)
          ? getLongStream(columnNumbers).sum()
          : getLongStream(columnNumbers).reduce(Day6::mult).orElse(0L);
    }

    return total;
  }
}
