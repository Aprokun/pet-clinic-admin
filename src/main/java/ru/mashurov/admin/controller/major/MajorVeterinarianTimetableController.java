package ru.mashurov.admin.controller.major;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.mashurov.admin.controller.CommonController;
import ru.mashurov.admin.dto.VeterinarianTimetableCreateDto;
import ru.mashurov.admin.dto.VeterinarianTimetableDto;
import ru.mashurov.admin.service.VeterinarianTimetableService;

@Controller
@AllArgsConstructor
@RequestMapping("/major")
public class MajorVeterinarianTimetableController extends CommonController {

	private final VeterinarianTimetableService veterinarianTimetableService;

	@GetMapping("/veterinarians/{id}/change_timetable")
	public String pageUpdateTimetable(@PathVariable final Long id, final Model model) {

		final VeterinarianTimetableDto veterinarianTimetableDto
				= veterinarianTimetableService.findTimetableByVeterinarianId(id);

		model.addAttribute("timetable", veterinarianTimetableDto.getTimetable());
		model.addAttribute("role", getRole());
		model.addAttribute("vetId", id);
		model.addAttribute("timetableId", veterinarianTimetableDto.getId());

		return "major/timetable_veterinarians";
	}

	@PostMapping("/veterinarians/{id}/change_timetable")
	public String updateTimetable(@RequestBody final VeterinarianTimetableCreateDto createDto) {

		veterinarianTimetableService.update(createDto);

		return "redirect:/major/veterinarians";
	}
}
