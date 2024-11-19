package aaiman.hrdashboardapi.controller;

import aaiman.hrdashboardapi.dto.JobPositionDto;
import aaiman.hrdashboardapi.model.JobPosition;
import aaiman.hrdashboardapi.service.JobPositionService;
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
        public ResponseEntity<JobPosition> getActiveJobPositionByName(@RequestParam("name") String name) {

                JobPosition jobPosition = jobPositionService.getActiveJobPositionByName(name);

                if (jobPosition == null) {
                        return ResponseEntity.status(HttpStatus.OK).body(null);
                }

                return ResponseEntity.status(HttpStatus.CONFLICT).body(null);

        }

        @GetMapping("/nameWithCount")
        @PreAuthorize("hasAuthority('ADMIN')")
        public ResponseEntity<List<JobPositionDto>> getAllActiveJobPositionsWIthCount() {

                List<JobPositionDto> jobPositionCountList = jobPositionService.getAllActiveJobPositionsWIthCount();

                if (jobPositionCountList.isEmpty()) {
                        return ResponseEntity.noContent().build();
                } else {
                        return ResponseEntity.status(HttpStatus.OK).body(jobPositionCountList);
                }

        }

        @DeleteMapping("/delete")
        @PreAuthorize("hasAuthority('ADMIN')")
        public ResponseEntity<JobPosition> deleteJobPosition(@RequestParam("id") int id, HttpServletRequest request) {

                int userId = (Integer) request.getAttribute("userId");

                int updateStatus = jobPositionService.deleteJobPositionById(id, userId);

                if (updateStatus == 0) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
                }

                return ResponseEntity.status(HttpStatus.OK).body(null);

        }

}
