package aaiman.hrdashboardapi.service;

import aaiman.hrdashboardapi.config.CustomUserDetails;
import aaiman.hrdashboardapi.model.User;
import aaiman.hrdashboardapi.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

        @Autowired
        private UserRepository userRepository;

        @Override
        public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

                User user = userRepository.findByUsernameAndStatus(username, "Active");
                if (user == null) {
                        log.error("User not found");
                        throw new UsernameNotFoundException("User not found");
                }

                log.info("User authenticated successfully!");
                return new CustomUserDetails(user);

        }
}
