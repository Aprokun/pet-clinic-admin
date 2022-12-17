package ru.mashurov.admin.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import ru.mashurov.admin.dto.VeterinarianDto;
import ru.mashurov.admin.dto.page.PageResolver;

@Service
@AllArgsConstructor
public class VeterinarianService {

    private final WebClient webClient;

    public PageResolver<VeterinarianDto> findAllByClinicId(
            final Long clinicId, final Integer size, final Integer page
    ) {

        return webClient
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
}
