package aaiman.hrdashboardapi.repository;

import aaiman.hrdashboardapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

        List<User> findAllByStatus(String status);

        /*@Query(value = "SELECT u.username, u.password, r.name " +
                "FROM \"user\" u " +
                "LEFT JOIN user_roles ur ON u.id = ur.user_id " +
                "LEFT JOIN \"role\" r ON ur.role_id = r.id " +
                "WHERE u.username = :username", nativeQuery = true)*/
        User findByUsername(@Param("username") String username);

}
