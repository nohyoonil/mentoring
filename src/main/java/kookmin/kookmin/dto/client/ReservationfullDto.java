package kookmin.kookmin.dto.client;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.catalina.User;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservationfullDto {
    private String reservationId;
    private int askType;
    private String askContent;
    private Date desiredDate1;
    private Date desiredDate2;
    private Date reservationDate;
    private int reservationStatus;
    private String position;
    private UserDto user;
    private UserDto mento;
    private String mentoId;
    private PlanDto plan;
    private int reviewScore;
    private String reviewContent;
    private Date reviewDate;
    private Date refundTime;
    private String refundBankName;
    private String refundBankNum;
}