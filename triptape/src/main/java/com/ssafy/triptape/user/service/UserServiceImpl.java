package com.ssafy.triptape.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import com.ssafy.triptape.user.UserDto;
import com.ssafy.triptape.user.repo.UserRepo;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepo repo;
	
	@Override
	public int regist(UserDto user) {
		String salt = BCrypt.gensalt(12);
		String hashpw = BCrypt.hashpw(user.getPw(), salt);
		user.setPw(hashpw);
		return repo.regist(user,salt);
	}

}
