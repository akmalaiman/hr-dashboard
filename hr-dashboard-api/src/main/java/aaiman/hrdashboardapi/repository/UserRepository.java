package aaiman.hrdashboardapi.repository;

import aaiman.hrdashboardapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    
    List<User> findAllByStatus(String status);
    
    User findByUsernameAndStatus(String username, String status);
    
    User findByEmailAndStatus(String email, String status);
    
    @Modifying
    @Transactional
    @Query(
            value = "UPDATE \"user\" SET status = :status, updated_by = :updatedBy, updated_at = :updatedAt WHERE id = :id",
            nativeQuery = true
    )
    int updateUserStatus(@Param("status") String status, @Param("updatedBy") int updatedBy, @Param("updatedAt") Timestamp updatedAt, @Param("id") int id);
    
    User findById(int id);
    
    @Modifying
    @Transactional
    @Query(
            value = "UPDATE \"user\" SET password = :password, updated_by = :updatedBy, updated_at = :updatedAt WHERE id = :id",
            nativeQuery = true
    )
    int updatePassword(@Param("password") String password, @Param("updatedBy") int updatedBy, @Param("updatedAt") Timestamp updatedAt, @Param("id") int id);
    
    @Query(
            value = "SELECT u.* FROM \"user\" u " +
                    "INNER JOIN user_roles ur ON ur.user_id = u.id " +
                    "INNER JOIN \"role\" r ON r.id = ur.role_id " +
                    "WHERE u.status = 'Active' AND r.id = 3 AND u.department_id = :departmentId",
            nativeQuery = true
    )
    List<User> findManagerByDepartment(@Param("departmentId") int departmentId);
    
}
