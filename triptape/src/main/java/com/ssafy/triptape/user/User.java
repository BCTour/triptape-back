package com.ssafy.triptape.user;

import java.util.List;

import com.ssafy.triptape.file.FileInfoDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User {
	private int userKey;
	private String id;
	private String pw;
	private String name;
	private String nickname;
	private String email;
	private String tel;
	private List<FileInfoDto> fileinfos;
	private String birthday;
	private String gender;
	private String joindate;
	private int isAdmin;
	private String salt;
}
