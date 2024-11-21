package aaiman.hrdashboardapi.service;

import aaiman.hrdashboardapi.dto.CsvProcessDto;
import com.opencsv.CSVReader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;

@Service
@Slf4j
public class UploadService {

        public CsvProcessDto processFile(MultipartFile file) {

                int savedCount = 0;
                int duplicateCount = 0;

                try {

                        CSVReader csvReader = new CSVReader(new InputStreamReader(file.getInputStream()));

                        //Skip the header row
                        csvReader.readNext();

                        String[] rowContent = csvReader.readNext();
                        if (rowContent != null && rowContent.length > 0) {
                                String dataType = rowContent[0];
                                log.warn("CSV data type: {}", dataType);
                        }

                } catch (Exception e) {
                        log.error("Failed to process CSV file: {}", e.getMessage());
                        throw new RuntimeException("Failed to process CSV file: " + e.getMessage());
                }

                return new CsvProcessDto(savedCount, duplicateCount);

        }

        private void processUser() {}

        private void processJobPosition() {}

}
