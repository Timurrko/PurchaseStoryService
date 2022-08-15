package com.story.PurchaseStoryService;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;


@EnableFeignClients
@SpringBootApplication
public class PurchaseStoryServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(PurchaseStoryServiceApplication.class, args);
	}

}


@RestController
@RequestMapping("/purchases")
class RestApiDemoController {

	@Autowired
	CurrencyServiceClient client;

	public RestApiDemoController(CurrencyServiceClient client){
		this.client = client;
	}
	@GetMapping("")
	Map<String, List<Purchase>> getAllPurchaseStories() {
		return client.getAllPurchaseStories();
	}

	@GetMapping("/{currencyPairName}")
	List<Purchase> getPurchaseStoryByCurrencyPair(@PathVariable String currencyPairName) {
		return client.getPurchaseStoryByCurrencyPair(currencyPairName);
	}
}