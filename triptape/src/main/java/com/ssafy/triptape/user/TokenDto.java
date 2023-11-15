package com.ssafy.triptape.user;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import com.ssafy.triptape.file.FileInfoDto;

import io.swagger.annotations.ApiModelProperty;

@ToString
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TokenDto {
	
	private int userKey;
	private int refreshToken;
}
