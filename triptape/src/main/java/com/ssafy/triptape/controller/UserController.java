package com.ssafy.triptape.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ssafy.triptape.common.codes.ErrorCode;
import com.ssafy.triptape.common.codes.SuccessCode;
import com.ssafy.triptape.common.response.ApiResponse;
import com.ssafy.triptape.common.util.JWTUtil;
import com.ssafy.triptape.user.UserDto;
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
	private JWTUtil jwtUtil;
	
	@PostMapping(value ="/regist", consumes = "multipart/form-data")
	@ApiOperation("회원가입을 진행합니다.")
	public ResponseEntity<?> regist(@Validated @ApiParam(value = "사용자 정보", required = true) @RequestPart(value="user") UserDto user, @RequestPart(name="file", required = false) MultipartFile file) throws IllegalStateException, IOException {
		int result = service.regist(user, file);
		
		ApiResponse<Object> ar = ApiResponse.builder()
					.result(result).resultCode(SuccessCode.INSERT_SUCCESS.getStatus())
					.resultMsg(SuccessCode.INSERT_SUCCESS.getMessage())
					.build();
		
		return new ResponseEntity<>(ar, HttpStatus.OK);
	}
	
	@PostMapping(value ="/login")
	@ApiOperation("로그인을 진행합니다.")
	public ResponseEntity<?> login(@RequestBody UserDto user) {

		UserDto loginUser = service.login(user.getUserId(), user.getUserPw());
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		int code;
		String msg;
		if(loginUser != null) {
			String accessToken = jwtUtil.createAccessToken(loginUser.getUserId());
			String refreshToken = jwtUtil.createRefreshToken(loginUser.getUserId());
			log.debug("access token : {}", accessToken);
			log.debug("refresh token : {}", refreshToken);
			
			service.saveRefreshToken(loginUser.getUserId(), refreshToken);
			
			resultMap.put("access-token", accessToken);
			resultMap.put("refresh-token", refreshToken);
			
			code = SuccessCode.SELECT_SUCCESS.getStatus();
			msg = SuccessCode.SELECT_SUCCESS.getMessage();
			
		}
		else {
			code = SuccessCode.NO_CONTENT.getStatus();
			msg = SuccessCode.NO_CONTENT.getMessage();	
		}
		
		ApiResponse<Object> ar = ApiResponse.builder()
				.result(resultMap).resultCode(code)
				.resultMsg(msg)
				.build();
		
		return new ResponseEntity<>(ar, HttpStatus.OK);
	}
	
	@ApiOperation(value = "회원인증", notes = "회원 정보를 담은 Token을 반환한다.", response = Map.class)
	@GetMapping("/info/{userId}")
	public ResponseEntity<?> info(
			@PathVariable("userId") @ApiParam(value = "인증할 회원의 아이디.", required = true) String userId,
			HttpServletRequest request) {

		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = HttpStatus.ACCEPTED;
		
		int code;
		String msg;
		if (jwtUtil.checkToken(request.getHeader("Authorization"))) {
			try {
//				로그인 사용자 정보.
				UserDto user = service.userInfo(userId);
				resultMap.put("userInfo", user);
				status = HttpStatus.OK;
				
				code = SuccessCode.SELECT_SUCCESS.getStatus();
				msg = SuccessCode.SELECT_SUCCESS.getMessage();				
			} catch (Exception e) {
				resultMap.put("message", e.getMessage());
				status = HttpStatus.INTERNAL_SERVER_ERROR;
				code = ErrorCode.INTERNAL_SERVER_ERROR.getStatus();
				msg = ErrorCode.INTERNAL_SERVER_ERROR.getMessage();
			}
		} else {
			log.error("사용 불가능 토큰!!!");
			status = HttpStatus.UNAUTHORIZED;
			code = ErrorCode.UNAUTHORIZED.getStatus();
			msg = ErrorCode.UNAUTHORIZED.getMessage();
		}
		ApiResponse<Object> ar = ApiResponse.builder()
				.result(resultMap).resultCode(code)
				.resultMsg(msg)
				.build();
		
		return new ResponseEntity<>(ar, status);
	}
	
	
	@ApiOperation(value = "Access Token 재발급", notes = "만료된 access token을 재발급받는다.", response = Map.class)
	@PostMapping("/refresh")
	public ResponseEntity<?> refreshToken(@RequestBody UserDto user, HttpServletRequest request)
			throws Exception {
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = HttpStatus.ACCEPTED;
		String token = request.getHeader("refreshToken");
		log.debug("token : {}, memberDto : {}", token, user);
		if (jwtUtil.checkToken(token)) {
			if (token.equals(service.getRefreshToken(user.getUserId()))) {
				String accessToken = jwtUtil.createAccessToken(user.getUserId());
				log.debug("token : {}", accessToken);
				log.debug("정상적으로 액세스토큰 재발급!!!");
				resultMap.put("access-token", accessToken);
				status = HttpStatus.CREATED;
			}
		} else {
			log.debug("리프레쉬토큰도 사용불가!!!!!!!");
			status = HttpStatus.UNAUTHORIZED;
		}
		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}
	
	@ApiOperation(value = "로그아웃", notes = "회원 정보를 담은 Token을 제거한다.", response = Map.class)
	@GetMapping("/logout/{userId}")
	public ResponseEntity<?> removeToken(@PathVariable ("userId") @ApiParam(value = "로그아웃할 회원의 아이디.", required = true) String userId) {
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = HttpStatus.ACCEPTED;
		try {
			service.deleteRefreshToken(userId);
			status = HttpStatus.OK;
		} catch (Exception e) {
			log.error("로그아웃 실패 : {}", e);
			resultMap.put("message", e.getMessage());
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}

}
