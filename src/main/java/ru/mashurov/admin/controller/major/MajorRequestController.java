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
import ru.mashurov.admin.dto.AdminDto;
import ru.mashurov.admin.dto.AppointmentRequestDto;
import ru.mashurov.admin.dto.PageResolver;
import ru.mashurov.admin.service.AdminService;
import ru.mashurov.admin.service.RequestService;

import java.util.List;
import java.util.stream.IntStream;

import static ru.mashurov.admin.values.AppointmentRequestStatusValues.UNHANDLED;

@Slf4j
@Controller
@AllArgsConstructor
@RequestMapping("/major")
public class MajorRequestController {

    private final RequestService requestService;

    private final AdminService adminService;


    @GetMapping("/requests")
    public String requests(
            @RequestParam(defaultValue = "7") final Integer size,
            @RequestParam(defaultValue = "0") final Integer page,
            @RequestParam(required = false) List<String> statuses,
            final Model model
    ) {

        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        final AdminDto admin = adminService.findByUsername(authentication.getName());

        if (statuses != null) {
            statuses.add(UNHANDLED);
        } else {
            statuses = List.of(UNHANDLED);
        }

        final PageResolver<AppointmentRequestDto> requestsPage
                = requestService.findAllByStatusAndAdminIdWithSizeAndPage(statuses, admin.getId(), size, page);

        model.addAttribute("requests", requestsPage.getContent());
        model.addAttribute("role", authentication.getAuthorities().toArray()[0]);
        model.addAttribute("pageNumbers", IntStream.range(1, requestsPage.getTotalPages()).toArray());

        return "major/requests";
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
