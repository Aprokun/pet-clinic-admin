package ru.mashurov.admin.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import ru.mashurov.admin.dto.AppointmentRequestDto;
import ru.mashurov.admin.dto.page.PageResolver;

@Service
@AllArgsConstructor
public class RequestService {

	private final WebClient client;

	public PageResolver<AppointmentRequestDto> findAllByAdminIdWithSizeAndPage(
			final Long adminId, final Integer size, final Integer page
	) {

		return client
				.get()
				.uri(uriBuilder -> uriBuilder
						.path(String.join("/", "api", "major", adminId.toString(), "appointments").toString())
						.queryParam("size", size)
						.queryParam("page", page)
						.build()
				)
				.retrieve()
				.bodyToMono(PageResolver.class)
				.block();
	}

	public void approve(final Long requestId) {

		client
				.post()
				.uri(String.join("/", "api", "admin", "requests", requestId.toString(), "approve"))
				.retrieve();
	}

	public void reject(final Long requestId) {

		client
				.post()
				.uri(String.join("/", "api", "admin", "requests", requestId.toString(), "reject"))
				.retrieve();
	}
}
