package aaiman.hrdashboardapi;

import aaiman.hrdashboardapi.dto.DepartmentDto;
import aaiman.hrdashboardapi.model.Department;
import aaiman.hrdashboardapi.repository.DepartmentRepository;
import aaiman.hrdashboardapi.service.DepartmentService;
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

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DepartmentServiceTest {

        @InjectMocks
        private DepartmentService departmentService;

        @Mock
        private DepartmentRepository departmentRepository;

        private Department testDepartment;

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

        @AfterAll
        public static void afterAll() {
                postgres.stop();
        }

        @BeforeEach
        public void setUp() {
                testDepartment = new Department();
                testDepartment.setId(1);
                testDepartment.setName("Engineering");
                testDepartment.setStatus("Active");
                testDepartment.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
                testDepartment.setCreatedBy(1);
        }

        @Test
        public void createDepartmentTest() {

                when(departmentRepository.save(any(Department.class))).thenReturn(testDepartment);
                Department newDepartment = departmentService.createDepartment(testDepartment, 1);

                assertNotNull(newDepartment);
                assertEquals(testDepartment.getName(), newDepartment.getName());
                verify(departmentRepository, times(1)).save(any(Department.class));

        }

        @Test
        public void getAllTest() {

                when(departmentRepository.findAllByStatus(anyString())).thenReturn(Arrays.asList(testDepartment));
                List<Department> departmentList = departmentService.getAll();

                assertNotNull(departmentList);
                assertFalse(departmentList.isEmpty());
                assertEquals(departmentList.size(), 1);
                verify(departmentRepository, times(1)).findAllByStatus(anyString());

        }

        @Test
        public void getActiveDepartmentByNameTest() {

                when(departmentRepository.findByNameAndStatus(anyString(), anyString())).thenReturn(testDepartment);
                Department department = departmentService.getActiveDepartmentByName("Engineering");

                assertNotNull(department);
                assertEquals(testDepartment.getName(), department.getName());
                verify(departmentRepository, times(1)).findByNameAndStatus(anyString(), anyString());

        }

        @Test
        public void getAllActiveDepartmentsWithCountTest() {

                List<Map<String, Object>> rawResults = List.of(
                        Map.of("id", 1, "name", "Engineering", "staffDepartmentCount", 10),
                        Map.of("id", 2, "name", "HR", "staffDepartmentCount", 5)
                );

                when(departmentRepository.findAllActiveWithCount()).thenReturn(rawResults);
                List<DepartmentDto> departmentList = departmentService.getAllActiveDepartmentsWithCount();

                assertEquals(2, departmentList.size());

                DepartmentDto firstDepartment = departmentList.get(0);
                assertEquals(1, firstDepartment.getId());
                assertEquals("Engineering", firstDepartment.getName());
                assertEquals(10, firstDepartment.getStaffCount());

                DepartmentDto secondDepartment = departmentList.get(1);
                assertEquals(2, secondDepartment.getId());
                assertEquals("HR", secondDepartment.getName());
                assertEquals(5, secondDepartment.getStaffCount());

                verify(departmentRepository, times(1)).findAllActiveWithCount();

        }

        @Test
        public void deleteDepartmentByIdTest_Success() {

                when(departmentRepository.updateDepartmentStatusById(anyString(), anyInt(), any(Timestamp.class), anyInt())).thenReturn(1);
                int result = departmentService.deleteDepartmentById(1, 1);

                assertEquals(1, result);
                verify(departmentRepository, times(1)).updateDepartmentStatusById(anyString(), anyInt(), any(Timestamp.class), anyInt());

        }

        @Test
        public void deleteDepartmentByIdTest_Failure() {

                when(departmentRepository.updateDepartmentStatusById(anyString(), anyInt(), any(Timestamp.class), anyInt())).thenReturn(0);
                int result = departmentService.deleteDepartmentById(1, 1);

                assertEquals(0, result);
                verify(departmentRepository, times(1)).updateDepartmentStatusById(anyString(), anyInt(), any(Timestamp.class), anyInt());

        }

}
