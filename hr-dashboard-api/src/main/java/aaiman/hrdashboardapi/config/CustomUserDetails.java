package aaiman.hrdashboardapi.config;

import aaiman.hrdashboardapi.model.Role;
import aaiman.hrdashboardapi.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CustomUserDetails extends User implements UserDetails {

        private String username;
        private String password;

        Collection<? extends GrantedAuthority> authorities;

        public CustomUserDetails(User user) {

                this.username = user.getUsername();
                this.password = user.getPassword();

                List<GrantedAuthority> authorities = new ArrayList<>();

                for (Role role : user.getRoles()) {
                        authorities.add(new SimpleGrantedAuthority(role.getName().toUpperCase()));
                }

                this.authorities = authorities;
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
                return authorities;
        }

        @Override
        public String getPassword() {
                return password;
        }

        @Override
        public String getUsername() {
                return username;
        }

        @Override
        public boolean isAccountNonExpired() {
                return true;
        }

        @Override
        public boolean isAccountNonLocked() {
                return true;
        }

        @Override
        public boolean isCredentialsNonExpired() {
                return true;
        }

        @Override
        public boolean isEnabled() {
                return true;
        }

}
