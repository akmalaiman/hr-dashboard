package aaiman.hrdashboardapi;

import aaiman.hrdashboardapi.dto.JobPositionDto;
import aaiman.hrdashboardapi.model.JobPosition;
import aaiman.hrdashboardapi.repository.JobPositionRepository;
import aaiman.hrdashboardapi.service.JobPositionService;
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
public class JobPositionServiceTest {

        @InjectMocks
        private JobPositionService jobPositionService;

        @Mock
        private JobPositionRepository jobPositionRepository;

        private JobPosition testJobPosition;

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
                testJobPosition = new JobPosition();
                testJobPosition.setId(1);
                testJobPosition.setName("Software Engineer");
                testJobPosition.setStatus("Active");
                testJobPosition.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
                testJobPosition.setCreatedBy(1);
        }

        @Test
        public void testCreateJobPosition() {

                when(jobPositionRepository.save(any(JobPosition.class))).thenReturn(testJobPosition);
                JobPosition newJobPosition = jobPositionService.createJobPosition(testJobPosition, 1);

                assertNotNull(newJobPosition);
                assertEquals(testJobPosition.getName(), newJobPosition.getName());
                verify(jobPositionRepository, times(1)).save(any(JobPosition.class));

        }

        @Test
        public void getAllTest() {

                when(jobPositionRepository.findAllByStatus(anyString())).thenReturn(Arrays.asList(testJobPosition));
                List<JobPosition> jobPositionList = jobPositionService.getAll();

                assertNotNull(jobPositionList);
                assertFalse(jobPositionList.isEmpty());
                assertEquals(1, jobPositionList.size());
                verify(jobPositionRepository, times(1)).findAllByStatus(anyString());

        }

        @Test
        public void getActiveJobPositionByNameTest() {

                when(jobPositionRepository.findByNameAndStatus(anyString(), anyString())).thenReturn(testJobPosition);
                JobPosition jobPosition = jobPositionService.getActiveJobPositionByName("Software Engineer");

                assertNotNull(jobPosition);
                assertEquals(testJobPosition.getName(), jobPosition.getName());
                verify(jobPositionRepository, times(1)).findByNameAndStatus(anyString(), anyString());

        }

        @Test
        public void getAllActiveJobPositionsWIthCountTest() {

                List<Map<String, Object>> rawResults = List.of(
                        Map.of("id", 1, "name", "Software Engineer", "staffJobCount", 5),
                        Map.of("id", 2, "name", "HR Manager", "staffJobCount", 10)
                );

                when(jobPositionRepository.findAllActiveWithCount()).thenReturn(rawResults);
                List<JobPositionDto> jobPositionList = jobPositionService.getAllActiveJobPositionsWIthCount();

                assertNotNull(jobPositionList);
                assertEquals(2, jobPositionList.size());

                JobPositionDto firstJobPosition = jobPositionList.get(0);
                assertEquals(1, firstJobPosition.getId());
                assertEquals("Software Engineer", firstJobPosition.getName());
                assertEquals(5, firstJobPosition.getStaffCount());

                JobPositionDto secondJobPosition = jobPositionList.get(1);
                assertEquals(2, secondJobPosition.getId());
                assertEquals("HR Manager", secondJobPosition.getName());
                assertEquals(10, secondJobPosition.getStaffCount());

                verify(jobPositionRepository, times(1)).findAllActiveWithCount();

        }

        @Test
        public void deleteJobPositionByIdTest_Success() {

                when(jobPositionRepository.updateJobPositionStatusById(anyString(), anyInt(), any(Timestamp.class), anyInt())).thenReturn(1);
                int result = jobPositionService.deleteJobPositionById(1, 1);

                assertEquals(1, result);
                verify(jobPositionRepository, times(1)).updateJobPositionStatusById(anyString(), anyInt(), any(Timestamp.class), anyInt());

        }

        @Test
        public void deleteJobPositionByIdTest_Failure() {

                when(jobPositionRepository.updateJobPositionStatusById(anyString(), anyInt(), any(Timestamp.class), anyInt())).thenReturn(0);
                int result = jobPositionService.deleteJobPositionById(1, 1);

                assertEquals(0, result);
                verify(jobPositionRepository, times(1)).updateJobPositionStatusById(anyString(), anyInt(), any(Timestamp.class), anyInt());

        }

}
