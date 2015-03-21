package stormapplied.flashsale.services;

import java.util.List;
import java.util.ArrayList;
import java.util.Random;

class FlashSaleRecommendationService {
  private final Random random = new Random();
  private final LatencySimulator latency = new LatencySimulator(100, 50, 150, 1000, 10);

  public List<String> findSalesFor(String customerId, int timeoutInMillis) {
    latency.simulate(timeoutInMillis);

    return generateSales();
  }

  private List<String> generateSales() {
    List<String> sales = new ArrayList<String>();

    if (shouldGenerateSales()) {
      int numberOfSales = random.nextInt(5) + 1;

      for(int i=1; i<= numberOfSales; i++) {
        String sku = Integer.toString(random.nextInt(200000) + 1000) + "-x-";
        String saleNumber = sku + Integer.toString(i);
      
        sales.add(saleNumber);
      }
    } 

    return sales;
  }

  private boolean shouldGenerateSales() {
    return random.nextInt(2) == 0;
  }
}