package com.nachoverdon.common;

import java.util.Arrays;
import java.util.stream.Stream;

public class Grid {
  public Cell[][] data;
  public int height;
  public int width;

  public Grid(Cell[][] data) {
    this.data = data;
    this.height = data.length;
    this.width = data.length == 0 ? 0 : data[0].length;
  }

  private Cell getCell(Cell cell, int dx, int dy) {
    int x = cell.x(),
        y = cell.y();
    if (x + dx < 0 || x + dx >= width || y + dy < 0 || y + dy >= height) return Cell.EMPTY_CELL;

    return data[y + dy][x + dx];
  }

  private Cell n(Cell cell) {
    return getCell(cell, 0, -1);
  }

  private Cell s(Cell cell) {
    return getCell(cell, 0, 1);
  }

  private Cell e(Cell cell) {
    return getCell(cell, 1, 0);
  }

  private Cell w(Cell cell) {
    return getCell(cell, -1, 0);
  }

  private Cell ne(Cell cell) {
    return getCell(cell, 1, -1);
  }

  private Cell nw(Cell cell) {
    return getCell(cell, -1, -1);
  }

  private Cell se(Cell cell) {
    return getCell(cell, 1, 1);
  }

  private Cell sw(Cell cell) {
    return getCell(cell, -1, 1);
  }

  public void remove(Cell cell) {
    data[cell.y()][cell.x()] = Cell.EMPTY_CELL;
  }

  public Stream<Cell> getCellsAround(Cell cell) {
    return Stream.of(n(cell), s(cell), e(cell), w(cell), ne(cell), nw(cell), se(cell), sw(cell));
  }

  public Stream<Cell> getPaperCells(Cell cell) {
    return getCellsAround(cell).filter(Cell::hasValue);
  }

  public Stream<Cell> getPapersCellStream() {
    return Arrays.stream(data).flatMap(Arrays::stream).filter(Cell::hasValue);
  }
}
