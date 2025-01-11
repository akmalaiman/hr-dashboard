package aaiman.hrdashboardapi.repository;

import aaiman.hrdashboardapi.model.Holiday;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface HolidayRepository extends JpaRepository<Holiday, Integer> {

        List<Holiday> findAllByStatus(String status);

        @Query(
                value = "SELECT * FROM holiday WHERE name = :name AND holiday_date = :holidayDate AND status = :status",
                nativeQuery = true
        )
        Holiday findByNameDateAndStatus(@Param("name") String name, @Param("holidayDate") LocalDate holidayDate, @Param("status") String status);

        Holiday findById(int id);

        @Modifying
        @Transactional
        @Query(
                value = "UPDATE holiday SET status = :status, updated_by = :updatedBy, updated_at = :updatedAt WHERE id = :id",
                nativeQuery = true
        )
        int updateHolidayStatus(@Param("status") String status, @Param("updatedBy") int updatedBy, @Param("updatedAt") Timestamp updatedAt, @Param("id") int id);

}
