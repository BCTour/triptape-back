package com.ssafy.triptape.user.repo;

import java.util.Map;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.triptape.user.UserDto;

@Mapper
public interface UserRepo {
	int regist(UserDto user, String salt) ;
	UserDto login(String userId, String userPw); 
	UserDto userInfo(String userId);
	void saveRefreshToken(String userId, String refreshToken);
	Object getRefreshToken(String userId);
	void deleteRefreshToken(String userId);
	
//	public UserDto searchUser(String key, String value);
	int modifyUser(UserDto user);
	int deleteUser(String userId, String userPw);
//	int resetPw(String id, String pw);
	String getSalt(String userId);
}
