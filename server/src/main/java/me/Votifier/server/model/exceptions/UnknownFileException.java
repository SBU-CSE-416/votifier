package me.Votifier.server.model.exceptions;

public class UnknownFileException extends Exception {
  public UnknownFileException() {
      super("The specified file may not exist!");
  }
}
