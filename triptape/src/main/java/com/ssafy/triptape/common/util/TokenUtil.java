package com.ssafy.triptape.common.util;

import com.ssafy.triptape.user.UserDto;

import io.jsonwebtoken.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/*
 * JWT에서 사용되는 토큰 관련 유틸들을 관리하는 클래스
 * - JWT를 생성하거나 유효성을 체크하는 등의 전반적으로 처리되는 기능들을 모아둔 클래스
*/
@Log4j2
public class TokenUtil {
	@Value("${jwt.secret}")
	private static String jwtSecretKey;
	
	// 사용자 정보를 기반으로 토큰 생성 후 반환해주는 메서드
	public static String generateJwtToken(UserDto user) {
		JwtBuilder builder = Jwts.builder()
				.setHeader(createHeader()) // 헤더 지정
				.setClaims(createClaims(user)) // 클레임 지정
				.setSubject(String.valueOf(user.getUserKey())) // subject 구성
				.signWith(SignatureAlgorithm.HS256, createSignature()) // signature 구성
				.setExpiration(createExpiredDate()); // 만료일 지정
		return builder.compact();
	}
	
	// 토큰을 기반으로 사용자 정보 반환
	public static String parseTokenToUserInfo(String token) {
		return Jwts.parser()
				   .setSigningKey(jwtSecretKey)
				   .parseClaimsJws(token)
				   .getBody()
				   .getSubject();
							
	}
	
	// 유효한 토큰인지 확인
	public static boolean isValidToken(String token) {
		try {
			Claims claimes = getClaimsFormToken(token);
			
			return true;
		} catch (ExpiredJwtException exception) {
            log.error("Token Expired");
            return false;
        } catch (JwtException exception) {
            log.error("Token Tampered");
            return false;
        } catch (NullPointerException exception) {
            log.error("Token is null");
            return false;
        }
	}
	
	// 헤더 내에 토큰 추출
	public static String getTokenFromHeader(String header) {
		return header.split(" ")[1];
	}
	
	// 토큰 만료 시간 지정 - 30일로 설정
	private static Date createExpiredDate() {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.HOUR, 8);
		return c.getTime();
	}
	
	// JWT 헤더 값을 생성해주는 메서드
	private static Map<String, Object> createHeader() {
		Map<String, Object> header = new HashMap<>();
		
		header.put("typ", "JWT");
		header.put("alg", "HS256");
		header.put("regDate", System.currentTimeMillis());
		return header;
	}
	
	// 사용자 정보 기반 클레임 생성
	private static Map<String, Object> createClaims(UserDto user) {
		// 공개 클레임에 사용자 이름과 이메일을 설정하여 정보 조회 가능
		Map<String, Object> claims = new HashMap<>();
		
		claims.put("userId", user.getId());
		claims.put("userEmail", user.getEmail());
		return claims;
	}
	
	// jwt 서명 발급을 해주는 메서드
	private static Key createSignature() {
		byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(jwtSecretKey);
		return new SecretKeySpec(apiKeySecretBytes, SignatureAlgorithm.HS256.getJcaName());
	}
	
	// 토큰 정보를 기반으로 Claims 정보를 반환 받는 메서드
	private static Claims getClaimsFormToken(String token) {
		return Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(jwtSecretKey))
				.parseClaimsJws(token).getBody();
	}
	// 토큰을 기반으로 사용자 정보를 반환하는 메서드
	public static String getUserIdFromToken(String token) {
        Claims claims = getClaimsFormToken(token);
        return claims.get("userId").toString();
    }
}
