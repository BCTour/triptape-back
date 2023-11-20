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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ssafy.triptape.attraction.AttractionDto;
import com.ssafy.triptape.common.util.JWTUtil;
import com.ssafy.triptape.record.RecordDto;
import com.ssafy.triptape.record.service.RecordService;
import com.ssafy.triptape.tape.TapeDto;
import com.ssafy.triptape.tape.service.TapeService;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;

@RestController
@CrossOrigin("*")
@RequestMapping("/record")
@ApiModel("레코드에 대한 API 입니다.")
public class RecordController {

	@Autowired
	private JWTUtil jwtUtil;
	
	@Autowired
	private RecordService service;
	
	@Autowired
	private TapeService tapeService;
	
	@PostMapping(value="/regist", consumes="multipart/form-data")
	@ApiOperation(value="레코드를 등록합니다.")
	public ResponseEntity<?> registRecord(
			@RequestPart(value="record") RecordDto record, 
			@RequestPart(value="file", required = false) MultipartFile file,
			HttpServletRequest request) {
		
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = HttpStatus.ACCEPTED;
		
		String token = request.getHeader("Authorization");
		
		if(!jwtUtil.checkToken(token)) {
			resultMap.put("message", "사용불가능한 토큰입니다.");
			status = HttpStatus.UNAUTHORIZED;
			return new ResponseEntity<Map<String, Object>>(resultMap, status);
		}

		if(!jwtUtil.getUserId(token).equals(record.getUser().getUserId())) {
			resultMap.put("message", "사용자 정보가 일치하지 않습니다.");
			status = HttpStatus.FORBIDDEN;
			return new ResponseEntity<Map<String, Object>>(resultMap, status);
		}
		try {
			
			int result = service.registRecord(record, file);
			if(result == 1) {
				tapeService.updateJoin(record.getTapeKey());
				return new ResponseEntity<Void>(HttpStatus.CREATED);
			}
			else return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
		} catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/attraction")
	@ApiOperation(value="레코드에 포함된 장소들 목록을 조회합니다.")
	public ResponseEntity<?> recordAttraction(
			@RequestParam int tapeKey, 
			@RequestParam(required=false)String keyword,
			@RequestParam(required=false) String word, 
			@RequestParam int currentPage) {
		
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = HttpStatus.ACCEPTED;
		String message = "";
		try {
			int countPerPage = 10;
			int start = (currentPage - 1) * countPerPage;
			List<AttractionDto> attractionList = service.recordAttraction(tapeKey, keyword, word, start, countPerPage);
			if(attractionList == null || attractionList.size() == 0) {
				message = "조회할 데이터가 없습니다.";
				status = HttpStatus.NO_CONTENT;
				resultMap.put("message", message);
			} else {
				resultMap.put("attraction", attractionList);
				status = HttpStatus.OK;
			}
		} catch(Exception e) {
			message = e.getMessage();
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			resultMap.put("message", message);
		}
		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}

	@GetMapping("/search/{tapeKey}")
	@ApiOperation(value="레코드를 조회합니다.")
	public ResponseEntity<?> searchRecord(
			@PathVariable int tapeKey, @RequestParam(required=false)String keyword,@RequestParam(required=false) String word, @RequestParam int currentPage) {
		
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = HttpStatus.ACCEPTED;
		String message = "";
		try {
			int countPerPage = 10;
			int start = (currentPage - 1) * countPerPage;
			List<RecordDto> record = service.searchRecord(tapeKey, keyword, word, start, countPerPage);
			if(record == null || record.size() == 0) {
				message = "조회할 데이터가 없습니다.";
				status = HttpStatus.NO_CONTENT;
				resultMap.put("message", message);
			} else {
				resultMap.put("record", record);
				status = HttpStatus.OK;
			}
		} catch(Exception e) {
			message = e.getMessage();
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			resultMap.put("message", message);
		}
		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}

	
	@GetMapping("/info/{tapeKey}/{recordKey}")
	@ApiOperation("특정 레코드 정보를 조회합니다.")
	public ResponseEntity<?> searchInfo(
			@PathVariable int tapeKey,
			@PathVariable int recordKey){
		
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = HttpStatus.ACCEPTED;
		String message;

		try {
			RecordDto record = service.recordInfo(tapeKey, recordKey);

			if(record == null) { 
				message = "조회할 데이터가 없습니다.";
				status = HttpStatus.NO_CONTENT;
				resultMap.put("message", message);
			} else {
				resultMap.put("record", record);
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
	@ApiOperation(value="특정 레코드를 수정합니다.")
	public ResponseEntity<?> modifyRecord(
			@RequestPart(value="record") RecordDto record, 
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
	
		
		if(!jwtUtil.getUserId(token).equals(record.getUser().getUserId())) {
			resultMap.put("message", "사용자 정보가 일치하지 않습니다.");
			status = HttpStatus.FORBIDDEN;
			return new ResponseEntity<Map<String, Object>>(resultMap, status);
		}
		
		RecordDto info = service.recordInfo(record.getTapeKey(),record.getRecordKey());
	
		
		if(info == null) {
			resultMap.put("message", "존재하지 않는 레코드입니다.");
			status = HttpStatus.NOT_FOUND;
			return new ResponseEntity<Map<String, Object>>(resultMap, status);
		}
		
		if(jwtUtil.getRole(token) == 0 && !record.getUser().getUserId().equals(info.getUser().getUserId())) {
			resultMap.put("message", "사용자가 작성한 글이 아닙니다.");
			status = HttpStatus.FORBIDDEN;
			return new ResponseEntity<Map<String, Object>>(resultMap, status);
		}

		try {
			int result = service.modifyRecord(record, file);
			if(result == 1) {
				status = HttpStatus.OK;
				RecordDto recordResult = service.recordInfo(record.getTapeKey(), record.getRecordKey());
				resultMap.put("record", recordResult);
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
	
	@DeleteMapping("/delete/{tapeKey}/{recordKey}")
	@ApiOperation("특정 레코드를 삭제합니다.")
	public ResponseEntity<?> deleteRecord(
			@PathVariable int tapeKey,
			@PathVariable int recordKey,
			@RequestParam String userId,
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
	
		
		if(!jwtUtil.getUserId(token).equals(userId)) {
			resultMap.put("message", "사용자 정보가 일치하지 않습니다.");
			status = HttpStatus.FORBIDDEN;
			return new ResponseEntity<Map<String, Object>>(resultMap, status);
		}
		
		RecordDto info = service.recordInfo(tapeKey,recordKey);
	
		
		if(info == null) {
			resultMap.put("message", "존재하지 않는 레코드입니다.");
			status = HttpStatus.NOT_FOUND;
			return new ResponseEntity<Map<String, Object>>(resultMap, status);
		}
		
		if(jwtUtil.getRole(token) == 0 && !userId.equals(info.getUser().getUserId())) {
			resultMap.put("message", "사용자가 작성한 글이 아닙니다.");
			status = HttpStatus.FORBIDDEN;
			return new ResponseEntity<Map<String, Object>>(resultMap, status);
		}
		
		try {
			int result = service.deleteRecord(tapeKey, recordKey);
			if(result == 1) {
				status = HttpStatus.OK;
				return new ResponseEntity<>(status);
			}else {
				status = HttpStatus.NO_CONTENT;
				message = "삭제할 내용이 없습니다.";
				resultMap.put("message", message);
			}
		} catch(Exception e) {
			message = e.getMessage();
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<String>(message, status);
	}
	
	@PostMapping("/like/{tapeKey}/{recordKey}/{userId}")
	@ApiOperation("특정 레코드를 관심 리스트에 추가합니다.")
	public ResponseEntity<?> likeRecord(
			@PathVariable int tapeKey,
			@PathVariable int recordKey, 
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
		
		try {
			int result = service.likeRecord(tapeKey, recordKey, userId);
			if(result == 1) {
				status = HttpStatus.CREATED;
				return new ResponseEntity<>(status);
			} else {
				resultMap.put("message", "저장한 정보가 없습니다.");
				status = HttpStatus.NO_CONTENT;
				return new ResponseEntity<Map<String, Object>>(resultMap, status);
			}
		} catch(DuplicateKeyException e) {
			resultMap.put("message", "이미 좋아요를 하였습니다.");
			return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.CONFLICT);	
		}
		catch(Exception e) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			return new ResponseEntity<String>(e.getMessage(), status);
		}
	}
	@DeleteMapping("/dislike/{tapeKey}/{recordKey}/{userId}")
	@ApiOperation("특정 레코드를 관심 리스트에 삭제합니다.")
	public ResponseEntity<?> dislikeRecord(
			@PathVariable int tapeKey,
			@PathVariable int recordKey, 
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

		try {
			int result = service.dislikeRecord(tapeKey, recordKey, userId);
			if(result == 1) {
				status = HttpStatus.OK;
				return new ResponseEntity<>(status);
			} else {
				resultMap.put("message", "저장한 정보가 없습니다.");
				status = HttpStatus.NO_CONTENT;
				return new ResponseEntity<Map<String, Object>>(resultMap, status);
			}
		} catch(Exception e) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			return new ResponseEntity<String>(e.getMessage(), status);
		}
	}
}
