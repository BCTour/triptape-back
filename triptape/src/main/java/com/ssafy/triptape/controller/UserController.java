package com.ssafy.triptape.controller;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.ssafy.triptape.common.util.JWTUtil;
import com.ssafy.triptape.user.UserDto;
import com.ssafy.triptape.user.service.EmailService;
import com.ssafy.triptape.user.service.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

@RestController
@CrossOrigin("*")
@RequestMapping("/user")
@Api(tags="회원 정보 관리 API")
@Slf4j
public class UserController {

	
	@Autowired
	private UserService service;
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private JWTUtil jwtUtil;
	
	@PostMapping(value ="/regist", consumes = "multipart/form-data")
	@ApiOperation("회원가입을 진행합니다.")
	public ResponseEntity<?> regist(@Validated @ApiParam(value = "사용자 정보", required = true) @RequestPart(value="user") UserDto user, @RequestPart(name="file", required = false) MultipartFile file) throws IllegalStateException, IOException {
		HttpStatus status = HttpStatus.ACCEPTED;
		String message ="";
		
		try{
			int result = service.regist(user, file);
			if(result == 1) {
				status = HttpStatus.CREATED;
				return new ResponseEntity<>(status);
			}
		} catch(DuplicateKeyException e) {
			status = HttpStatus.CONFLICT;
			message = e.getMessage();
		} catch(Exception e) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			message = e.getMessage();
		}

