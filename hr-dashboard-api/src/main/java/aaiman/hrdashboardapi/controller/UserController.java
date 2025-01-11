package aaiman.hrdashboardapi.controller;

import aaiman.hrdashboardapi.model.User;
import aaiman.hrdashboardapi.service.UserService;
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
    @Operation(summary = "Add a new user")
    public ResponseEntity<User> addUser(@RequestBody User user, HttpServletRequest request) {

        try {

            int userId = (Integer) request.getAttribute("userId");

            User createdUser = userService.createUser(user, userId);

            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);

        } catch (NullPointerException e) {
            log.error("Exception occurred while adding user: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Get all active users")
    public ResponseEntity<List<User>> getAllActive() {

        List<User> userList = userService.getAllActiveUsers();

        return ResponseEntity.status(HttpStatus.OK).body(userList);

    }

    @GetMapping("/byUsername")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @Operation(summary = "Get active user by username")
    public ResponseEntity<User> getActiveUserByUsername(@RequestParam("username") String username) {

        User user = userService.getActiveUserByUsername(username);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.OK).build();
        }

        return ResponseEntity.status(HttpStatus.CONFLICT).build();

    }

    @GetMapping("/byEmail")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @Operation(summary = "Get active user by email")
    public ResponseEntity<User> getActiveUserByEmail(@RequestParam("email") String email) {

        User user = userService.getActiveUserByEmail(email);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.OK).build();
        }

        return ResponseEntity.status(HttpStatus.CONFLICT).body(user);
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Delete a user by id")
    public ResponseEntity<User> deleteUser(@RequestParam("userId") Integer userId, HttpServletRequest request) {

        int requestorId = (Integer) request.getAttribute("userId");

        int updateStatus = userService.deleteUserById(userId, requestorId);

        if (updateStatus == 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/{userId}")
    @Operation(summary = "Get user by id")
    public ResponseEntity<User> getUserById(@PathVariable("userId") Integer userId) {

        User user = userService.findUserById(userId);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.status(HttpStatus.OK).body(user);

    }

    @PutMapping("/update/{userId}")
    @Operation(summary = "Update an existing user")
    public ResponseEntity<User> updateUser(@RequestBody User user, HttpServletRequest request) {

        int userId = (Integer) request.getAttribute("userId");

        User updatedUser = userService.updateUser(user, userId);
        if (updatedUser == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        return ResponseEntity.status(HttpStatus.OK).body(updatedUser);

    }

    @PutMapping("/updatePassword/{userId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    @Operation(summary = "Update user password by id")
    public ResponseEntity<User> updateUserPassword(@RequestParam("password") String password, @PathVariable("userId") Integer userId, HttpServletRequest request) {

        int requestorId = (Integer) request.getAttribute("userId");

        int updateStatus = userService.updateUserPassword(requestorId, password, userId);

        if (updateStatus == 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
