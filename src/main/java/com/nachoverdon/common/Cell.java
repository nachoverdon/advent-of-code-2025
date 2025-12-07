package com.nachoverdon.common;

public record Cell(int x, int y, int type) {
  public static int EMPTY = 0;
  public static int VALUE = 1;
  public static Cell EMPTY_CELL = new Cell(0, 0, EMPTY);

  public Cell(int x, int y, boolean hasValue) {
    this(x, y, hasValue ? Cell.VALUE : Cell.EMPTY);
  }

  public boolean hasValue() {
    return type != EMPTY;
  }
}
