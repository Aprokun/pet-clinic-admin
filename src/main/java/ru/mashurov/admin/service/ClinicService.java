package ru.mashurov.admin.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import ru.mashurov.admin.dto.ClinicDto;
import ru.mashurov.admin.dto.PageResolver;

@Service
@AllArgsConstructor
public class ClinicService {

    private final WebClient client;

    public PageResolver<ClinicDto> findAllWithSizeAndPageAndRegion(
            final Integer size, final Integer page, final Long regionCode
    ) {

	    return client
			    .get()
			    .uri(uriBuilder -> uriBuilder
					    .path(String.join("/", "api", "clinics"))
					    .queryParam("region", regionCode)
					    .queryParam("size", size)
					    .queryParam("page", page)
					    .build()
			    )
			    .retrieve()
			    .bodyToMono(PageResolver.class)
			    .block();
    }

	public void deleteById(final Long id) {

		client
				.delete()
				.uri(String.join("/", "api", "clinics", id.toString()))
				.retrieve()
				.toBodilessEntity()
				.block();
	}

	public ClinicDto findById(final Long id) {

		return client
				.get()
				.uri(String.join("/", "api", "clinics", id.toString()))
				.retrieve()
				.bodyToMono(ClinicDto.class)
				.block();
	}

	public void update(final ClinicDto clinicDto) {

		client
				.patch()
				.uri(String.join("/", "api", "clinics", "update"))
				.body(BodyInserters.fromValue(clinicDto))
				.retrieve()
				.toBodilessEntity()
				.block();
	}

	public void create(final ClinicDto clinicDto) {

		client
				.post()
				.uri(String.join("/", "api", "clinics", "create"))
				.body(BodyInserters.fromValue(clinicDto))
				.retrieve()
				.toBodilessEntity()
				.block();
	}
}
