package com.ssafy.triptape.user.repo;

import java.util.Map;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.triptape.user.UserDto;
import com.ssafy.triptape.user.WithdrawalsDto;

@Mapper
public interface UserRepo {
	int regist(UserDto user, String salt) ;
	UserDto login(String userId, String userPw); 
	UserDto userInfo(String userId);
	void saveRefreshToken(String userId, String refreshToken);
	Object getRefreshToken(String userId);
	void deleteRefreshToken(String userId);
	
	UserDto searchByEmail(String email);
	int modifyUser(UserDto user);
	int deleteUser(String userId, String userPw);
	void updatePw(String userId, String userPw, String salt);
	String getSalt(String userId);
	
	int withdrawal(WithdrawalsDto withdrawalsDto);
	int isState(String userId, String userPw, int isState);
	UserDto selectIsState(String userId, String userPw);
}
