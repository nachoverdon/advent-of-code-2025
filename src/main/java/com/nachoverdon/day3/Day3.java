package com.nachoverdon.day3;

import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;


public class Day3 {
  private static final char CLEAR = '0';
  private static final String RESOURCES = "src/main/resources/day3/";
  private long result = 0;
  private final int batteriesPerBank;

  static void main() {
    Day3 testPart1 = new Day3(2);
    Day3 testPart2 = new Day3(12);
    Day3 exercisePart1 = new Day3(2);
    Day3 exercisePart2 = new Day3(12);

    testPart1.execute(RESOURCES + "test.txt");
    System.out.println("Test result is " + (testPart1.result == 357 ? "" : "in") + "correct: " + testPart1.result);

    testPart2.execute(RESOURCES + "test.txt");
    System.out.println("Test result is " + (testPart2.result == 3121910778619L ? "" : "in") + "correct: " + testPart2.result);

    exercisePart1.execute(RESOURCES + "input.txt");
    System.out.println("Result is: " + exercisePart1.result);

    exercisePart2.execute(RESOURCES + "input.txt");
    System.out.println("Result is: " + exercisePart2.result);
  }

  public Day3(int batteriesPerBank) {
    this.batteriesPerBank = batteriesPerBank;
  }

  private void execute(String fileName) {
    Path path = Paths.get(fileName);

    try (BufferedReader reader = Files.newBufferedReader(path)) {
      result = reader.lines()
          .mapToLong(this::processBank)
          .sum();
    } catch (Exception e) {
      System.err.println(e.getMessage());
      e.printStackTrace();
    }
  }

  private void fill(char[] active, int index) {
    Arrays.fill(active, index, batteriesPerBank - 1, CLEAR);
    active[index] = CLEAR;
  }

  private long processBank(String bank) {
    char[] batteries = bank.toCharArray();
    char[] active = new char[batteriesPerBank];

    fill(active, 0);

    for (int i = 0; i < batteries.length; i++) {
      processBattery(active, batteries[i], i, batteries);
    }

    return Long.parseLong(new String(active));
  }

  // Loop through the active batteries to see if the joltage is superior, and if so, and there are
  // still enough batteries to check next, the active battery gets assigned the joltage and clears
  // the next active batteries (sets them to '0') so that they get reassigned on the next iteration
  private void processBattery(char[] active, char joltage, int index, char[] batteries) {
    for (int i = 0; i < active.length; i++) {
      int next = i + 1;

      if (active[i] < joltage && index < batteries.length - (active.length - next)) {
        active[i] = joltage;

        if (i < batteriesPerBank - 1) fill(active, next);

        return;
      }
    }
  }

}
