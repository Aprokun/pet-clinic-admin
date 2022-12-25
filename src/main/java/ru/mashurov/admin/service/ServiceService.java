package ru.mashurov.admin.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import ru.mashurov.admin.dto.PageResolver;
import ru.mashurov.admin.dto.ServiceClinicDto;
import ru.mashurov.admin.dto.ServiceDto;

@Service
@AllArgsConstructor
public class ServiceService {

    private final WebClient client;

    public PageResolver<ServiceDto> findAllByAdminIdWithSizeAndPage(
            final Long clinicId, final Integer page, final Integer size
    ) {

	    return client
			    .get()
			    .uri(uriBuilder -> uriBuilder
					    .path(String.join("/", "api", "clinics", clinicId.toString(), "services"))
					    .queryParam("size", size)
					    .queryParam("page", page)
					    .build()
			    )
			    .retrieve()
			    .bodyToMono(PageResolver.class)
			    .block();
    }

	public void removeByIdAndClinicId(final Long clinicId, final Long serviceId) {

		client
				.delete()
				.uri(String.join("/", "api", "clinics", clinicId.toString(), "services", serviceId.toString(), "remove"))
				.retrieve();
	}

	public void update(final ServiceDto serviceDto) {

		client
				.patch()
				.uri(String.join("/", "api", "services", "update"))
				.body(BodyInserters.fromValue(serviceDto))
				.retrieve()
				.toBodilessEntity()
				.block();
	}

	public void create(final ServiceClinicDto serviceClinicDto) {

		client
				.post()
				.uri(String.join("/", "api", "services", "create"))
				.body(BodyInserters.fromValue(serviceClinicDto))
				.retrieve()
				.toBodilessEntity()
				.block();
	}

	public ServiceDto findById(final Long id) {

		return client
				.get()
				.uri(String.join("/", "api", "services", id.toString()))
				.retrieve()
				.bodyToMono(ServiceDto.class)
				.block();
	}

	public void deleteById(final Long id) {

		client
				.delete()
				.uri(String.join("/", "api", "services", id.toString()))
				.retrieve()
				.toBodilessEntity()
				.block();
	}
}
