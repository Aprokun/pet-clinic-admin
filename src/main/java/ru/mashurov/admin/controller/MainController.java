package ru.mashurov.admin.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Objects;

@Slf4j
@Controller
@AllArgsConstructor
@RequestMapping(value = { "", "/" })
public class MainController extends CommonController {

	@GetMapping
	public String main(final Model model) {

		if (Objects.equals(getRole(), "ROLE_ADMIN")) {
			return "redirect:/admin/majors";
		} else {
			return "redirect:/major/requests";
		}
	}
}
