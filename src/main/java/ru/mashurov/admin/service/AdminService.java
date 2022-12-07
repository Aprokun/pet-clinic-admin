package ru.mashurov.admin.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import ru.mashurov.admin.dto.AdminDto;

@Service
@AllArgsConstructor
public class AdminService {

	private final WebClient client;

	public AdminDto findByUsername(final String username) {

		return client
				.get()
				.uri(uriBuilder -> uriBuilder.path("/api/admins").queryParam("username", username).build())
				.retrieve()
				.bodyToMono(AdminDto.class)
				.block();
	}
}
