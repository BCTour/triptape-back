package com.ssafy.triptape.user;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import com.ssafy.triptape.file.FileInfoDto;

import io.swagger.annotations.ApiModelProperty;

/**
 * JWT 토큰에 대한 DTO입니다.
 */

@ToString
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TokenDto {
	private int userKey;
	private int refreshToken;
}
