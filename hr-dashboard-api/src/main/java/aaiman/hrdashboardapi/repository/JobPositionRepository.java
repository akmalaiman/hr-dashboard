package aaiman.hrdashboardapi.repository;

import aaiman.hrdashboardapi.model.JobPosition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobPositionRepository extends JpaRepository<JobPosition, Integer> {

        List<JobPosition> findAllByStatus(String status);

        JobPosition findByNameAndStatus(String name, String status);

}
