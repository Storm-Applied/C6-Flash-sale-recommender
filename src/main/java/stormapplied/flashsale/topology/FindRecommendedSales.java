package stormapplied.flashsale.topology;

import backtype.storm.task.TopologyContext;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.topology.ReportedFailedException;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

import java.util.List;
import java.util.Map;

import stormapplied.flashsale.services.FlashSaleRecommendationClient;
import stormapplied.flashsale.services.Timeout;

public class FindRecommendedSales extends BaseBasicBolt {
  private final static int TIMEOUT = 200;
  private FlashSaleRecommendationClient client;

  @Override
  public void prepare(Map config,
                      TopologyContext context) {
    client = new FlashSaleRecommendationClient(TIMEOUT);
  }

  @Override
  public void execute(Tuple tuple,
                      BasicOutputCollector outputCollector) {
    String customerId = tuple.getStringByField("customer");

    try {
      List<String> sales = client.findSalesFor(customerId);
      if (!sales.isEmpty()) outputCollector.emit(new Values(customerId, sales));
    } catch (Timeout e) {
      throw new ReportedFailedException(e);
    }
  }

  @Override
  public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
    outputFieldsDeclarer.declare(new Fields("customer", "sales"));
  }
}
