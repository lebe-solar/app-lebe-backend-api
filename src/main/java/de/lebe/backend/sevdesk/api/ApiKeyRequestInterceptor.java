package de.lebe.backend.sevdesk.api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import feign.RequestInterceptor;
import feign.RequestTemplate;

@Service
public class ApiKeyRequestInterceptor implements RequestInterceptor {

	@Value("${solar.order.crm.client.lebe}")
	private String apiKey;
	
	@Override
	public void apply(RequestTemplate template) {
		template.header("Authorization", apiKey);
	}

}
