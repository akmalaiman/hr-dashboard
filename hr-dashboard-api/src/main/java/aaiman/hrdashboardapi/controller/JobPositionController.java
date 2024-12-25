package aaiman.hrdashboardapi.controller;

import aaiman.hrdashboardapi.dto.JobPositionDto;
import aaiman.hrdashboardapi.model.JobPosition;
import aaiman.hrdashboardapi.service.JobPositionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
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
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jobPosition")
@Tag(name = "Job Position", description = "Handles job positions management operations such as creation, retrieval, and updating of job position.")
@Slf4j
public class JobPositionController {

    private final JobPositionService jobPositionService;

    public JobPositionController(JobPositionService jobPositionService) {
        this.jobPositionService = jobPositionService;
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Get all active job positions", description = "Fetches all active job positions from the system. Only admin users are allowed to get all active job positions.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No content found", content = @Content(schema = @Schema(example = "No job position found"))),
            @ApiResponse(responseCode = "200", description = "List of job positions found", content = @Content(array = @ArraySchema(schema = @Schema(implementation = JobPosition.class))))
    })
    public ResponseEntity<List<JobPosition>> getAllJobPositions() {

        List<JobPosition> jobPositionList = jobPositionService.getAll();

        if (jobPositionList.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(jobPositionList);
        }

    }

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Add a new job position", description = "Creates a new job position in the system. Only admin users are allowed to create a new job position.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Job position created successfully", content = @Content(schema = @Schema(implementation = JobPosition.class))),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(example = "Bad request"))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(example = "Internal server error")))
    })
    public ResponseEntity<JobPosition> addJobPosition(@RequestBody JobPosition jobPosition, HttpServletRequest request) {

        try {

            int userId = (Integer) request.getAttribute("userId");

            JobPosition createdJobPosition = jobPositionService.createJobPosition(jobPosition, userId);

            if (createdJobPosition != null) {
                return ResponseEntity.status(HttpStatus.CREATED).body(createdJobPosition);
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }

        } catch (NullPointerException e) {
            log.error("Error while creating new Job Position: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

    }

    @GetMapping("/byName")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Get active job position by name", description = "Fetches an active job position by name from the system. Only admin users are allowed to get an active job position by name.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Job position found", content = @Content(schema = @Schema(implementation = JobPosition.class))),
            @ApiResponse(responseCode = "409", description = "Job position already exists", content = @Content(schema = @Schema(example = "Job position already exists")))
    })
    public ResponseEntity<JobPosition> getActiveJobPositionByName(@RequestParam("name") String name) {

        JobPosition jobPosition = jobPositionService.getActiveJobPositionByName(name);

        if (jobPosition == null) {
            return ResponseEntity.status(HttpStatus.OK).body(null);
        }

        return ResponseEntity.status(HttpStatus.CONFLICT).body(null);

    }

    @GetMapping("/nameWithCount")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Get all active job positions with staff count", description = "Fetches all active job positions with staff count from the system. Only admin users are allowed to get all active job positions with staff count.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No content found", content = @Content(schema = @Schema(example = "No job position found"))),
            @ApiResponse(responseCode = "200", description = "List of job positions found", content = @Content(array = @ArraySchema(schema = @Schema(implementation = JobPositionDto.class))))
    })
    public ResponseEntity<List<JobPositionDto>> getAllActiveJobPositionsWithCount() {

        List<JobPositionDto> jobPositionCountList = jobPositionService.getAllActiveJobPositionsWithCount();

        if (jobPositionCountList.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(jobPositionCountList);
        }

    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Delete a job position by id", description = "Deletes a job position by id from the system. Only admin users are allowed to delete a job position by id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Job position deleted successfully", content = @Content(schema = @Schema(example = "Job position deleted successfully"))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(example = "Internal server error")))
    })
    public ResponseEntity<JobPosition> deleteJobPosition(@RequestParam("id") int id, HttpServletRequest request) {

        int userId = (Integer) request.getAttribute("userId");

        int updateStatus = jobPositionService.deleteJobPositionById(id, userId);

        if (updateStatus == 0) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

        return ResponseEntity.status(HttpStatus.OK).body(null);

    }

}
