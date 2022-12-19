package ru.mashurov.admin.controller.major;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.mashurov.admin.dto.AdminDto;
import ru.mashurov.admin.dto.PageResolver;
import ru.mashurov.admin.dto.VeterinarianDto;
import ru.mashurov.admin.service.AdminService;
import ru.mashurov.admin.service.VeterinarianService;

import java.util.stream.IntStream;

@Slf4j
@Controller
@AllArgsConstructor
@RequestMapping("/major")
public class MajorVeterinarianController {

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
        model.addAttribute("role", authentication.getAuthorities().toArray()[0]);
        model.addAttribute("pageNumbers", IntStream.range(1, veterinarianPage.getTotalPages()).toArray());

        return "major/veterinarians";
    }
}
