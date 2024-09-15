package kookmin.kookmin.mapper.client;

import kookmin.kookmin.dto.client.ActDto;
import kookmin.kookmin.dto.client.MentoDto;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface MentoMapper {
    MentoDto findByMentoId(String mentoId);
    List<ActDto> findActByMentoId(String mentoId);
}
