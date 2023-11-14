package com.ssafy.triptape.controller;

import java.io.IOException;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ssafy.triptape.common.codes.SuccessCode;
import com.ssafy.triptape.common.response.ApiResponse;
import com.ssafy.triptape.user.UserDto;
import com.ssafy.triptape.user.service.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@CrossOrigin("*")
@RequestMapping("/user")
@Api(tags="회원 정보 관리 API")
public class UserController {

	
	@Autowired
	private UserService service;
	
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
	
//	@PostMapping(value ="/login")
//	@ApiOperation("회원가입을 진행합니다.")
//	public ResponseEntity<?> regist(@Validated String id, @Validated String pw) {
//
//		UserDto user = service.login(id);
//		
//		ApiResponse<Object> ar = ApiResponse.builder()
//					.result(user).resultCode(SuccessCode.SELECT_SUCCESS.getStatus())
//					.resultMsg(SuccessCode.SELECT_SUCCESS.getMessage())
//					.build();
//		
//		return new ResponseEntity<>(ar, HttpStatus.OK);
//	}
}
