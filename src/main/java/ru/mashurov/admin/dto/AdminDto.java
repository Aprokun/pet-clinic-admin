package ru.mashurov.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminDto {

    private Long id;

    private String login;

    private String password;

    private List<NamedEntityDto> roles;

    private NamedEntityDto clinic;
}
