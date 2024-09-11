package kookmin.kookmin.service.client;

import kookmin.kookmin.dto.client.PlanDto;
import kookmin.kookmin.mapper.client.PlanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlanService {
    @Autowired
    private PlanMapper planMapper;

    public PlanDto findByPlanTitle(String title) {
        return planMapper.findByPlanTitle(title);
    }
}
