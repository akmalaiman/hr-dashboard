package aaiman.hrdashboardapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/template")
@Tag(name = "CSV Template", description = "APIs for managing and downloading template files")
@Slf4j
public class TemplateController {

    @GetMapping("/download")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Download a template file")
    public ResponseEntity<Resource> downloadTemplate(@RequestParam("templateName") String filename) {

        try {

            String filePath = switch (filename.toLowerCase()) {
                case "staff" -> filePath = "template/staff.csv";
                case "jobposition" -> filePath = "template/job-position.csv";
                case "department" -> filePath = "template/department.csv";
                case "holiday" -> filePath = "template/holiday.csv";
                default -> filePath = null;
            };

            Resource resource = new ClassPathResource(filePath);

            if (!resource.exists()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + resource.getFilename())
                    .body(resource);

        } catch (Exception e) {
            log.error("Error downloading template file: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

}
