package ru.mashurov.admin.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import ru.mashurov.admin.dto.AdminCreateDto;
import ru.mashurov.admin.dto.AdminDto;
import ru.mashurov.admin.dto.PageResolver;

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

	public PageResolver<AdminDto> findAllMajorsWithSizeAndPage(final Integer size, final Integer page) {

		return client
				.get()
				.uri(uriBuilder -> uriBuilder.path(String.join("/", "api", "majors")).build())
				.retrieve()
				.bodyToMono(PageResolver.class)
				.block();
	}

	public AdminDto findById(final Long id) {

		return client
				.get()
				.uri(uriBuilder -> uriBuilder
						.path(String.join("/", "api", "majors", id.toString()))
						.build()
				)
				.retrieve()
				.bodyToMono(AdminDto.class)
				.block();
	}

	public void save(final AdminCreateDto adminCreateDto) {

		client
				.post()
				.uri(uriBuilder -> uriBuilder.path(String.join("/", "api", "majors", "create")).build())
				.body(BodyInserters.fromValue(adminCreateDto))
				.retrieve()
				.toBodilessEntity()
				.block();
	}
}
