package com.ssafy.triptape.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.triptape.banner.BannerDto;
import com.ssafy.triptape.banner.service.BannerService;
import com.ssafy.triptape.common.util.JWTUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@CrossOrigin("*")
@RequestMapping("/banner")
@Api(tags="배너 관리 API")
public class BannerController {
	
	@Autowired
	private JWTUtil jwtUtil;
	
	@Autowired
	private BannerService service;
	
	@PostMapping("/regist/{userId}")
	@ApiOperation("배너를 등록합니다.")
	public ResponseEntity<?> registBanner(
			@PathVariable String userId,
			@RequestBody BannerDto banner,
			HttpServletRequest request) {
		
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = HttpStatus.ACCEPTED;
		String token = request.getHeader("Authorization");
		
		if(!jwtUtil.checkToken(token)) {
			resultMap.put("message", "사용불가능한 토큰입니다.");
			status = HttpStatus.UNAUTHORIZED;
			return new ResponseEntity<Map<String, Object>>(resultMap, status);
		}
		if(jwtUtil.getRole(token) != 1) {
			resultMap.put("message", "접근 권한이 없습니다.");
			status = HttpStatus.FORBIDDEN;
			return new ResponseEntity<Map<String, Object>>(resultMap, status);
		}
		if(!jwtUtil.getUserId(token).equals(userId)) {
			resultMap.put("message", "사용자 정보가 일치하지 않습니다.");
			status = HttpStatus.FORBIDDEN;
			return new ResponseEntity<Map<String, Object>>(resultMap, status);
		}	
		
		try {
			int result = service.registBanner(banner);

			if(result==1) return new ResponseEntity<Void>(HttpStatus.CREATED);
			else return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
		} catch(DuplicateKeyException e) {
			resultMap.put("message", "이미 등록된 Tape입니다.");
			return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.CONFLICT);
		}
		catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	@GetMapping("/search")
	@ApiOperation("배너를 조회합니다.")
	public ResponseEntity<?> registBanner() {
		
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = HttpStatus.ACCEPTED;
		
		try {
			List<BannerDto> banner = service.searchBanner();

			if(banner != null && !banner.isEmpty()) {
				resultMap.put("banner",banner);
				status = HttpStatus.OK;
			} else {
				resultMap.put("message","조회할 내용이 없습니다.");
				status = HttpStatus.NO_CONTENT;
			}
		} 
		catch(Exception e) {
			e.printStackTrace();
			resultMap.put("message",e.getMessage());
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}
	
	@PutMapping("/modify/{userId}")
	@ApiOperation("배너를 수정합니다.")
	public ResponseEntity<?> modifyBanner(
			@PathVariable String userId,
			@RequestBody BannerDto banner,
			HttpServletRequest request) {
		
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = HttpStatus.ACCEPTED;
		
		String token = request.getHeader("Authorization");
		
		if(!jwtUtil.checkToken(token)) {
			resultMap.put("message", "사용불가능한 토큰입니다.");
			status = HttpStatus.UNAUTHORIZED;
			return new ResponseEntity<Map<String, Object>>(resultMap, status);
		}
		if(jwtUtil.getRole(token) != 1) {
			resultMap.put("message", "접근 권한이 없습니다.");
			status = HttpStatus.FORBIDDEN;
			return new ResponseEntity<Map<String, Object>>(resultMap, status);
		}
		if(!jwtUtil.getUserId(token).equals(userId)) {
			resultMap.put("message", "사용자 정보가 일치하지 않습니다.");
			status = HttpStatus.FORBIDDEN;
			return new ResponseEntity<Map<String, Object>>(resultMap, status);
		}
		
		try {
			System.out.println(banner);
			int result = service.modifyBanner(banner);

			if(result==1) return new ResponseEntity<Void>(HttpStatus.OK);
			else return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
		} catch(DuplicateKeyException e) {
			resultMap.put("message", "이미 등록된 Tape입니다.");
			return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.CONFLICT);
		}
		catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@DeleteMapping("/delete/{userId}")
	@ApiOperation("배너를 삭제합니다.")
	public ResponseEntity<?> deleteBanner(
			@PathVariable String userId,
			@RequestParam int bannerKey,
			HttpServletRequest request) {
		
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = HttpStatus.ACCEPTED;
		
		String token = request.getHeader("Authorization");
		
		if(!jwtUtil.checkToken(token)) {
			resultMap.put("message", "사용불가능한 토큰입니다.");
			status = HttpStatus.UNAUTHORIZED;
			return new ResponseEntity<Map<String, Object>>(resultMap, status);
		}
		if(jwtUtil.getRole(token) != 1) {
			resultMap.put("message", "접근 권한이 없습니다.");
			status = HttpStatus.FORBIDDEN;
			return new ResponseEntity<Map<String, Object>>(resultMap, status);
		}
		if(!jwtUtil.getUserId(token).equals(userId)) {
			resultMap.put("message", "사용자 정보가 일치하지 않습니다.");
			status = HttpStatus.FORBIDDEN;
			return new ResponseEntity<Map<String, Object>>(resultMap, status);
		}
		try {
			int result = service.deleteBanner(bannerKey);

			if(result==1) return new ResponseEntity<Void>(HttpStatus.OK);
			else return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
		} catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
