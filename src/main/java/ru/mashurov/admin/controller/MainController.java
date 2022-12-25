package ru.mashurov.admin.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@AllArgsConstructor
@RequestMapping("/")
public class MainController extends CommonController {

	@GetMapping
	public String main(final Model model) {

		model.addAttribute("role", getRole());

		return "main";
	}
}
