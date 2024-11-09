package aaiman.hrdashboardapi.service;

import aaiman.hrdashboardapi.model.User;
import aaiman.hrdashboardapi.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserService {

        private final UserRepository userRepository;

        public UserService(UserRepository userRepository) {
                this.userRepository = userRepository;
        }

        public User createUser(User user, int userId) {

                User newUser = new User();
                newUser.setFirstName(user.getFirstName());
                newUser.setLastName(user.getLastName());
                newUser.setUsername(user.getUsername());
                newUser.setEmail(user.getEmail());
                newUser.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
                newUser.setAddress(user.getAddress());
                newUser.setCity(user.getCity());
                newUser.setState(user.getState());
                newUser.setPostalCode(user.getPostalCode());
                newUser.setCountry(user.getCountry());
                newUser.setJobPositionId(user.getJobPositionId());
                newUser.setRoles(user.getRoles());
                newUser.setStatus("Active");
                newUser.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
                newUser.setCreatedBy(userId);

                userRepository.save(newUser);
                return newUser;

        }

        public List<User> getAllActiveUsers() {
                return userRepository.findAllByStatus("Active");
        }

}
