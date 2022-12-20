package ru.mashurov.admin.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import ru.mashurov.admin.dto.RegionDto;

import java.util.List;

@Service
@AllArgsConstructor
public class RegionService {

	private final WebClient client;

	public List<RegionDto> findAll() {

		return client
				.get()
				.uri(String.join("/", "api", "regions"))
				.retrieve()
				.bodyToMono(List.class)
				.block();
	}
}
