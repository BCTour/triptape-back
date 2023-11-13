package com.ssafy.triptape.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.triptape.common.codes.SuccessCode;
import com.ssafy.triptape.common.response.ApiResponse;
import com.ssafy.triptape.user.UserDto;
import com.ssafy.triptape.user.service.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@CrossOrigin("*")
@RequestMapping("/user")
@Api(tags="회원 정보 관리 API")
public class UserController {

	@Autowired
	private UserService service;
	
	@PostMapping("/regist")
	@ApiOperation("회원가입을 진행합니다.")
	public ResponseEntity<?> regist(@Validated @RequestBody  UserDto user) {
		System.out.println(user);
		int result = service.regist(user);
		
		ApiResponse<Object> ar = ApiResponse.builder()
					.result(result).resultCode(SuccessCode.INSERT_SUCCESS.getStatus())
					.resultMsg(SuccessCode.INSERT_SUCCESS.getMessage())
					.build();
		
		return new ResponseEntity<>(ar, HttpStatus.OK);
	}
}
