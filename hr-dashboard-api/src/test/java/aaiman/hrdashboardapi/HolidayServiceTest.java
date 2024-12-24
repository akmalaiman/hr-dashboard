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
                testHoliday.setId(1);
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
                assertEquals(1, holidayList.size());
                verify(holidayRepository, times(1)).findAllByStatus("Active");

        }

        @Test
        public void updateHolidayTest() {

                Holiday updateHoliday = new Holiday();
                updateHoliday.setId(testHoliday.getId());
                updateHoliday.setName("Updated Test Holiday");
                updateHoliday.setHolidayDate(LocalDate.of(2024, 12, 22));

                when(holidayRepository.findById(anyInt())).thenReturn(testHoliday);
                when(holidayRepository.save(any(Holiday.class))).thenReturn(updateHoliday);

                Holiday updatedHoliday = holidayService.updateHoliday(updateHoliday, 1);

                assertNotNull(updatedHoliday);
                assertEquals("Updated Test Holiday", updatedHoliday.getName());
                assertEquals(LocalDate.of(2024, 12, 22), updatedHoliday.getHolidayDate());
                verify(holidayRepository, times(1)).findById(anyInt());
                verify(holidayRepository, times(1)).save(any(Holiday.class));
        }

        @Test
        public void deleteHolidayByIdTest_Success() {
                when(holidayRepository.updateHolidayStatus(anyString(), anyInt(), any(Timestamp.class), anyInt())).thenReturn(1);
                int result = holidayService.deleteHolidayById(1, 1);

                assertEquals(1, result);
                verify(holidayRepository, times(1)).updateHolidayStatus(anyString(), anyInt(), any(Timestamp.class), anyInt());
        }

        @Test
        public void deleteHolidayByIdTest_Failure() {
                when(holidayRepository.updateHolidayStatus(anyString(), anyInt(), any(Timestamp.class), anyInt())).thenReturn(0);
                int result = holidayService.deleteHolidayById(1, 1);

                assertEquals(0, result);
                verify(holidayRepository, times(1)).updateHolidayStatus(anyString(), anyInt(), any(Timestamp.class), anyInt());
        }

}
