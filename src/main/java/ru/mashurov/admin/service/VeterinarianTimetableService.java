package ru.mashurov.admin.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import ru.mashurov.admin.dto.VeterinarianTimetableCreateDto;
import ru.mashurov.admin.dto.VeterinarianTimetableDto;

@Service
@AllArgsConstructor
public class VeterinarianTimetableService {

	private final WebClient client;

	public VeterinarianTimetableDto findTimetableByVeterinarianId(final Long id) {

		return client
				.get()
				.uri(String.join("/", "api", "veterinarians", id.toString(), "timetable"))
				.retrieve()
				.bodyToMono(VeterinarianTimetableDto.class)
				.block();
	}

	public void update(final VeterinarianTimetableCreateDto createDto) {

		client
				.put()
				.uri(String.join("/", "api", "veterinarians", "update-timetable"))
				.body(BodyInserters.fromValue(createDto))
				.retrieve()
				.toBodilessEntity()
				.block();
	}
}
