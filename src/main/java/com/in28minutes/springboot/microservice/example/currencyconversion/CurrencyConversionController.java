package com.in28minutes.springboot.microservice.example.currencyconversion;

import java.math.BigDecimal;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@ConfigurationProperties(prefix="endpoints")
public class CurrencyConversionController {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private String forex_service;
	
	@GetMapping("/currency-converter/from/{from}/to/{to}/quantity/{quantity}")
	public CurrencyConversionBean convertCurrency(@PathVariable String from, 
			@PathVariable String to, @PathVariable BigDecimal quantity) {
		
		logger.info("convert currency from {} to {} with quantity {}", from, to, quantity);
		
		HashMap<String, String> uriVariables = new HashMap<>();
		uriVariables.put("from", from);
		uriVariables.put("to", to);
		
		ResponseEntity<CurrencyConversionBean> responseEntity = new RestTemplate().getForEntity(
				 forex_service + "/currency-exchange/from/{from}/to/{to}", CurrencyConversionBean.class, uriVariables);
		
		CurrencyConversionBean response = responseEntity.getBody();
		
		return new CurrencyConversionBean(response.getId(), from, to, response.getConversionMultiple(), 
				quantity, quantity.multiply(response.getConversionMultiple()), response.getPort());
	}
	
	public String getForex_servicet() {
		return forex_service;
	}

	public void setForex_service(String forex_service) {
		this.forex_service = forex_service;
	}
}
