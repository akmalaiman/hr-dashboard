package aaiman.hrdashboardapi.service;

import aaiman.hrdashboardapi.model.Holiday;
import aaiman.hrdashboardapi.repository.HolidayRepository;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class HolidayService {
    
    private final HolidayRepository holidayRepository;
    
    public HolidayService(HolidayRepository holidayRepository) {
        this.holidayRepository = holidayRepository;
    }
    
    public List<Holiday> getAll() {
        return holidayRepository.findAllByStatus("Active");
    }
    
    public Holiday createHoliday(Holiday holiday, int userId) {
        
        Holiday newHoliday = new Holiday();
        newHoliday.setName(holiday.getName());
        newHoliday.setHolidayDate(holiday.getHolidayDate());
        newHoliday.setStatus("Active");
        newHoliday.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
        newHoliday.setCreatedBy(userId);
        
        Holiday checkDuplicate = holidayRepository.findByNameDateAndStatus(holiday.getName(), holiday.getHolidayDate(), "Active");
        
        if (checkDuplicate == null) {
            
            holidayRepository.save(newHoliday);
            return newHoliday;
            
        } else {
            return null;
        }
        
    }
    
    public Holiday updateHoliday(Holiday holiday, int userId) {
        
        Holiday existingHoliday = holidayRepository.findById(holiday.getId());
        
        existingHoliday.setName(holiday.getName());
        existingHoliday.setHolidayDate(holiday.getHolidayDate());
        existingHoliday.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
        existingHoliday.setUpdatedBy(userId);
        
        holidayRepository.save(existingHoliday);
        
        return existingHoliday;
        
    }
    
    public int deleteHolidayById(int holidayId, int userId) {
        Timestamp now = Timestamp.valueOf(LocalDateTime.now());
        return holidayRepository.updateHolidayStatus("Deleted", userId, now, holidayId);
    }
    
    public Holiday getHolidayById(int id) {
        return holidayRepository.findById(id);
    }
    
}
