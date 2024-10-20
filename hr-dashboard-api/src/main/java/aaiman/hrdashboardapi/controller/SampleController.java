package aaiman.hrdashboardapi.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@Tag(name = "Sample")
public class SampleController {

        @PreAuthorize("hasAuthority('ADMIN')")
        @GetMapping("/admin")
        public String admin() {
                return "This is admin";
        }

        @PreAuthorize("hasAuthority('USER')")
        @GetMapping("/user")
        public String user() {
                return "This is user";
        }

        @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
        @GetMapping("/all")
        public String all() {
                return "This is all user";
        }

}
