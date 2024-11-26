package me.Votifier.server.model.exceptions;

public class InvalidRacialGroupException extends Exception {
  public InvalidRacialGroupException() {
    super("The selected racial group is invalid!");
  }
}
