package kookmin.kookmin.dto.client;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private UUID userId;
    private String email;
    private String pwd;
    private String nickname;
    private String department;
    private String studentNumber;
    private int grade;
    private int birthYear;
    private int currentStatus;
    private String phone;
}
