package aaiman.hrdashboardapi.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
public class DepartmentDto {

        private int id;
        private String name;
        private String status;
        private Timestamp createdAt;
        private int createdBy;
        private Timestamp updatedAt;
        private int updatedBy;
        private int staffCount;

}
