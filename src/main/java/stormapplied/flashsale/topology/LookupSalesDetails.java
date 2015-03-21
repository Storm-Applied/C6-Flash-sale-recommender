package stormapplied.flashsale.topology;

import backtype.storm.task.TopologyContext;
import backtype.storm.task.OutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import stormapplied.flashsale.metrics.MultiSuccessRateMetric;

import stormapplied.flashsale.services.FlashSaleClient;
import stormapplied.flashsale.domain.Sale;
import stormapplied.flashsale.services.Timeout;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;

public class LookupSalesDetails extends BaseRichBolt {
  private final static int TIMEOUT = 100;
  private FlashSaleClient client;
  private OutputCollector outputCollector;

  private final int METRICS_WINDOW = 15;
  private transient MultiSuccessRateMetric sucessRates;

  @Override
  public void prepare(Map config,
                      TopologyContext context,
                      OutputCollector outputCollector) {
    this.outputCollector = outputCollector;
    client = new FlashSaleClient(TIMEOUT);

    sucessRates = new MultiSuccessRateMetric();
    context.registerMetric("sales-lookup-success-rate", sucessRates, METRICS_WINDOW);    
  }

  @Override
  public void execute(Tuple tuple) {
    String customerId = tuple.getStringByField("customer");
    List<String> saleIds = (List<String>) tuple.getValueByField("sales");

    List<Sale> sales = new ArrayList<Sale>();
    for (String saleId: saleIds) {
      try {
        Sale sale = client.lookupSale(saleId);
        sales.add(sale);
      } catch (Timeout e) {
        sucessRates.scope(customerId).incrFail(1);
        outputCollector.reportError(e);
      }
    }

    if (sales.isEmpty()) {
      outputCollector.fail(tuple);
    } else {
      sucessRates.scope(customerId).incrSuccess(sales.size());
      outputCollector.emit(new Values(customerId, sales));
      outputCollector.ack(tuple);
    }
  }

  @Override
  public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
    outputFieldsDeclarer.declare(new Fields("customer", "sales"));
  }
}
