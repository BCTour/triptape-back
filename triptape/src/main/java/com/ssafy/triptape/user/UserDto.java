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

@ToString
@Setter
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder(builderMethodName = "userBuilder", toBuilder = true)
public class UserDto {
	
	private int userKey;
	@NotEmpty(message="id는 필수값입니다.")
	private String id;
	@NotEmpty(message="pw는 필수값입니다.")
	private String pw;
	private String name;
	private String nickname;
	@Email(message="이메일 타입이어야 합니다.")
	private String email;
	private String tel;
	private FileInfoDto profileImg;
	private String birthday;
	private int gender;
	private String joindate;

}
