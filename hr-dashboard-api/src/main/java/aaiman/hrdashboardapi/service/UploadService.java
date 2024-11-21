package aaiman.hrdashboardapi.service;

import aaiman.hrdashboardapi.dto.CsvProcessDto;
import aaiman.hrdashboardapi.model.JobPosition;
import aaiman.hrdashboardapi.model.Role;
import aaiman.hrdashboardapi.model.User;
import aaiman.hrdashboardapi.repository.JobPositionRepository;
import aaiman.hrdashboardapi.repository.RoleRepository;
import aaiman.hrdashboardapi.repository.UserRepository;
import com.opencsv.CSVReader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStreamReader;
import java.sql.Timestamp;
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

        public UploadService(RoleRepository roleRepository, UserRepository userRepository, JobPositionRepository jobPositionRepository) {
                this.roleRepository = roleRepository;
                this.userRepository = userRepository;
                this.jobPositionRepository = jobPositionRepository;
        }

        public CsvProcessDto processFile(MultipartFile file, int userId) {

                int savedCount = 0;
                int duplicateCount = 0;
                String entity = "";

                try {

                        CSVReader csvReader = new CSVReader(new InputStreamReader(file.getInputStream()));

                        //Skip the header row
                        csvReader.readNext();

                        String[] rowContent = csvReader.readNext();
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
                *1: firstName
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
                * */

                int savedCount = 0;
                int duplicateCount = 0;

                try {

                        List<User> userList = new ArrayList<>();
                        String[] row;

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

                                Role role = roleRepository.findByName(roleName);
                                Set<Role> roles = new HashSet<>();
                                if (role != null) {
                                        roles.add(role);
                                }

                                JobPosition jobPosition = jobPositionRepository.findByNameAndStatus(jobPositionName, "Active");
                                if (jobPosition == null)  {
                                        jobPosition = new JobPosition();
                                        jobPosition.setName(jobPositionName);
                                        jobPosition.setStatus("Active");
                                        jobPosition.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
                                        jobPosition.setCreatedBy(userId);
                                        jobPositionRepository.save(jobPosition);
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

}
