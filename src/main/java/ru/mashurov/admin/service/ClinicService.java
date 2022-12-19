package ru.mashurov.admin.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
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
}
