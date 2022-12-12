package ru.mashurov.admin.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import ru.mashurov.admin.dto.RequestsPageDto;

@Service
@AllArgsConstructor
public class RequestService {

	private final WebClient client;

	public RequestsPageDto findAllByAdminIdWithSizeAndPage(
			final Long adminId, final Integer size, final Integer page
	) {

		return client
				.get()
				.uri(uriBuilder -> uriBuilder
						.path(String.join("/", "api", "admin", adminId.toString(), "appointments").toString())
						.queryParam("size", size)
						.queryParam("page", page)
						.build()
				)
				.retrieve()
				.bodyToMono(RequestsPageDto.class)
				.block();
	}
}
