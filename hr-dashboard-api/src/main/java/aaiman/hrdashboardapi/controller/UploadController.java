package aaiman.hrdashboardapi.controller;

import aaiman.hrdashboardapi.dto.CsvProcessDto;
import aaiman.hrdashboardapi.service.UploadService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/upload")
@Tag(name = "Upload CSV", description = "This API process the contents of CSV and store it in database")
@Slf4j
public class UploadController {

        private final UploadService uploadService;

        public UploadController(UploadService uploadService) {
                this.uploadService = uploadService;
        }

        @PostMapping("/file")
        @PreAuthorize("hasAuthority('ADMIN')")
        public ResponseEntity<CsvProcessDto> uploadFile(@RequestParam("file") MultipartFile file) {

                try {

                        if (file.isEmpty()) {
                                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
                        }

                        if (!file.getOriginalFilename().endsWith(".csv")) {
                                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
                        }

                        CsvProcessDto result = uploadService.processFile(file);

                        return ResponseEntity.status(HttpStatus.OK).body(result);

                } catch (Exception e) {
                        log.error("Error while uploading file: {}", e.getMessage());
                        return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                }

        }

}
