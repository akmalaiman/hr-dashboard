package aaiman.hrdashboardapi.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@Entity
@Table(name = "\"user\"")
public class User {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id")
        private int id;

        @Column(name = "first_name", nullable = false)
        private String firstName;

        @Column(name = "last_name", nullable = false)
        private String lastName;

        @Column(name = "username", nullable = false)
        private String username;

        @Column(name = "email", nullable = false)
        private String email;

        @Column(name = "password", nullable = false)
        private String password;

        @Column(name = "address")
        private String address;

        @Column(name = "city")
        private String city;

        @Column(name = "state")
        private String state;

        @Column(name = "postal_code")
        private int postalCode;

        @Column(name = "country")
        private String country;

        @ManyToOne
        @JoinColumn(name = "job_position_id", referencedColumnName = "id", nullable = false)
        private JobPosition jobPositionId;

        @ManyToMany(fetch = FetchType.EAGER)
        @JoinTable(name = "user_roles",
                joinColumns = @JoinColumn(name = "user_id"),
                inverseJoinColumns = @JoinColumn(name = "role_id")
        )
        private Set<Role> roles = new HashSet<>();

        @Column(name = "status", nullable = false)
        private String status;

        @Column(name = "created_at", nullable = false)
        private Timestamp createdAt;

        @Column(name = "created_by")
        private int createdBy;

        @Column(name = "updated_at")
        private Timestamp updatedAt;

        @Column(name = "updated_by")
        private int updatedBy;

}
