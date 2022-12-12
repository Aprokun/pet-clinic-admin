package ru.mashurov.admin.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.mashurov.admin.dto.AdminDto;
import ru.mashurov.admin.dto.RequestsPageDto;
import ru.mashurov.admin.service.AdminService;
import ru.mashurov.admin.service.RequestService;

@Slf4j
@Controller
@AllArgsConstructor
public class MainController {

	private final AdminService adminService;

	private final RequestService requestService;

	@GetMapping("/main")
	public String main() {

		return "main";
	}


	@GetMapping("/requests")
	public String requests(final Model model) {

		final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		final AdminDto admin = adminService.findByUsername(authentication.getName());

		final RequestsPageDto requestsPageDto
				= requestService.findAllByAdminIdWithSizeAndPage(admin.getId(), 7, 1);

		model.addAttribute("requests", requestsPageDto.getContent());
		model.addAttribute("role", authentication.getAuthorities().toArray()[0]);
		model.addAttribute("totalPages", requestsPageDto.getTotalPages());

		return "requests";
	}
}
