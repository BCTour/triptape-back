package com.ssafy.triptape.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
	private TapeService service;
	
	@PostMapping("/regist")
	@ApiOperation(value="테이프를 등록합니다.", consumes="multipart/form-data")
	public ResponseEntity<?> registTape(@RequestPart(value="tape") TapeDto tape, @RequestPart(value="file", required = false) MultipartFile file) {
		
		try {
			service.registTape(tape, file);
			return new ResponseEntity<Void>(HttpStatus.CREATED);
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
			String word = "";
			
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
	
	@GetMapping("/search")
	@ApiOperation("조건에 따른 테이프 목록을 조회합니다.")
	public ResponseEntity<?> searchByCondition(@RequestParam(required=false)String keyword,@RequestParam(required=false)String word, @RequestParam int currentPage){
		
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
	public ResponseEntity<?> searchInfo(@PathVariable int tapeKey){
		
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
				service.updateView(tapeKey);	
				status = HttpStatus.OK;
			}
		} catch(Exception e) {
			message = e.getMessage();
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			resultMap.put("message", message);
		}
		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}
	
	@PostMapping("/like")
	@ApiOperation("특정 테이프를 관심 리스트에 추가합니다.")
	public ResponseEntity<?> likeTape(@RequestParam int tapeKey, @RequestParam String userId){
		
		HttpStatus status = HttpStatus.ACCEPTED;

		try {
			service.likeTape(tapeKey, userId);
			status = HttpStatus.CREATED;
			return new ResponseEntity<>(status);
		} catch(Exception e) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			return new ResponseEntity<String>(e.getMessage(), status);
		}
	}
	@DeleteMapping("/dislike")
	@ApiOperation("특정 테이프를 관심 리스트에 추가합니다.")
	public ResponseEntity<?> dislikeTape(@RequestParam int tapeKey, @RequestParam String userId){
		
		HttpStatus status = HttpStatus.ACCEPTED;

		try {
			service.dislikeTape(tapeKey, userId);
			status = HttpStatus.OK;
			return new ResponseEntity<>(status);
		} catch(Exception e) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			return new ResponseEntity<String>(e.getMessage(), status);
		}
	}
}
