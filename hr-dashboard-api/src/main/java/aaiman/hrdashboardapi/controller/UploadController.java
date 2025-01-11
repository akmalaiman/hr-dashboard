package aaiman.hrdashboardapi.controller;

import aaiman.hrdashboardapi.dto.CsvProcessDto;
import aaiman.hrdashboardapi.service.UploadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/upload")
@Tag(name = "Upload CSV", description = "Handles uploading of CSV files for processing and storing in the database")
@Slf4j
public class UploadController {

    private final UploadService uploadService;

    public UploadController(UploadService uploadService) {
        this.uploadService = uploadService;
    }

    @PostMapping("/file")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Upload a CSV file for processing", description = "Uploads a CSV file for processing and storing in the database. Only admin users are allowed to upload a CSV file.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "CSV file uploaded successfully", content = @Content(schema = @Schema(implementation = CsvProcessDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(example = "Bad request"))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(example = "Internal server error")))
    })
    public ResponseEntity<CsvProcessDto> uploadFile(@RequestParam("file") MultipartFile file, HttpServletRequest request) {

        try {

            int userId = (Integer) request.getAttribute("userId");

            if (file.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }

            if (!file.getOriginalFilename().endsWith(".csv")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }

            CsvProcessDto result = uploadService.processFile(file, userId);

            return ResponseEntity.status(HttpStatus.OK).body(result);

        } catch (Exception e) {
            log.error("Error while uploading file: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

}
