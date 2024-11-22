package aaiman.hrdashboardapi;

import aaiman.hrdashboardapi.model.JobPosition;
import aaiman.hrdashboardapi.model.Role;
import aaiman.hrdashboardapi.model.User;
import aaiman.hrdashboardapi.repository.JobPositionRepository;
import aaiman.hrdashboardapi.repository.RoleRepository;
import aaiman.hrdashboardapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SpringBootApplication
public class HrDashboardApiApplication implements CommandLineRunner {

        @Autowired
        private JobPositionRepository jobPositionRepository;

        @Autowired
        private UserRepository userRepository;

        @Autowired
        private RoleRepository roleRepository;

        public static void main(String[] args) {
                SpringApplication.run(HrDashboardApiApplication.class, args);
        }

        public void run(String... args) {

                List<Role> roleList = roleRepository.findAll();
                if (roleList.isEmpty()) {

                        Role adminRole = new Role();
                        adminRole.setName("ADMIN");
                        roleRepository.save(adminRole);

                }

                List<JobPosition> jobPositionList = jobPositionRepository.findAllByStatus("Active");
                if (jobPositionList.isEmpty()) {

                        JobPosition jobPosition = new JobPosition();
                        jobPosition.setName("Administrator");
                        jobPosition.setStatus("Active");
                        jobPosition.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
                        jobPosition.setCreatedBy(1);
                        jobPositionRepository.save(jobPosition);

                }

                List<User> userDetailsList = userRepository.findAllByStatus("Active");
                if (userDetailsList.isEmpty()) {

                        JobPosition jobPosition = jobPositionRepository.findByNameAndStatus("Administrator", "Active");

                        Role adminRole = roleRepository.findByName("ADMIN");

                        Set<Role> role = new HashSet<>();
                        role.add(adminRole);

                        User user = new User();
                        user.setFirstName("");
                        user.setLastName("");
                        user.setUsername("admin");
                        user.setEmail("admin@gmail.com");
                        user.setPassword(new BCryptPasswordEncoder().encode("admin"));
                        user.setJobPositionId(jobPosition);
                        user.setRoles(role);
                        user.setStatus("Active");
                        user.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
                        user.setCreatedBy(1);
                        userRepository.save(user);

                }

        }

}
