package stormapplied.flashsale;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.utils.Utils;

public class LocalTopologyRunner {
  public static void main(String[] args) {
    Config config = new Config();
    config.setDebug(true);

    LocalCluster localCluster = new LocalCluster();
    localCluster.submitTopology("flash-sale-recommender", config, FlashSaleTopologyBuilder.build());
  }
}