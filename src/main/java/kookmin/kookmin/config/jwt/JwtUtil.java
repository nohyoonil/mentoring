package kookmin.kookmin.config.jwt;

import io.jsonwebtoken.*;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import kookmin.kookmin.config.message.MessageComponent;
import kookmin.kookmin.dto.client.user.UserInfoDto;
import kookmin.kookmin.dto.client.MessageCodeAndResDto;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Base64;

@Slf4j
@Component
public class JwtUtil {
    private final SecretKey key;
    private final long accessTokenExpTime;

    private final long refreshTokenExpTime;

    @Autowired
    private MessageComponent messageComponent;

    @Autowired
    private MessageCodeAndResDto messageCodeAndResDto;

    public JwtUtil(@Value("${jwt.private_secret_key}") String secretKey,
                   @Value("${jwt.expiration_time_ten_min}") long accessTokenExpTime,
                   @Value("${jwt.expiration_time_one_year}") long refreshTokenExpTime)
    {
        byte[] keyBytes = Base64.getDecoder().decode(secretKey);
        key = new SecretKeySpec(keyBytes, "HmacSHA256");
        this.accessTokenExpTime = accessTokenExpTime;
        this.refreshTokenExpTime = refreshTokenExpTime;
    }

    /**
     * Access Token 생성
     * @param userInfoDto
     * @return Access Token String
     */
    public String createAccessToken(UserInfoDto userInfoDto)
    {
        return createToken(userInfoDto, accessTokenExpTime);
    }

    public String createRefreshToken(UserInfoDto userInfoDto)
    {
        return createToken(userInfoDto, refreshTokenExpTime);
    }


    /**
     * JWT 생성
     * @param userInfoDto
     * @param expireTime
     * @return JWT String
     */
    private String createToken(UserInfoDto userInfoDto, long expireTime)
    {
        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime tokenValidity = now.plusSeconds(expireTime);
        return Jwts.builder()
                .signWith(key, SignatureAlgorithm.HS256)
                .setSubject(userInfoDto.getUserId())
                .setIssuer("elliott_kim")
                .setExpiration(Date.from(tokenValidity.toInstant()))
                .setIssuedAt(Date.from(now.toInstant()))
                .compact();
    }


    /**
     * Token에서 User ID 추출
     * @param token
     * @return User ID
     */
    public String getUserId(String token)
    {
        return parseSub(token).get("sub", String.class);
    }


    /**
     * JWT 검증
     * @param token
     * @return IsValidate
     */
    public String validateToken(String token)
    {
        try {
            Jws<Claims> header = Jwts.parser()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
//            Date exp = header.getPayload().getExpiration();
//            Date iat = header.getPayload().getIssuedAt();
//            if (exp.compareTo(iat) == -1)
//            {
//
//            }

            System.out.println(Jwts.parser()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token));
            return messageComponent.getSUCCESS();
        }
        catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e)
        {
            log.info("Invalid JWT Token", e);
            throw new RuntimeException(messageComponent.getINVAILD_ACCESS());
        }
        catch (ExpiredJwtException e)
        {
            log.info("Expired JWT Token", e);
            throw new RuntimeException(messageComponent.getEXPIRED_TOKEN());
        }
        catch (UnsupportedJwtException e)
        {
            log.info("Unsupported JWT Token", e);
            throw new RuntimeException(messageComponent.getINVAILD_ACCESS());
        }
        catch (IllegalArgumentException e)
        {
            log.info("JWT claims string is empty.", e);
            throw new RuntimeException(messageComponent.getINVAILD_ACCESS());
        }
    }


    /**
     * JWT Claims 추출
     * @param accessToken
     * @return JWT Claims
     */
    public Claims parseSub(String accessToken)
    {
        try {
            return Jwts.parser().
                    setSigningKey(key).
                    build().
                    parseClaimsJws(accessToken).
                    getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }
}
