package aaiman.hrdashboardapi.controller;

import aaiman.hrdashboardapi.model.Holiday;
import aaiman.hrdashboardapi.service.HolidayService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @Operation(summary = "Get all active holidays", description = "Fetches all active holidays from the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No content found", content = @Content(schema = @Schema(example = "No holiday found"))),
            @ApiResponse(responseCode = "200", description = "List of holidays found", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Holiday.class))))
    })
    public ResponseEntity<List<Holiday>> getAllHoliday() {
        
        List<Holiday> holidayList = holidayService.getAll();
        
        if (holidayList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } else {
            return ResponseEntity.ok(holidayList);
        }
        
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get holiday by id", description = "Fetches a holiday from the system by its id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No content found", content = @Content(schema = @Schema(example = "No holiday found"))),
            @ApiResponse(responseCode = "200", description = "Holiday found", content = @Content(schema = @Schema(implementation = Holiday.class)))
    })
    public ResponseEntity<Holiday> getHolidayById(@PathVariable("id") int id) {
        
        Holiday holiday = holidayService.getHolidayById(id);
        
        if (holiday == null) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } else {
            return ResponseEntity.ok(holiday);
        }
        
    }
    
    @PostMapping("/add")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Add a new holiday", description = "Creates a new holiday in the system. Only admin users are allowed to create a new holiday.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Holiday created successfully", content = @Content(schema = @Schema(implementation = Holiday.class))),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(example = "Bad request"))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(example = "Internal server error")))
    })
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
    @Operation(summary = "Update an existing holiday", description = "Updates an existing holiday in the system. Only admin users are allowed to update a holiday.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Holiday updated successfully", content = @Content(schema = @Schema(implementation = Holiday.class))),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(example = "Bad request"))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(example = "Internal server error")))
    })
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
    @Operation(summary = "Delete a holiday by id", description = "Deletes a holiday from the system by its id. Only admin users are allowed to delete a holiday.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Holiday deleted successfully", content = @Content(schema = @Schema(example = "Holiday deleted successfully"))),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(example = "Bad request"))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(example = "Internal server error")))
    })
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
