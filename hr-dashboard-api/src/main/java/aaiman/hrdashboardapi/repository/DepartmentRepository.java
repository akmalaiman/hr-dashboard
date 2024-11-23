package aaiman.hrdashboardapi.repository;

import aaiman.hrdashboardapi.model.Department;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Integer> {

        List<Department> findAllByStatus(String status);

        Department findByNameAndStatus(String name, String status);

        @Query(
                value = "SELECT d.id as id, d.\"name\" AS name, COUNT(u.id) AS staffDepartmentCount " +
                        "FROM department d " +
                        "LEFT JOIN \"user\" u ON u.job_position_id = d.id AND u.status = 'Active' " +
                        "WHERE d.status = 'Active' " +
                        "GROUP BY d.id " +
                        "ORDER BY d.\"name\"",
                nativeQuery = true
        )
        List<Map<String, Object>> findAllActiveWithCount();

        @Modifying
        @Transactional
        @Query(
                value = "UPDATE department SET status = :status, updated_by = :updatedBy, updated_at = :updatedAt WHERE id = :id",
                nativeQuery = true
        )
        int updateDepartmentStatusById(@Param("status") String status, @Param("updatedBy") int updatedBy, @Param("updatedAt") Timestamp updatedAt, @Param("id") int id);

}
