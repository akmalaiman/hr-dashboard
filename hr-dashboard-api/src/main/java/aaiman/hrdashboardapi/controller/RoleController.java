package aaiman.hrdashboardapi.controller;

import aaiman.hrdashboardapi.model.Role;
import aaiman.hrdashboardapi.service.RoleService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/role")
@Tag(name = "Role", description = "Handles role management operations such as creation, retrieval, and updating of role.")
public class RoleController {

        private final RoleService roleService;

        public RoleController(RoleService roleService) {
                this.roleService = roleService;
        }

        @GetMapping("/all")
        public ResponseEntity<List<Role>> getAllRoles() {

                List<Role> roleList = roleService.getAll();

                if (roleList.isEmpty()) {
                        return ResponseEntity.noContent().build();
                } else {
                        return ResponseEntity.ok(roleList);
                }

        }

}
