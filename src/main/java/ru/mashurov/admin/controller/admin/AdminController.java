package ru.mashurov.admin.controller.admin;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.mashurov.admin.dto.AdminCreateDto;
import ru.mashurov.admin.dto.AdminDto;
import ru.mashurov.admin.dto.ClinicDto;
import ru.mashurov.admin.dto.PageResolver;
import ru.mashurov.admin.service.AdminService;
import ru.mashurov.admin.service.ClinicService;

import java.util.stream.IntStream;

@Slf4j
@Controller
@AllArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    private final ClinicService clinicService;

    private final PasswordEncoder passwordEncoder;

    @GetMapping("/clinics")
    public String clinics(
            @RequestParam(defaultValue = "7") final Integer size,
            @RequestParam(defaultValue = "0") final Integer page,
            final Model model
    ) {

        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        final AdminDto admin = adminService.findByUsername(authentication.getName());

        final PageResolver<ClinicDto> clinicsPage
                = clinicService.findAllWithSizeAndPageAndRegion(size, page, admin.getClinic().getRegion().getCode());

        model.addAttribute("clinics", clinicsPage.getContent());
        model.addAttribute("role", authentication.getAuthorities().toArray()[0]);
        model.addAttribute("pageNumbers", IntStream.range(1, clinicsPage.getTotalPages() + 1).toArray());

        return "admin/clinics";
    }

    @GetMapping("/majors")
    public String majors(
            @RequestParam(defaultValue = "7") final Integer size,
            @RequestParam(defaultValue = "0") final Integer page,
            final Model model
    ) {

        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        final PageResolver<AdminDto> majorsPage = adminService.findAllMajorsWithSizeAndPage(size, page);

        model.addAttribute("majors", majorsPage.getContent());
        model.addAttribute("role", authentication.getAuthorities().toArray()[0]);
        model.addAttribute("pageNumbers", IntStream.range(1, majorsPage.getTotalPages()).toArray());

        return "admin/majors";
    }

    @GetMapping("/majors/create")
    public String pageCreate(final Model model) {

        model.addAttribute("adminCreateDto", new AdminCreateDto());

        return "admin/major";
    }

    @PostMapping("/majors/create")
    public String create(@ModelAttribute final AdminCreateDto adminCreateDto) {

        adminCreateDto.setPassword(passwordEncoder.encode(adminCreateDto.getPassword()));

        adminService.save(adminCreateDto);

        return "redirect:/admin/majors";
    }

    @GetMapping("/majors/{id}/update")
    public String pageUpdate(@PathVariable final Long id, final Model model) {

        final AdminDto major = adminService.findById(id);
        final AdminCreateDto adminCreateDto = new AdminCreateDto(
                major.getId(), major.getLogin(), passwordEncoder.encode(major.getPassword()), major.getClinic().getId()
        );

        model.addAttribute("adminCreateDto", adminCreateDto);

        return "admin/major";
    }

    @PostMapping("/majors/{id}/update")
    public String update(@ModelAttribute final AdminCreateDto adminCreateDto) {

        adminCreateDto.setPassword(passwordEncoder.encode(adminCreateDto.getPassword()));

        adminService.save(adminCreateDto);

        return "redirect:/admin/majors";
    }
}
