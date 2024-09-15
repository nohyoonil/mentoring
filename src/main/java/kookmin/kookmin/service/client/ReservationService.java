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
    public ReservationfullDto replaceFullDto(ReservationDto r) {
        ReservationfullDto rf = new ReservationfullDto();
        rf.setReservationId(r.getReservationId());
        rf.setAskType(r.getAskType());
        rf.setAskContent(r.getAskContent());
        rf.setDesiredDate1(r.getDesiredDate1());
        rf.setDesiredDate2(r.getDesiredDate2());
        rf.setReservationDate(r.getReservationDate());
        rf.setReservationStatus(r.getReservationStatus());
        rf.setPosition(r.getPosition());
        rf.setUser(userService.findByUserId(r.getUserId()));
        rf.setMento(userService.findByMentoUserInfoReservation(r.getReservationId()));
        rf.setPlan(planService.findByPlanTitle(r.getPlanTitle()));
        rf.setReviewScore(r.getReviewScore());
        rf.setReviewContent(r.getReviewContent());
        rf.setReviewDate(r.getReviewDate());
        rf.setRefundTime(r.getRefundTime());
        rf.setRefundBankName(r.getRefundBankName());
        rf.setRefundBankNum(r.getRefundBankNum());
        return rf;
    }
    public Map<Integer, List<ReservationfullDto>> findByEmailSplitStatus(String email){
        Map<Integer, List<ReservationfullDto>> map = findByEmail(email).stream()
                .map(r -> replaceFullDto(r)).collect(Collectors.groupingBy(r -> r.getReservationStatus()));

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

    public void refund(ReservationDto reservationDto){
        reservationMapper.refund(reservationDto);
    }

    public ReservationDto findById(String reservationId){
        return reservationMapper.findById(reservationId);
    }
}
