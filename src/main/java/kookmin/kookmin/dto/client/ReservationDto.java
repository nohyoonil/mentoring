package kookmin.kookmin.dto.client;

import lombok.*;

import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservationDto {
    private UUID reservationId;
    private int askType;
    private String askContent;
    private Date desiredDate1;
    private Date desiredDate2;
    private Date reservationDate;
    private Date confirmedDate;
    private String position;
    private UUID userId;
    private UUID mentoId;
    private String planTitle;
    private int reviewScore;
    private String reviewContent;
    private Date reviewDate;
    private Date refundTime;
    private String refundBankName;
    private String refundBankNum;
}
