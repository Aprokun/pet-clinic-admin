package ru.mashurov.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class VeterinarianTimetableCreateDto extends VeterinarianTimetableDto {

	private Long veterinarianId;
}
