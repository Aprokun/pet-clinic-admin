package ru.mashurov.admin.configuration.security;

import org.springframework.security.core.userdetails.UserDetails;
import ru.mashurov.admin.dto.AdminDto;

public interface UserFactory {

	UserDetails create(AdminDto admin);
}
