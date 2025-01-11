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
        newUser.setDepartmentId(user.getDepartmentId());
        
        userRepository.save(newUser);
        return newUser;
        
    }
    
    public List<User> getAllActiveUsers() {
        return userRepository.findAllByStatus("Active");
    }
    
    public User getActiveUserByUsername(String username) {
        return userRepository.findByUsernameAndStatus(username, "Active");
    }
    
    public User getActiveUserByEmail(String email) {
        return userRepository.findByEmailAndStatus(email, "Active");
    }
    
    public int deleteUserById(int userId, int requestorId) {
        Timestamp now = Timestamp.valueOf(LocalDateTime.now());
        return userRepository.updateUserStatus("Deleted", requestorId, now, userId);
    }
    
    public User findUserById(int userId) {
        return userRepository.findById(userId);
    }
    
    public User updateUser(User user, int userId) {
        
        User existingUser = userRepository.findById(user.getId());
        
        existingUser.setFirstName(user.getFirstName());
        existingUser.setLastName(user.getLastName());
        existingUser.setJobPositionId(user.getJobPositionId());
        existingUser.setDepartmentId(user.getDepartmentId());
        existingUser.setAddress(user.getAddress());
        existingUser.setCity(user.getCity());
        existingUser.setState(user.getState());
        existingUser.setPostalCode(user.getPostalCode());
        existingUser.setCountry(user.getCountry());
        existingUser.setUpdatedBy(userId);
        existingUser.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
        
        existingUser.getRoles().clear();
        existingUser.getRoles().addAll(user.getRoles());
        
        return userRepository.save(existingUser);
    }
    
    public int updateUserPassword(int userId, String password, int id) {
        
        Timestamp now = Timestamp.valueOf(LocalDateTime.now());
        password = new BCryptPasswordEncoder().encode(password);
        return userRepository.updatePassword(password, userId, now, id);
        
    }
    
    public List<User> getManagerByDepartmentList(int departmentId) {
        return userRepository.findManagerByDepartment(departmentId);
    }
    
}
