package stormapplied.flashsale.services;

public class Timeout extends RuntimeException {
  public Timeout(String message) {   
    super(message); 
  }
}
