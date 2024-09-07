package kookmin.kookmin.dto.client;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MentoDto {
    private UUID mentoId;
    private String primary_major;
    private String sub_major;
    private String introduce_title;
    private String introduce_content;
    private UUID userId;
    private String multi_major;
}
