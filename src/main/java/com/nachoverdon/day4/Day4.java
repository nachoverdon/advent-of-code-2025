package com.nachoverdon.day4;

import com.nachoverdon.common.Cell;
import com.nachoverdon.common.Grid;
import com.nachoverdon.common.Utils;
import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

public class Day4 {
  private static final char PAPER = '@';
  private static final int LIMIT_ADJACENT = 3;
  private static final String RESOURCES = "src/main/resources/" + Day4.class.getSimpleName().toLowerCase() + "/";
  private long result = 0;
  private final int part;

  static void main() {
    Day4 testPart1 = new Day4(1);

    testPart1.execute(RESOURCES + "test.txt");
    System.out.println("Test result is " + (testPart1.result == 13 ? "" : "in") + "correct: " + testPart1.result);

    Day4 exercisePart1 = new Day4(1);
    exercisePart1.execute(RESOURCES + "input.txt");
    System.out.println("Result is: " + exercisePart1.result);

    Day4 testPart2 = new Day4(2);

    testPart2.execute(RESOURCES + "test.txt");
    System.out.println("Test result is " + (testPart2.result == 43 ? "" : "in") + "correct: " + testPart2.result);

    Day4 exercisePart2 = new Day4(2);
    exercisePart2.execute(RESOURCES + "input.txt");
    System.out.println("Result is: " + exercisePart2.result);
  }

  public Day4(int part) {
    this.part = part;
  }

  private void execute(String fileName) {
    Path path = Paths.get(fileName);

    try (BufferedReader reader = Files.newBufferedReader(path)) {
      Grid grid = Utils.getGridFromStringStream(reader.lines(), PAPER);

      if (part == 1) {
        result = getAccessiblePapersCount(grid);
      } else {
        result = getAmountOfRemovable(grid);
      }
    } catch (Exception e) {
      System.err.println(e.getMessage());
      e.printStackTrace();
    }
  }

  private Stream<Cell> getAccessiblePapers(Grid grid) {
    return grid.getValuedCellStream()
        .filter(cell -> getPapersAroundCount(grid, cell) <= LIMIT_ADJACENT);
  }

  private long getPapersAroundCount(Grid grid, Cell cell) {
    return grid.getValuedCellsAround(cell).count();
  }

  private long getAccessiblePapersCount(Grid grid) {
    return getAccessiblePapers(grid).count();
  }

  private long getAmountOfRemovable(Grid grid) {
    AtomicLong totalRemoved = new AtomicLong();

    while (true) {
      long removed = getAccessiblePapers(grid)
          .peek(grid::remove)
          .count();

      if (removed == 0) return totalRemoved.get();

      totalRemoved.addAndGet(removed);
    }
  }

}
