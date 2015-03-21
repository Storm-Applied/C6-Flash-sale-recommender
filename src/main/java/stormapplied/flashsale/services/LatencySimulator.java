package stormapplied.flashsale.services;

import java.util.Random;

public class LatencySimulator {
	private final Random random = new Random();

	private final int lowLatencyFloor;
	private final int lowLatencyVariance;
	private final int highLatencyFloor;
	private final int highLatencyVariance;
	private final int percentHighLatency;

	public LatencySimulator(
		int lowLatencyFloor,
		int lowLatencyVariance,
		int highLatencyFloor,
		int highLatencyVariance,
		int percentHighLatency) {
		this.lowLatencyFloor = lowLatencyFloor;
		this.lowLatencyVariance = lowLatencyVariance;
		this.highLatencyFloor = highLatencyFloor;
		this.highLatencyVariance = highLatencyVariance;
		this.percentHighLatency = percentHighLatency;
	}

	public void simulate(int timeoutInMillis) {
    try {
      int latency;

      int percentile = random.nextInt(99) + 1;
      if (percentile <= percentHighLatency) {
        latency = highLatency();
      } else {
        latency = lowLatency();
      }

      beLatent(latency, timeoutInMillis);
    } catch (InterruptedException e) {}		
	}

  private int lowLatency() throws InterruptedException {
    return lowLatencyFloor + random.nextInt(lowLatencyVariance);
  }

  private int highLatency() throws InterruptedException {
    return highLatencyFloor + random.nextInt(highLatencyVariance);
  }

  private void beLatent(int latency, int timeoutInMillis) throws InterruptedException, Timeout {
    if (timeoutInMillis <= latency) {
      Thread.sleep(timeoutInMillis);
      String message = "Timeout after " + Integer.toString(timeoutInMillis) + "ms";
      throw new Timeout(message);
    } else {
      Thread.sleep(latency);
    }
  }  	
}