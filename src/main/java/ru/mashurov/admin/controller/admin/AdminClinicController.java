package ru.mashurov.admin.controller.admin;

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
import ru.mashurov.admin.dto.ClinicDto;
import ru.mashurov.admin.dto.PageResolver;
import ru.mashurov.admin.dto.RegionDto;
import ru.mashurov.admin.service.AdminService;
import ru.mashurov.admin.service.ClinicService;
import ru.mashurov.admin.service.RegionService;

import java.util.List;
import java.util.stream.IntStream;

@Slf4j
@Controller
@AllArgsConstructor
@RequestMapping("/admin")
public class AdminClinicController extends CommonController {

    private final AdminService adminService;

    private final ClinicService clinicService;

    private final RegionService regionService;

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
        model.addAttribute("role", getRole());
        model.addAttribute("pageNumbers", IntStream.range(1, clinicsPage.getTotalPages() + 1).toArray());

        return "admin/clinics";
    }

    @GetMapping("/clinics/{id}/remove")
    public String remove(@PathVariable final Long id) {

        clinicService.deleteById(id);

        return "redirect:/admin/clinics";
    }

    @GetMapping("/clinics/{id}/update")
    public String pageUpdate(@PathVariable final Long id, final Model model) {

        final ClinicDto clinicDto = clinicService.findById(id);

        final List<RegionDto> regions = regionService.findAll();

        model.addAttribute("clinicDto", clinicDto);
        model.addAttribute("role", getRole());
        model.addAttribute("regions", regions);

        return "admin/clinic";
    }

    @PostMapping("/clinics/{id}/update")
    public String update(@ModelAttribute final ClinicDto clinicDto) {

        clinicService.update(clinicDto);

        return "redirect:/admin/clinics";
    }

    @GetMapping("/clinics/create")
    public String pageCreate(final Model model) {

        final List<RegionDto> regions = regionService.findAll();

        model.addAttribute("clinicDto", new ClinicDto());
        model.addAttribute("role", getRole());
        model.addAttribute("regions", regions);

        return "/admin/clinic";
    }

    @PostMapping("/clinics/create")
    public String create(@ModelAttribute final ClinicDto clinicDto) {

        clinicService.create(clinicDto);

        return "redirect:/admin/clinics";
    }
}
