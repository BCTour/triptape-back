package com.ssafy.triptape.user.service;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

import com.ssafy.triptape.user.UserDto;

public interface UserService {
	int regist(UserDto user, MultipartFile file) throws IOException;
	UserDto login(String userId, String userPw); 
	UserDto userInfo(String userId) throws IOException;
	void saveRefreshToken(String userId, String refreshToken);
	Object getRefreshToken(String userId);
	void deleteRefreshToken(String userId);
	
	int modify(UserDto user, MultipartFile file) throws IOException;
	int deleteUser(String userId, String userPw);
}
