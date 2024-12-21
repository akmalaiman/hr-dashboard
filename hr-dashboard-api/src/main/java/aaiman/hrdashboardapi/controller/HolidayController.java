package aaiman.hrdashboardapi.controller;

import aaiman.hrdashboardapi.model.Holiday;
import aaiman.hrdashboardapi.service.HolidayService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/api/holiday")
@Tag(name = "Holiday", description = "Handles holiday management operations such as creation, retrieval, and updating of holiday.")
@Slf4j
public class HolidayController {

        private final HolidayService holidayService;

        public HolidayController(HolidayService holidayService) {
                this.holidayService = holidayService;
        }

        @GetMapping("/all")
        public ResponseEntity<List<Holiday>> getAllHoliday() {

                List<Holiday> holidayList = holidayService.getAll();

                if (holidayList.isEmpty()) {
                        return ResponseEntity.noContent().build();
                } else {
                        return ResponseEntity.ok(holidayList);
                }

        }

        @PostMapping("/add")
        @PreAuthorize("hasAuthority('ADMIN')")
        public ResponseEntity<Holiday> addHoliday(@RequestBody Holiday holiday, HttpServletRequest request) {

                try {

                        int userId = (Integer) request.getAttribute("userId");

                        Holiday createdHoliday = holidayService.createHoliday(holiday, userId);

                        if (createdHoliday != null) {
                                return ResponseEntity.status(HttpStatus.CREATED).body(createdHoliday);
                        } else {
                                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                        }

                } catch (NullPointerException e) {
                        log.error("Error while creating new Holiday: {}", e.getMessage());
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
                }

        }

}
