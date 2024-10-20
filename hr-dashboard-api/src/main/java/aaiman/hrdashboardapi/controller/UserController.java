package aaiman.hrdashboardapi.controller;

import aaiman.hrdashboardapi.model.User;
import aaiman.hrdashboardapi.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@Tag(name = "User", description = "Handles user management operations such as creation, retrieval, and updating of users.")
public class UserController {

        private final UserService userService;

        public UserController(UserService userService) {
                this.userService = userService;
        }

        @PostMapping("/add")
        @PreAuthorize("hasAuthority('ADMIN')")
        public ResponseEntity<User> addUser(@RequestBody User user, HttpServletRequest request){

                int userId = (Integer) request.getAttribute("userId");

                if (userId == 0) {
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
                }

                User createdUser = userService.createUser(user, userId);

                return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);

        }

}
