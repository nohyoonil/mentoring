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

    public MentoDto findByMentoId(String mentoId) {
        return mentoMapper.findByMentoId(mentoId);
    }
    public UserDto findUserByMentoId(String mentoId) {
        return mentoMapper.findUserByMentoId(mentoId);
    }

    public Map<Boolean, List<ActDto>> findActByMentoId(String mentoId) {
        Map<Boolean, List<ActDto>> map= mentoMapper.findActByMentoId(mentoId).stream().collect(Collectors.groupingBy(a -> a.getCampusAct()));
        return map;
    }

    public Map<String, Integer> mentoNumInfo(String mentoId) {
        Map <String, Integer> map = new HashMap<>();
        List<ReservationDto> reservationList = reservationService.findByMentoId(mentoId);
        if(reservationList != null){
            map.put("count", reservationList.size());
            map.put("satisfaction", (int)((reservationList.stream().filter(r -> r.getReviewScore() != 0).mapToInt(r -> r.getReviewScore()).average().getAsDouble()) / 7 * 100));
            map.put("moneyAvg", (int)(reservationList.stream().mapToInt(r -> {
                return reservationService.replaceFullDto(r).getPlan().getPlanPrice();
            }).average().getAsDouble()));
        }else{
            map.put("countReservation", 0);
            map.put("satisfaction", 0);
            map.put("moneyAvg", 0);
        }
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
