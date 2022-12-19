package ru.mashurov.admin.controller.major;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.mashurov.admin.dto.AdminDto;
import ru.mashurov.admin.dto.PageResolver;
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

        final PageResolver requestsPage
                = serviceService.findAllByAdminIdWithSizeAndPage(admin.getClinic().getId(), page, size);

        model.addAttribute("services", requestsPage.getContent());
        model.addAttribute("role", authentication.getAuthorities().toArray()[0]);
        model.addAttribute("pageNumbers", IntStream.range(1, requestsPage.getTotalPages()).toArray());

        return "major/services";
    }

    @DeleteMapping("/services/{serviceId}/remove")
    public String remove(@PathVariable final Long serviceId) {

        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        final AdminDto admin = adminService.findByUsername(authentication.getName());

        serviceService.removeServiceFromClinic(admin.getClinic().getId(), serviceId);

        return "redirect:/major/services";
    }
}
