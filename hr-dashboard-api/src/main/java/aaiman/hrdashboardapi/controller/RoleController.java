package aaiman.hrdashboardapi.controller;

import aaiman.hrdashboardapi.model.Role;
import aaiman.hrdashboardapi.service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
        @Operation(summary = "Get all active roles", description = "Fetches all active roles from the system.")
        @ApiResponses(value = {
                @ApiResponse(responseCode = "204", description = "No content found", content = @Content(schema = @Schema(example = "No role found"))),
                @ApiResponse(responseCode = "200", description = "List of roles found", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Role.class))))
        })
        public ResponseEntity<List<Role>> getAllRoles() {

                List<Role> roleList = roleService.getAll();

                if (roleList.isEmpty()) {
                        return ResponseEntity.noContent().build();
                } else {
                        return ResponseEntity.ok(roleList);
                }

        }

}
