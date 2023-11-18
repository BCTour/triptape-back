package com.ssafy.triptape.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ssafy.triptape.attraction.AttractionDto;
import com.ssafy.triptape.common.util.JWTUtil;
import com.ssafy.triptape.tape.TapeDto;
import com.ssafy.triptape.tape.service.TapeService;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;

@RestController
@CrossOrigin("*")
@RequestMapping("/tape")
@ApiModel("테이프에 대한 API 입니다.")
public class TapeController {

	@Autowired
	private JWTUtil jwtUtil;
	
	@Autowired
	private TapeService service;
	
	@PostMapping(value="/regist", consumes="multipart/form-data")
	@ApiOperation(value="테이프를 등록합니다.")
	public ResponseEntity<?> registTape(
			@RequestPart(value="tape") TapeDto tape, 
			@RequestPart(value="file", required = false) MultipartFile file,
			HttpServletRequest request){
		
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = HttpStatus.ACCEPTED;
		
		String token = request.getHeader("Authorization");
		
		if(!jwtUtil.checkToken(token)) {
			resultMap.put("message", "사용불가능한 토큰입니다.");
			status = HttpStatus.UNAUTHORIZED;
			return new ResponseEntity<Map<String, Object>>(resultMap, status);
		}

		if(!jwtUtil.getUserId(token).equals(tape.getUser().getUserId())) {
			resultMap.put("message", "사용자 정보가 일치하지 않습니다.");
			status = HttpStatus.FORBIDDEN;
			return new ResponseEntity<Map<String, Object>>(resultMap, status);
		}
		
		try {
			int result = service.registTape(tape, file);
			if(result == 1) return new ResponseEntity<Void>(HttpStatus.CREATED);
			else return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
		} catch(Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/search/recent/{n}")
	@ApiOperation(value="최신 테이프 목록을 상위 {n}개를 조회합니다.")
	public ResponseEntity<?> searchKeyword(@PathVariable int n) {
		
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = HttpStatus.ACCEPTED;
		String message = "";
		try {
			List<TapeDto> tapeList = service.searchTopRecent(n);
			if(tapeList == null || tapeList.size() == 0) {
				message = "조회할 데이터가 없습니다.";
				status = HttpStatus.NO_CONTENT;
				resultMap.put("message", message);
			} else {
				resultMap.put("tape", tapeList);
				status = HttpStatus.OK;
			}
		} catch(Exception e) {
			message = e.getMessage();
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			resultMap.put("message", message);
		}
		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}
	
	@GetMapping("/search/popular/{n}")
	@ApiOperation(value="인기 테이프 목록을 상위 {n}개를 조회합니다.")
	public ResponseEntity<?> searchTopPopular(@PathVariable int n) {
		
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = HttpStatus.ACCEPTED;
		String message = "";
		try {
			List<TapeDto> tapeList = service.searchTopPopular(n);
			if(tapeList == null || tapeList.size() == 0) {
				message = "조회할 데이터가 없습니다.";
				status = HttpStatus.NO_CONTENT;
				resultMap.put("message", message);
			} else {
				resultMap.put("tape", tapeList);
				status = HttpStatus.OK;
			}
		} catch(Exception e) {
			message = e.getMessage();
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			resultMap.put("message", message);
		}
		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}
	
	@GetMapping("/search")
	@ApiOperation("조건에 따른 테이프 목록을 조회합니다.")
	public ResponseEntity<?> searchByCondition(
			@RequestParam(required=false)String keyword,@RequestParam(required=false)String word, @RequestParam int currentPage){
		
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = HttpStatus.ACCEPTED;
		String message;
		
		if(currentPage == 0) currentPage = 1;
		
		int countPerPage = 20;
		int start = (currentPage - 1) * countPerPage;
		
		try {
			List<TapeDto> tapeList = service.searchByCondition(keyword, word, start, countPerPage);
			int totalCount = service.getTotalListCount(keyword, word);

			if(tapeList == null || tapeList.size() == 0) {
				message = "조회할 데이터가 없습니다.";
				status = HttpStatus.NO_CONTENT;
				resultMap.put("message", message);
			} else {
				resultMap.put("tape", tapeList);
				resultMap.put("totalCount", totalCount);
				status = HttpStatus.OK;
			}
		} catch(Exception e) {
			message = e.getMessage();
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			resultMap.put("message", message);
		}
		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}
	
	@GetMapping("/search/info/{tapeKey}")
	@ApiOperation("특정 테이프 정보를 조회합니다.")
	public ResponseEntity<?> searchInfo(
			@PathVariable int tapeKey,
			HttpServletRequest request){
		
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = HttpStatus.ACCEPTED;
		String message;

		try {
			TapeDto tape = service.tapeInfo(tapeKey);

			if(tape == null) { 
				message = "조회할 데이터가 없습니다.";
				status = HttpStatus.NO_CONTENT;
				resultMap.put("message", message);
			} else {
				resultMap.put("tape", tape);
				status = HttpStatus.OK;
			}
		} catch(Exception e) {
			message = e.getMessage();
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			resultMap.put("message", message);
		}
		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}
	
	@PutMapping(value="/modify", consumes="multipart/form-data")
	@ApiOperation(value="특정 테이프를 수정합니다.")
	public ResponseEntity<?> modifyTape(
			@RequestPart(value="tape") TapeDto tape, 
			@RequestPart(value="file", required = false) MultipartFile file,
			HttpServletRequest request){
		
		HttpStatus status = HttpStatus.ACCEPTED;
		Map<String, Object> resultMap = new HashMap<>();
		String token = request.getHeader("Authorization");
		String message;
		
		if(!jwtUtil.checkToken(token)) {
			resultMap.put("message", "사용불가능한 토큰입니다.");
			status = HttpStatus.UNAUTHORIZED;
			return new ResponseEntity<Map<String, Object>>(resultMap, status);
		}
		
		if(!jwtUtil.getUserId(token).equals(tape.getUser().getUserId())) {
			resultMap.put("message", "사용자 정보가 일치하지 않습니다.");
			status = HttpStatus.FORBIDDEN;
			return new ResponseEntity<Map<String, Object>>(resultMap, status);
		}
		
		TapeDto info = service.tapeInfo(tape.getTapeKey());
		
		if(jwtUtil.getRole(token) == 0 && !tape.getUser().getUserId().equals(info.getUser().getUserId())) {
			resultMap.put("message", "사용자가 작성한 글이 아닙니다.");
			status = HttpStatus.FORBIDDEN;
			return new ResponseEntity<Map<String, Object>>(resultMap, status);
		}
		
		try {
			int result = service.updateTape(tape, file);
			if(result == 1) {
				status = HttpStatus.OK;
				TapeDto tapeResult = service.tapeInfo(tape.getTapeKey());
				resultMap.put("tape", tapeResult);
			}else {
				status = HttpStatus.NO_CONTENT;
				message = "수정할 내용이 없습니다.";
				resultMap.put("message", message);
			}
		} catch(Exception e) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			message = e.getMessage();
		}
		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}
	
	@DeleteMapping("/delete/{tapeKey}/{userId}")
	@ApiOperation("특정 테이프를 삭제합니다.")
	public ResponseEntity<?> deleteTape(
			@PathVariable int tapeKey,
			@PathVariable String userId,
			HttpServletRequest request){
		
		HttpStatus status = HttpStatus.ACCEPTED;
		Map<String, Object> resultMap = new HashMap<>();
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
		

		TapeDto info = service.tapeInfo(tapeKey);
		
		if(info == null) {
			resultMap.put("message", "이미 삭제된 글입니다.");
			status = HttpStatus.NOT_FOUND;
			return new ResponseEntity<Map<String, Object>>(resultMap, status);
		}
		
		if(jwtUtil.getRole(token) == 0 && !userId.equals(info.getUser().getUserId())) {
			resultMap.put("message", "사용자가 작성한 글이 아닙니다.");
			status = HttpStatus.FORBIDDEN;
			return new ResponseEntity<Map<String, Object>>(resultMap, status);
		}
		
		try {
			int result = service.deleteTape(tapeKey);
			if(result == 1 ) {
				status = HttpStatus.OK;
				return new ResponseEntity<>(status);
			} else {
				resultMap.put("message","삭제할 내용이 없습니다.");
				status = HttpStatus.NO_CONTENT;
			}
		} catch(Exception e) {
			resultMap.put("message", e.getMessage());
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		
		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}
	
	@PostMapping("/like/{tapeKey}/{userId}")
	@ApiOperation("특정 테이프를 관심 리스트에 추가합니다.")
	public ResponseEntity<?> likeTape(
			@PathVariable int tapeKey, 
			@PathVariable String userId,
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
			if(service.isLikeTape(tapeKey, userId)) {
				resultMap.put("message", "이미 좋아요를 하였습니다.");
				return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.CONFLICT);
			}
			
			int result = service.likeTape(tapeKey, userId);
			if(result == 1) return new ResponseEntity<Void>(HttpStatus.CREATED);
			else {
				resultMap.put("message", "등록한 내용이 없습니다.");
				return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.NO_CONTENT);
			}
		} catch(Exception e) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			return new ResponseEntity<String>(e.getMessage(), status);
		}
	}
	@DeleteMapping("/dislike/{tapeKey}/{userId}")
	@ApiOperation("특정 테이프를 관심 리스트에 삭제합니다.")
	public ResponseEntity<?> dislikeTape(
			@PathVariable int tapeKey, 
			@PathVariable String userId,
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
			int result = service.dislikeTape(tapeKey, userId);
			if(result == 1) return new ResponseEntity<Void>(HttpStatus.OK);
			else {
				resultMap.put("message","이미 좋아요가 취소되었습니다.");
				return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.NO_CONTENT);
			}
		} catch(Exception e) {
			resultMap.put("message",e.getMessage());
			return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/include/attraction")
	@ApiOperation("특정 장소가 포함된 테이프 조회")	
	public ResponseEntity<?> tapeIncludeAttraction(@RequestParam int attractionKey) {
		
		HttpStatus status = HttpStatus.ACCEPTED;
		Map<String, Object> resultMap = new HashMap<>();
		
		try {
			List<TapeDto> tape = service.attractionTape(attractionKey);
		
			if(tape != null && !tape.isEmpty()) {
				resultMap.put("tape", tape);
				status = HttpStatus.OK;
			} else {
				resultMap.put("message", "조회할 내용이 없습니다.");
				status= HttpStatus.NO_CONTENT;
			}
			
		} catch(Exception e) {
			resultMap.put("message", e.getMessage());
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		
		return new ResponseEntity<Map<String,Object>>(resultMap, status);
		
	}
}
