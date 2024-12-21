package aaiman.hrdashboardapi.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@Entity
@Table(name = "holiday")
public class Holiday {

        @Id
        @Column(name = "id")
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private int id;

        @Column(name = "name", nullable = false)
        private String name;

        @Column(name = "holiday_date", nullable = false)
        private LocalDate holidayDate;

        @Column(name = "status", nullable = false)
        private String status;

        @Column(name = "created_at", nullable = false)
        private Timestamp createdAt;

        @Column(name = "created_by", nullable = false)
        private int createdBy;

        @Column(name = "updated_at")
        private Timestamp updatedAt;

        @Column(name = "updated_by")
        private int updatedBy;

}
