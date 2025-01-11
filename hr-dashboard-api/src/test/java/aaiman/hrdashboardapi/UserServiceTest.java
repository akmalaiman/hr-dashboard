package aaiman.hrdashboardapi;

import aaiman.hrdashboardapi.model.Department;
import aaiman.hrdashboardapi.model.JobPosition;
import aaiman.hrdashboardapi.model.Role;
import aaiman.hrdashboardapi.model.User;
import aaiman.hrdashboardapi.repository.JobPositionRepository;
import aaiman.hrdashboardapi.repository.RoleRepository;
import aaiman.hrdashboardapi.repository.UserRepository;
import aaiman.hrdashboardapi.service.UserService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

import java.sql.Array;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserServiceTest {
    
    @InjectMocks
    private UserService userService;
    
    @Mock
    private UserRepository userRepository;
    
    private User testUser;
    private JobPosition jobPosition;
    private Role role;
    private Department department;
    
    public static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16.3");
    
    @DynamicPropertySource
    public static void setDatasourceProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }
    
    @BeforeAll
    public static void beforeAll() {
        postgres.start();
    }
    
    @BeforeEach
    public void setUp() {
        jobPosition = new JobPosition();
        jobPosition.setName("Software Engineer");
        jobPosition.setStatus("Active");
        jobPosition.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
        jobPosition.setCreatedBy(1);
        
        department = new Department();
        department.setName("Engineering");
        department.setStatus("Active");
        department.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
        department.setCreatedBy(1);
        
        role = new Role();
        role.setName("ADMIN");
        
        Set<Role> adminRole = new HashSet<>();
        adminRole.add(role);
        
        testUser = new User();
        testUser.setId(1);
        testUser.setFirstName("John");
        testUser.setLastName("Doe");
        testUser.setUsername("john.doe");
        testUser.setEmail("john.doe@gmail.com");
        testUser.setPassword("password");
        testUser.setAddress("123 Main St");
        testUser.setCity("Anytown");
        testUser.setState("AnyState");
        testUser.setPostalCode(12345);
        testUser.setCountry("USA");
        testUser.setJobPositionId(jobPosition);
        testUser.setRoles(adminRole);
        testUser.setStatus("Active");
        testUser.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
        testUser.setCreatedBy(1);
        testUser.setDepartmentId(department);
        testUser.setReportingTo(1);
    }
    
    @AfterAll
    public static void afterAll() {
        postgres.stop();
    }
    
    @Test
    public void createUserTest() {
        
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        User newUser = userService.createUser(testUser, 1);
        
        assertNotNull(newUser);
        assertEquals(testUser.getFirstName(), newUser.getFirstName());
        assertEquals(testUser.getLastName(), newUser.getLastName());
        assertEquals(testUser.getUsername(), newUser.getUsername());
        assertEquals(testUser.getEmail(), newUser.getEmail());
        assertEquals(testUser.getAddress(), newUser.getAddress());
        assertEquals(testUser.getCity(), newUser.getCity());
        assertEquals(testUser.getState(), newUser.getState());
        assertEquals(testUser.getPostalCode(), newUser.getPostalCode());
        assertEquals(testUser.getCountry(), newUser.getCountry());
        assertEquals(testUser.getJobPositionId(), newUser.getJobPositionId());
        assertEquals(testUser.getRoles(), newUser.getRoles());
        assertEquals(testUser.getStatus(), newUser.getStatus());
        assertEquals(testUser.getCreatedBy(), newUser.getCreatedBy());
        assertEquals(testUser.getDepartmentId(), newUser.getDepartmentId());
        assertEquals(testUser.getReportingTo(), newUser.getReportingTo());
        verify(userRepository, times(1)).save(any(User.class));
        
    }
    
    @Test
    public void getAllActiveUsersTest() {
        
        when(userRepository.findAllByStatus("Active")).thenReturn(Arrays.asList(testUser));
        List<User> userList = userService.getAllActiveUsers();
        
        assertNotNull(userList);
        assertFalse(userList.isEmpty());
        assertEquals(1, userList.size());
        verify(userRepository, times(1)).findAllByStatus("Active");
        
    }
    
    @Test
    public void getActiveUserByUsernameTest() {
        
        when(userRepository.findByUsernameAndStatus("john.doe", "Active")).thenReturn(testUser);
        User user = userService.getActiveUserByUsername("john.doe");
        
        assertNotNull(user);
        assertEquals(testUser.getUsername(), user.getUsername());
        verify(userRepository, times(1)).findByUsernameAndStatus("john.doe", "Active");
        
    }
    
    @Test
    public void getActiveUserByEmailTest() {
        
        when(userRepository.findByEmailAndStatus("john.doe@gmail.com", "Active")).thenReturn(testUser);
        User user = userService.getActiveUserByEmail("john.doe@gmail.com");
        
        assertNotNull(user);
        assertEquals(testUser.getEmail(), user.getEmail());
        verify(userRepository, times(1)).findByEmailAndStatus("john.doe@gmail.com", "Active");
        
    }
    
    @Test
    public void deleteUserByIdTest_Success() {
        
        when(userRepository.updateUserStatus(anyString(), anyInt(), any(), anyInt())).thenReturn(1);
        int result = userService.deleteUserById(1, 1);
        
        assertEquals(1, result);
        verify(userRepository, times(1)).updateUserStatus(anyString(), anyInt(), any(), anyInt());
        
    }
    
    @Test
    public void deleteUserByIdTest_Failure() {
        
        when(userRepository.updateUserStatus(anyString(), anyInt(), any(), anyInt())).thenReturn(0);
        int result = userService.deleteUserById(1, 1);
        
        assertEquals(0, result);
        verify(userRepository, times(1)).updateUserStatus(anyString(), anyInt(), any(), anyInt());
        
    }
    
    @Test
    public void findUserByIdTest_Success() {
        
        when(userRepository.findById(1)).thenReturn(testUser);
        User user = userService.findUserById(1);
        
        assertNotNull(user);
        assertEquals(testUser.getId(), user.getId());
        assertEquals(testUser.getFirstName(), user.getFirstName());
        verify(userRepository, times(1)).findById(1);
        
    }
    
    @Test
    public void findUserByIdTest_Failure() {
        
        when(userRepository.findById(1)).thenReturn(null);
        User user = userService.findUserById(1);
        
        assertNull(user);
        verify(userRepository, times(1)).findById(1);
        
    }
    
    @Test
    public void updateUserTest() {
        
        User updatedUser = new User();
        updatedUser.setId(1);
        updatedUser.setFirstName("Jane");
        updatedUser.setLastName("Doe");
        updatedUser.setJobPositionId(jobPosition);
        updatedUser.setDepartmentId(department);
        updatedUser.setAddress("456 Main St");
        updatedUser.setCity("Anytown");
        updatedUser.setState("AnyState");
        updatedUser.setPostalCode(12345);
        updatedUser.setCountry("USA");
        
        when(userRepository.findById(1)).thenReturn(testUser);
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);
        
        User user = userService.updateUser(updatedUser, 1);
        
        assertNotNull(user);
        assertEquals(updatedUser.getFirstName(), user.getFirstName());
        assertEquals(updatedUser.getLastName(), user.getLastName());
        assertEquals(updatedUser.getJobPositionId(), user.getJobPositionId());
        assertEquals(updatedUser.getDepartmentId(), user.getDepartmentId());
        assertEquals(updatedUser.getAddress(), user.getAddress());
        assertEquals(updatedUser.getCity(), user.getCity());
        assertEquals(updatedUser.getState(), user.getState());
        assertEquals(updatedUser.getPostalCode(), user.getPostalCode());
        assertEquals(updatedUser.getCountry(), user.getCountry());
        verify(userRepository, times(1)).findById(1);
        verify(userRepository, times(1)).save(any(User.class));
        
    }
    
    @Test
    public void updateUserPasswordTest_Success() {
        
        when(userRepository.updatePassword(anyString(), anyInt(), any(Timestamp.class), anyInt())).thenReturn(1);
        int result = userService.updateUserPassword(1, "password", 1);
        
        assertEquals(1, result);
        verify(userRepository, times(1)).updatePassword(anyString(), anyInt(), any(Timestamp.class), anyInt());
        
    }
    
    @Test
    public void updateUserPasswordTest_Failure() {
        
        when(userRepository.updatePassword(anyString(), anyInt(), any(), anyInt())).thenReturn(0);
        int result = userService.updateUserPassword(1, "password", 1);
        
        assertEquals(0, result);
        verify(userRepository, times(1)).updatePassword(anyString(), anyInt(), any(), anyInt());
        
    }
    
    @Test
    public void getManagerByDepartmentListTest() {
        
        when(userRepository.findManagerByDepartment(1)).thenReturn(Arrays.asList(testUser));
        List<User> userList = userService.getManagerByDepartmentList(1);
        
        assertNotNull(userList);
        assertFalse(userList.isEmpty());
        assertEquals(1, userList.size());
        verify(userRepository, times(1)).findManagerByDepartment(1);
        
    }
    
}
