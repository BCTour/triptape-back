package com.ssafy.triptape.user.service;


import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.ssafy.triptape.file.FileInfoDto;
import com.ssafy.triptape.user.UserDetailsDto;
import com.ssafy.triptape.user.UserDto;
import com.ssafy.triptape.user.repo.UserRepo;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	ResourceLoader resLoader;
	
	@Autowired
	private UserService service;
	
	@Override
	public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
		UserDto userDto = UserDto
                .userBuilder()
                .id(userId)
                .build();

        // 사용자 정보가 존재하지 않는 경우
        if (userId == null || userId.equals("")) {
            return service.login(userId)
                    .map(u -> new UserDetailsDto(u, Collections.singleton(new SimpleGrantedAuthority(u.getId()))))
                    .orElseThrow(() -> new AuthenticationServiceException(userId));
        }
        // 비밀번호가 맞지 않는 경우
        else {
            return service.login(userId)
                    .map(u -> new UserDetailsDto(u, Collections.singleton(new SimpleGrantedAuthority(u.getId()))))
                    .orElseThrow(() -> new BadCredentialsException(userId));
        }
	}

	
}
