package aaiman.hrdashboardapi.service;

import aaiman.hrdashboardapi.dto.DepartmentDto;
import aaiman.hrdashboardapi.model.Department;
import aaiman.hrdashboardapi.repository.DepartmentRepository;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DepartmentService {

        private  final DepartmentRepository departmentRepository;

        public DepartmentService(DepartmentRepository departmentRepository) {
                this.departmentRepository = departmentRepository;
        }

        public List<Department> getAll() {
                return departmentRepository.findAllByStatus("Active");
        }

        public Department createDepartment(Department department, int userId) {

                Department newDepartment = new Department();
                newDepartment.setName(department.getName());
                newDepartment.setStatus("Active");
                newDepartment.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
                newDepartment.setCreatedBy(userId);

                departmentRepository.save(newDepartment);
                return newDepartment;

        }

        public Department getActiveDepartmentByName(String name) {
                return departmentRepository.findByNameAndStatus(name, "Active");
        }

        public List<DepartmentDto> getAllActiveDepartmentsWithCount() {
                List<Map<String, Object>> rawResults = departmentRepository.findAllActiveWithCount();
                return rawResults.stream()
                        .map(row -> {
                                DepartmentDto departmentDto = new DepartmentDto();
                                departmentDto.setId(((Number) row.get("id")).intValue());
                                departmentDto.setName((String) row.get("name"));
                                departmentDto.setStaffCount(((Number) row.get("staffDepartmentCount")).intValue());
                                return departmentDto;
                        }).collect(Collectors.toList());
        }

        public int deleteDepartmentById(int departmentId, int userId) {
                Timestamp now = Timestamp.valueOf(LocalDateTime.now());
                return departmentRepository.updateDepartmentStatusById("Deleted", userId, now, departmentId);
        }

}
