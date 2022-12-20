package ru.mashurov.admin.controller;

import org.springframework.security.core.context.SecurityContextHolder;

public class CommonController {

	protected String getRole() {
		return SecurityContextHolder.getContext().getAuthentication().getAuthorities().toArray()[0].toString();
	}
}
