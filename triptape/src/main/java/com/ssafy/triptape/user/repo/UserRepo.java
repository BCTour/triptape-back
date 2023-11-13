package com.ssafy.triptape.user.repo;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.triptape.user.UserDto;

@Mapper
public interface UserRepo {
	public int regist(UserDto user, String salt);
//	public UserDto login(String id, String pw); 
//	public UserDto searchUser(String key, String value);
//	public int modifyUser(UserDto user, String salt);
//	int deleteUser(String id);
//	int resetPw(String id, String pw);
//	String getSalt(String id);
}
