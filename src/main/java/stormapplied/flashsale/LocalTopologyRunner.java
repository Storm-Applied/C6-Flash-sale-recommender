package stormapplied.flashsale;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.utils.Utils;
import backtype.storm.metric.LoggingMetricsConsumer;

public class LocalTopologyRunner {
  public static void main(String[] args) {
    Config config = new Config();
    config.setDebug(true);
    config.registerMetricsConsumer(LoggingMetricsConsumer.class, 1);

    LocalCluster localCluster = new LocalCluster();
    localCluster.submitTopology("flash-sale-recommender", config, FlashSaleTopologyBuilder.build());
  }
}
