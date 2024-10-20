package aaiman.hrdashboardapi.controller;

import aaiman.hrdashboardapi.model.JobPosition;
import aaiman.hrdashboardapi.service.JobPositionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/jobPosition")
@Tag(name = "Job Position", description = "Handles job positions management operations such as creation, retrieval, and updating of job position.")
public class JobPositionController {

        private final JobPositionService jobPositionService;

        public JobPositionController(JobPositionService jobPositionService) {
                this.jobPositionService = jobPositionService;
        }

        @GetMapping("/all")
        public ResponseEntity<List<JobPosition>> getAllJobPositions() {

                List<JobPosition> jobPositionList = jobPositionService.getAll();

                if (jobPositionList.isEmpty()) {
                        return ResponseEntity.noContent().build();
                } else {
                        return ResponseEntity.ok(jobPositionList);
                }

        }

}
