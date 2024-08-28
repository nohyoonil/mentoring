//package kookmin.kookmin.service.client;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import kookmin.kookmin.config.jwt.JwtUtil;
//import kookmin.kookmin.config.message.MessageComponent;
//import kookmin.kookmin.dto.client.user.UserInfoDto;
//import kookmin.kookmin.mapper.client.UserInfoMapper;
//
//import java.util.HashMap;
//
//@RequiredArgsConstructor
//@Transactional(readOnly = true)
//@Service
//public class AuthService {
//
//    private final JwtUtil jwtUtil;
//    //private final PasswordEncoder encoder;
//
//    @Autowired
//    private MessageComponent messageComponent;
//
//    // 얘 매퍼로 엮어서 db조회로 데려가야함. 그래서 내가 회원관리해줄 Dto던 뭐던 따로 만들어야 할 듯?
//    @Autowired
//    private UserInfoMapper userInfoMapper;
//
//    // model mapper를 안쓸꺼면 필요가 굳이 있나 싶네.... 이거 지금 타입 변경해주면서 매핑할 때 필요한건데 이게 굳이 지금..?
//    //private final ModelMapper modelMapper;
//    //@Override
//
//    @Transactional
//    public UserInfoDto checkExistUserByPhoneNum(String phoneNum)
//    {
//        HashMap<String, Object> paramMap = new HashMap<>();
//        paramMap.put("PhoneNum", phoneNum);
//        UserInfoDto userInfoDto = userInfoMapper.findUserInfoByPhone(paramMap);
//        if(userInfoDto == null)
//        {
//            throw new RuntimeException(messageComponent.getNO_MEMBER());
//        }
//        return userInfoDto;
//    }
//
//    @Transactional
//    public UserInfoDto checkExistUserById(String id)
//    {
//        HashMap<String, Object> paramMap = new HashMap<>();
//        paramMap.put("UserID", id);
//        UserInfoDto userInfoDto = userInfoMapper.findUserInfoById(paramMap);
//        if(userInfoDto == null)
//        {
//            throw new RuntimeException(messageComponent.getNO_MEMBER());
//        }
//        return userInfoDto;
//    }
//
////    @Transactional
////    public boolean checkPhoneAuth(PhoneAuthDto phoneAuthDto)
////    {
////        HashMap<String, Object> paramMap = new HashMap<>();
////        paramMap.put("PhoneNum", phoneAuthDto.getPhone());
////        String code = phoneAuthDto.getCode();
////
////        // 여기서 인증번호 code 확인하는 절차 있어야함........ 아직 코드를 못짜서..............
////        // 여기서 인증번호 code 확인 실패하면 다음과 같이 return 넘기기
////        // throw new RuntimeException(messageComponent.getAUTH_FAIL());
////
////        return true;
////    }
////
////    @Transactional
////    public boolean register(RegisterDto registerDto)
////    {
////        try
////        {
////            HashMap<String, Object> paramMap = new HashMap<>();
////            paramMap.put("Name", registerDto.getName());
////            paramMap.put("PhoneNum", registerDto.getPhone());
////            paramMap.put("Address", registerDto.getAddress());
////            userInfoMapper.registerUserInfo(paramMap);
////        }
////        catch (Exception e)
////        {
////            throw new RuntimeException(messageComponent.getINVAILD_ACCESS());
////        }
////        return true;
////    }
////    @Transactional
////    public boolean modifyUserInfo(UserInfoDto userInfoDto)
////    {
////        try
////        {
////            HashMap<String, Object> paramMap = new HashMap<>();
////            paramMap.put("UserID", userInfoDto.getUserID());
////            paramMap.put("PhoneNum", userInfoDto.getPhoneNum());
////            paramMap.put("Address", userInfoDto.getAddress());
////            userInfoMapper.modifyUserInfo(paramMap);
////        }
////        catch (Exception e)
////        {
////            throw new RuntimeException(messageComponent.getINVAILD_ACCESS());
////        }
////        return true;
////    }
//
//    public String getAccessToken(UserInfoDto userInfoDto)
//    {
//        return jwtUtil.createAccessToken(userInfoDto);
//    }
//
//    public String getRefreshToken(UserInfoDto userInfoDto)
//    {
//        return jwtUtil.createRefreshToken(userInfoDto);
//    }
//
//}