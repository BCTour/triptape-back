package com.ssafy.triptape.user.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import com.ssafy.triptape.user.UserDto;

public interface UserService {
	public int regist(UserDto user, MultipartFile file) throws IOException;
}
