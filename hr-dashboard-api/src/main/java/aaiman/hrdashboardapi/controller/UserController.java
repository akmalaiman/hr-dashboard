package aaiman.hrdashboardapi.controller;

import aaiman.hrdashboardapi.model.User;
import aaiman.hrdashboardapi.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@Tag(name = "User", description = "Handles user management operations such as creation, retrieval, and updating of users.")
@Slf4j
public class UserController {

        private final UserService userService;

        public UserController(UserService userService) {
                this.userService = userService;
        }

        @PostMapping("/add")
        @PreAuthorize("hasAuthority('ADMIN')")
        public ResponseEntity<User> addUser(@RequestBody User user, HttpServletRequest request) {

                try {

                        int userId = (Integer) request.getAttribute("userId");

                        if (userId == 0) {
                                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
                        }

                        User createdUser = userService.createUser(user, userId);

                        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);

                } catch (NullPointerException e) {
                        log.error("Exception occurred while adding user: {}", e.getMessage());
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
                }

        }

        @GetMapping("/all")
        @PreAuthorize("hasAuthority('ADMIN')")
        public ResponseEntity<List<User>> getAllActive() {

                List<User> userList = userService.getAllActiveUsers();

                return ResponseEntity.status(HttpStatus.OK).body(userList);

        }

        @GetMapping("/byUsername")
        @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
        public ResponseEntity<User> getActiveUserByUsername(@RequestParam("username") String username) {

                User user = userService.getActiveUserByUsername(username);

                if (user == null) {
                        return ResponseEntity.status(HttpStatus.OK).body(null);
                }

                return ResponseEntity.status(HttpStatus.CONFLICT).body(null);

        }

        @GetMapping("/byEmail")
        @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
        public ResponseEntity<User> getActiveUserByEmail(@RequestParam("email") String email) {

                User user = userService.getActiveUserByEmail(email);

                if (user == null) {
                        return ResponseEntity.status(HttpStatus.OK).body(null);
                }

                return ResponseEntity.status(HttpStatus.CONFLICT).body(user);
        }

}
