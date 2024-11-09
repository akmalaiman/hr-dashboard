package aaiman.hrdashboardapi.service;

import aaiman.hrdashboardapi.model.JobPosition;
import aaiman.hrdashboardapi.repository.JobPositionRepository;
import org.springframework.stereotype.Service;

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

}
