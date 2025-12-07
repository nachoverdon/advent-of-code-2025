package com.nachoverdon.common;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Utils {
  public static Grid getGridFromStringStream(List<String> list, char value) {
    return getGridFromStringStream(list.stream(), value);
  }

  public static Grid getGridFromStringStream(Stream<String> stream, char value) {
    return new Grid(getCellDataFromStringStream(stream, value));
  }

  public static Cell[][] getCellDataFromStringStream(Stream<String> stream, char value) {
    AtomicInteger y = new AtomicInteger();

    return stream
        .map(row -> getCellsFromRow(row, y.getAndIncrement(), value))
        .toArray(Cell[][]::new);
  }

  public static Cell[] getCellsFromRow(String row, int y, char value) {
    return IntStream.range(0, row.length())
        .mapToObj(x -> new Cell(x, y, row.charAt(x) == value))
        .toArray(Cell[]::new);
  }
}
