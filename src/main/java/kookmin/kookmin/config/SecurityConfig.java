//package kookmin.kookmin.config;
//
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.Customizer;
//import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.web.AuthenticationEntryPoint;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.access.AccessDeniedHandler;
//import kookmin.kookmin.config.jwt.JwtUtil;
////import sumichan.sumichan.service.client.SumichanUserDetailsService;
//
//@Configuration
//@EnableWebSecurity
//@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
//@AllArgsConstructor
//@Getter
//public class SecurityConfig  {
//    private final JwtUtil jwtUtil;
//    private final AccessDeniedHandler accessDeniedHandler;
//    private final AuthenticationEntryPoint authenticationEntryPoint;
//
//    private static final String[] AUTH_WHITELIST = {
//            "/api/v1/member/**", "/swagger-ui/**", "/api-docs", "/swagger-ui-custom.html",
//            "/v3/api-docs/**", "/api-docs/**", "/swagger-ui.html", "/api/v1/auth/**",
//            "/auth/**", "/bento/**", "/preOrder", "/user"
//    };
//
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        //CSRF, CORS
//        http.csrf((csrf) -> csrf.disable());
//        http.cors(Customizer.withDefaults());
//
//        //세션 관리 상태 없음으로 구성, Spring Security가 세션 생성 or 사용 X
//        http.sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(
//                SessionCreationPolicy.STATELESS));
//
//        //FormLogin, BasicHttp 비활성화
//        http.formLogin((form) -> form.disable());
//        http.httpBasic(AbstractHttpConfigurer::disable);
//
//        http.exceptionHandling((exceptionHandling) -> exceptionHandling
//                .authenticationEntryPoint(authenticationEntryPoint)
//                .accessDeniedHandler(accessDeniedHandler)
//        );
//        http.authorizeRequests().requestMatchers(AUTH_WHITELIST).permitAll().anyRequest().authenticated();
//
////        //JwtAuthFilter를 UsernamePasswordAuthenticationFilter 앞에 추가
//        //http.addFilterBefore(new JwtAuthFilter(sumichanUserDetailsService, jwtUtil), UsernamePasswordAuthenticationFilter.class);
//        return http.build();
//    }
//}
//
//
//
