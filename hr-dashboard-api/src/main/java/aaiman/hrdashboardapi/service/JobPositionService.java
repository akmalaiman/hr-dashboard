package aaiman.hrdashboardapi.service;

import aaiman.hrdashboardapi.dto.JobPositionDto;
import aaiman.hrdashboardapi.model.JobPosition;
import aaiman.hrdashboardapi.model.User;
import aaiman.hrdashboardapi.repository.JobPositionRepository;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

                jobPositionRepository.save(newJobPosition);
                return newJobPosition;

        }

        public JobPosition getActiveJobPositionByName(String name) {
                return jobPositionRepository.findByNameAndStatus(name, "Active");
        }

        public List<JobPositionDto> getAllActiveJobPositionsWIthCount() {
                List<Map<String, Object>> rawResults = jobPositionRepository.findAllActiveWithCount();
                return rawResults.stream()
                        .map(row -> {
                                JobPositionDto jobPositionDto = new JobPositionDto();
                                jobPositionDto.setName((String) row.get("name"));
                                jobPositionDto.setStaffCount(((Number) row.get("staffJobCount")).intValue());
                                return jobPositionDto;
                        }).collect(Collectors.toList());
        }

        public int deleteJobPositionById(int jobPositionId, int userId) {
                Timestamp now = Timestamp.valueOf(LocalDateTime.now());
                return jobPositionRepository.updateJobPositionStatusById("Deleted", userId, now, jobPositionId);
        }

}
