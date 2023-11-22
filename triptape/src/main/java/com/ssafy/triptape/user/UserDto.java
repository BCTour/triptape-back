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
@ApiModel(value = "사용자에 대한 정보")
public class UserDto {
	
	@ApiModelProperty(value = "아이디")
	private String userId;
	
	@ApiModelProperty(value = "비밀번호")
	private String userPw;
	
	@ApiModelProperty(value = "이름")
	private String userName;
	
	@ApiModelProperty(value = "이메일")
	private String email;
	
	@ApiModelProperty(value = "전화번호")
	private String tel;
	
	@ApiModelProperty(value = "프로필 이미지")
	private FileInfoDto profileImg;
	
	@ApiModelProperty(value = "생일")
	private String birthday;
	
	@ApiModelProperty(value = "성별")
	private int gender;
	
	@ApiModelProperty(value = "등록일자")
	private String joindate;
	
	@ApiModelProperty(value="관리자여부")
	private int isAdmin;
	
	@ApiModelProperty(value="활성여부")
	private int isState;
}
