package aaiman.hrdashboardapi.repository;

import aaiman.hrdashboardapi.model.Holiday;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

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

}
