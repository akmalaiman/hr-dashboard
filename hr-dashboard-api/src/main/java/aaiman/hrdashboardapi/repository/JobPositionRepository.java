package aaiman.hrdashboardapi.repository;

import aaiman.hrdashboardapi.model.JobPosition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface JobPositionRepository extends JpaRepository<JobPosition, Integer> {

        List<JobPosition> findAllByStatus(String status);

        JobPosition findByNameAndStatus(String name, String status);

        @Query(
                value = "SELECT jp.\"name\" AS name, COUNT(u.id) AS staffJobCount " +
                        "FROM job_position jp " +
                        "LEFT JOIN \"user\" u ON u.job_position_id = jp.id AND u.status = 'Active' " +
                        "WHERE jp.status = 'Active' " +
                        "GROUP BY jp.id " +
                        "ORDER BY jp.\"name\"",
                nativeQuery = true
        )
        List<Map<String, Object>> findAllActiveWithCount();

}
