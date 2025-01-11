package aaiman.hrdashboardapi.controller;

import aaiman.hrdashboardapi.model.User;
import aaiman.hrdashboardapi.service.UserService;
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
    @Operation(summary = "Add a new user", description = "Add a new user to the system. Only admin can perform this operation.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created", content = @Content(schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(example = "Internal server error")))
    })
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
    @Operation(summary = "Get all active users", description = "Fetches all active users from the system. Only admin can perform this operation.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No content found", content = @Content(schema = @Schema(example = "No user found"))),
            @ApiResponse(responseCode = "200", description = "List of users found", content = @Content(array = @ArraySchema(schema = @Schema(implementation = User.class))))
    })
    public ResponseEntity<List<User>> getAllActive() {

        List<User> userList = userService.getAllActiveUsers();
        
        if (userList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        return ResponseEntity.status(HttpStatus.OK).body(userList);

    }

    @GetMapping("/byUsername")
    @Operation(summary = "Get active user by username", description = "Fetches an active user from the system by username.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Username is available", content = @Content(schema = @Schema(example = "User available"))),
            @ApiResponse(responseCode = "409", description = "Conflict", content = @Content(schema = @Schema(example = "User already exists")))
    })
    public ResponseEntity<User> getActiveUserByUsername(@RequestParam("username") String username) {

        User user = userService.getActiveUserByUsername(username);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.OK).build();
        }

        return ResponseEntity.status(HttpStatus.CONFLICT).build();

    }

    @GetMapping("/byEmail")
    @Operation(summary = "Get active user by email", description = "Fetches an active user from the system by email.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Email is available", content = @Content(schema = @Schema(example = "Email available"))),
            @ApiResponse(responseCode = "409", description = "Conflict", content = @Content(schema = @Schema(example = "Email already exists")))
    })
    public ResponseEntity<User> getActiveUserByEmail(@RequestParam("email") String email) {

        User user = userService.getActiveUserByEmail(email);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.OK).build();
        }

        return ResponseEntity.status(HttpStatus.CONFLICT).body(user);
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Delete a user by id", description = "Delete a user from the system by id. Only admin can perform this operation.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User deleted successfully", content = @Content(schema = @Schema(example = "User deleted successfully"))),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(example = "Bad request")))
    })
    public ResponseEntity<User> deleteUser(@RequestParam("userId") Integer userId, HttpServletRequest request) {

        int requestorId = (Integer) request.getAttribute("userId");

        int updateStatus = userService.deleteUserById(userId, requestorId);

        if (updateStatus == 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/{userId}")
    @Operation(summary = "Get user by id", description = "Fetches a user from the system by id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found", content = @Content(schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content(schema = @Schema(example = "User not found")))
    })
    public ResponseEntity<User> getUserById(@PathVariable("userId") Integer userId) {

        User user = userService.findUserById(userId);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.status(HttpStatus.OK).body(user);

    }

    @PutMapping("/update/{userId}")
    @Operation(summary = "Update an existing user", description = "Updates an existing user in the system by user id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated successfully", content = @Content(schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(example = "Bad request")))
    })
    public ResponseEntity<User> updateUser(@RequestBody User user, HttpServletRequest request) {

        int userId = (Integer) request.getAttribute("userId");

        User updatedUser = userService.updateUser(user, userId);
        if (updatedUser == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        return ResponseEntity.status(HttpStatus.OK).body(updatedUser);

    }

    @PutMapping("/updatePassword/{userId}")
    @Operation(summary = "Update user password by id", description = "Updates user password in the system by user id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password updated successfully", content = @Content(schema = @Schema(example = "Password updated successfully"))),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(example = "Bad request")))
    })
    public ResponseEntity<User> updateUserPassword(@RequestParam("password") String password, @PathVariable("userId") Integer userId, HttpServletRequest request) {

        int requestorId = (Integer) request.getAttribute("userId");

        int updateStatus = userService.updateUserPassword(requestorId, password, userId);

        if (updateStatus == 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        return ResponseEntity.status(HttpStatus.OK).build();
    }
    
    @GetMapping("/managerByDepartment")
    @Operation(summary = "Get all managers by department", description = "Get all managers by department id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Managers found", content = @Content(array = @ArraySchema(schema = @Schema(implementation = User.class)))),
            @ApiResponse(responseCode = "204", description = "No content found", content = @Content(schema = @Schema(example = "No manager found")))
    })
    public ResponseEntity<List<User>> getManagerByDepartmentList(@RequestParam("departmentId") int departmentId) {
        
        List<User> managerList = userService.getManagerByDepartmentList(departmentId);
        
        if (managerList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        
        return ResponseEntity.status(HttpStatus.OK).body(managerList);
    }

}
