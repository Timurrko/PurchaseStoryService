package com.story.PurchaseStoryService;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;


@EnableFeignClients
@EnableScheduling
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
	DBMaintainer maintainer;

	public RestApiDemoController(DBMaintainer maintainer){
		this.maintainer = maintainer;
	}

	@GetMapping("/{currencyPairName}:{periodOfTimeInSeconds}")
	public List<Purchase> getPurchasesByCurrencyPairAndTimePeriod(@PathVariable String currencyPairName, @PathVariable long periodOfTimeInSeconds) {
		return this.maintainer.getPurchasesByCurrencyPairNameAndTimePeriodInSeconds(currencyPairName, periodOfTimeInSeconds);
	}

}