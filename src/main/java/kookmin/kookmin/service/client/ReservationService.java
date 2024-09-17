package kookmin.kookmin.service.client;

import kookmin.kookmin.dto.client.ReservationDto;
import kookmin.kookmin.dto.client.ReservationfullDto;
import kookmin.kookmin.mapper.client.MentoMapper;
import kookmin.kookmin.mapper.client.ReservationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
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
    @Autowired
    private MentoMapper mentoMapper;

    public List<ReservationDto> findByEmail(String email) {
        return reservationMapper.findByEmail(email);
    }
    public List<ReservationDto> findByMentoId(String mentoId){
        return reservationMapper.findByMentoId(mentoId);
    }

    public ReservationfullDto replaceFullDto(ReservationDto r) {
        ReservationfullDto rf = new ReservationfullDto();
        if(r == null){
            System.out.println("r 이 null임");
            return null;
        }
        rf.setReservationId(r.getReservationId());
        rf.setAskType(r.getAskType());
        rf.setAskContent(r.getAskContent());
        rf.setDesiredDate1(r.getDesiredDate1());
        rf.setDesiredDate2(r.getDesiredDate2());
        rf.setReservationDate(r.getReservationDate());
        rf.setReservationStatus(r.getReservationStatus());
        rf.setPosition(r.getPosition());
        rf.setUser(userService.findByUserId(r.getUserId()));
        rf.setMento(mentoMapper.findUserByMentoId(r.getMentoId()));
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
        String []keyset = {"yetMoneyNum", "moneyGet","reviewNo","refundStay"};
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

    public void review(ReservationDto reservationDto){
        reservationMapper.review(reservationDto);
    }

    public Date replaceDate(LocalDate ld, LocalTime lt){
        LocalDateTime ldt = LocalDateTime.of(ld, lt);
        Date d = Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
        return d;
    }

    public ReservationDto stepSetReservation01(String mentoId, String userId, LocalDate desiredDateDay1, LocalTime desiredDateTime1, LocalDate desiredDateDay2, LocalTime desiredDateTime2, ReservationDto InputReservation){
        ReservationDto reservationDto = new ReservationDto();
        Date d1 = replaceDate(desiredDateDay1, desiredDateTime1);
        Date d2 = replaceDate(desiredDateDay2, desiredDateTime2);
        reservationDto.setUserId(userId);
        if(mentoId != null && !mentoId.equals("")){
            reservationDto.setMentoId(mentoId);
        }
        reservationDto.setAskType(InputReservation.getAskType());
        reservationDto.setAskContent(InputReservation.getAskContent());
        reservationDto.setPosition(InputReservation.getPosition());
        reservationDto.setDesiredDate1(d1);
        reservationDto.setDesiredDate2(d2);
        reservationDto.setReservationDate(new Date());
        reservationDto.setReservationStatus(1);
        return reservationDto;
    }

    public void newReservation(ReservationDto reservationDto){
        reservationMapper.newReservation(reservationDto);
    }
}
