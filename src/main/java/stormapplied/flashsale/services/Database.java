package stormapplied.flashsale.services;

import java.util.List;

import stormapplied.flashsale.domain.Sale;

class Database {
  private final LatencySimulator latency = new LatencySimulator(20, 10, 30, 1500, 1);

  public void save(String customerId, List<Sale> sale, int timeoutInMillis) {
    latency.simulate(timeoutInMillis);
  }
}