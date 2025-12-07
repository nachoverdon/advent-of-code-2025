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
import java.util.stream.Stream;

class Operation {
  public long total;
  public String operator;

  public Operation(long total, String operator) {
    this.total = total;
    this.operator = operator;
  }
}

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

  private Stream<String> rowToNumberStringStream(String row) {
    return Arrays.stream(trimAndSplit(row));
  }

  private List<Long> rowToNumberList(String row) {
    return rowToNumberStringStream(row)
        .map(Long::parseLong)
        .toList();
  }

  private List<List<Long>> getNumbersLists(List<String> rows) {
    return rows.stream()
        .map(this::rowToNumberList)
        .toList();
  }

  private List<String> rowToOperator(String row) {
    return Arrays.stream(trimAndSplit(row)).toList();
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
    var operatorsRow = map.get(OPERATORS).getFirst();
    var numbersList = map.get(NUMBERS);
    List<String> numbersInColumn = new ArrayList<>(numbersList.size());

    return getTotal(operatorsRow, numbersList, numbersInColumn);
  }

  private long getTotal(String operatorsRow, List<String> numbersList, List<String> numbersInColumn) {
    Operation op = new Operation(0, "");

    for (int i = operatorsRow.length() - 1; i != -1; i--) {
      processColumn(operatorsRow, numbersList, numbersInColumn, i, op);
    }

    return op.total;
  }

  private void processColumn(String operatorsRow, List<String> numbersList, List<String> numbersInColumn, int i, Operation op) {
    String operatorRowStr = operatorsRow.substring(i, i + 1);
    StringBuilder subcolumnNumber = new StringBuilder();

    op.operator = operatorRowStr.isBlank() ? op.operator : operatorRowStr;
    appendDigitToSubcolumnNumber(numbersList, i, subcolumnNumber);
    addNumberOrCalculateTotal(numbersInColumn, i, op, subcolumnNumber);
  }

  private void addNumberOrCalculateTotal(List<String> numbersInColumn, int i, Operation op, StringBuilder subcolumnNumber) {
    if (i == 0) numbersInColumn.add(subcolumnNumber.toString());

    if (subcolumnNumber.isEmpty() || i == 0) {
      calculateTotalAndClearColumn(numbersInColumn, op);
    } else {
      numbersInColumn.add(subcolumnNumber.toString());
    }
  }

  private void calculateTotalAndClearColumn(List<String> numbersInColumn, Operation op) {
    op.total += sumOrMultiplyColumn(op.operator, numbersInColumn);
    numbersInColumn.clear();
  }

  private long sumOrMultiplyColumn(String currentOperator, List<String> numbersInColumn) {
    LongStream stream = numbersInColumn.stream().mapToLong(Long::parseLong);

    return currentOperator.equals(ADD)
        ? stream.sum()
        : stream.sequential().reduce(Day6::mult).orElse(0);
  }

  private void appendDigitToSubcolumnNumber(List<String> numbersList, int i, StringBuilder subcolumnNumber) {
    for (String row : numbersList) {
      String digit = row.substring(i, i +1);

      if (!" ".equals(digit)) {
        subcolumnNumber.append(digit);
      }
    }
  }
}
