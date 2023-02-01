package best.ccg.JbAdminApp.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

public class TransientJbServerEmployeeGroupLogDate {
    @Getter
    @Setter
    Long id;
    @Getter
    @Setter
    LocalDateTime dateTime;


    public TransientJbServerEmployeeGroupLogDate(Long id, LocalDateTime dateTime) {
        this.id = id;
        this.dateTime = dateTime;
    }

    @Override
    public String toString() {
        return "ReportJbServerEmployee{" +
                "employeeId=" + id +
                ", MaxDateError=" + dateTime +
                '}';
    }
}
