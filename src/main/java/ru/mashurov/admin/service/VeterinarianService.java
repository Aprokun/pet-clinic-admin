package ru.mashurov.admin.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import ru.mashurov.admin.dto.PageResolver;
import ru.mashurov.admin.dto.VeterinarianCreateDto;
import ru.mashurov.admin.dto.VeterinarianDto;

@Service
@AllArgsConstructor
public class VeterinarianService {

    private final WebClient client;

    public PageResolver<VeterinarianDto> findAllByClinicId(
            final Long clinicId, final Integer size, final Integer page
    ) {

        return client
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path(String.join("/", "api", "clinics", clinicId.toString(), "veterinarians"))
                        .queryParam("size", size)
                        .queryParam("page", page)
                        .build()
                )
                .retrieve()
                .bodyToMono(PageResolver.class)
                .block();
    }

    public VeterinarianDto findById(final Long id) {

        return client
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path(String.join("/", "api", "veterinarians", id.toString()))
                        .build()
                )
                .retrieve()
                .bodyToMono(VeterinarianDto.class)
                .block();
    }

    public void update(final VeterinarianDto veterinarianUpdateDto) {

        client
                .patch()
                .uri(uriBuilder -> uriBuilder
                        .path(String.join("/", "api", "veterinarians", "update"))
                        .build()
                )
                .body(BodyInserters.fromValue(veterinarianUpdateDto))
                .retrieve()
                .toBodilessEntity()
                .block();
    }

    public void create(final VeterinarianCreateDto veterinarianCreateDto) {

        client
                .post()
                .uri(uriBuilder -> uriBuilder
                        .path(String.join("/", "api", "veterinarians", "create"))
                        .build()
                )
                .body(BodyInserters.fromValue(veterinarianCreateDto))
                .retrieve()
                .toBodilessEntity()
                .block();
    }

    public void deleteById(final Long id) {

        client
                .delete()
                .uri(uriBuilder -> uriBuilder
                        .path(String.join("/", "api", "veterinarians", id.toString()))
                        .build()
                )
                .retrieve()
                .toBodilessEntity()
                .block();
    }
}
