package stormapplied.flashsale.metrics;

import backtype.storm.metric.api.IMetric;
import java.util.HashMap;
import java.util.Map;

public class SuccessRateMetric implements IMetric {
  double success;
  double fail;

  public void incrSuccess(long incrementBy) {
    success += Double.valueOf(incrementBy);
  }

  public void incrFail(long incrementBy) {
    fail += Double.valueOf(incrementBy);
  }    

  public Object getValueAndReset() {
    double rate = (success / (success + fail)) * 100.0;

    success = 0;
    fail = 0;

    return rate;
  }
}