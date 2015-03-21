package stormapplied.flashsale.topology;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import backtype.storm.utils.Utils;

import stormapplied.flashsale.services.LatencySimulator;

public class CustomerRetrievalSpout extends BaseRichSpout {
  private SpoutOutputCollector outputCollector;
  private final Random idGenerator = new Random();

  @Override
  public void open(Map map,
                   TopologyContext topologyContext,
                   SpoutOutputCollector outputCollector) {
    this.outputCollector = outputCollector;
  }

  @Override
  public void nextTuple() {
    new LatencySimulator(1, 25, 10, 40, 5).simulate(1000);

    int numberPart = idGenerator.nextInt(9999999) + 1;
    String customerId = "customer-" + Integer.toString(numberPart);

    outputCollector.emit(new Values(customerId));
  }

  @Override
  public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
    outputFieldsDeclarer.declare(new Fields("customer"));
  }
}
