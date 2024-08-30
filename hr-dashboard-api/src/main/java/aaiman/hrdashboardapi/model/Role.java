package aaiman.hrdashboardapi.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "role")
public class Role {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id")
        private int id;

        @Column(name = "name", nullable = false)
        private String name;

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
