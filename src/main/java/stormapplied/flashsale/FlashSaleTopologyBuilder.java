package stormapplied.flashsale;

import backtype.storm.generated.StormTopology;
import backtype.storm.topology.TopologyBuilder;

import stormapplied.flashsale.topology.*;

public class FlashSaleTopologyBuilder {
  private static final String CUSTOMER_RETRIEVAL_SPOUT = "customer-retrieval";
  private static final String FIND_RECOMMENDED_SALES_FAST = "find-recommended-sales-fast";
  private static final String FIND_RECOMMENDED_SALES_SLOW = "find-recommended-sales-slow";
  private static final String LOOKUP_SALES_DETAILS = "lookup-sales-details";
  private static final String SAVE_RECOMMENDED_SALES = "save-recommended-sales";

  public static StormTopology build() {
    TopologyBuilder builder = new TopologyBuilder();

    builder.setSpout(CUSTOMER_RETRIEVAL_SPOUT, new CustomerRetrievalSpout())
      .setMaxSpoutPending(250);

    builder
      .setBolt(FIND_RECOMMENDED_SALES_FAST, new FindRecommendedSales(), 16)
      .addConfiguration("timeout", 150)
      .setNumTasks(16)
      .shuffleGrouping(CUSTOMER_RETRIEVAL_SPOUT);

    builder
      .setBolt(FIND_RECOMMENDED_SALES_SLOW, new FindRecommendedSales(), 16)
      .addConfiguration("timeout", 1500)
      .setNumTasks(16)
      .shuffleGrouping(FIND_RECOMMENDED_SALES_FAST,
                       FindRecommendedSales.RETRY_STREAM)
      .shuffleGrouping(FIND_RECOMMENDED_SALES_SLOW,
                       FindRecommendedSales.RETRY_STREAM);

    builder.setBolt(LOOKUP_SALES_DETAILS, new LookupSalesDetails(), 16)
      .setNumTasks(16)
      .shuffleGrouping(FIND_RECOMMENDED_SALES_FAST,
                       FindRecommendedSales.SUCCESS_STREAM)
      .shuffleGrouping(FIND_RECOMMENDED_SALES_SLOW,
                       FindRecommendedSales.SUCCESS_STREAM);

    builder.setBolt(SAVE_RECOMMENDED_SALES, new SaveRecommendedSales(), 4)
      .setNumTasks(4)
      .shuffleGrouping(LOOKUP_SALES_DETAILS);

    return builder.createTopology();
  }
}
