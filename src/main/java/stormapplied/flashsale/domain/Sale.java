package stormapplied.flashsale.domain;

import java.util.Date;
import java.io.Serializable;

public class Sale implements Serializable {
  private final String productName;
  private final Date starts;

  public Sale(String productName, Date starts) {
    this.productName = productName;
    this.starts = starts;
  }

  public String toString() {
  	return productName + " on " + starts.toString();
  }
}