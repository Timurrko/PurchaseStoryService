package com.story.PurchaseStoryService;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.Map;

@FeignClient(value = "currencyService-api", url = "http://localhost:8081")
public interface CurrencyServiceClient {
    @RequestMapping(method = RequestMethod.GET, value = "/purchases")
    Map<String, List<Purchase>> getAllPurchaseStories();

    @RequestMapping(method = RequestMethod.GET, value = "/purchases/{currencyPairName}")
    List<Purchase> getPurchaseStoryByCurrencyPair(@PathVariable("currencyPairName") String currencyPairName);
}
