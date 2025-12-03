package com.nachoverdon.day1;

import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

record Rotation(int dir, int dist) {}

public class Day1 {
  private static final int PART = 2;
  private static final String RESOURCES = "src/main/resources/day1/";
  private static final int L = -1;
  private static final int R = 1;
  private int pos = 50;
  private int password = 0;

  static void main() {
    Day1 test = new Day1();
    test.execute(RESOURCES + "test.txt");
    System.out.println("Test password is: " + test.password);

    Day1 exercise = new Day1();

    exercise.execute(RESOURCES + "input.txt");
    System.out.println("Password is: " + exercise.password);
  }

  private Rotation parseRotation(String line) {
    int dir = "L".equals(line.substring(0, 1)) ? L : R;
    int dist = Integer.parseInt(line.substring(1));

    return new Rotation(dir, dist);
  }

  private void execute(String fileName) {
    Path path = Paths.get(fileName);

    try (BufferedReader reader = Files.newBufferedReader(path)) {
      if (PART == 1) {
        reader.lines().map(this::parseRotation).forEachOrdered(this::processRotation);
      } else if (PART == 2) {
        reader.lines().map(this::parseRotation).forEachOrdered(this::processRotationLoop);
      }
    } catch (Exception e) {
      System.err.println(e.getMessage());
      e.printStackTrace();
    }
  }

  // Simple implementation that goes step by step.
  private void processRotationLoop(Rotation rot) {
    for (int i = 0; i < rot.dist(); i++) {
      pos += rot.dir();

      if (pos == -1) pos = 99;
      else if (pos == 100) pos = 0;

      if (pos == 0) password++;
    }
  }

  private void processRotation(Rotation rot) {
    pos = (pos + (rot.dist() * rot.dir()) % 100 + 100) % 100;

    if (pos == 0) password++;
  }
}
