package com.ssafy.triptape.util;

import org.springframework.beans.factory.annotation.Value;

import lombok.extern.log4j.Log4j2;

/*
 * JWT에서 사용되는 토큰 관련 유틸들을 관리하는 클래스
 * - JWT를 생성하거나 유효성을 체크하는 등의 전반적으로 처리되는 기능들을 모아둔 클래스
*/
@Log4j2
public class TokenUtil {
	@Value("${jwt.secret}")
	private static String jwtSecretKey;
	
	// 사용자 정보를 기반으로 토큰 생
	public static String generateJwtToken(User user) {
		JwtBuilder builder
	}
}
