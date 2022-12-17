package ru.mashurov.admin.controller.major;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.mashurov.admin.dto.AdminDto;
import ru.mashurov.admin.dto.AppointmentRequestDto;
import ru.mashurov.admin.dto.VeterinarianDto;
import ru.mashurov.admin.dto.page.PageResolver;
import ru.mashurov.admin.service.AdminService;
import ru.mashurov.admin.service.RequestService;
import ru.mashurov.admin.service.ServiceService;
import ru.mashurov.admin.service.VeterinarianService;

import java.util.stream.IntStream;

@Slf4j
@Controller
@AllArgsConstructor
@RequestMapping("/major")
public class MajorController {

    private final ServiceService serviceService;

    private final RequestService requestService;

    private final AdminService adminService;

    private final VeterinarianService veterinarianService;

    @GetMapping("/services")
    public String services(
            @RequestParam(defaultValue = "7") final Integer size,
            @RequestParam(defaultValue = "0") final Integer page,
            final Model model
    ) {

        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        final AdminDto admin = adminService.findByUsername(authentication.getName());

        final PageResolver requestsPage
                = serviceService.findAllByAdminIdWithSizeAndPage(admin.getClinic().getId(), page, size);

        model.addAttribute("services", requestsPage.getContent());
        model.addAttribute("role", authentication.getAuthorities().toArray()[0]);
        model.addAttribute("pageNumbers", IntStream.range(1, requestsPage.getTotalPages()).toArray());

        return "services";
    }

    @DeleteMapping("/services/{serviceId}/remove")
    public String remove(@PathVariable final Long serviceId) {

        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        final AdminDto admin = adminService.findByUsername(authentication.getName());

        serviceService.removeServiceFromClinic(admin.getClinic().getId(), serviceId);

        return "redirect:/major/services";
    }

    @GetMapping("/requests")
    public String requests(
            @RequestParam(defaultValue = "7") final Integer size,
            @RequestParam(defaultValue = "0") final Integer page,
            final Model model
    ) {

        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        final AdminDto admin = adminService.findByUsername(authentication.getName());

        final PageResolver<AppointmentRequestDto> requestsPage
                = requestService.findAllByAdminIdWithSizeAndPage(admin.getId(), size, page);

        model.addAttribute("requests", requestsPage.getContent());
        model.addAttribute("role", authentication.getAuthorities().toArray()[0]);
        model.addAttribute("pageNumbers", IntStream.range(1, requestsPage.getTotalPages()).toArray());

        return "requests";
    }

    @PostMapping("/requests/{id}/approve")
    public String approve(@PathVariable final Long id) {

        requestService.approve(id);

        return "redirect:/major/requests";
    }

    @PostMapping("/requests/{id}/reject")
    public String reject(@PathVariable final Long id) {

        requestService.reject(id);

        return "redirect:/major/requests";
    }

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
        model.addAttribute("role", authentication.getAuthorities().toArray()[0]);
        model.addAttribute("pageNumbers", IntStream.range(1, veterinarianPage.getTotalPages()).toArray());

        return "veterinarians";
    }
}
