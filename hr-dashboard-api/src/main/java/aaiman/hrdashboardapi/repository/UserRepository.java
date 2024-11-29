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
        @Query(value = "UPDATE \"user\" SET status = :status, updated_by = :updatedBy, updated_at = :updatedAt WHERE id = :id", nativeQuery = true)
        int updateUserStatus(@Param("status") String status, @Param("updatedBy") int updatedBy, @Param("updatedAt") Timestamp updatedAt, @Param("id") int id);

        User findById(int id);

}
