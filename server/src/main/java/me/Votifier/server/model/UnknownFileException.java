package me.Votifier.server.model;

public class UnknownFileException extends Exception {

  // Constructor with no arguments
  public UnknownFileException() {
      super("The specified file may not exist!");
  }
}
