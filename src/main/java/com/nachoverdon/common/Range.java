package com.nachoverdon.common;


public record Range(long start, long end) {
  public static Range getRangeFromString(String range) {
    String[] numbers = range.split("-");

    return new Range(Long.parseLong(numbers[0]), Long.parseLong(numbers[1]));
  }

  public boolean inRange(Long id) {
    return start() <= id && id <= end;
  }

  public Long howManyInRange() {
    return end - start + 1;
  }

  public boolean isOverlapping(Range b) {
    return start <= b.end() && end >= b.start();
  }

  public Range getMergedRange(Range b) {
    return new Range(start, Math.max(end, b.end()));
  }
}

