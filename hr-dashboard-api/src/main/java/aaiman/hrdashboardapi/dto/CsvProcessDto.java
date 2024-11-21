package aaiman.hrdashboardapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CsvProcessDto {

        private int savedCount;
        private int duplicateCount;
        private String entity;

}
