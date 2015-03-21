package stormapplied.flashsale.services;

import java.util.List;

import stormapplied.flashsale.domain.Sale;

public class FlashSaleClient {
  private final int timeoutInMillis;

  private final FlashSaleService stubService;

  public FlashSaleClient(int timeoutInMillis) {
    this.timeoutInMillis = timeoutInMillis;
    this.stubService = new FlashSaleService();
  }

  public Sale lookupSale(String saleId) {
    return stubService.lookupSale(saleId, timeoutInMillis);
  }
}