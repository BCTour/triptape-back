package com.ssafy.triptape.user.service;


import java.io.File;
import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.ssafy.triptape.file.FileInfoDto;
import com.ssafy.triptape.user.UserDto;
import com.ssafy.triptape.user.repo.UserRepo;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	ResourceLoader resLoader;
	
	@Autowired
	private UserRepo repo;
	
	private void fileHandling(UserDto user, MultipartFile file) throws IOException {
		String path = "classpath:static/resources/upload/user";
		Resource res = resLoader.getResource(path);
		if (file != null && file.getSize() > 0) {
			FileInfoDto fileInfo = new FileInfoDto();
			fileInfo.setSaveFolder(path);
			fileInfo.setSaveFile(System.currentTimeMillis() + "_" + file.getOriginalFilename());
			fileInfo.setOriginalFile(file.getOriginalFilename());
			
			user.setProfileImg(fileInfo);

//			System.out.println(res.getFile().getCanonicalPath() + "/" + user.getProfileImg().getSaveFile());
			file.transferTo(new File(res.getFile().getCanonicalPath() + "/" + user.getProfileImg().getSaveFile()));
		}
	}
	
	@Override
	@Transactional
	public int regist(UserDto user, MultipartFile file) throws IOException {

		fileHandling(user, file);
		String salt = BCrypt.gensalt(12);
		String hashpw = BCrypt.hashpw(user.getPw(), salt);
		user.setPw(hashpw);
		return repo.regist(user,salt);
	}

	/**
     * 로그인 구현체
     *
     */
    @Override
    public Optional<UserDto> login(String id) {
        return repo.login(id);
    }

	
}
