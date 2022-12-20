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
import ru.mashurov.admin.dto.AdminDto;
import ru.mashurov.admin.dto.PageResolver;
import ru.mashurov.admin.dto.ServiceClinicDto;
import ru.mashurov.admin.dto.ServiceDto;
import ru.mashurov.admin.service.AdminService;
import ru.mashurov.admin.service.ServiceService;

import java.util.stream.IntStream;

@Slf4j
@Controller
@AllArgsConstructor
@RequestMapping("/major")
public class MajorServiceController {

    private final AdminService adminService;

    private final ServiceService serviceService;

    @GetMapping("/services")
    public String services(
            @RequestParam(defaultValue = "7") final Integer size,
            @RequestParam(defaultValue = "0") final Integer page,
            final Model model
    ) {

	    final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

	    final AdminDto admin = adminService.findByUsername(authentication.getName());

	    final PageResolver<ServiceDto> requestsPage
			    = serviceService.findAllByAdminIdWithSizeAndPage(admin.getClinic().getId(), page, size);

	    model.addAttribute("services", requestsPage.getContent());
	    model.addAttribute("role", authentication.getAuthorities().toArray()[0]);
	    model.addAttribute("pageNumbers", IntStream.range(0, requestsPage.getTotalPages()).toArray());

	    return "major/services";
    }

	@GetMapping("/services/{id}/remove")
	public String remove(@PathVariable final Long id) {

		serviceService.deleteById(id);

		return "redirect:/major/services";
	}

	@GetMapping("/services/{id}/update")
	public String pageUpdate(@PathVariable final Long id, final Model model) {

		final ServiceDto serviceDto = serviceService.findById(id);

		model.addAttribute("serviceDto", serviceDto);

		return "major/service";
	}

	@PostMapping("/services/{id}/update")
	public String update(@ModelAttribute final ServiceClinicDto serviceClinicDto) {

		final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		final Long clinicId = adminService.findByUsername(authentication.getName()).getClinic().getId();

		serviceClinicDto.setClinicId(clinicId);

		serviceService.update(serviceClinicDto);

		return "redirect:/major/services";
	}

	@GetMapping("/services/create")
	public String pageCreate(final Model model) {

		model.addAttribute("serviceDto", new ServiceDto());

		return "/major/service";
	}

	@PostMapping("/services/create")
	public String create(@ModelAttribute final ServiceClinicDto serviceClinicDto) {

		final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		final Long clinicId = adminService.findByUsername(authentication.getName()).getClinic().getId();

		serviceClinicDto.setClinicId(clinicId);

		serviceService.create(serviceClinicDto);

		return "redirect:/major/services";
	}
}
