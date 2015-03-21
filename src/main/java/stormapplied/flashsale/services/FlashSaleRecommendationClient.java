package stormapplied.flashsale.services;

import java.util.List;

public class FlashSaleRecommendationClient {
  private final int timeoutInMillis;

  private final FlashSaleRecommendationService stubService;

  public FlashSaleRecommendationClient(int timeoutInMillis) {
    this.timeoutInMillis = timeoutInMillis;
    this.stubService = new FlashSaleRecommendationService();
  }

  public List<String> findSalesFor(String customerId) {
    return stubService.findSalesFor(customerId, timeoutInMillis);
  }
}