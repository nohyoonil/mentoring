package kookmin.kookmin.service.client;

import kookmin.kookmin.dto.client.ReservationDto;
import kookmin.kookmin.dto.client.ReservationfullDto;
import kookmin.kookmin.mapper.client.ReservationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReservationService {
    @Autowired
    private ReservationMapper reservationMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private PlanService planService;

    public List<ReservationDto> findByEmail(String email) {
        return reservationMapper.findByEmail(email);
    }

    public Map<Integer, List<ReservationfullDto>> findByEmailSplitStatus(String email){
        //Map<Integer, List<ReservationDto>> map = findByEmail(email).stream().collect(Collectors.groupingBy(r -> r.getReservationStatus()));
        Map<Integer, List<ReservationfullDto>> map = findByEmail(email).stream()
                .map(r -> {
                    ReservationfullDto r2 = new ReservationfullDto();
                    r2.setReservationId(r.getReservationId());
                    r2.setAskType(r.getAskType());
                    r2.setAskContent(r.getAskContent());
                    r2.setDesiredDate1(r.getDesiredDate1());
                    r2.setDesiredDate2(r.getDesiredDate2());
                    r2.setReservationDate(r.getReservationDate());
                    r2.setReservationStatus(r.getReservationStatus());
                    r2.setPosition(r.getPosition());
                    r2.setUser(userService.findByUserId(r.getUserId()));
                    r2.setMento(userService.findByMentoUserInfoReservation(r.getReservationId()));
                    r2.setPlan(planService.findByPlanTitle(r.getPlanTitle()));
                    r2.setReviewScore(r.getReviewScore());
                    r2.setReviewContent(r.getReviewContent());
                    r2.setReviewDate(r.getReviewDate());
                    r2.setRefundTime(r.getRefundTime());
                    r2.setRefundBankName(r.getRefundBankName());
                    r2.setRefundBankNum(r.getRefundBankNum());
                    return r2;
                }).collect(Collectors.groupingBy(r -> r.getReservationStatus()));

        return map;
    }

    public HashMap<String, Integer> myInfoNums(String email){
        HashMap<String, Integer> map = new HashMap<>();
        String keyset [] = {"yetMoneyNum", "moneyGet","reviewNo","refundStay"};
        for(int i = 0; i < keyset.length; i++){
            List<ReservationfullDto> list =findByEmailSplitStatus(email).get(i+1);
            if(list != null){
                if(keyset[i].equals("reviewNo")){
                    list = list.stream().filter(r-> r.getReviewDate() == null).toList();
                }
                map.put(keyset[i], list.size());
            }else{
                map.put(keyset[i], 0);
            }
        }
        return map;
    }
}
