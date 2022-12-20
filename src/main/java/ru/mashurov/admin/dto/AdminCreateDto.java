package ru.mashurov.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminCreateDto {

	private Long id;

	private String login;

	private String password;

	private Long clinicId;
}
