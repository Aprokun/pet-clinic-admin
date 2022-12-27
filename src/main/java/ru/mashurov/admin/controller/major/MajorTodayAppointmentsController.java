package ru.mashurov.admin.controller.major;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.mashurov.admin.controller.CommonController;
import ru.mashurov.admin.dto.AppointmentRequestDto;
import ru.mashurov.admin.dto.PageResolver;
import ru.mashurov.admin.service.AdminService;
import ru.mashurov.admin.service.RequestService;

import java.util.stream.IntStream;

@Slf4j
@Controller
@AllArgsConstructor
@RequestMapping("/major")
public class MajorTodayAppointmentsController extends CommonController {

	private final RequestService requestService;

	private final AdminService adminService;

	@GetMapping("/today_appointments")
	public String pageTodayAppointments(
			@RequestParam(defaultValue = "7") final Integer size,
			@RequestParam(defaultValue = "0") final Integer page,
			final Model model
	) {

		final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		final Long adminId = adminService.findByUsername(authentication.getName()).getId();

		final PageResolver<AppointmentRequestDto> requestsPage
				= requestService.findTodayAppointmentRequests(size, page, adminId);

		model.addAttribute("requests", requestsPage.getContent());
		model.addAttribute("role", getRole());
		model.addAttribute("pageNumbers", IntStream.range(0, requestsPage.getTotalPages()).toArray());

		return "major/today_appointments";
	}

	@PostMapping("/today_appointments/{reqId}/visited")
	public String setVisited(@PathVariable final Long reqId) {

		requestService.setRequestVisited(reqId, true);

		return "redirect:/major/today_appointments";
	}

	@PostMapping("/today_appointments/{reqId}/not_visited")
	public String setNotVisited(@PathVariable final Long reqId) {

		requestService.setRequestVisited(reqId, false);

		return "redirect:/major/today_appointments";
	}
}
