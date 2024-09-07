package kookmin.kookmin.dto.client;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActDto {
    private int campusYear;
    private String title;
    private String content;
    private Boolean campusAct;
    private UUID mentoId;
}
