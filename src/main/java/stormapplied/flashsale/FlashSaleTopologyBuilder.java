package stormapplied.flashsale;

import backtype.storm.generated.StormTopology;
import backtype.storm.topology.TopologyBuilder;

import stormapplied.flashsale.topology.*;

public class FlashSaleTopologyBuilder {
  private static final String CUSTOMER_RETRIEVAL_SPOUT = "customer-retrieval";
  private static final String FIND_RECOMMENDED_SALES = "find-recommended-sales";
  private static final String LOOKUP_SALES_DETAILS = "lookup-sales-details";
  private static final String SAVE_RECOMMENDED_SALES = "save-recommended-sales";

  public static StormTopology build() {
    TopologyBuilder builder = new TopologyBuilder();

    builder.setSpout(CUSTOMER_RETRIEVAL_SPOUT, new CustomerRetrievalSpout())
      .setMaxSpoutPending(25);

    builder.setBolt(FIND_RECOMMENDED_SALES, new FindRecommendedSales(), 1)
      .setNumTasks(1)
      .shuffleGrouping(CUSTOMER_RETRIEVAL_SPOUT);

    builder.setBolt(LOOKUP_SALES_DETAILS, new LookupSalesDetails(), 1)
      .setNumTasks(1)
      .shuffleGrouping(FIND_RECOMMENDED_SALES);

    builder.setBolt(SAVE_RECOMMENDED_SALES, new SaveRecommendedSales(), 1)
      .setNumTasks(1)
      .shuffleGrouping(LOOKUP_SALES_DETAILS);

    return builder.createTopology();
  }
}
