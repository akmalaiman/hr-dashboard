package aaiman.hrdashboardapi.service;

import aaiman.hrdashboardapi.dto.CsvProcessDto;
import aaiman.hrdashboardapi.model.*;
import aaiman.hrdashboardapi.repository.*;
import com.opencsv.CSVReader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class UploadService {
    
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final JobPositionRepository jobPositionRepository;
    private final DepartmentRepository departmentRepository;
    private final HolidayRepository holidayRepository;
    
    public UploadService(RoleRepository roleRepository, UserRepository userRepository, JobPositionRepository jobPositionRepository, DepartmentRepository departmentRepository, HolidayRepository holidayRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.jobPositionRepository = jobPositionRepository;
        this.departmentRepository = departmentRepository;
        this.holidayRepository = holidayRepository;
    }
    
    public CsvProcessDto processFile(MultipartFile file, int userId) {
        
        int savedCount = 0;
        int duplicateCount = 0;
        String entity = "";
        
        try {
            
            CSVReader bodyContent = new CSVReader(new InputStreamReader(file.getInputStream()));
            CSVReader csvReader = new CSVReader(new InputStreamReader(file.getInputStream()));
            
            //Skip the header row
            bodyContent.readNext();
            
            String[] rowContent = bodyContent.readNext();
            if (rowContent != null && rowContent.length > 0) {
                String dataType = rowContent[0];
                
                switch (dataType) {
                    case "staff":
                        CsvProcessDto userResult = processUser(csvReader, userId);
                        savedCount = userResult.getSavedCount();
                        duplicateCount = userResult.getDuplicateCount();
                        entity = userResult.getEntity();
                        break;
                    case "jobPosition":
                        CsvProcessDto jobPositionResult = processJobPosition(csvReader, userId);
                        savedCount = jobPositionResult.getSavedCount();
                        duplicateCount = jobPositionResult.getDuplicateCount();
                        entity = jobPositionResult.getEntity();
                        break;
                    case "department":
                        CsvProcessDto departmentResult = processDepartment(csvReader, userId);
                        savedCount = departmentResult.getSavedCount();
                        duplicateCount = departmentResult.getDuplicateCount();
                        entity = departmentResult.getEntity();
                        break;
                    case "holiday":
                        CsvProcessDto holidayResult = processHoliday(csvReader, userId);
                        savedCount = holidayResult.getSavedCount();
                        duplicateCount = holidayResult.getDuplicateCount();
                        entity = holidayResult.getEntity();
                        break;
                    default:
                        log.error("Invalid data type: {}", dataType);
                        throw new RuntimeException("Invalid data type: " + dataType);
                }
            }
            
        } catch (Exception e) {
            log.error("Failed to process CSV file: {}", e.getMessage());
            throw new RuntimeException("Failed to process CSV file: " + e.getMessage());
        }
        
        return new CsvProcessDto(savedCount, duplicateCount, entity);
        
    }
    
    private CsvProcessDto processUser(CSVReader csvReader, int userId) {
        
        /*
         * Staff CSV file format
         * 0: dataType
         * 1: firstName
         * 2: lastName
         * 3: username
         * 4: email
         * 5: password
         * 6: address
         * 7: city
         * 8: state
         * 9: postalCode
         * 10: country
         * 11: jobPosition
         * 12: role
         * 13: department
         * 14: reportingTo
         * */
        
        int savedCount = 0;
        int duplicateCount = 0;
        
        try {
            
            List<User> userList = new ArrayList<>();
            String[] row;
            csvReader.readNext();
            
            while ((row = csvReader.readNext()) != null) {
                
                String firstName = row[1];
                String lastName = row[2];
                String username = row[3];
                String email = row[4];
                String password = row[5];
                String address = row[6];
                String city = row[7];
                String state = row[8];
                int postalCode = Integer.parseInt(row[9]);
                String country = row[10];
                String jobPositionName = row[11];
                String roleName = row[12];
                String departmentName = row[13];
                int reportingTo = Integer.parseInt(row[14]);
                
                Role role = roleRepository.findByName(roleName);
                Set<Role> roles = new HashSet<>();
                if (role != null) {
                    roles.add(role);
                }
                
                JobPosition jobPosition = jobPositionRepository.findByNameAndStatus(jobPositionName, "Active");
                if (jobPosition == null) {
                    jobPosition = new JobPosition();
                    jobPosition.setName(jobPositionName);
                    jobPosition.setStatus("Active");
                    jobPosition.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
                    jobPosition.setCreatedBy(userId);
                    jobPositionRepository.save(jobPosition);
                }
                
                Department department = departmentRepository.findByNameAndStatus(departmentName, "Active");
                if (department == null) {
                    department = new Department();
                    department.setName(departmentName);
                    department.setStatus("Active");
                    department.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
                    department.setCreatedBy(userId);
                    departmentRepository.save(department);
                }
                
                User byUsername = userRepository.findByUsernameAndStatus(username, "Active");
                User byEmail = userRepository.findByEmailAndStatus(email, "Active");
                
                if (byUsername == null && byEmail == null) {
                    User user = new User();
                    user.setFirstName(firstName);
                    user.setLastName(lastName);
                    user.setUsername(username);
                    user.setEmail(email);
                    user.setPassword(new BCryptPasswordEncoder().encode(password));
                    user.setAddress(address);
                    user.setCity(city);
                    user.setState(state);
                    user.setPostalCode(postalCode);
                    user.setCountry(country);
                    user.setJobPositionId(jobPosition);
                    user.setRoles(roles);
                    user.setStatus("Active");
                    user.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
                    user.setCreatedBy(userId);
                    user.setDepartmentId(department);
                    user.setReportingTo(reportingTo);
                    userList.add(user);
                    savedCount++;
                } else {
                    duplicateCount++;
                }
            }
            
            userRepository.saveAll(userList);
            
        } catch (Exception e) {
            log.error("Failed to process staff CSV file: {}", e.getMessage());
            throw new RuntimeException("Failed to process staff CSV file: " + e.getMessage());
        }
        
        return new CsvProcessDto(savedCount, duplicateCount, "Staff");
        
    }
    
    private CsvProcessDto processJobPosition(CSVReader csvReader, int userId) {
        
        /*
         * Job Position CSV file format
         * 0: dataType
         *1: name
         * */
        
        int savedCount = 0;
        int duplicateCount = 0;
        
        try {
            
            List<JobPosition> jobPositionList = new ArrayList<>();
            String[] row;
            csvReader.readNext();
            
            while ((row = csvReader.readNext()) != null) {
                
                String name = row[1];
                
                JobPosition byName = jobPositionRepository.findByNameAndStatus(name, "Active");
                if (byName != null) {
                    duplicateCount++;
                } else {
                    JobPosition jobPosition = new JobPosition();
                    jobPosition.setName(name);
                    jobPosition.setStatus("Active");
                    jobPosition.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
                    jobPosition.setCreatedBy(userId);
                    jobPositionList.add(jobPosition);
                    savedCount++;
                }
                
            }
            
            jobPositionRepository.saveAll(jobPositionList);
            
        } catch (Exception e) {
            log.error("Failed to process job position CSV file: {}", e.getMessage());
            throw new RuntimeException("Failed to process job position CSV file: " + e.getMessage());
        }
        
        return new CsvProcessDto(savedCount, duplicateCount, "Job Position");
        
    }
    
    private CsvProcessDto processDepartment(CSVReader csvReader, int userId) {
        
        /*
         * Department CSV file format
         * 0: dataType
         *1: name
         * */
        
        int savedCount = 0;
        int duplicateCount = 0;
        
        try {
            
            List<Department> departmentList = new ArrayList<>();
            String[] row;
            csvReader.readNext();
            
            while ((row = csvReader.readNext()) != null) {
                
                String name = row[1];
                
                Department byName = departmentRepository.findByNameAndStatus(name, "Active");
                if (byName != null) {
                    duplicateCount++;
                } else {
                    Department department = new Department();
                    department.setName(name);
                    department.setStatus("Active");
                    department.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
                    department.setCreatedBy(userId);
                    departmentList.add(department);
                    savedCount++;
                }
                
            }
            
            departmentRepository.saveAll(departmentList);
            
        } catch (Exception e) {
            log.error("Failed to process department CSV file: {}", e.getMessage());
            throw new RuntimeException("Failed to process department CSV file: " + e.getMessage());
        }
        
        return new CsvProcessDto(savedCount, duplicateCount, "Department");
        
    }
    
    private CsvProcessDto processHoliday(CSVReader csvReader, int userId) {
        
        /*
         * Department CSV file format
         * 0: dataType
         *1: name
         *2: holidayDate
         * */
        
        int savedCount = 0;
        int duplicateCount = 0;
        
        try {
            
            List<Holiday> holidayList = new ArrayList<>();
            String[] row;
            csvReader.readNext();
            
            while ((row = csvReader.readNext()) != null) {
                String name = row[1];
                LocalDate holidayDate = LocalDate.parse(row[2]);
                
                Holiday checkDuplicate = holidayRepository.findByNameDateAndStatus(name, holidayDate, "Active");
                if (checkDuplicate != null) {
                    duplicateCount++;
                } else {
                    Holiday holiday = new Holiday();
                    holiday.setName(name);
                    holiday.setHolidayDate(holidayDate);
                    holiday.setStatus("Active");
                    holiday.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
                    holiday.setCreatedBy(userId);
                    holidayList.add(holiday);
                    savedCount++;
                }
            }
            
            holidayRepository.saveAll(holidayList);
            
        } catch (Exception e) {
            log.error("Failed to process holiday CSV file: {}", e.getMessage());
            throw new RuntimeException("Failed to process holiday CSV file: " + e.getMessage());
        }
        
        return new CsvProcessDto(savedCount, duplicateCount, "Holiday");
        
    }
    
}
