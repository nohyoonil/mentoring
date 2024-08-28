//package kookmin.kookmin.controller.client;
//
//import jakarta.validation.Valid;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//import kookmin.kookmin.config.jwt.JwtUtil;
//import kookmin.kookmin.config.message.MessageComponent;
//import kookmin.kookmin.dto.client.*;
//import kookmin.kookmin.dto.client.auth.*;
//import kookmin.kookmin.service.client.AuthService;
//
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("/auth")
//public class AuthApiController {
//
//    private final AuthService authService;
//
//    @Autowired
//    private MessageComponent messageComponent;
//    @Autowired
//    private MessageCodeAndResDto response;
//
////    @Autowired
////    private PhoneAuthDto phoneAuthDto;
//
//    @Autowired
//    private final JwtUtil jwtUtil;
//
////    @PostMapping("/phone")
////    public MessageCodeAndResDto phone(@Valid @RequestBody PhoneDto phoneDto)
////    {
////        try
////        {
////            this.authService.checkExistUserByPhoneNum(phoneDto.getPhone());
////            response.setMessage(messageComponent.getLOG_IN());
////            response.setData(null);
////            return response;
////        }
////        catch (Exception e)
////        {
////            response.setMessage(e.getMessage());
////            response.setData(null);
////            return response;
////        }
////    }
////
////    @PostMapping("/login")
////    public MessageCodeAndResDto logIn(@Valid @RequestBody PhoneAuthDto phoneAuthDto)
////    {
////        try
////        {
////            UserInfoDto userInfoDto = this.authService.checkExistUserByPhoneNum(phoneAuthDto.getPhone());
////            this.authService.checkPhoneAuth(phoneAuthDto);
////            LoginResDto loginResDto = new LoginResDto();
////            loginResDto.setName(userInfoDto.getName());
////            loginResDto.setPhoneNumber(userInfoDto.getPhoneNum());
////            loginResDto.setAccessToken(this.authService.getAccessToken(userInfoDto));
////            loginResDto.setRefreshToken(this.authService.getRefreshToken(userInfoDto));
////
////            response.setMessage(messageComponent.getSUCCESS());
////            response.setData(loginResDto);
////            return response;
////        }
////        catch (Exception e)
////        {
////            response.setMessage(e.getMessage());
////            response.setData(null);
////            return response;
////        }
////    }
////
////    @PostMapping("/verify")
////    public MessageCodeAndResDto verify(@Valid @RequestBody PhoneAuthDto phoneAuthDto)
////    {
////        try
////        {
////            response.setMessage(messageComponent.getAUTH_SUCCESS());
////            response.setData(null);
////            return response;
////        }
////        catch (Exception e)
////        {
////            response.setMessage(e.getMessage());
////            response.setData(null);
////            return response;
////        }
////    }
////
////    @PostMapping("/register")
////    public MessageCodeAndResDto register(@Valid @RequestBody() RegisterDto registerDto)
////    {
////        try
////        {
////            if (this.authService.checkExistUserByPhoneNum(registerDto.getPhone()) != null)
////            {
////                response.setMessage(messageComponent.getINVAILD_ACCESS());
////                response.setData(null);
////                return response;
////            }
////
////        }
////        catch (Exception e)
////        {
////        }
////
////        try
////        {
////            response.setMessage(messageComponent.getSUCCESS());
////            this.authService.register(registerDto);
////
////            UserInfoDto userInfoDto = this.authService.checkExistUserByPhoneNum(registerDto.getPhone());
////
////            LoginResDto loginResDto = new LoginResDto();
////            loginResDto.setName(userInfoDto.getName());
////            loginResDto.setPhoneNumber(userInfoDto.getPhoneNum());
////            loginResDto.setAccessToken(this.authService.getAccessToken(userInfoDto));
////            loginResDto.setRefreshToken(this.authService.getRefreshToken(userInfoDto));
////
////            response.setData(loginResDto);
////            return response;
////        }
////        catch (Exception e)
////        {
////            response.setMessage(messageComponent.getINVAILD_ACCESS());
////            response.setData(null);
////            return response;
////        }
////    }
////
////    @PostMapping("/logout")
////    public MessageCodeAndResDto logout()
////    {
////        try
////        {
////            response.setMessage(messageComponent.getSUCCESS());
////            response.setData(null);
////            return response;
////        }
////        catch (Exception e)
////        {
////            response.setMessage(messageComponent.getINVAILD_ACCESS());
////            response.setData(null);
////            return response;
////        }
////    }
////
////    @PostMapping("/refreshToken")
////    public MessageCodeAndResDto refreshToken(@RequestHeader("Authorization") String Authorization)
////    {
////        try
////        {
////            String token = Authorization.substring(7);
////            UserInfoDto userInfoDto = this.authService.checkExistUserById(jwtUtil.getUserId(token));
////
////            GetAccessTokenDto getAccessTokenDto = new GetAccessTokenDto();
////            getAccessTokenDto.setName(userInfoDto.getName());
////            getAccessTokenDto.setAccessToken(this.authService.getAccessToken(userInfoDto));
////
////            response.setMessage(messageComponent.getSUCCESS());
////            response.setData(getAccessTokenDto);
////            return response;
////        }
////        catch (Exception e)
////        {
////            response.setMessage(e.getMessage());
////            response.setData(null);
////            return response;
////        }
////    }
//}