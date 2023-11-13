package com.ssafy.triptape.user;

import java.util.List;

import javax.validation.constraints.*;

import com.ssafy.triptape.file.FileInfoDto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access=AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString
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
	private List<FileInfoDto> fileInfos;
	private String birthday;
	private int gender;
	private String joindate;
	private int isAdmin;

}
