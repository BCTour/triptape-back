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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.triptape.attraction.AttractionDto;
import com.ssafy.triptape.common.util.JWTUtil;
import com.ssafy.triptape.record.RecordDto;
import com.ssafy.triptape.tape.TapeDto;
import com.ssafy.triptape.user.service.UserListService;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;

@RestController
@CrossOrigin("*")
@RequestMapping("/user")
@ApiModel("사용자의 정보 조회 API 입니다.")
public class UseInfoController {
	
	@Autowired
	private JWTUtil jwtUtil;
	
	@Autowired
	private UserListService service;
	
	
	@GetMapping("/like/attraction")
	@ApiOperation("사용자가 관심있는 관광지 조회")
	public ResponseEntity<?> userLikeAttraction(
			@RequestParam String userId,
			HttpServletRequest request){
		
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
			List<AttractionDto> attraction = service.userLikeAttraction(userId);
			if(attraction != null && !attraction.isEmpty()) {
				resultMap.put("attraction", attraction);
				status = HttpStatus.OK;
			} else {
				resultMap.put("message", "조회할 내용이 없습니다.");
				status = HttpStatus.NO_CONTENT;
			}
		} catch(Exception e) {
			resultMap.put("message", e.getMessage());
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}
	
	@PostMapping("/report/attraction/{attractionKey}/{userId}")
	@ApiOperation("문제있는 관광지 신고")
	public ResponseEntity<?> userReportAttraction(
			@PathVariable int attractionKey,
			@PathVariable String userId,
			HttpServletRequest request){
		
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
			int result = service.userReportAttraction(userId, attractionKey);
			if(result == 1) {
				status = HttpStatus.OK;
				return new ResponseEntity<>(status);
			} else {
				resultMap.put("message", "조회할 내용이 없습니다.");
				status = HttpStatus.NO_CONTENT;
			}
		} catch(DuplicateKeyException e) {
			resultMap.put("message", "이미 신고하였습니다.");
			status = HttpStatus.CONFLICT;
		}
		catch(Exception e) {
			resultMap.put("message", e.getMessage());
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}
	
	@GetMapping("/search/report")
	@ApiOperation("신고횟수 많은 장소 조회")
	public ResponseEntity<?> searchReportAttraction(
			@RequestParam String userId,
			@RequestParam int countReport,
			HttpServletRequest request){
		
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = HttpStatus.ACCEPTED;
		
		String token = request.getHeader("Authorization");
		
		if(!jwtUtil.checkToken(token)) {
			resultMap.put("message", "사용불가능한 토큰입니다.");
			status = HttpStatus.UNAUTHORIZED;
			return new ResponseEntity<Map<String, Object>>(resultMap, status); 
		}
		
		if(jwtUtil.getRole(token) != 1) {
			resultMap.put("message", "권한이 없습니다.");
			status = HttpStatus.FORBIDDEN;
			return new ResponseEntity<Map<String, Object>>(resultMap, status);
		}
		

		if(!jwtUtil.getUserId(token).equals(userId)) {
			resultMap.put("message", "사용자 정보가 일치하지 않습니다.");
			status = HttpStatus.FORBIDDEN;
			return new ResponseEntity<Map<String, Object>>(resultMap, status);
		}
		
		try {
			List<AttractionDto> attraction = service.searchReportAttraction(countReport);
			if(attraction != null && !attraction.isEmpty()) {
				resultMap.put("attraction", attraction);
				status = HttpStatus.OK;
			} else {
				resultMap.put("message", "조회할 내용이 없습니다.");
				status = HttpStatus.NO_CONTENT;
			}
		} catch(Exception e) {
			resultMap.put("message", e.getMessage());
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}
	
	@GetMapping("/like/tape")
	@ApiOperation("사용자가 관심있는 테이프 조회")
	public ResponseEntity<?> userLikeTape(
			@RequestParam String userId,
			HttpServletRequest request){
		
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
			List<TapeDto> tape = service.userLikeTape(userId);
			if(tape != null && !tape.isEmpty()) {
				resultMap.put("tape", tape);
				status = HttpStatus.OK;
			} else {
				resultMap.put("message", "조회할 내용이 없습니다.");
				status = HttpStatus.NO_CONTENT;
			}
		} catch(Exception e) {
			resultMap.put("message", e.getMessage());
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}
	
	@GetMapping("/join/tape")
	@ApiOperation("사용자가 참여한 테이프 조회")
	public ResponseEntity<?> userJoinTape(
			@RequestParam String userId,
			HttpServletRequest request){
		
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
			List<TapeDto> tape = service.userJoinTape(userId);
			if(tape != null && !tape.isEmpty()) {
				resultMap.put("tape", tape);
				status = HttpStatus.OK;
			} else {
				resultMap.put("message", "조회할 내용이 없습니다.");
				status = HttpStatus.NO_CONTENT;
			}
		} catch(Exception e) {
			resultMap.put("message", e.getMessage());
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}
	
	@GetMapping("/like/record")
	@ApiOperation("사용자가 관심있는 레코드 조회")
	public ResponseEntity<?> userLikeRecord(
			@RequestParam String userId,
			HttpServletRequest request){
		
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
			List<RecordDto> record = service.userLikeRecord(userId);
			if(record != null && !record.isEmpty()) {
				resultMap.put("record", record);
				status = HttpStatus.OK;
			} else {
				resultMap.put("message", "조회할 내용이 없습니다.");
				status = HttpStatus.NO_CONTENT;
			}
		} catch(Exception e) {
			resultMap.put("message", e.getMessage());
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}
}