		return new ResponseEntity<String>(message, status);
	}
	
	@PostMapping(value ="/login")
	@ApiOperation("로그인을 진행합니다.")
	public ResponseEntity<?> login(@RequestBody UserDto user) {
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		HttpStatus status = HttpStatus.ACCEPTED;
		
		try {
			
			UserDto loginUser = service.login(user.getUserId(), user.getUserPw());
			
			Object token = service.getRefreshToken(user.getUserId());
			// 토큰이 이미 존재하는 경우
			if(token != null) {
				service.deleteRefreshToken(user.getUserId());
			}
			
			if(loginUser != null) {

				String accessToken = jwtUtil.createAccessToken(loginUser.getIsAdmin(), loginUser.getUserId());
				String refreshToken = jwtUtil.createRefreshToken(loginUser.getIsAdmin(), loginUser.getUserId());
				
				service.saveRefreshToken(loginUser.getUserId(), refreshToken);
				
				resultMap.put("access-token", accessToken);
				resultMap.put("refresh-token", refreshToken);
				
				status = HttpStatus.OK;
			}
			
			else {
				resultMap.put("message", "아이디 또는 패스워드를 확인해주세요.");
				status = HttpStatus.UNAUTHORIZED;
			}
		} catch(Exception e) {
			resultMap.put("message", e.getMessage());
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}
	
	@ApiOperation(value = "회원인증", notes = "회원 정보를 반환한다.", response = Map.class)
	@GetMapping("/info/{userId}")
	public ResponseEntity<?> info(
			@PathVariable("userId") @ApiParam(required = true) String userId,
			HttpServletRequest request) {

		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = HttpStatus.ACCEPTED;
		
		String token = request.getHeader("Authorization");
		if(!jwtUtil.checkToken(token)) {
			resultMap.put("message", "사용불가능한 토큰입니다.");
			status = HttpStatus.UNAUTHORIZED;
			return new ResponseEntity<Map<String, Object>>(resultMap, status);
		}
		
		if(!jwtUtil.getUserId(token).equals(userId)) {
			resultMap.put("message", "사용자 정보가 일치하지 않습니다.");
			status = HttpStatus.FORBIDDEN;
			return new ResponseEntity<Map<String, Object>>(resultMap, status);
		}
		
		try {
			UserDto user = service.userInfo(userId);
			if(user != null) {
				resultMap.put("userInfo", user);
				status = HttpStatus.OK;
			} else {
				status = HttpStatus.NO_CONTENT;
			}
		
		} catch (Exception e) {
			resultMap.put("message", e.getMessage());
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		} 
		
		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}

	@ApiOperation(value = "Access Token 재발급", notes = "만료된 access token을 재발급받는다.", response = Map.class)
	@PostMapping("/refresh")
	public ResponseEntity<?> refreshToken(@RequestParam String userId, HttpServletRequest request)
			throws Exception {
		Map<String, Object> resultMap = new HashMap<>();
		String token = request.getHeader("refreshToken");
		HttpStatus status = HttpStatus.ACCEPTED;

		if (jwtUtil.checkToken(token)) {
			if (token.equals(service.getRefreshToken(userId))) {
				UserDto user = service.userInfo(userId);
				String accessToken = jwtUtil.createAccessToken(user.getIsAdmin(), userId);
				resultMap.put("access-token", accessToken);
				status = HttpStatus.CREATED;
			} else {
				status = HttpStatus.UNAUTHORIZED;
				resultMap.put("message", "리프레시 토큰을 사용할 수 없습니다.");
			}
		} else {
			status = HttpStatus.UNAUTHORIZED;
			resultMap.put("message", "리프레시 토큰을 사용할 수 없습니다.");
		}
		
		return new ResponseEntity<Map<String, Object>>(resultMap, status);
		
	}
	
	@ApiOperation(value = "로그아웃", notes = "회원 정보를 담은 Token을 제거한다.")
	@GetMapping("/logout/{userId}")
	public ResponseEntity<?> removeToken(@PathVariable ("userId") @ApiParam(value = "로그아웃할 회원의 아이디.", required = true) String userId) {
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = HttpStatus.ACCEPTED;
		
		try {
			service.deleteRefreshToken(userId);
			status = HttpStatus.OK;
			return new ResponseEntity<Void>(status);
		} catch (Exception e) {
			resultMap.put("message", e.getMessage());
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}
	
	@ApiOperation(value ="회원 정보 삭제", notes = "회원 정보를 삭제한다.", response = Map.class)
	@DeleteMapping("/delete")
	public ResponseEntity<?> delete(HttpServletRequest request, @RequestBody UserDto user) {
		HttpStatus status = HttpStatus.ACCEPTED;
		Map<String, Object> resultMap = new HashMap<>();

		String token = request.getHeader("Authorization");
		if(!jwtUtil.checkToken(token)) {
			resultMap.put("message", "사용불가능한 토큰입니다.");
			status = HttpStatus.UNAUTHORIZED;
			return new ResponseEntity<Map<String, Object>>(resultMap, status);
		}
		
		if(!jwtUtil.getUserId(token).equals(user.getUserId())) {
			resultMap.put("message", "사용자 정보가 일치하지 않습니다.");
			status = HttpStatus.FORBIDDEN;
			return new ResponseEntity<Map<String, Object>>(resultMap, status);
		}
		
		try {
			int result = service.deleteUser(user.getUserId(), user.getUserPw());
			if(result == 1) {
				status = HttpStatus.OK;
				return new ResponseEntity<>(status);
			}
			else {
				resultMap.put("message", "아이디 또는 패스워드를 확인해주세요.");
				status = HttpStatus.UNAUTHORIZED;
			}
		} catch (Exception e) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			resultMap.put("message", e.getMessage());
		}
		
		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}
	
	@ApiOperation(value ="회원 정보 수정", notes = "회원 정보를 수정한다.", consumes = "multipart/form-data")
	@PutMapping("/modify")
	public ResponseEntity<?> modify(HttpServletRequest request, @ApiParam(value = "사용자 정보", required = true) @RequestPart(value="user") UserDto user, @RequestPart(name="file", required = false) MultipartFile file) {
		HttpStatus status = HttpStatus.ACCEPTED;
		Map<String, Object> resultMap = new HashMap<>();

		String token = request.getHeader("Authorization");
		if(!jwtUtil.checkToken(token)) {
			resultMap.put("message", "사용불가능한 토큰입니다.");
			status = HttpStatus.UNAUTHORIZED;
			return new ResponseEntity<Map<String, Object>>(resultMap, status);
		}
		
		if(!jwtUtil.getUserId(token).equals(user.getUserId())) {
			resultMap.put("message", "사용자 정보가 일치하지 않습니다.");
			status = HttpStatus.FORBIDDEN;
			return new ResponseEntity<Map<String, Object>>(resultMap, status);
		}
		
		try {
			int result = service.modify(user, file);
			if(result == 1) {
				UserDto userInfo = service.userInfo(user.getUserId());
				status = HttpStatus.OK;
				resultMap.put("userInfo", userInfo);
			}
			else {
				resultMap.put("message", "수정한 내용이 없습니다.");
				status = HttpStatus.NO_CONTENT;
			}
		} catch (Exception e) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			resultMap.put("message", e.getMessage());
		}
		
		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}
	
	@GetMapping("/findpw")
	public ResponseEntity<?> emailCheck(@RequestParam String email) {
			
		HttpStatus status = HttpStatus.ACCEPTED;
		Map<String, Object> resultMap = new HashMap<>();
		try {
			UserDto user = service.searchByEmail(email);
			if(user != null) {
				String code = emailService.sendSimpleMessage(email);
				resultMap.put("code", code);
				status = HttpStatus.OK;
			} else {
				resultMap.put("message", "존재하지 않는 이메일입니다");
				status = HttpStatus.UNAUTHORIZED;
			}
		} catch(Exception e) {
			resultMap.put("message", e.getMessage());
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}

	
	@PutMapping("/regist/pw")
	public ResponseEntity<?> registPw(@RequestBody UserDto user) {
		
		HttpStatus status = HttpStatus.ACCEPTED;
		Map<String, Object> resultMap = new HashMap<>();
		try {
			UserDto userInfo = service.searchByEmail(user.getEmail());
			if(userInfo != null) {
				service.updatePw(userInfo.getUserId(), user.getUserPw());
				resultMap.put("message", "비밀번호 재설정을 완료하였습니다.");
				status = HttpStatus.OK;
			} else {
				resultMap.put("message", "존재하지 않는 이메일입니다");
				status = HttpStatus.UNAUTHORIZED;
			}
		} catch(Exception e) {
			resultMap.put("message", e.getMessage());
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}
}
