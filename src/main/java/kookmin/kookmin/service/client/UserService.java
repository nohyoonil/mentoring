package kookmin.kookmin.service.client;

import kookmin.kookmin.dto.client.ReservationDto;
import kookmin.kookmin.dto.client.UserDto;
import kookmin.kookmin.mapper.client.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    public List<UserDto> findAll() {
        return userMapper.findAll();
    }

    public void insert(UserDto userdto){
        userMapper.insert(userdto);
    }

    public UserDto findByEmail(String email){
        return userMapper.findByEmail(email);
    }

    public void update(UserDto userdto){
        userMapper.update(userdto);
    }

    public UserDto findByUserId(String userId){
        return userMapper.findByUserId(userId);
    }

    public UserDto findByMentoUserInfoReservation(String reservationId){
        return userMapper.findByMentoUserInfoReservation(reservationId);
    }
}
