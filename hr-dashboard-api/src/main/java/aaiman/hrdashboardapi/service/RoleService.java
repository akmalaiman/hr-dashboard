package aaiman.hrdashboardapi.service;

import aaiman.hrdashboardapi.model.Role;
import aaiman.hrdashboardapi.repository.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {

        private final RoleRepository roleRepository;

        public RoleService(RoleRepository roleRepository) {
                this.roleRepository = roleRepository;
        }

        public List<Role> getAll() {
                return roleRepository.findAll();
        }

}
