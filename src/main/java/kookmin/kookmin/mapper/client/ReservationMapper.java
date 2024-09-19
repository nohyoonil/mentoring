package kookmin.kookmin.mapper.client;

import kookmin.kookmin.dto.client.ReservationDto;
import kookmin.kookmin.dto.client.UserDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ReservationMapper {
    List<ReservationDto> findByEmail(String email);
    void refund(ReservationDto reservationDto);
    ReservationDto findById(String reservationId);
    void review(ReservationDto reservationDto);
    List<ReservationDto> findByMentoId(String mentoId);
    void newReservation(ReservationDto reservationDto);
    void updateSign(String signature, String reservationId);
    void replaceBankNum(String refundBankNum, String reservationId);
    String findSignatureById(String reservationId);
    void refundEdit(ReservationDto reservationDto);
    void deleteById(String reservationId);
}
