package ru.mashurov.admin.controller.major;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.mashurov.admin.dto.AdminDto;
import ru.mashurov.admin.dto.RequestsPageDto;
import ru.mashurov.admin.dto.ServicesPageDto;
import ru.mashurov.admin.service.AdminService;
import ru.mashurov.admin.service.RequestService;
import ru.mashurov.admin.service.ServiceService;

import java.util.stream.IntStream;

@Slf4j
@Controller
@AllArgsConstructor
@RequestMapping("/major")
public class MajorController {

    private final ServiceService serviceService;

    private final RequestService requestService;

    private final AdminService adminService;

    @GetMapping("/services")
    public String services(
            @RequestParam(defaultValue = "7") final Integer size,
            @RequestParam(defaultValue = "0") final Integer page,
            final Model model
    ) {

        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        final AdminDto admin = adminService.findByUsername(authentication.getName());

        final ServicesPageDto requestsPageDto
                = serviceService.findAllByAdminIdWithSizeAndPage(admin.getClinic().getId(), page, size);

        model.addAttribute("services", requestsPageDto.getContent());
        model.addAttribute("role", authentication.getAuthorities().toArray()[0]);
        model.addAttribute("pageNumbers", IntStream.range(1, requestsPageDto.getTotalPages()).toArray());

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

        final RequestsPageDto requestsPageDto
                = requestService.findAllByAdminIdWithSizeAndPage(admin.getId(), size, page);

        model.addAttribute("requests", requestsPageDto.getContent());
        model.addAttribute("role", authentication.getAuthorities().toArray()[0]);
        model.addAttribute("pageNumbers", IntStream.range(1, requestsPageDto.getTotalPages()).toArray());

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

}
