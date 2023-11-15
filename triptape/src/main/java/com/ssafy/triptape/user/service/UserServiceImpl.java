package com.ssafy.triptape.user.service;


import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
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
	
	private String path = "classpath:static/resources/upload/user";
	private void fileHandling(UserDto user, MultipartFile file) throws IOException {
		
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
		String hashpw = BCrypt.hashpw(user.getUserPw(), salt);
		user.setUserPw(hashpw);
		return repo.regist(user,salt);
	}

	@Override
	public UserDto login(String userId, String pw) {
		String salt = repo.getSalt(userId);
		if(salt == null) {
			return null;
		}
		String hashPass = BCrypt.hashpw(pw, salt);
		return repo.login(userId, hashPass);
	}

	@Override
	public UserDto userInfo(String userId) throws IOException {
		UserDto user = repo.userInfo(userId);
		String img = resLoader.getResource(path).getFile().getCanonicalPath() + "/" + user.getProfileImg().getSaveFile();
		
		FileInfoDto fileInfo = new FileInfoDto();
		fileInfo.setSaveFolder(path);
		fileInfo.setSaveFile(img);
		
		user.setProfileImg(fileInfo);
		return user;
	}

	@Override
	public void saveRefreshToken(String userId, String refreshToken) {
		
		repo.saveRefreshToken(userId, refreshToken);
	}

	@Override
	public Object getRefreshToken(String userId) {
		return repo.getRefreshToken(userId);
	}

	@Override
	public void deleteRefreshToken(String userId) {
		
		repo.deleteRefreshToken(userId);	
	}

	@Override
	public int deleteUser(String userId, String userPw) {
		String salt = repo.getSalt(userId);
		String hashPass = BCrypt.hashpw(userPw, salt);
		return repo.deleteUser(userId, hashPass);
	}

	@Override
	public int modify(UserDto user, MultipartFile file) throws IOException {
		fileHandling(user, file);
		return repo.modifyUser(user);
	}

	@Override
	public UserDto searchByEmail(String email) {
		return repo.searchByEmail(email);
	}

	@Override
	public void updatePw(String userId, String userPw) {
		String salt = BCrypt.gensalt(12);
		String hashpw = BCrypt.hashpw(userPw, salt);

		repo.updatePw(userId, hashpw, salt);
	}
}
