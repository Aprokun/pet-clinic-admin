package ru.mashurov.admin.service;

import lombok.AllArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.mashurov.admin.dto.AdminDto;
import ru.mashurov.admin.dto.NamedEntityDto;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AdminDetailsService implements UserDetailsService {

	private final AdminService adminService;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		final AdminDto admin = adminService.findByUsername(username);

		return new User(
				admin.getLogin(),
				admin.getPassword(),
				toSimpleGrantedAuthority(admin.getRoles())
		);
	}

	private List<SimpleGrantedAuthority> toSimpleGrantedAuthority(final List<NamedEntityDto> roles) {

		return roles
				.stream()
				.map(role -> new SimpleGrantedAuthority(role.getName()))
				.collect(Collectors.toList());
	}
}
