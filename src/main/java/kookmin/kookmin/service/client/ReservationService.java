package kookmin.kookmin.service.client;

import kookmin.kookmin.dto.client.ReservationDto;
import kookmin.kookmin.mapper.client.ReservationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReservationService {
    @Autowired
    private ReservationMapper reservationMapper;

    public List<ReservationDto> findByEmail(String email) {
        return reservationMapper.findByEmail(email);
    }

    public HashMap<String, List<ReservationDto>> findByEmailSplitCurrent(String email) {
        Map<Boolean, List<ReservationDto>> currentComparison = findByEmail(email).stream().collect(Collectors.partitioningBy(r -> r.getConfirmedDate() != null && r.getConfirmedDate().before(new Date())));
        HashMap<String, List<ReservationDto>> result =  new HashMap<>();
        result.put("before", currentComparison.get(false));
        result.put("after", currentComparison.get(true));
        return result;
    }

    public HashMap<String, Integer> findByMyInfo(String email){
        HashMap<String, Integer> map = new HashMap<>();
        int confirmedNum = (int)findByEmail(email).stream().filter(r -> r.getConfirmedDate() != null).count();
        int noConfirmedNum = (int)findByEmail(email).stream().filter(r -> r.getConfirmedDate() == null).count();
        int noReviewedNum = (int)findByEmail(email).stream().filter(r -> r.getConfirmedDate() == null).count();
        int RefundApplyNum = (int)findByEmail(email).stream().filter(r -> r.getConfirmedDate() == null).count();

        map.put("noConfirmedNum", noConfirmedNum);
        map.put("confirmedNum", confirmedNum);
        map.put("noReviewedNum", noReviewedNum);
        map.put("RefundApplyNum", RefundApplyNum);

        return map;
    }
}
