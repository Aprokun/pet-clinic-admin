package ru.mashurov.admin.controller.major;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.mashurov.admin.controller.CommonController;
import ru.mashurov.admin.dto.AdminDto;
import ru.mashurov.admin.dto.PageResolver;
import ru.mashurov.admin.dto.VeterinarianCreateDto;
import ru.mashurov.admin.dto.VeterinarianDto;
import ru.mashurov.admin.service.AdminService;
import ru.mashurov.admin.service.VeterinarianService;

import java.util.stream.IntStream;

@Slf4j
@Controller
@AllArgsConstructor
@RequestMapping("/major")
public class MajorVeterinarianController extends CommonController {

	private final AdminService adminService;

	private final VeterinarianService veterinarianService;

	@GetMapping("/veterinarians")
	public String veterinarians(
			@RequestParam(defaultValue = "7") final Integer size,
			@RequestParam(defaultValue = "0") final Integer page,
			final Model model
    ) {

        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        final AdminDto admin = adminService.findByUsername(authentication.getName());

	    final PageResolver<VeterinarianDto> veterinarianPage
			    = veterinarianService.findAllByClinicId(admin.getClinic().getId(), size, page);

		model.addAttribute("veterinarians", veterinarianPage.getContent());
		model.addAttribute("role", getRole());
		model.addAttribute("pageNumbers", IntStream.range(1, veterinarianPage.getTotalPages()).toArray());

	    return "major/veterinarians";
    }

	@GetMapping("/veterinarians/{id}/remove")
	public String remove(@PathVariable final Long id) {

		veterinarianService.deleteById(id);

		return "redirect:/major/veterinarians";
	}

	@GetMapping("/veterinarians/{id}/update")
	public String pageUpdate(@PathVariable final Long id, final Model model) {

		final VeterinarianDto veterinarianDto = veterinarianService.findById(id);

		model.addAttribute("veterinarianDto", veterinarianDto);
		model.addAttribute("role", getRole());

		return "major/veterinarian";
	}

	@PostMapping("/veterinarians/{id}/update")
	public String update(@ModelAttribute final VeterinarianDto veterinarianUpdateDto) {

		veterinarianService.update(veterinarianUpdateDto);

		return "redirect:/major/veterinarians";
	}

	@GetMapping("/veterinarians/create")
	public String pageCreate(final Model model) {

		model.addAttribute("veterinarianDto", new VeterinarianCreateDto());
		model.addAttribute("role", getRole());

		return "major/veterinarian";
	}

	@PostMapping("/veterinarians/create")
	public String create(@ModelAttribute final VeterinarianCreateDto veterinarianCreateDto) {

		final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		final Long clinicId = adminService.findByUsername(authentication.getName()).getClinic().getId();

		veterinarianCreateDto.setClinicId(clinicId);

		veterinarianService.create(veterinarianCreateDto);

		return "redirect:/major/veterinarians";
	}
}
