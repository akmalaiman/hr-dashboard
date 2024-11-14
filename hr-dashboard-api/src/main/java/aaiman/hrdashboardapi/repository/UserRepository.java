package aaiman.hrdashboardapi.repository;

import aaiman.hrdashboardapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

        List<User> findAllByStatus(String status);

        User findByUsernameAndStatus(String username, String status);

        User findByEmailAndStatus(String email, String status);

}
