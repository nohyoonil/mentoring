package kookmin.kookmin.service.client;

import kookmin.kookmin.dto.client.*;
import kookmin.kookmin.mapper.client.MentoMapper;
import kookmin.kookmin.mapper.client.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MentoService {
    @Autowired
    private MentoMapper mentoMapper;
    @Autowired
    private ReservationService reservationService;
    @Autowired
    private UserService userService;

    private static final int MAX_REVIEW_SCORE = 7;

    public MentoDto findByMentoId(String mentoId) {
        return mentoMapper.findByMentoId(mentoId);
    }

    public Map<Boolean, List<ActDto>> findActByMentoId(String mentoId) {
        Map<Boolean, List<ActDto>> map= mentoMapper.findActByMentoId(mentoId).stream().collect(Collectors.groupingBy(a -> a.getCampusAct()));
        return map;
    }

    public Map<String, Integer> mentoNumInfo(String mentoId) {
        Map <String, Integer> map = new HashMap<>();
        List<ReservationDto> reservationList = reservationService.findByMentoId(mentoId);
        int count = 0;
        int stisfaction = 0;
        int moneyAvg = 0;
        if(reservationList != null){
            count = reservationList.size();
            stisfaction = (int)((reservationList.stream().filter(r -> r.getReviewScore() != 0).mapToInt(r -> r.getReviewScore()).average().getAsDouble()) / MAX_REVIEW_SCORE * 100);
            moneyAvg = (int)(reservationList.stream().mapToInt(r -> reservationService.replaceFullDto(r).getPlan().getPlanPrice()).average().getAsDouble());
        }
        map.put("count", count);
        map.put("satisfaction", stisfaction);
        map.put("moneyAvg", moneyAvg);
        return map;
    }

    public MentoFullDto ReplaceMentoFullDto(MentoDto m){
        MentoFullDto mf = new MentoFullDto();
        mf.setMentoId(m.getMentoId());
        mf.setPrimaryMajor(m.getPrimaryMajor());
        mf.setSubMajor(m.getSubMajor());
        mf.setIntroduceTitle(m.getIntroduceTitle());
        mf.setIntroduceContent( m.getIntroduceContent());
        mf.setUser(userService.findByUserId(m.getUserId()));
        mf.setMultiMajor(m.getMultiMajor());
        return mf;
    }
    public List<MentoFullDto> findMentoBig3(){
        List<MentoFullDto> list = mentoMapper.findMentoBig3().stream().map(m -> ReplaceMentoFullDto(m)).toList();
        return list;
    }
}
