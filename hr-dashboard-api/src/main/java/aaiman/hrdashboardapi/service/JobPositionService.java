package aaiman.hrdashboardapi.service;

import aaiman.hrdashboardapi.model.JobPosition;
import aaiman.hrdashboardapi.repository.JobPositionRepository;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class JobPositionService {

        private final JobPositionRepository jobPositionRepository;

        public JobPositionService(JobPositionRepository jobPositionRepository) {
                this.jobPositionRepository = jobPositionRepository;
        }

        public List<JobPosition> getAll() {
                return jobPositionRepository.findAllByStatus("Active");
        }

        public JobPosition createJobPosition(JobPosition jobPosition, int userId) {

                JobPosition newJobPosition = new JobPosition();
                newJobPosition.setName(jobPosition.getName());
                newJobPosition.setStatus("Active");
                newJobPosition.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
                newJobPosition.setCreatedBy(userId);

                jobPositionRepository.save(jobPosition);
                return newJobPosition;

        }

        public JobPosition getActiveJobPositionByName(String name) {
                return jobPositionRepository.findByNameAndStatus(name, "Active");
        }

}
