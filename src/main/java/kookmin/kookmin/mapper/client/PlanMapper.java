package kookmin.kookmin.mapper.client;

import kookmin.kookmin.dto.client.ReservationDto;
import kookmin.kookmin.dto.client.UserDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {
    List<UserDto> findAll();
    void insert(UserDto user);
    UserDto findByEmail(String email);
    void update(UserDto user);
    UserDto findByMentoUserInfoReservation(String reservationId);
    UserDto findByUserId(String userId);
}
