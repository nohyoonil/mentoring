package kookmin.kookmin.dto.client;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MentoDto {
    private String mentoId;
    private String primaryMajor;
    private String subMajor;
    private String introduceTitle;
    private String introduceContent;
    private String userId;
    private String multiMajor;
}
