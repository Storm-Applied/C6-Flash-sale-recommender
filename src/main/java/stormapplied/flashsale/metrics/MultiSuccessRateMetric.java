package stormapplied.flashsale.metrics;

import backtype.storm.metric.api.IMetric;
import java.util.HashMap;
import java.util.Map;

public class MultiSuccessRateMetric implements IMetric {
  Map<String, SuccessRateMetric> rates = new HashMap();

  public SuccessRateMetric scope(String key) {
    SuccessRateMetric rate = rates.get(key);
        
    if (rate == null) {
      rate = new SuccessRateMetric();
      rates.put(key, rate);
    }

    return rate;
  }

  public Object getValueAndReset() {
    Map ret = new HashMap();
      
    for(Map.Entry<String, SuccessRateMetric> e : rates.entrySet()) {
      ret.put(e.getKey(), e.getValue().getValueAndReset());
    }
      
    rates.clear();

    return ret;
  }
}
