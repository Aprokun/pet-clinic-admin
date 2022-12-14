package ru.mashurov.admin.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import ru.mashurov.admin.dto.AppointmentRequestDto;
import ru.mashurov.admin.dto.PageResolver;

import java.util.List;

@Service
@AllArgsConstructor
public class RequestService {

	private final WebClient client;

	public PageResolver<AppointmentRequestDto> findAllByStatusAndAdminIdWithSizeAndPage(
			final List<String> statusSysnames, final Long adminId, final Integer size, final Integer page
	) {

		return client
				.get()
				.uri(uriBuilder -> uriBuilder
						.path(String.join("/", "api", "major", adminId.toString(), "appointments").toString())
						.queryParam("statuses", statusSysnames)
						.queryParam("size", size)
						.queryParam("page", page)
						.build()
				)
				.retrieve()
				.bodyToMono(PageResolver.class)
				.block();
	}

	public AppointmentRequestDto findById(final Long reqId) {

		return client
				.get()
				.uri(String.join("/", "api", "requests", reqId.toString()))
				.retrieve()
				.bodyToMono(AppointmentRequestDto.class)
				.block();
	}

	public void approve(final Long requestId) {

		client
				.post()
				.uri(String.join("/", "api", "major", "requests", requestId.toString(), "approve"))
				.retrieve()
				.toBodilessEntity()
				.block();
	}

	public void reject(final Long requestId) {

		client
				.post()
				.uri(String.join("/", "api", "major", "requests", requestId.toString(), "reject"))
				.retrieve()
				.toBodilessEntity()
				.block();
	}

	public PageResolver<AppointmentRequestDto> findTodayAppointmentRequests(
			final Integer size, final Integer page, final Long adminId
	) {

		return client
				.get()
				.uri(uriBuilder -> uriBuilder
						.path(String.join("/", "api", "major", adminId.toString(), "today-appointments"))
						.queryParam("size", size)
						.queryParam("page", page)
						.build()
				)
				.retrieve()
				.bodyToMono(PageResolver.class)
				.block();
	}

	public void setRequestVisited(final Long reqId, final boolean isVisited) {

		client
				.patch()
				.uri(String.join(
						"/", "api", "major", "requests",
						reqId.toString(), isVisited ? "visited" : "not_visited"
				))
				.retrieve()
				.toBodilessEntity()
				.block();
	}
}
