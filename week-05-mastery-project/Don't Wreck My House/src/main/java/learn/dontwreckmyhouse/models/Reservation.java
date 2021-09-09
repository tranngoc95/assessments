package learn.dontwreckmyhouse.models;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class Reservation {
    private String reservationID;
    private LocalDate startDate;
    private LocalDate endDate;
    private Host host;
    private Guest guest;
    private BigDecimal total;

    public void updateTotal(){
        total = BigDecimal.ZERO;

        if(host==null || startDate==null || endDate==null){
            return;
        }

        for(LocalDate d=startDate; d.isBefore(endDate); d=d.plusDays(1)){
            int day = d.getDayOfWeek().getValue();
            total=total.add(day==6||day==7 ? host.getWeekendRate() : host.getStandardRate());
        }
    };
}
