package aaiman.hrdashboardapi.controller;

import aaiman.hrdashboardapi.model.Holiday;
import aaiman.hrdashboardapi.service.HolidayService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/holiday")
@Tag(name = "Holiday", description = "Handles holiday management operations such as creation, retrieval, and updating of holiday.")
@Slf4j
public class HolidayController {

        private final HolidayService holidayService;

        public HolidayController(HolidayService holidayService) {
                this.holidayService = holidayService;
        }

        @GetMapping("/all")
        @Operation(summary = "Get all active holidays")
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
        @Operation(summary = "Add a new holiday")
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

        @PutMapping("/update")
        @PreAuthorize("hasAuthority('ADMIN')")
        @Operation(summary = "Update an existing holiday")
        public ResponseEntity<Holiday> updateHoliday(@RequestBody Holiday holiday, HttpServletRequest request) {

                try {

                        int userId = (Integer) request.getAttribute("userId");

                        Holiday updatedHoliday = holidayService.updateHoliday(holiday, userId);

                        if (updatedHoliday != null) {
                                return ResponseEntity.ok(updatedHoliday);
                        } else {
                                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                        }

                } catch (NullPointerException e) {
                        log.error("Error while updating Holiday: {}", e.getMessage());
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
                }

        }

        @DeleteMapping("/delete/{id}")
        @PreAuthorize("hasAuthority('ADMIN')")
        @Operation(summary = "Delete a holiday by id")
        public ResponseEntity<String> deleteHoliday(@PathVariable int id, HttpServletRequest request) {

                try {

                        int userId = (Integer) request.getAttribute("userId");

                        int result = holidayService.deleteHolidayById(id, userId);

                        if (result > 0) {
                                return ResponseEntity.ok("Holiday deleted successfully.");
                        } else {
                                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                        }

                } catch (NullPointerException e) {
                        log.error("Error while deleting Holiday: {}", e.getMessage());
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
                }

        }

}
