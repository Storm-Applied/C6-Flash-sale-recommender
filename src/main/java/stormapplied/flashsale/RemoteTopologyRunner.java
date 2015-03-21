package stormapplied.flashsale;

import org.apache.commons.lang.StringUtils;
import backtype.storm.Config;
import backtype.storm.StormSubmitter;
import backtype.storm.metric.LoggingMetricsConsumer;

public class RemoteTopologyRunner {
  public static void main(String[] args) throws Exception {
    if (args.length <= 0 || args.length > 2 || StringUtils.isBlank(args[0])) {
      System.out.println("Error deploying topology.");
      System.out.println("Usage: <topology name> debug(optional)");
      System.out.println("Please provide correct command-line arguments and try again.");
      System.exit(1);
    }

    String topologyName = args[0];
    Config config = createConfig(shouldRunInDebugMode(args));
    StormSubmitter.submitTopology(topologyName, config, FlashSaleTopologyBuilder.build());
  }

  private static boolean shouldRunInDebugMode(String[] args) {
    return args.length > 1 && "debug".equalsIgnoreCase(args[1]);
  }

  private static Config createConfig(Boolean debug) {
    int NUM_WORKERS = 1;
    int NUM_ACKERS = NUM_WORKERS;
    int TIMEOUT = 30;

    Config config = new Config();
    config.setDebug(debug);
    config.setMessageTimeoutSecs(TIMEOUT);
    config.setNumWorkers(NUM_WORKERS);
    config.setNumAckers(NUM_ACKERS);
    config.registerMetricsConsumer(LoggingMetricsConsumer.class, 1);

    return config;
  }
}
