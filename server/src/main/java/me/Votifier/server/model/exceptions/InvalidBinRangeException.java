package me.Votifier.server.model.exceptions;

public class InvalidBinRangeException extends Exception {
  public InvalidBinRangeException() {
    super("The bin range is invalid!");
  }
}
