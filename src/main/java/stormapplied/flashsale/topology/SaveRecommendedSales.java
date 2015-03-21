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

import stormapplied.flashsale.services.DatabaseClient;
import stormapplied.flashsale.services.Timeout;
import stormapplied.flashsale.domain.Sale;

public class SaveRecommendedSales extends BaseBasicBolt {
  private final static int TIMEOUT = 50;
  private DatabaseClient dbClient;

  @Override
  public void prepare(Map config,
                      TopologyContext context) {
    dbClient = new DatabaseClient(TIMEOUT);
  }

  @Override
  public void execute(Tuple tuple,
                      BasicOutputCollector outputCollector) {
    String customerId = tuple.getStringByField("customer");
    List<Sale> sales = (List<Sale>) tuple.getValueByField("sales");

    try {
      dbClient.save(customerId, sales);
    } catch (Timeout e) {
      throw new ReportedFailedException(e);
    }
  }

  @Override
  public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
  }
}