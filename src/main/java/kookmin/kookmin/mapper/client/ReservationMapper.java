package kookmin.kookmin.mapper.client;

import kookmin.kookmin.dto.client.ReservationDto;
import kookmin.kookmin.dto.client.UserDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ReservationMapper {
    List<ReservationDto> findByEmail(String email);
    UserDto findByMentoInfo(ReservationDto reservationDto);
}
