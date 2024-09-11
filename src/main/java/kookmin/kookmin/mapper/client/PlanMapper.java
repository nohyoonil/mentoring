package kookmin.kookmin.mapper.client;

import kookmin.kookmin.dto.client.PlanDto;
import kookmin.kookmin.dto.client.UserDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PlanMapper {
    PlanDto findByPlanTitle(String planTitle);
}
