package aaiman.hrdashboardapi;

import aaiman.hrdashboardapi.model.JobPosition;
import aaiman.hrdashboardapi.model.Role;
import aaiman.hrdashboardapi.model.UserDetails;
import aaiman.hrdashboardapi.repository.JobPositionRepository;
import aaiman.hrdashboardapi.repository.UserDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SpringBootApplication
public class HrDashboardApiApplication implements CommandLineRunner {

        @Autowired
        private JobPositionRepository jobPositionRepository;

        @Autowired
        private UserDetailsRepository userDetailsRepository;

        public static void main(String[] args) {
                SpringApplication.run(HrDashboardApiApplication.class, args);
        }

        public void run(String... args) {

                List<JobPosition> jobPositionList = jobPositionRepository.findAllByStatus("Active");
                if (jobPositionList.isEmpty()) {

                        JobPosition jobPosition = new JobPosition();
                        jobPosition.setName("Administrator");
                        jobPosition.setStatus("Active");
                        jobPosition.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
                        jobPosition.setCreatedBy(1);
                        jobPositionRepository.save(jobPosition);

                }

                List<UserDetails> userDetailsList = userDetailsRepository.findAllByStatus("Active");
                if (userDetailsList.isEmpty()) {

                        JobPosition jobPosition = jobPositionRepository.findByNameAndStatus("Administrator", "Active");

                        UserDetails user = new UserDetails();
                        user.setFirstName("");
                        user.setLastName("");
                        user.setUsername("admin");
                        user.setEmail("admin@gmail.com");
                        user.setPassword(new BCryptPasswordEncoder().encode("admin"));
                        user.setJobPositionId(jobPosition);
                        user.setRole(Role.ADMIN);
                        user.setStatus("Active");
                        user.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
                        user.setCreatedBy(1);
                        userDetailsRepository.save(user);

                }

        }

}
