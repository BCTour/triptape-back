package com.ssafy.triptape.user;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import com.ssafy.triptape.file.FileInfoDto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ToString
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "탈퇴 회원에 대한 정보")
public class WithdrawalsDto {
	
	@ApiModelProperty(value = "아이디")
	private String userId;
	
	@ApiModelProperty(value="탈퇴 날짜")
	private String withdrawalDate;
	
	@ApiModelProperty(value="탈퇴 사유")
	private String withdrawalReason;
	
	@ApiModelProperty(value = "비밀번호")
	private String userPw;
}
