package aaiman.hrdashboardapi;

import aaiman.hrdashboardapi.model.Holiday;
import aaiman.hrdashboardapi.repository.HolidayRepository;
import aaiman.hrdashboardapi.service.HolidayService;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HolidayServiceTest {

        @InjectMocks
        private HolidayService holidayService;

        @Mock
        private HolidayRepository holidayRepository;

        private Holiday testHoliday;

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
                testHoliday = new Holiday();
                testHoliday.setName("Test Holiday");
                testHoliday.setHolidayDate(LocalDate.of(2024, 12, 21));
                testHoliday.setStatus("Active");
                testHoliday.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
                testHoliday.setCreatedBy(1);
        }

        @Test
        public void createHolidayTest() {

                when(holidayRepository.save(any(Holiday.class))).thenReturn(testHoliday);
                Holiday newHoliday = holidayService.createHoliday(testHoliday, 1);

                assertNotNull(newHoliday);
                assertEquals(testHoliday.getName(), newHoliday.getName());
                verify(holidayRepository, times(1)).save(any(Holiday.class));

        }

        @Test
        public void getAllTest() {

                when(holidayRepository.findAllByStatus(anyString())).thenReturn(List.of(testHoliday));
                List<Holiday> holidayList = holidayService.getAll();

                assertNotNull(holidayList);
                assertFalse(holidayList.isEmpty());
                assertEquals(1, holidayService.getAll().size());
                verify(holidayRepository, times(1)).findAllByStatus("Active");

        }

}
