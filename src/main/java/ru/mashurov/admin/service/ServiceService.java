package ru.mashurov.admin.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import ru.mashurov.admin.dto.ServicesPageDto;

@Service
@AllArgsConstructor
public class ServiceService {

    private final WebClient client;

    public ServicesPageDto findAllByAdminIdWithSizeAndPage(
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
                .bodyToMono(ServicesPageDto.class)
                .block();
    }

    public void removeServiceFromClinic(
            final Long clinicId, final Long serviceId
    ) {

        client
                .delete()
                .uri(String.join("/", "api", "clinics", clinicId.toString(), "services", serviceId.toString(), "remove"))
                .retrieve();
    }
}
