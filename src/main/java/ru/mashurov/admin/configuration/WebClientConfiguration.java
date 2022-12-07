package ru.mashurov.admin.configuration;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Configuration
public class WebClientConfiguration {

	private static String REST_SERVICE_URL = "http://localhost:8080";
	private static Integer TIMEOUT = 3000;


	@Bean
	public WebClient webClient() {

		final HttpClient httpClient = HttpClient.create()
				.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, TIMEOUT)
				.responseTimeout(Duration.ofMillis(TIMEOUT))
				.doOnConnected(connection -> {
					connection.addHandlerLast(new ReadTimeoutHandler(TIMEOUT, TimeUnit.MILLISECONDS));
					connection.addHandlerLast(new WriteTimeoutHandler(TIMEOUT, TimeUnit.MILLISECONDS));
				});

		return WebClient.builder()
				.baseUrl(REST_SERVICE_URL)
				.clientConnector(new ReactorClientHttpConnector(httpClient))
				.build();
	}

}
