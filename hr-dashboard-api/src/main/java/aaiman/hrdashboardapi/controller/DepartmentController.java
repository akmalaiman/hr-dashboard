package aaiman.hrdashboardapi.controller;

import aaiman.hrdashboardapi.dto.DepartmentDto;
import aaiman.hrdashboardapi.model.Department;
import aaiman.hrdashboardapi.service.DepartmentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/department")
@Tag(name = "Department", description = "Handles department management operations such as creation, retrieval, and updating of department.")
@Slf4j
public class DepartmentController {

        private final DepartmentService departmentService;

        public DepartmentController(DepartmentService departmentService) {
                this.departmentService = departmentService;
        }

        @GetMapping("/all")
        public ResponseEntity<List<Department>> getAllDepartments() {

                List<Department> departmentList = departmentService.getAll();

                if (departmentList.isEmpty()) {
                        return ResponseEntity.noContent().build();
                } else {
                        return ResponseEntity.ok(departmentList);
                }

        }

        @PostMapping("/add")
        @PreAuthorize("hasAuthority('ADMIN')")
        public ResponseEntity<Department> addDepartment(@RequestBody Department department, HttpServletRequest request) {

                try {

                        int userId = (Integer) request.getAttribute("userId");

                        Department createdDepartment = departmentService.createDepartment(department, userId);

                        if (createdDepartment != null) {
                                return ResponseEntity.status(HttpStatus.CREATED).body(createdDepartment);
                        } else {
                                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                        }

                } catch (NullPointerException e) {
                        log.error("Error while creating new Department: {}", e.getMessage());
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
                }

        }

        @GetMapping("/byName")
        @PreAuthorize("hasAuthority('ADMIN')")
        public ResponseEntity<Department> getActiveDepartmentByName(@RequestParam String name) {

                Department department = departmentService.getActiveDepartmentByName(name);

                if (department == null) {
                        return ResponseEntity.ok().build();
                } else {
                        return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
                }

        }

        @GetMapping("/nameWithCount")
        @PreAuthorize("hasAuthority('ADMIN')")
        public ResponseEntity<List<DepartmentDto>> getAllActiveDepartmentsWithCount() {

                List<DepartmentDto> departmentList = departmentService.getAllActiveDepartmentsWithCount();

                if (departmentList.isEmpty()) {
                        return ResponseEntity.noContent().build();
                } else {
                        return ResponseEntity.ok(departmentList);
                }

        }

        @DeleteMapping("/delete")
        @PreAuthorize("hasAuthority('ADMIN')")
        public ResponseEntity<Department> deleteDepartment(@RequestParam("id") int id, HttpServletRequest request) {

                int userId = (Integer) request.getAttribute("userId");

                int updateStatus = departmentService.deleteDepartmentById(id, userId);

                if (updateStatus == 1) {
                        return ResponseEntity.ok().build();
                } else {
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                }

        }

}
