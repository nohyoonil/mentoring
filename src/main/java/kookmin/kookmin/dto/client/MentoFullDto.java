package kookmin.kookmin.dto.client;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MentoFullDto {
    private String mentoId;
    private String primaryMajor;
    private String subMajor;
    private String introduceTitle;
    private String introduceContent;
    private UserDto user;
    private String multiMajor;
}
