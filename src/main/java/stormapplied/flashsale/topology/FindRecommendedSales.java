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
  public static final String RETRY_STREAM = "retry";
  public static final String SUCCESS_STREAM = "success";

  private FlashSaleRecommendationClient client;

  @Override
  public void prepare(Map config,
                      TopologyContext context) {
    long timeout = (Long)config.get("timeout");
    client = new FlashSaleRecommendationClient((int)timeout);
  }

  @Override
  public void execute(Tuple tuple,
                      BasicOutputCollector outputCollector) {
    String customerId = tuple.getStringByField("customer");

    try {
      List<String> sales = client.findSalesFor(customerId);
      if (!sales.isEmpty()) {
        outputCollector.emit(SUCCESS_STREAM,
                             new Values(customerId, sales));
      }
    } catch (Timeout e) {
      outputCollector.emit(RETRY_STREAM, new Values(customerId));
    }
  }

  @Override
  public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
    outputFieldsDeclarer.declareStream(SUCCESS_STREAM, new Fields("customer", "sales"));
    outputFieldsDeclarer.declareStream(RETRY_STREAM, new Fields("customer"));
  }
}
