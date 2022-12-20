package ru.mashurov.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VeterinarianCreateDto {

	private String surname;

	private String name;

	private String patronymic;

	private Integer experience;

	private Long clinicId;
}
