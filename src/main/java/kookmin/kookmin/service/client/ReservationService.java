package kookmin.kookmin.service.client;

import kookmin.kookmin.Utility.CryptoUtil;
import kookmin.kookmin.config.handler.SignatureNomatchException;
import kookmin.kookmin.dto.client.ReservationDto;
import kookmin.kookmin.dto.client.ReservationfullDto;
import kookmin.kookmin.mapper.client.MentoMapper;
import kookmin.kookmin.mapper.client.ReservationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    @Value("${crypto.kookmin.key}")
    private String kookminKey;

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
        if(r.getReservationStatus() == 4){
            rf.setRefundTime(r.getRefundTime());
            rf.setRefundBankName(r.getRefundBankName());
            rf.setRefundBankNum(decryptBankNums(r.getRefundBankNum()));
        }
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
    public Boolean signatureChk(ReservationDto reservationDto){
        System.out.println("서명검증 시작");
        String originSign = reservationMapper.findSignatureById(reservationDto.getReservationId());
        System.out.println("기존 서명:"+originSign);
        ReservationDto oldR = findById(reservationDto.getReservationId());
        String newSign = null;
        try{
            String num = CryptoUtil.decryptDataByAES(oldR.getRefundBankNum(),kookminKey);
            System.out.println(num);
            oldR.setRefundBankNum(num);
            newSign = CryptoUtil.hashingSha512(oldR.getRefundBankNum()+"_"+oldR.getRefundBankName()+"_"+oldR.getReservationId());
            System.out.println("확인 서명:" + newSign);
        }catch(Exception e){
            System.out.println(e);
            e.printStackTrace();
        }
        return originSign.equals(newSign);
    }
    public void updateReservationSignature(ReservationDto reservationDto){
        String signature = null;
        try{
            signature = CryptoUtil.hashingSha512(reservationDto.getRefundBankNum()+"_"+reservationDto.getRefundBankName()+"_"+reservationDto.getReservationId());
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }
        reservationMapper.updateSign(signature, reservationDto.getReservationId());
    }
    public void replaceBankNum(ReservationDto reservationDto){
        String replaceRefundBankNum = null;
        try{
            replaceRefundBankNum = CryptoUtil.encryptDataByAES(reservationDto.getRefundBankNum(), kookminKey);
        }catch(Exception e){
            System.out.println(e);
            e.printStackTrace();
        }
        reservationMapper.replaceBankNum(replaceRefundBankNum, reservationDto.getReservationId());
    }
    public void refundEdit(ReservationDto r){
        //서명검증
        Boolean flag = signatureChk(r);
        System.out.println("서명검증 결과값 :"+flag);
        System.out.println("서명검증 완료 해서 다음 메소드로 넘어옴");
        System.out.println("업데이트 할 값 : "+ r);

        try{
            if(flag){
                //서명검증 완료되면 DB 값 변경 및 변경된 값으로 r 값 변경
                reservationMapper.refundEdit(r);

                //r 값으로 새 서명검증 업데이트
                updateReservationSignature(r);

                //계좌 번호 암호화
                replaceBankNum(r);
            }else{
                //서명검증이 안되면 떤지기ㅠㅠ
                throw new SignatureNomatchException("서명검증 값이 알맞지 않은듯?");
            }
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }
    }
    public void refund(ReservationDto reservationDto){
        reservationMapper.refund(reservationDto);
        updateReservationSignature(reservationDto);
        replaceBankNum(reservationDto);
    }

    public String decryptBankNums(String encryptBankNums){
        String decryptRefundBankNum = null;
        try{
            decryptRefundBankNum = CryptoUtil.decryptDataByAES(encryptBankNums, kookminKey);
        }catch(Exception e){
            System.out.println(e);
            e.printStackTrace();
        }
        return decryptRefundBankNum;
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
