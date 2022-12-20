package ru.mashurov.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceDto {

    private Long id;

    private String name;

    private String description;

    private Integer cost;
}
